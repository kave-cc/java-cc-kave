/**
 * Copyright (c) 2010-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.commons.utils.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.io.Logger;

public class LoggerTest {

	private PrintStream origOut;
	private PrintStream origErr;

	private StringBuilder logBuilder;
	private StringBuilder errBuilder;

	@Before
	public void setup() {
		origOut = System.out;
		origErr = System.err;

		logBuilder = new StringBuilder();
		errBuilder = new StringBuilder();
		System.setOut(createStream(logBuilder));
		System.setErr(createStream(errBuilder));

		Logger.reset();
	}

	@After
	public void teardown() {
		System.setOut(origOut);
		System.setErr(origErr);
	}

	@Test
	public void testingDefautls() {
		assertFalse(Logger.isCapturing());
		assertFalse(Logger.isPrinting());
		assertFalse(Logger.isDebugging());
	}

	@Test
	public void customPrintStreamsAreRegisterdAndCustomAssertsAreWorking() {
		System.out.println("A");
		System.err.println("B");

		assertLog(String.format("A%n"));
		assertErr(String.format("B%n"));
	}

	@Test
	public void logCanBeDisabled() {
		Logger.setPrinting(false);

		Logger.log("a");
		Logger.log("b");
		assertLog("");
		assertErr("");
	}

	@Test
	@Ignore("untested date extension")
	public void printingCanBeTurnedOn() {
		Logger.setPrinting(true);

		Logger.log("a");
		Logger.err("b");

		assertLog("    a");
		assertErr(" EE b");
	}

	@Test
	public void capturingCanBeDisabled() {
		Logger.setCapturing(false);

		Logger.log("a");
		Logger.err("b");

		List<String> actual = Logger.getCapturedLog();
		List<String> expected = Lists.newArrayList();
		assertEquals(expected, actual);
	}

	@Test
	@Ignore("untested date extension")
	public void capturingCanBeTurnedOn() {
		Logger.setCapturing(true);

		Logger.log("a");
		Logger.err("b");

		List<String> actual = stripTime(Logger.getCapturedLog());
		List<String> expected = Lists.newArrayList("    a", " EE b");
		assertEquals(expected, actual);
	}

	@Test
	public void logCanBeCleared() {
		Logger.setCapturing(true);

		Logger.log("a");
		Logger.clearLog();
		List<String> actual = Logger.getCapturedLog();
		List<String> expected = Lists.newArrayList();
		assertEquals(expected, actual);
	}
	@Test
	public void byDefaultDebugIsNotPrinted() {
		Logger.setPrinting(true);

		Logger.debug("a");
		assertLog("");
	}

	@Test
	@Ignore("untested date extension")
	public void debuggingCanBeActivated() {
		Logger.setPrinting(true);
		Logger.setDebugging(true);

		Logger.debug("a");
		assertLog(" ~~ a");
	}

	@Test
	public void initLoggerForCodeCoverage() {
		new Logger();
	}

	@Test
	public void getCapturedLogCreatesCopy_reset() {
		Logger.setCapturing(true);
		Logger.append("a");
		List<String> actual = Logger.getCapturedLog();
		Logger.reset();
		List<String> expected = Lists.newArrayList("a");
		assertEquals(expected, actual);
	}

	@Test
	public void getCapturedLogCreatesCopy_clearLog() {
		Logger.setCapturing(true);
		Logger.append("a");
		List<String> actual = Logger.getCapturedLog();
		Logger.clearLog();
		List<String> expected = Lists.newArrayList("a");
		assertEquals(expected, actual);
	}

	@Test
	public void getCapturedLogCreatesCopy_append() {
		Logger.setCapturing(true);
		Logger.append("a");
		List<String> actual = Logger.getCapturedLog();
		Logger.append("b");
		List<String> expected = Lists.newArrayList("a");
		assertEquals(expected, actual);
	}
	
	private void assertErr(String expected) {
		String actual = stripTime(errBuilder.toString());
		assertEquals(expected, actual);
	}

	private void assertLog(String expected) {
		String actual = stripTime(logBuilder.toString());
		assertEquals(expected, actual);
	}

	private static PrintStream createStream(final StringBuilder log) {
		return new PrintStream(new OutputStream() {
			@Override
			public void write(final int b) throws IOException {
				log.append(String.valueOf((char) b));
			}
		});
	}

	private List<String> stripTime(List<String> capturedLog) {
		List<String> trimmed = Lists.newLinkedList();
		for (String in : capturedLog) {
			trimmed.add(stripTime(in));
		}
		return trimmed;
	}

	private String stripTime(String msg) {
		Pattern pattern = Pattern.compile(".*(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d).*");
		Matcher matcher = pattern.matcher(msg);

		if (!matcher.matches()) {
			return msg;
		}

		for (int i = 1; i <= matcher.groupCount(); i++) {
			String match = matcher.group(i);
			msg = msg.replace(match, "");
		}
		return msg;
	};
}