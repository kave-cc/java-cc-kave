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
package cc.kave.testcommons;

import static org.junit.Assert.fail;

import java.util.List;

import cc.kave.commons.utils.io.Logger;

public class LoggerAsserts {
	public static void assertLogContains(int index, String expected) {
		List<String> log = Logger.getCapturedLog();
		String actual = log.get(index);
		if (!actual.contains(expected)) {
			fail(String.format(
					"incorrect log output.\nline %d is expected to contain:\n     %s\nbut was (trimmed):\n     '%s'",
					index, expected, actual.trim()));
		}
	}

	public static void assertLogContains(int index, String... expecteds) {
		for (String expected : expecteds) {
			assertLogContains(index++, expected);
		}
	}

	public static void dumpLog() {
		int i = 0;
		for (String s : Logger.getCapturedLog()) {
			System.out.printf("[%d:%s]\n", i++, s);
		}
	}
}