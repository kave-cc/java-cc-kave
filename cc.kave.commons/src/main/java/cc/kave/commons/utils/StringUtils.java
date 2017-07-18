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
package cc.kave.commons.utils;

import static cc.kave.commons.assertions.Asserts.assertNotNull;
import static cc.kave.commons.assertions.Asserts.assertTrue;
import static cc.kave.commons.assertions.Asserts.fail;
import static java.util.Arrays.binarySearch;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

import cc.kave.commons.assertions.Asserts;

public class StringUtils {

	public static byte[] AsBytes(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	public static String AsString(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static String f(String string, Object... args) {
		return String.format(string, args);
	}

	public static boolean containsIgnoreCase(String strIn, String needle) {
		return strIn.toLowerCase().contains(needle.toLowerCase());
	}

	public static boolean containsAny(String string, String... needles) {
		for (String needle : needles) {
			if (string.contains(needle)) {
				return true;
			}
		}
		return false;
	}

	public static int FindNext(String str, int currentIndex, char... characters) {
		AssertIndexBoundaries(str, currentIndex);
		for (int i = currentIndex; i < str.length(); i++) {
			char c = str.charAt(i);
			if (contains(characters, c)) {
				return i;
			}
		}
		return -1;
	}

	private static boolean contains(char[] characters, char c) {
		for (char c2 : characters) {
			if (c2 == c) {
				return true;
			}
		}
		return false;
	}

	private static void AssertIndexBoundaries(String str, int currentIndex) {
		assertNotNull(str);
		if (currentIndex < 0 || currentIndex >= str.length()) {
			fail(f("index '%d' is out of bounds for string '%s'", currentIndex, str));
		}
	}

	public static int FindPrevious(String str, int currentIndex, char... characters) {
		AssertIndexBoundaries(str, currentIndex);
		for (int i = currentIndex; i >= 0; i--) {
			char c = str.charAt(i);
			if (contains(characters, c)) {
				return i;
			}
		}
		return -1;
	}

	private static final char[] openingBrackets = new char[] { '(', '<', '[', '{' };
	private static final char[] closingBrackets = new char[] { ')', '>', ']', '}' };

	public static int FindCorrespondingOpenBracket(String str, int currentIndex) {
		AssertIndexBoundaries(str, currentIndex);
		assertTrue(binarySearch(closingBrackets, str.charAt(currentIndex)) >= 0);

		char open = str.charAt(currentIndex);
		char close = GetCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i > 0; i--) {
			depth = UpdateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	public static int FindCorrespondingCloseBracket(String str, int currentIndex) {
		AssertIndexBoundaries(str, currentIndex);
		assertTrue(binarySearch(openingBrackets, str.charAt(currentIndex)) >= 0);

		char open = str.charAt(currentIndex);
		char close = GetCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i < str.length(); i++) {
			depth = UpdateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	private static int UpdateDepth(int depth, char open, char close, char current) {
		if (current == open) {
			return depth + 1;
		}
		if (current == close) {
			return depth - 1;
		}
		return depth;
	}

	public static char GetCorresponding(char c) {
		switch (c) {
		case '(':
			return ')';
		case ')':
			return '(';
		case '{':
			return '}';
		case '}':
			return '{';
		case '[':
			return ']';
		case ']':
			return '[';
		case '<':
			return '>';
		case '>':
			return '<';
		default:
			throw new InvalidParameterException(String.format("no supported bracket type: %c", c));
		}
	}

	public static String TakeUntil(String strIn, char... needles) {
		StringBuilder sb = new StringBuilder();
		int cur = 0;
		char c;
		while (cur < strIn.length() && !contains(needles, (c = strIn.charAt(cur++)))) {
			sb.append(c);
		}
		return sb.toString();
	}

	public static String insert(String original, int index, String addition) {
		Asserts.assertFalse(original == null);
		Asserts.assertFalse(addition == null);
		Asserts.assertFalse(index < 0);
		Asserts.assertFalse(index > original.length());
		String start = original.substring(0, index);
		String end = original.substring(index, original.length());
		return start + addition + end;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String remove(String orig, int startIdx, int count) {
		Asserts.assertFalse(orig == null);
		Asserts.assertFalse(startIdx < 0);
		Asserts.assertFalse(startIdx > orig.length());
		Asserts.assertFalse(count < 0);
		Asserts.assertFalse(startIdx + count > orig.length());
		String start = orig.substring(0, startIdx);
		String end = orig.substring(startIdx + count, orig.length());
		return start + end;
	}

	public static String repeat(char c, int count) {
		Asserts.assertTrue(count >= 0);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(c);
		}
		return sb.toString();
	}
}