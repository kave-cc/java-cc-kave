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
package cc.kave.rsse.calls.bmn;

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.utils.FileNamingStrategy;

public class BMNModelStore implements IModelStore<BMNModel> {

	private static FileNamingStrategy naming = new FileNamingStrategy();

	private File rootDir;

	public BMNModelStore(String dir, String label) {
		this.rootDir = new File(dir, label);
		assertTrue(!rootDir.exists() || rootDir.isDirectory());
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
	}

	@Override
	public void clear() {
		try {
			FileUtils.deleteDirectory(rootDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void store(ITypeName t, BMNModel m) {
		JsonUtils.toJson(m, file(t));
	}

	@Override
	public boolean hasModel(ITypeName t) {
		return file(t).exists();
	}

	private File file(ITypeName t) {
		String path = String.join(File.separator, rootDir.getAbsolutePath(), naming.getRelativePath(t) + ".json");
		return new File(path);
	}

	@Override
	public BMNModel getModel(ITypeName t) {
		Asserts.assertTrue(hasModel(t), "no model available, better use 'hasModel(t)' to check existance before accessing it!");
		return JsonUtils.fromJson(file(t), BMNModel.class);
	}
}