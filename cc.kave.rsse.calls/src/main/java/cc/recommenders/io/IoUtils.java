/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.json.JsonUtils;

public class IoUtils {
	public static final String TEMP_PREFIX = "recommenders-temp-folder.";

	public Directory getRandomTempDirectory() {
		try {
			Path path = Files.createTempDirectory(TEMP_PREFIX);
			return new Directory(path.toFile().getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getRandomTempFile() {
		try {
			File file = File.createTempFile(IoUtils.TEMP_PREFIX, "");
			file.delete();
			return file.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Directory getParentDirectory(String absoluteFileName) {
		File file = new File(absoluteFileName);
		Asserts.assertTrue(file.isAbsolute());
		String parent = file.getParent();
		if (parent == null) {
			return new Directory(absoluteFileName);
		} else {
			return new Directory(parent);
		}
	}

	public String toNestedFileName(Object o) {
		return simplify(o);
	}

	public String toNestedFileName(Object o, String extension) {
		return simplify(o) + '.' + extension;
	}

	public String toNestedDirectoryName(Object o) {
		return simplify(o) + '/';
	}

	public String toFlatFileName(Object o) {
		return flatten(simplify(o));
	}

	public String toFlatFileName(Object o, String extension) {
		String name = flatten(simplify(o));
		return name + '.' + extension;
	}

	public String toFlatDirectoryName(Object o) {
		return flatten(simplify(o)) + '/';
	}

	private String flatten(String nested) {
		return nested.replace('/', '_');
	}

	private String simplify(Object o) {
		String file = JsonUtils.toJson(o);
		file = file.replaceAll("\\\"", "\""); // quotes inside json
		file = file.replaceAll("\"", ""); // surrounding quotes
		file = file.replaceAll("\\.", "/"); // . -> /
		file = file.replaceAll("[^a-zA-Z0-9\\[\\](){}\\\\/,-_+$]", "_");
		return file;
	}
}