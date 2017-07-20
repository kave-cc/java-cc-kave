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

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.WritingArchive;
import cc.kave.commons.utils.io.ZipFolder;

public class ZipFolderTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File _root;
	private ZipFolder _sut;

	@Before
	public void setup() throws IOException {
		_root = tmp.newFolder("data");
		_sut = new ZipFolder(_root.getAbsolutePath());
	}

	@Test
	public void FoldersAreMarkedOnCreationOfArchive() throws IOException {
		File markerFile = file(_root, ZipFolder.MARKER_FILE_NAME);
		assertFalse(markerFile.exists());

		_sut.createNewArchive();

		assertTrue(markerFile.exists());
	}

	@Test
	public void FolderMarkersContainMetaData() throws IOException {
		_sut = new ZipFolder(_root.getAbsolutePath(), "xyz");
		_sut.createNewArchive();

		String actual = FileUtils.readFileToString(file(_root, ZipFolder.MARKER_FILE_NAME));
		assertEquals("xyz", actual);
	}

	@Test
	public void ArchivesAreCreatedOnRequest() throws IOException {
		assertEquals(0, _sut.getNumArchives());

		try (WritingArchive wa = _sut.createNewArchive()) {
			wa.add("a");
			wa.add("b");
		}

		assertEquals(1, _sut.getNumArchives());

		try (WritingArchive wa = _sut.createNewArchive()) {
			wa.add("c");
			wa.add("d");
		}

		assertEquals(2, _sut.getNumArchives());
	}

	@Test
	public void NoFilesAreOverwritten() throws IOException {
		file(_root, "0.zip").createNewFile();
		file(_root, "1.zip").createNewFile();

		assertEquals(2, _sut.getNumArchives());

		try (WritingArchive wa = _sut.createNewArchive()) {
			wa.add("a");
		}

		assertEquals(3, _sut.getNumArchives());
	}

	@Test
	public void CorrectContentIsWrittenAndRead() throws IOException {
		try (WritingArchive wa = _sut.createNewArchive()) {
			wa.add("a");
			wa.add("b");
		}
		try (WritingArchive wa = _sut.createNewArchive()) {
			wa.add("c");
			wa.add("d");
		}

		List<ReadingArchive> ras = _sut.openAll();
		assertEquals(2, ras.size());

		for (ReadingArchive ra : ras) {
			List<String> entries = ra.getAll(String.class);

			assertEquals(2, entries.size());

			if (entries.contains("a")) {
				assertTrue(entries.contains("b"));
			} else {
				assertTrue(entries.contains("c"));
				assertTrue(entries.contains("d"));
			}
		}
	}

	@Test(expected = AssertionException.class)
	public void DirectoryHasToExist() throws IOException {
		File f = file(_root, "non", "existing", "folder");
		new ZipFolder(f.getAbsolutePath());
	}

	@Test(expected = AssertionException.class)
	public void DirectoryMustNotBeAFile() throws IOException {
		File f = file(_root, "f");
		f.createNewFile();
		new ZipFolder(f.getAbsolutePath());
	}

	private File file(File dir, String... parts) throws IOException {
		return Paths.get(dir.getAbsolutePath(), parts).toFile();
	}
}