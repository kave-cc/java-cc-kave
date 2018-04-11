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
package cc.kave.rsse.calls.recs.pbn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class PBNModelTest {

	@Before
	public void setup() {
	}

	@Test
	public void todo() {
		// TODO test me
		Assert.fail("test me!");
	}

	@Test(expected = AssertionException.class)
	public void validate() {
		new PBNModel().assertValidity();
	}

	@Test(expected = AssertionException.class)
	public void validate2() {
		// TODO test me
		Assert.fail("test me!");
	}

	@Test
	public void equality_default() {
		PBNModel a = new PBNModel();
		PBNModel b = new PBNModel();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_withValues() {
		PBNModel a = new PBNModel();
		a.patternProbabilities = new double[] { 0.1, 0.2 };
		PBNModel b = new PBNModel();
		b.patternProbabilities = new double[] { d(0.1), d(0.2) };
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	private static double d(double d) {
		return d + 0.00000000001;
	}

	@Test
	public void equality_diffPatterns() {
		PBNModel a = new PBNModel();
		a.patternProbabilities = new double[] { 0.1, 0.2 };
		PBNModel b = new PBNModel();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_diffX() {
		Assert.fail("test me!");
	}
}