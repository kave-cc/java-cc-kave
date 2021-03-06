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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.TypeFileNaming;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.mining.Options;

public class PBNModelStore implements IModelStore<PBNModel> {

	private static final TypeFileNaming naming = new TypeFileNaming();

	private File baseDir;

	public PBNModelStore(String dir, Options opts) {
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
		try {
			FileUtils.deleteDirectory(baseDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private File file(ITypeName t) {
		String path = String.join(File.separator, baseDir.getAbsolutePath(), naming.getRelativePath(t) + ".json");
		return new File(path);
	}

	@Override
	public void store(ITypeName t, PBNModel m) {
		double size = m.getSize() / (double) (1024 * 1024);
		Logger.debug("writing BMN Model (size: %.2f MB, numPatterns: %d)", size, m.patternProbabilities.length);
		File file = file(t);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		JsonUtils.toJson(m, file);
	}

	@Override
	public boolean hasModel(ITypeName t) {
		return file(t).exists();
	}

	@Override
	public PBNModel getModel(ITypeName t) {
		Asserts.assertTrue(hasModel(t),
				"no model available, better use 'hasModel(t)' to check existance before accessing it!");
		return JsonUtils.fromJson(file(t), PBNModel.class);
	}
}