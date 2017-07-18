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
package cc.kave.commons.assertions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.exceptions.AssertionException;

public class AssertsTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test(expected = AssertionException.class)
	public void nullThrowsException() {
		Asserts.assertNotNull(null);
	}

	@Test
	public void nonNullDoesNotThrowsException() {
		Asserts.assertNotNull("non null");
	}

	@Test
	public void positivValues() {
		Asserts.assertNotNegative(1);
	}

	@Test
	public void zeroValue() {
		Asserts.assertNotNegative(0);
	}

	@Test(expected = AssertionException.class)
	public void negativeValues() {
		Asserts.assertNotNegative(-1);
	}

	@Test
	public void equalInts() {
		Asserts.assertEquals(1, 1);
	}

	@Test(expected = AssertionException.class)
	public void unequalInts() {
		Asserts.assertEquals(1, 2);
	}

	@Test
	public void assertNotNegative() {
		Asserts.assertNotNegative(1.0);
		Asserts.assertNotNegative(1);
	}

	@Test(expected = AssertionException.class)
	public void assertNotNegativeCanFailForDoubles() {
		Asserts.assertNotNegative(-1.0);
	}

	@Test(expected = AssertionException.class)
	public void assertNotNegativeCanFailForInts() {
		Asserts.assertNotNegative(-1);
	}

	@Test
	public void assertGreaterThan() {
		Asserts.assertGreaterThan(3.0, 2.0);
		Asserts.assertGreaterThan(3, 2);
	}

	@Test(expected = AssertionException.class)
	public void assertGreaterThanCanFailForDoubles() {
		Asserts.assertGreaterThan(3.0, 3.0);
	}

	@Test(expected = AssertionException.class)
	public void assertGreaterThanCanFailForInts() {
		Asserts.assertGreaterThan(2, 2);
	}

	@Test
	public void assertLessOrEqual() {
		Asserts.assertLessOrEqual(1.0, 2.0);
		Asserts.assertLessOrEqual(4.0, 4.0);
	}

	@Test(expected = AssertionException.class)
	public void assertLessOrEqualCanFail() {
		Asserts.assertLessOrEqual(4.0, 3.0);
	}

	@Test
	public void assertNull() {
		Asserts.assertNull(null);
	}

	@Test
	public void assertNullCanFail() {
		expectedEx.expect(AssertionException.class);
		expectedEx.expectMessage("expected null");

		Asserts.assertNull(new Object());
	}

	@Test
	public void initAssertsForCodeCoverage() {
		// don't blame the coder, blame the metric :P
		new Asserts();
	}
}