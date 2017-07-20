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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

public class ReadingArchiveTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File dir;
	private File zip;

	private IReadingArchive sut;

	@Before
	public void setup() throws IOException {
		dir = tmp.newFolder("data");
		zip = new File(dir, "a.zip");
	}

	@Test(expected = AssertionException.class)
	public void nonExistingZip() {
		try (IReadingArchive ra = new ReadingArchive(zip)) {
		}
	}

	@Test
	public void EmptyZip() {
		PrepareZip();
		assertFalse(sut.hasNext());
	}

	@Test
	public void NonEmptyZip() {
		PrepareZip("a", "b");

		assertTrue(sut.hasNext());
		assertEquals("a", sut.getNext(String.class));
		assertTrue(sut.hasNext());
		assertEquals("b", sut.getNext(String.class));
		assertFalse(sut.hasNext());
	}

	@Test
	public void Count_Empty() {
		PrepareZip();
		assertEquals(0, sut.getNumberOfEntries());
	}

	@Test
	public void Count_1() {
		PrepareZip("a");
		assertEquals(1, sut.getNumberOfEntries());
	}

	@Test
	public void Count_2() {
		PrepareZip("a", "b");
		assertEquals(2, sut.getNumberOfEntries());
	}

	@Test
	public void GetAll() {
		PrepareZip("a", "b", "c");

		List<String> actual = sut.getAll(String.class);
		List<String> expected = Lists.newArrayList("a", "b", "c");
		assertEquals(expected, actual);
	}

	private void PrepareZip(String... entries) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));

			int i = 0;
			for (String entry : entries) {
				String fileName = (i++) + ".json";
				out.putNextEntry(new ZipEntry(fileName));

				out.write(entry.getBytes());
				out.closeEntry();
			}
			out.close();
			sut = new ReadingArchive(zip);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}