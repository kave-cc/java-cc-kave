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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.json.JsonUtils;

public class WritingArchive implements IWritingArchive {

	private final File file;

	private ZipOutputStream out;
	private int count = 0;

	public WritingArchive(File file) {
		Asserts.assertFalse(file.exists());
		File parent = file.getParentFile();
		Asserts.assertTrue(parent.exists());
		Asserts.assertTrue(parent.isDirectory());
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
		String filename = count + ".json";
		add(obj, filename);
	}

	@Override
	public <T> void add(T obj, String fileName) {
		addPlain(JsonUtils.toJson(obj), fileName);
	}

	@Override
	public <T> void addPlain(String str) {
		String filename = count + ".txt";
		addPlain(str, filename);
	}

	@Override
	public <T> void addPlain(String str, String fileName) {
		if (out == null) {
			open();
		}
		try {
			count++;
			// TODO crash on fileName exists
			out.putNextEntry(new ZipEntry(fileName));
			out.write(str.getBytes());
			out.closeEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void open() {
		try {
			out = new ZipOutputStream(new FileOutputStream(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void close() {
		try {
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}