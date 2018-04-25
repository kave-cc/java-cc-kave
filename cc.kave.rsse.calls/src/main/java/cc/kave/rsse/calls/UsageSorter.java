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
import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.NestedZipFolders;
import cc.kave.commons.utils.io.ZipFolderLRUCache;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.utils.FileNamingStrategy;

public class UsageSorter {

	private File baseDir;
	private ZipFolderLRUCache<ITypeName> cache;

	public UsageSorter(String dir, Options opts) {
		File rootDir = new File(dir);
		assertTrue(!rootDir.exists() || rootDir.isDirectory());
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		baseDir = new File(rootDir, opts.toString());
		assertTrue(!baseDir.exists() || baseDir.isDirectory());
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
	}

	public void clear() {
		Logger.log("Clearing contents of %s...\n", baseDir);
		try {
			FileUtils.deleteDirectory(baseDir);
			baseDir.mkdirs();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void openLRUCache() {
		assertNull(cache);
		cache = new TypeZipFolderLRUCache(baseDir);
	}

	public void close() {
		if (cache != null) {
			cache.close();
			cache = null;
		}
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
		Directory d = new Directory(baseDir.getAbsolutePath());
		NestedZipFolders<ITypeName> zf = new NestedZipFolders<>(d, ITypeName.class);
		return zf.findKeys();
	}

	public List<IUsage> read(ITypeName t) {
		Directory d = new Directory(baseDir.getAbsolutePath());
		NestedZipFolders<ITypeName> zf = new NestedZipFolders<>(d, ITypeName.class);
		return zf.readAllZips(t, IUsage.class);
	}

	private static class TypeZipFolderLRUCache extends ZipFolderLRUCache<ITypeName> {

		private static final FileNamingStrategy naming = new FileNamingStrategy();
		private File root;

		public TypeZipFolderLRUCache(File root) {
			super(root, 1000);
			this.root = root;
		}

		@Override
		protected String GetTargetFolder(ITypeName key) {
			return new File(root, naming.getRelativePath(key)).getAbsolutePath();
		}
	}
}