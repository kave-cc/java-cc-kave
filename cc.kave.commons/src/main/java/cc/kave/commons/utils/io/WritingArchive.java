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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.collect.Maps;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;

public class WritingArchive implements IWritingArchive {

	private final File file;
	private final Map<String, String> content;

	public WritingArchive(File file) {
		Asserts.assertFalse(file.exists());
		File parent = file.getParentFile();
		Asserts.assertTrue(parent.exists());
		Asserts.assertTrue(parent.isDirectory());

		content = Maps.newLinkedHashMap();
		this.file = file;
	}

	@Override
	public <T> void addAll(Iterable<T> objs) {
		for (T obj : objs) {
			add(obj);
		}
	}

	@Override
	public <T> void add(T obj) {
		int count = content.size();
		String filename = count + ".json";
		add(obj, filename);
	}

	@Override
	public <T> void add(T obj, String fileName) {
		addPlain(JsonUtils.toJson(obj), fileName);
	}

	@Override
	public <T> void addPlain(String str) {
		int count = content.size();
		String filename = count + ".txt";
		addPlain(str, filename);
	}

	@Override
	public <T> void addPlain(String str, String fileName) {
		// TODO crash on fileName exists
		content.put(fileName, str);
	}

	@Override
	public void close() {
		try {
			if (!content.isEmpty()) {
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
				for (String fileName : content.keySet()) {
					out.putNextEntry(new ZipEntry(fileName));
					String json = content.get(fileName);
					out.write(json.getBytes());
					out.closeEntry();
				}
				out.close();
				content.clear();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}