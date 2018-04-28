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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonFileNamingTest {

	@Test
	public void happyPath() {
		assertPath(1, "1");
	}

	@Test
	public void replacesDots() {
		assertPath(0.123, "0/123");
	}

	@Test
	public void removesQuotes() {
		assertPath("a\"b", "ab");
	}

	@Test
	public void remosedNestedJsonQuotes() {
		assertPath("a\\\"b", "ab");
	}

	@Test
	public void replacesBackslashes() {
		assertPath("a\b", "a/b");
	}

	@Test
	public void replacesNonFileChars() {
		assertPath(";:[]{}|,.<>`~!@#$%^&*()_-+=", "__[]{}_,/_______$____()_-+_");
	}

	private void assertPath(Object o, String expected) {
		String actual = new JsonFileNaming<Object>().getRelativePath(o);
		assertEquals(expected, actual);
	}
}