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
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;

public class ZipFolder {

	public static final String MARKER_FILE_NAME = ".zipfolder";

	private final String _root;
	private final String _metaData;

	private int _fileCounter;

	public ZipFolder(@Nonnull String root) {
		this(root, "");
	}

	public ZipFolder(@Nonnull String root, @Nonnull String metaData) {
		File f = new File(root);
		Asserts.assertTrue(f.exists() && f.isDirectory());
		_root = root;
		_metaData = metaData;
	}

	public WritingArchive createNewArchive() {
		createMarker();
		File fileName = createNextUnusedFileName();
		return new WritingArchive(fileName);
	}

	private void createMarker() {
		File markerName = new File(_root, MARKER_FILE_NAME);
		if (!markerName.exists()) {
			try {
				FileUtils.writeStringToFile(markerName, _metaData);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private File createNextUnusedFileName() {
		File fileName = CreateNextFileName();
		while (fileName.exists()) {
			fileName = CreateNextFileName();
		}
		return fileName;
	}

	private File CreateNextFileName() {
		return new File(_root, String.format("%d.zip", _fileCounter++));
	}

	public int getNumArchives() {
		return findFiles().size();
	}

	private Set<String> findFiles() {
		return new Directory(_root).findFiles(f -> f.endsWith(".zip"));
	}

	public List<ReadingArchive> openAll() {
		List<ReadingArchive> archives = Lists.newLinkedList();
		for (String zip : findFiles()) {
			archives.add(open(zip));
		}
		return archives;
	}

	public ReadingArchive open(String relPath) {
		File f = Paths.get(_root, relPath).toFile();
		return new ReadingArchive(f);
	}
}