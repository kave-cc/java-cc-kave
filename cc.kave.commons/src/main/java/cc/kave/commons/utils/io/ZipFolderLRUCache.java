/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.io;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;

/**
 * Abstraction over ZipFolder that allows to request .zip archives by key while
 * guaranteeing that only <code>capacity</code> number of files are open at the
 * same time. The cache makes sure that new archives are created when a key has
 * not been seen so far or when it has timed out. Once the capacity is reached,
 * the least-recently used file is being closed.<br>
 * <br>
 * Note: The implementation is for <i>writing only</i>, use
 * {@link NestedZipFolders} to consume the generated data afterwards.
 */
public class ZipFolderLRUCache<T> implements AutoCloseable {

	private final IFileNaming<T> _fileNaming;
	private final File _root;
	private final int _capacity;

	private final List<T> _accessOrderList = Lists.newLinkedList();
	private final Map<T, WritingArchive> _openArchives = Maps.newLinkedHashMap();
	private final Map<T, ZipFolder> _folders = Maps.newLinkedHashMap();

	public ZipFolderLRUCache(File root, int capacity) {
		this(root, capacity, new JsonFileNaming<>());
	}

	public ZipFolderLRUCache(File root, int capacity, IFileNaming<T> fileNaming) {
		Asserts.assertTrue(root.exists());
		Asserts.assertTrue(root.isDirectory());
		Asserts.assertTrue(capacity > 0);

		_root = root;
		_capacity = capacity;
		_fileNaming = fileNaming;
	}

	public IWritingArchive getArchive(T key) {
		RefreshAccess(key);

		if (_openArchives.containsKey(key)) {
			return _openArchives.get(key);
		}

		ZipFolder folder = _folders.containsKey(key) ? _folders.get(key) : GetFolder(key);

		WritingArchive wa = folder.createNewArchive();
		_openArchives.put(key, wa);

		EnsureCapacityIsRespected();

		return wa;
	}

	private void EnsureCapacityIsRespected() {
		if (_accessOrderList.size() > _capacity) {
			T oldest = _accessOrderList.iterator().next();
			_accessOrderList.remove(oldest);
			_openArchives.get(oldest).close();
			_openArchives.remove(oldest);
		}
	}

	private void RefreshAccess(T key) {
		_accessOrderList.remove(key);
		_accessOrderList.add(key);
	}

	private ZipFolder GetFolder(T key) {
		if (_folders.containsKey(key)) {
			return _folders.get(key);
		}

		File folder = new File(_root.getAbsolutePath(), _fileNaming.getRelativePath(key));
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ZipFolder folderUtil = new ZipFolder(folder.getAbsolutePath(), JsonUtils.toJson(key));

		_folders.put(key, folderUtil);

		return folderUtil;
	}

	public boolean isCached(T key) {
		return _openArchives.containsKey(key);
	}

	public int size() {
		return _openArchives.size();
	}

	public void close() {
		for (WritingArchive wa : _openArchives.values()) {
			wa.close();
		}
		_accessOrderList.clear();
		_openArchives.clear();
		_folders.clear();
	}
}