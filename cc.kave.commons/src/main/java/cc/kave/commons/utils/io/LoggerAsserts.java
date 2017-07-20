/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.commons.utils.io;

import static org.junit.Assert.fail;

import java.util.List;

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