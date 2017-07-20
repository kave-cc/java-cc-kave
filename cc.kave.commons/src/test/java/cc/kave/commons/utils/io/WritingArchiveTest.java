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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.io.IWritingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.WritingArchive;

public class WritingArchiveTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File dir;
	private File zip;

	@Before
	public void setUp() throws IOException {
		dir = tmp.newFolder("data");
		zip = file(dir, "f.zip");
	}

	@Test(expected = AssertionException.class)
	public void fileMustNotExist() throws IOException {
		zip.createNewFile();
		try (WritingArchive wa = new WritingArchive(zip.getAbsoluteFile())) {
		}
	}

	@Test(expected = AssertionException.class)
	public void parentHastoExist() {
		zip = file(dir, "does", "not", "exist");
		write();
	}

	@Test
	public void writeEmptyDoesNotCreateFile() {
		write();
		assertFalse(zip.exists());
	}

	@Test
	public void writeSomething() {
		List<String> expected = Lists.newArrayList("a", "b", "c");
		write(expected.toArray(new String[0]));
		List<String> actual = readZip();
		assertEquals(expected, actual);
	}

	@Test
	public void writeAll() {
		List<String> expected = Lists.newArrayList("a", "b", "c");
		try (IWritingArchive sut = new WritingArchive(zip)) {
			sut.addAll(expected);
		}
		List<String> actual = readZip();
		assertEquals(expected, actual);
	}

	@Test
	public void nothingHappensOnSecondDispose() {
		IWritingArchive sut = new WritingArchive(zip);
		sut.add("x");

		assertFalse(zip.exists());
		sut.close();
		assertTrue(zip.exists());

		zip.delete();
		sut.close();
		assertFalse(zip.exists());
	}

	private void write(String... entries) {
		try (WritingArchive sut = new WritingArchive(zip)) {
			for (String entry : entries) {
				sut.add(entry);
			}
		}
	}

	private List<String> readZip() {
		try (ReadingArchive ra = new ReadingArchive(zip)) {
			return ra.getAll(String.class);
		}
	}

	private File file(File dir, String... parts) {
		return Paths.get(dir.getAbsolutePath(), parts).toFile();
	}
}