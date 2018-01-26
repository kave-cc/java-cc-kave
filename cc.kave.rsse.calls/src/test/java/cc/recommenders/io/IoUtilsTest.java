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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

import cc.kave.commons.utils.io.Directory;

public class IoUtilsTest {

	private String type;
	private IoUtils sut;

	@Before
	public void setup() {
		type = "La/B";
		sut = new IoUtils();
	}

	@Test
	public void randomDirIsSubfolderOfTmp() throws IOException {
		URL actual = sut.getRandomTempDirectory().getUrl();
		URL expected = File.createTempFile(IoUtils.TEMP_PREFIX, "").toURI().toURL();

		int len = expected.toString().indexOf(IoUtils.TEMP_PREFIX);

		String actual2 = actual.toString().substring(0, len);
		String expected2 = expected.toString().substring(0, len);

		assertEquals(expected2, actual2);
	}

	@Test
	public void randomFileIsSubfolderOfTmp() throws IOException {
		String actual = sut.getRandomTempFile();
		String expected = File.createTempFile(IoUtils.TEMP_PREFIX, "").getAbsolutePath();

		int len = expected.toString().indexOf(IoUtils.TEMP_PREFIX);

		String actual2 = actual.substring(0, len);
		String expected2 = expected.substring(0, len);

		assertEquals(expected2, actual2);
	}

	@Test
	public void parentOfSomething() throws MalformedURLException {
		File tmpDir = Files.createTempDir();
		String tmp = tmpDir.getAbsolutePath();
		String parent = tmpDir.getParent();
		URL actual = sut.getParentDirectory(tmp).getUrl();
		URL expected = new Directory(parent).getUrl();
		assertEquals(expected, actual);
	}

	@Test
	public void parentOfRoot_Unixoid() throws MalformedURLException {
		Assume.assumeFalse("test cannot be run in Windows", SystemUtils.IS_OS_WINDOWS);

		URL actual = sut.getParentDirectory("/").getUrl();
		URL expected = new Directory("/").getUrl();
		assertEquals(expected, actual);
	}

	@Test
	public void parentOfRoot_Windows() throws MalformedURLException {
		Assume.assumeTrue("test can only be run in Windows", SystemUtils.IS_OS_WINDOWS);

		URL actual = sut.getParentDirectory("c:\\").getUrl();
		URL expected = new Directory("c:\\").getUrl();
		assertEquals(expected, actual);
	}

	@Test
	public void toNestedFileName() {
		String actual = sut.toNestedFileName(type);
		String expected = "La/B";
		assertEquals(expected, actual);
	}

	@Test
	public void toNestedFileNameExt() {
		String actual = sut.toNestedFileName(type, "cde");
		String expected = "La/B.cde";
		assertEquals(expected, actual);
	}

	@Test
	public void toNestedDirectoryName() {
		String actual = sut.toNestedDirectoryName(type);
		String expected = "La/B/";
		assertEquals(expected, actual);
	}

	@Test
	public void toFlatFileName() {
		String actual = sut.toFlatFileName(type);
		String expected = "La_B";
		assertEquals(expected, actual);
	}

	@Test
	public void toFlatFileNameExt() {
		String actual = sut.toFlatFileName(type, "cde");
		String expected = "La_B.cde";
		assertEquals(expected, actual);
	}

	@Test
	public void toFlatDirectoryName() {
		String actual = sut.toFlatDirectoryName(type);
		String expected = "La_B/";
		assertEquals(expected, actual);
	}
}