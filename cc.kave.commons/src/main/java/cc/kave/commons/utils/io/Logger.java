/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.commons.utils.io;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.google.common.collect.Lists;

public class Logger {

	private static PrintStream out = System.out;
	private static boolean isCapturing = false;
	private static boolean isPrinting = false;
	private static boolean isDebugging = false;

	private static List<String> log = Lists.newLinkedList();

	// TODO add info(String msg) method

	public static void log(String rawmsg, Object... args) {
		out = System.out;
		reallyLog("\n" + getTimeStamp() + "    " + rawmsg, args);
	}

	public static void err(String rawmsg, Object... args) {
		out = System.err;
		reallyLog("\n" + getTimeStamp() + " EE " + rawmsg, args);
	}

	public static void debug(String rawmsg, Object... args) {
		out = System.out;
		if (isDebugging) {
			reallyLog("\n" + getTimeStamp() + " ~~ " + rawmsg, args);
		}
	}

	public static void append(String rawmsg, Object... args) {
		reallyLog(rawmsg, args);
	}

	private static void reallyLog(String rawmsg, Object[] args) {
		String msg = String.format(rawmsg, args);
		if (isCapturing) {
			log.add(msg);
		}
		if (isPrinting) {
			out.print(msg);
		}
	}

	private static String getTimeStamp() {
		Calendar cal = Calendar.getInstance();
		DateFormat fmt = new SimpleDateFormat("MMM d, HH:mm:ss.SSS");
		String timestamp = fmt.format(cal.getTime());
		return timestamp;
	}

	public static void setCapturing(boolean isCapturing) {
		Logger.isCapturing = isCapturing;
	}

	public static boolean isCapturing() {
		return isCapturing;
	}

	public static List<String> getCapturedLog() {
		return Lists.newLinkedList(log);
	}

	public static void reset() {
		log = Lists.newLinkedList();
		isCapturing = false;
		isPrinting = false;
		isDebugging = false;
	}

	public static void setPrinting(boolean isPrinting) {
		Logger.isPrinting = isPrinting;
	}

	public static boolean isPrinting() {
		return isPrinting;
	}

	public static void setDebugging(boolean debugging) {
		isDebugging = debugging;
	}

	public static boolean isDebugging() {
		return isDebugging;
	}

	public static void clearLog() {
		log = Lists.newLinkedList();
	}
}