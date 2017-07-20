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

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.exceptions.AssertionException;

public class DirectoryTest {

	private String tempFileName;

	private Directory uut;

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Before
	public void setup() {
		tempFileName = tempFolder.getRoot().getAbsolutePath();
		uut = new Directory(tempFileName);
	}

	@Test
	public void InitAddsTrailingSeparator() throws IOException {
		String withSlash = relFile(tempFileName, "with") + File.separator;
		URL a = new Directory(withSlash).getUrl();
		assertTrue(a.toString().endsWith("/"));

		String withoutSlash = relFile(tempFileName, File.separator) + "without";
		URL b = new Directory(withoutSlash).getUrl();
		assertTrue(b.toString().endsWith("/"));
	}

	@Test
	public void objectWritesCreateNewFiles() throws IOException {
		setup();
		uut.write("blubb", "file.txt");
		assertFileExists("file.txt");
	}

	@Test
	public void serializationRoundtripWorks() throws IOException {
		setup();

		String expected = "someContent";
		uut.write(expected, "somefile.txt");
		String actual = uut.read("somefile.txt", String.class);

		assertNotSame(expected, actual);
		assertEquals(expected, actual);
	}

	@Test
	public void existingFilesAreFound() throws IOException {
		setup();
		String fileName = "afile";
		tempFolder.newFile(fileName);
		assertTrue(uut.exists(fileName));
	}

	@Test
	public void notExistingFilesAreDetected() throws IOException {
		setup();
		String fileName = "aNonExistingFile";
		assertFalse(uut.exists(fileName));
	}

	@Test
	public void ifNoFileExistsTheCountIsZero() {
		setup();
		int count = uut.count();
		int expected = 0;
		assertEquals(expected, count);
	}

	@Test
	public void ifOneFileExistsTheCountIsOne() throws IOException {
		setup();
		tempFolder.newFile("firstFile");
		int count = uut.count();
		int expected = 1;
		assertEquals(expected, count);
	}

	@Test
	public void ifAFolderExistsItIsCountedWithoutChilds() throws IOException {
		setup();
		tempFolder.newFolder("afolder");
		tempFolder.newFile(relFile("afolder", "file"));
		tempFolder.newFile(relFile("afolder", "file2"));

		int count = uut.count();
		int expected = 1;
		assertEquals(expected, count);
	}

	private String relFile(String... parts) {
		return String.join(File.separator, parts);
	}

	private File file(File dir, String... parts) {
		return Paths.get(dir.getAbsolutePath(), parts).toFile();
	}

	@Test
	public void directoryCanBeCleared() throws IOException {
		setup();
		tempFolder.newFile("someFile");
		tempFolder.newFolder("afolder");
		tempFolder.newFile(relFile("afolder", "file"));
		tempFolder.newFile(relFile("afolder", "file2"));

		uut.clear();

		int actual = tempFolder.getRoot().list().length;
		int expected = 0;
		assertEquals(expected, actual);
	}

	@Test
	public void filesCanBeDeleted() throws IOException {
		setup();
		String fileName = "someFile";
		tempFolder.newFile(fileName);
		uut.delete(fileName);
		assertFalse(uut.exists(fileName));
	}

	@Test
	public void listingContainsAllFiles() throws IOException {
		setup();

		Set<String> expected = new HashSet<String>();

		for (String file : new String[] { "fileA", "fileB", "fileC" }) {
			tempFolder.newFile(file);
			expected.add(file);
		}

		assertEquals(expected, uut.list());
	}

	@Test
	public void validUrlsAreCreatedForTheDirectory() throws IOException {
		setup();

		URL expected = new File(tempFileName).toURI().toURL();
		URL actual = uut.getUrl();

		assertEquals(expected, actual);
	}

	@Test
	public void validUrlsAreCreatedForContainingFiles() throws IOException {
		setup();

		File newFile = tempFolder.newFile("test");
		URL expected = new File(newFile.getAbsolutePath()).toURI().toURL();

		URL actual = uut.getUrl("test");

		assertEquals(expected, actual);
	}

	@Test
	public void foldersCanBeCreated() {
		setup();
		uut.createDirectory("subdir");
		File folder = new File(tempFileName, "subdir");

		assertTrue(folder.exists());
		assertTrue(folder.isDirectory());
	}

	@Test
	public void foldersWithSubfoldersCanBeCreated() {
		setup();
		uut.createDirectory(relFile("subdir", "subsubdir"));
		File folder = new File(tempFileName, relFile("subdir", "subsubdir"));

		assertTrue(folder.exists());
		assertTrue(folder.isDirectory());
	}

	@Test
	public void creatingFoldersGetsTheNewFolderReturned() throws MalformedURLException {
		setup();
		Directory blubbDir = uut.createDirectory("blubb");

		URL expected = Paths.get(tempFileName, "blubb").toUri().toURL();
		URL actual = blubbDir.getUrl();

		assertEquals(expected, actual);
	}

	@Test
	public void writeReadContentRoundtrip() throws IOException {
		setup();
		String expected = "This is some string, that should be written and read";
		String fileName = "roundtrip.txt";
		uut.writeContent(expected, fileName);
		String actual = uut.readContent(fileName);
		assertEquals(expected, actual);
	}

	@Test
	public void writeReadArchiveRoundtrip() throws IOException {
		setup();

		String fileName = "archive.zip";
		WritingArchive writingArchive = uut.getWritingArchive(fileName);

		Set<String> expected = new HashSet<String>();
		Set<String> actual = new HashSet<String>();

		for (String content : new String[] { "first string", "a second string for serializing", "last one" }) {
			writingArchive.add(content);
			expected.add(content);
		}
		writingArchive.close();

		IReadingArchive readingArchive = uut.getReadingArchive(fileName);

		assertEquals(3, readingArchive.getNumberOfEntries());

		while (readingArchive.hasNext()) {
			String s = readingArchive.getNext(String.class);
			actual.add(s);
		}
		readingArchive.close();

		assertEquals(expected, actual);
	}

	@Test
	public void writeReadArchivePlainRoundtrip() throws IOException {
		setup();

		String fileName = "archive.zip";
		WritingArchive writingArchive = uut.getWritingArchive(fileName);

		Set<String> expected = new HashSet<String>();
		Set<String> actual = new HashSet<String>();

		writingArchive.addPlain("xyz", "x.txt");
		expected.add("xyz");

		for (String content : new String[] { "fir<st st>ring", "a second string for serializing", "last one" }) {
			writingArchive.addPlain(content);
			expected.add(content);
		}
		writingArchive.close();

		IReadingArchive readingArchive = uut.getReadingArchive(fileName);

		assertEquals(4, readingArchive.getNumberOfEntries());

		while (readingArchive.hasNext()) {
			String s = readingArchive.getNextPlain();
			actual.add(s);
		}
		readingArchive.close();

		assertEquals(expected, actual);
	}

	@Test
	public void getWritingArchiveCreateParentFolder() throws IOException {
		uut.getWritingArchive(relFile("a", "b.zip"));
		File parent = file(new File(tempFileName), "a");
		assertTrue(parent.exists());
	}

	@Test
	public void writeReopenReadRoundtrip() throws IOException {
		setup();

		WritingArchive archive = uut.getWritingArchive("test.zip");
		archive.add("eins");
		archive.close();

		archive = uut.reopenWritingArchive("test.zip", String.class);
		archive.add("zwei");
		archive.close();

		List<String> actual = Lists.newArrayList();
		IReadingArchive readingArchive = uut.getReadingArchive("test.zip");
		while (readingArchive.hasNext()) {
			String next = readingArchive.getNext(String.class);
			actual.add(next);
		}

		List<String> expected = Lists.newArrayList("eins", "zwei");
		assertEquals(expected, actual);

		assertEquals(newHashSet("test.zip"), uut.list());
	}

	@Test
	public void generatedFileNamesDoNOtContainBadChars() {
		String input = "azAZ09.-_;,:?!\"�$%&/()=?";
		String expec = "azAZ09.-__";
		String actual = Directory.createFileName(input);
		assertEquals(expec, actual);
	}

	@Test
	public void listCanBeFilteredByPredicate() throws IOException {
		setup();
		uut.write("a", "a.jpg");
		uut.write("b", "b.zip");
		uut.write("c", "c.png");
		uut.write("d", "d.zip");
		uut.write("e", "e.pdf");

		Set<String> actual = uut.list(new Predicate<String>() {
			@Override
			public boolean apply(String name) {
				return name.endsWith(".zip");
			}
		});

		Set<String> expected = newHashSet("b.zip", "d.zip");

		assertEquals(expected, actual);
	}

	@Test
	public void directoriesAreCreatedOnInit() {
		setup();
		String newFolderName = relFile(tempFolder.getRoot().getAbsolutePath(), "test");
		new Directory(newFolderName);

		File newFolder = new File(newFolderName);
		assertTrue(newFolder.exists());
		assertTrue(newFolder.isDirectory());
	}

	@Test(expected = RuntimeException.class)
	public void creatingFolderOnExistingFile() throws IOException {
		setup();
		tempFolder.newFile("a.txt");
		new Directory(relFile(tempFileName, "a.txt"));
	}

	@Test
	public void findingFilesUsesPredicate() {
		createFileInSubDir("a.zip");
		createFileInSubDir("x.txt");

		Directory sut = new Directory(tempFileName);
		Set<String> actual = sut.findFiles(f -> f.endsWith(".zip"));
		Set<String> expected = Sets.newHashSet("a.zip");
		assertEquals(expected, actual);
	}

	@Test
	public void findingFilesRecursively() {
		createFileInSubDir("a.zip");
		createFileInSubDir("b.zip", "b");
		createFileInSubDir("c.zip", "c", "c");

		Directory sut = new Directory(tempFileName);
		Set<String> actual = sut.findFiles(f -> true);
		Set<String> expected = Sets.newHashSet("a.zip", combine("b", "b.zip"), combine("c", "c", "c.zip"));
		assertEquals(expected, actual);
	}

	@Test
	public void findingFilesInRelativeRoot() throws IOException {
		File root = new File("relativeFolder");
		try {
			root.mkdir();
			File zip = new File(root, "a.zip");
			zip.createNewFile();

			Directory sut = new Directory("relativeFolder");
			Set<String> actual = sut.findFiles(f -> true);
			Set<String> expected = Sets.newHashSet("a.zip");
			assertEquals(expected, actual);

		} finally {
			FileUtils.deleteQuietly(root);
		}
	}

	private String combine(String... pathElements) {
		return String.join(File.separator, pathElements);
	}

	@Test(expected = AssertionException.class)
	public void getParent_nonExisting() {
		Directory sut = new Directory(tempFileName);
		sut.getParentDirectory(relFile("a", "b", "c", "etc"));
	}

	@Test(expected = AssertionException.class)
	public void getParent_tryingToEscape() {
		Directory sut = new Directory(tempFileName);
		sut.getParentDirectory(".." + File.separator);
	}

	@Test
	public void getParent_realCase() throws IOException {
		Directory sut = new Directory(tempFileName);
		sut.createDirectory(relFile("a", "b")).writeContent("...", "c.txt");

		Directory d = sut.getParentDirectory(relFile("a", "b", "c.txt"));

		URL actual = d.getUrl();
		URL expected = new Directory(tempFileName + "/a/b").getUrl();
		assertEquals(expected, actual);
	}

	private void createFileInSubDir(String fileName, String... folders) {
		String folderPath = tempFileName + "/" + String.join("/", folders);
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String file = folderPath + "/" + fileName;
		try {
			new File(file).createNewFile();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void assertFileExists(String fileToCheck) {
		String fileName = tempFileName + "/" + fileToCheck;
		File file = new File(fileName);

		assertTrue(file.exists());
	}
}
