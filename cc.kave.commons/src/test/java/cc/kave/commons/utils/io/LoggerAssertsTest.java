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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.LoggerAsserts;

public class LoggerAssertsTest {

	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void positiveMatching() {
		Logger.log("a");
		Logger.log("b");
		Logger.log("c");

		LoggerAsserts.assertLogContains(0, "a");
		LoggerAsserts.assertLogContains(1, "b");
		LoggerAsserts.assertLogContains(2, "c");
	}

	@Test(expected = AssertionError.class)
	public void negativeMatching() {
		Logger.log("abc");
		LoggerAsserts.assertLogContains(0, "xyz");
	}
}