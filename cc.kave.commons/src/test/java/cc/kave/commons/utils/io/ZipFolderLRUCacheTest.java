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

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.IWritingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.WritingArchive;
import cc.kave.commons.utils.io.ZipFolderLRUCache;
import cc.kave.commons.utils.io.json.JsonUtils;

public class ZipFolderLRUCacheTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File root;
	private ZipFolderLRUCache<String> sut;

	@Before
	public void setUp() throws IOException {
		root = tmp.newFolder("data");
		sut = new ZipFolderLRUCache<String>(root, 2);
	}

	@Test
	public void sizeAndCache() {
		assertEquals(0, sut.size());
		assertFalse(sut.isCached("a"));

		sut.getArchive("a");

		assertEquals(1, sut.size());
		assertTrue(sut.isCached("a"));
	}

	@Test
	public void filesAreCreatedInCorrectSubfolders() throws IOException {
		sut.getArchive(relFile("a"));
		sut.getArchive(relFile("b", "b"));
		sut.getArchive(relFile("c", "c", "c"));
		sut.getArchive(relFile("d", "d", "d", "d"));

		sut.close();

		assertTrue(root.exists());
		assertTrue(file(root, "a", ".zipfolder").exists());
		assertTrue(file(root, "b", "b", ".zipfolder").exists());
		assertTrue(file(root, "c", "c", "c", ".zipfolder").exists());
		assertTrue(file(root, "d", "d", "d", "d", ".zipfolder").exists());
	}

	@Test
	public void doubleSlashIsNotAnIssue() throws IOException {
		sut.getArchive("La//a");

		sut.close();

		assertTrue(root.exists());
		assertTrue(file(root, "La", "a", ".zipfolder").exists());
	}

	@Test
	public void keyIsPassedAsMetaDataWithoutReplacement() throws IOException {
		String key = "La/A.1/_!";

		sut.getArchive(key);
		sut.close();

		File metaFile = file(root, "La", "A", "1", "__", ".zipfolder");
		String actual = FileUtils.readFileToString(metaFile);
		assertEquals(JsonUtils.toJson(key), actual);
	}

	@Test
	public void complexKeysArePossibleThoughNotRecommended() throws IOException {
		ZipFolderLRUCache<List<String>> sut = new ZipFolderLRUCache<List<String>>(root, 2);
		List<String> key = Lists.newArrayList("a", "b");

		sut.getArchive(key);
		sut.close();

		File metaFile = file(root, "[a,b]", ".zipfolder");
		String actual = FileUtils.readFileToString(metaFile);
		String expected = JsonUtils.toJson(key);
		assertEquals(expected, actual);
	}

	@Test
	public void replacementInKeysWorks() {
		String a = "a,.+-/\\_$()[]{}:*?\"|";
		String e = "a,/+-/_$()[]{}____";
		try (IWritingArchive wa = sut.getArchive(a)) {
			wa.add("something");
		}

		assertTrue(root.exists());
		assertTrue(file(root, e, "0.zip").exists());
	}

	@Test
	public void cacheDoesNotGrowLargerThanCapacity() {
		sut.getArchive("a");
		sut.getArchive("b");
		sut.getArchive("c");

		assertEquals(2, sut.size());
	}

	@Test
	public void existingFilesAreNotOverwritten() throws IOException {
		sut.close();

		File metaFile = file(root, "a", ".zipfolder");
		File zip0 = file(root, "a", "0.zip");
		File zip1 = file(root, "a", "1.zip");

		// setup fake data from previous run
		FileUtils.writeStringToFile(metaFile, "test");
		try (WritingArchive testZip = new WritingArchive(zip0)) {
			testZip.add("test");
		}

		// new initialization
		sut = new ZipFolderLRUCache<String>(root, 2);
		IWritingArchive a = sut.getArchive("a");
		a.add("x");
		sut.close();

		assertTrue(metaFile.exists());
		assertTrue(zip0.exists());
		assertTrue(zip1.exists());

		assertEquals("test", FileUtils.readFileToString(metaFile));
		assertZipContent(zip0, "test");
		assertZipContent(zip1, "x");
	}

	private File file(File dir, String... parts) {
		return Paths.get(dir.getAbsolutePath(), parts).toFile();
	}

	private String relFile(String... tokens) {
		return String.join(File.separator, tokens);
	}

	private void assertZipContent(File file, String... expectedsArr) {
		try (IReadingArchive ra = new ReadingArchive(file)) {
			List<String> expecteds = Lists.newArrayList(expectedsArr);
			List<String> actuals = Lists.newArrayList();
			while (ra.hasNext()) {
				actuals.add(ra.getNext(String.class));
			}
			assertEquals(expecteds, actuals);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void leastRecentlyUsedKeyIsRemoved() {
		sut.getArchive("a");
		sut.getArchive("b");
		sut.getArchive("c");

		assertFalse(sut.isCached("a"));
		assertTrue(sut.isCached("b"));
		assertTrue(sut.isCached("c"));
	}

	@Test
	public void refreshingWorks() {
		sut.getArchive("a");
		sut.getArchive("b");
		sut.getArchive("a");
		sut.getArchive("c");

		assertFalse(sut.isCached("b"));
		assertTrue(sut.isCached("a"));
		assertTrue(sut.isCached("c"));
	}

	@Test
	public void cacheRemoveClosesOpenArchive() throws IOException {
		File expectedFileName = file(root, "a", "0.zip");

		IWritingArchive wa = sut.getArchive("a");
		wa.add("something");
		sut.getArchive("b");

		assertFalse(expectedFileName.exists());
		sut.getArchive("c");
		assertTrue(expectedFileName.exists());
	}

	@Test
	public void closeClosesAllOpenArchives() throws IOException {
		IWritingArchive wa = sut.getArchive("a");
		wa.add("something");

		sut.close();

		assertFalse(sut.isCached("a"));
		assertEquals(0, sut.size());
		assertTrue(file(root, "a", "0.zip").exists());
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void directoryHasToExist() {
		new ZipFolderLRUCache<String>(file(root, "does", "not", "exist"), 10);
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void directoryMustNotBeAFile() throws IOException {
		File f = file(root, "x.txt");
		f.createNewFile();
		new ZipFolderLRUCache<String>(f, 10);
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void capacityMustBeLargerThanZero() {
		new ZipFolderLRUCache<String>(root, 0);
	}
}