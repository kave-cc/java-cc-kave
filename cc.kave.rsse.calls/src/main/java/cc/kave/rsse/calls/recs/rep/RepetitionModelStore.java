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
package cc.kave.rsse.calls.recs.rep;

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.TypeFileNaming;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.rsse.calls.IModelStore;

public class RepetitionModelStore implements IModelStore<RepetitionModel> {

	private static final TypeFileNaming naming = new TypeFileNaming();

	private File rootDir;

	public RepetitionModelStore(File rootDir) {
		this.rootDir = rootDir;
		assertTrue(!rootDir.exists() || rootDir.isDirectory());
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
	}

	public void clear() {
		try {
			FileUtils.deleteDirectory(rootDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void store(ITypeName t, RepetitionModel model) {
		Logger.debug("Writing Repetition Model... (%s)", file(t).getAbsolutePath());
		File f = file(t);
		ensureParent(f);
		JsonUtils.toJson(model, f);
	}

	private void ensureParent(File f) {
		File parent = f.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	@Override
	public boolean hasModel(ITypeName t) {
		return file(t).exists();
	}

	@Override
	public RepetitionModel getModel(ITypeName t) {
		Asserts.assertTrue(hasModel(t),
				"no model available, better use 'hasModel(t)' to check existance before accessing it!");
		return JsonUtils.fromJson(file(t), RepetitionModel.class);
	}

	private File file(ITypeName t) {
		String path = String.join(File.separator, rootDir.getAbsolutePath(), naming.getRelativePath(t) + ".json");
		return new File(path);
	}
}