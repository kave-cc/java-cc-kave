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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.utils.io.Logger;
import cc.kave.testcommons.LoggerAsserts;

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