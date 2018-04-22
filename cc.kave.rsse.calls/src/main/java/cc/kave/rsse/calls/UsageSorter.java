/**
 * Copyright 2018 University of Zurich
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
package cc.kave.rsse.calls;

import static cc.kave.commons.assertions.Asserts.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.NestedZipFolders;
import cc.kave.commons.utils.io.ZipFolderLRUCache;
import cc.kave.rsse.calls.model.usages.IUsage;

public class UsageSorter {

	private final String dir;
	private final String label;
	private ZipFolderLRUCache<ITypeName> cache;

	public UsageSorter(String dir, String label) {
		this.dir = dir;
		this.label = label;

		ensureRootDir();
	}

	public void openLRUCache() {
		assertNull(cache);
		cache = new ZipFolderLRUCache<>(getRootDir(), 1000);
	}

	public void close() {
		if (cache != null) {
			cache.close();
			cache = null;
		}
	}

	private void ensureRootDir() {
		File rootDir = getRootDir();
		Asserts.assertTrue(!rootDir.exists() || rootDir.isDirectory());
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
	}

	private File getRootDir() {
		return Paths.get(dir, label).toFile();
	}

	public void clear() {
		Logger.log("Clearing contents of %s...\n", getRootDir());
		try {
			FileUtils.deleteDirectory(getRootDir());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ensureRootDir();
	}

	public void store(List<IUsage> mixedUsages) {
		for (IUsage u : mixedUsages) {
			ITypeName type = u.getType();
			if (type.isArray() || type.isUnknown() || type.isDelegateType() || type.getAssembly().isLocalProject()) {
				continue;
			}
			cache.getArchive(u.getType()).add(u);
		}
	}

	public Set<ITypeName> registeredTypes() {
		Directory d = new Directory(getRootDir().getAbsolutePath());
		NestedZipFolders<ITypeName> zf = new NestedZipFolders<>(d, ITypeName.class);
		return zf.findKeys();
	}

	public List<IUsage> read(ITypeName t) {
		Directory d = new Directory(getRootDir().getAbsolutePath());
		NestedZipFolders<ITypeName> zf = new NestedZipFolders<>(d, ITypeName.class);
		return zf.readAllZips(t, IUsage.class);
	}
}