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
package cc.kave.rsse.calls.model.features;

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class PatternTest {

	private static final double DOUBLE_TRESHOLD = 0.0000001;

	@Test
	public void defaultValues() {
		Pattern sut = new Pattern("abc", 123);
		Assert.assertEquals("abc", sut.name);
		Assert.assertEquals(123, sut.numObservations);
	}

	@Test
	public void addingProbabilities() {
		IFeature a = mock(IFeature.class);
		IFeature b = mock(IFeature.class);

		Pattern sut = new Pattern("abc", 123);
		sut.setProbability(a, 0.123);
		sut.setProbability(b, 0.234);

		Assert.assertEquals(0.123, sut.getProbability(a), DOUBLE_TRESHOLD);
		Assert.assertEquals(0.234, sut.getProbability(b), DOUBLE_TRESHOLD);
	}

	@Test
	public void addingProbabilitiesRoundToMaxPrecision() {
		IFeature a = mock(IFeature.class);
		IFeature b = mock(IFeature.class);

		Pattern sut = new Pattern("abc", 123);
		sut.setProbability(a, 0.1230001);
		sut.setProbability(b, 0.2339999);

		Assert.assertEquals(0.123, sut.getProbability(a), DOUBLE_TRESHOLD);
		Assert.assertEquals(0.234, sut.getProbability(b), DOUBLE_TRESHOLD);
	}

	@Test
	public void unsetPropabilities() {
		double actual = new Pattern("p", 123).getProbability(mock(IFeature.class));
		Assert.assertEquals(0.0, actual, DOUBLE_TRESHOLD);
	}

	@Test
	public void patternsCanBeCloned() {
		IFeature a = mock(IFeature.class);
		IFeature b = mock(IFeature.class);

		Pattern orig = new Pattern("orig", 123);
		orig.setProbability(a, 0.1);
		orig.setProbability(b, 0.2);

		Pattern clone = orig.clone("other");

		assertEquals("other", clone.name);
		assertEquals(123, clone.numObservations);
		assertEquals(0.1, clone.getProbability(a), DOUBLE_TRESHOLD);
		assertEquals(0.2, clone.getProbability(b), DOUBLE_TRESHOLD);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new Pattern("p", 123));
	}

	@Test
	public void equality_default() {
		Pattern a = new Pattern("p", 123);
		Pattern b = new Pattern("p", 123);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_Values() {
		IFeature f = mock(IFeature.class);
		Pattern a = new Pattern("p", 123);
		a.setProbability(f, 0.123);
		Pattern b = new Pattern("p", 123);
		b.setProbability(f, 0.123);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffName() {
		Pattern a = new Pattern("p", 123);
		Pattern b = new Pattern("q", 123);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffNumObservations() {
		Pattern a = new Pattern("p", 123);
		Pattern b = new Pattern("p", 124);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffProbabilities() {
		IFeature f = mock(IFeature.class);
		Pattern a = new Pattern("p", 123);
		a.setProbability(f, 0.123);
		Pattern b = new Pattern("p", 123);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_minorDiffProbabilities() {
		IFeature f = mock(IFeature.class);
		Pattern a = new Pattern("p", 123);
		a.setProbability(f, 0.123);
		Pattern b = new Pattern("p", 123);
		b.setProbability(f, 0.1230001);
		assertEqualDataStructures(a, b);
	}

	@Test(expected = AssertionException.class)
	public void fail_nullName() {
		new Pattern(null, 123);
	}

	@Test(expected = AssertionException.class)
	public void fail_emptyName() {
		new Pattern("", 123);
	}

	@Test(expected = AssertionException.class)
	public void fail_numObservationsTooSmall() {
		new Pattern("p", 0);
	}

	@Test(expected = AssertionException.class)
	public void fail_addedProbabilityTooSmall() {
		new Pattern("p", 123).setProbability(mock(IFeature.class), -0.000001);
	}

	@Test(expected = AssertionException.class)
	public void fail_addedProbabilityTooBig() {
		new Pattern("p", 123).setProbability(mock(IFeature.class), 1.000001);
	}
}