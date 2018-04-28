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
package cc.kave.commons.utils.io;

import cc.kave.commons.utils.io.json.JsonUtils;

public class JsonFileNaming<T> implements IFileNaming<T> {

	@Override
	public String getRelativePath(T o) {
		String relName = JsonUtils.toJson(o);
		relName = relName.replace('.', '/');
		relName = relName.replace("\\\\\"", "\""); // ??
		relName = relName.replace("\\\"", "\""); // quotes inside json
		relName = relName.replace("\"", ""); // surrounding quotes
		relName = relName.replace('\\', '/');
		relName = relName.replaceAll("[^a-zA-Z0-9,\\-_/+$(){}\\[\\]]", "_");
		return relName;
	}
}