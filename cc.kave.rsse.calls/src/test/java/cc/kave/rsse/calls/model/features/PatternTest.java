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
import static cc.kave.rsse.calls.model.features.Pattern.PRECISION;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.rsse.calls.model.Dictionary;

public class PatternTest {

	private static final double PREC = PRECISION * 0.1;

	private static final IFeature F1 = mock(IFeature.class);
	private static final IFeature F2 = mock(IFeature.class);

	@Test
	public void constants() {
		assertEquals(6, Pattern.PRECISION_SCALE);
		assertEquals(0.000001, Pattern.PRECISION, PREC);
	}

	@Test
	public void defaultValues() {
		Pattern sut = new Pattern(123, arr(0.1), dict(F1));
		Assert.assertEquals(123, sut.numObservations);
		Assert.assertArrayEquals(arr(0.1), sut.cloneProbabilities(), PREC);
	}

	@Test
	public void defaultValues_noValues() {
		Pattern sut = new Pattern(123, dict(F1));
		Assert.assertEquals(123, sut.numObservations);
		Assert.assertArrayEquals(arr(0), sut.cloneProbabilities(), PREC);
	}

	@Test
	public void getProb() {
		Pattern sut = new Pattern(123, arr(0.123), dict(F1));
		Assert.assertEquals(0.123, sut.getProbability(F1), PREC);
	}

	@Test
	public void getProb_defaultsAreRounded() {
		Pattern sut = new Pattern(123, arr(0.1230001), dict(F1));
		Assert.assertEquals(0.123, sut.getProbability(F1), PREC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getProb_featureDoesNotExist() {
		Pattern sut = new Pattern(123, arr(0.123), dict(F1));
		sut.getProbability(F2);
	}

	@Test
	public void setProb() {
		Pattern sut = new Pattern(123, dict(F1));
		sut.setProbability(F1, 0.234);
		Assert.assertEquals(0.234, sut.getProbability(F1), PREC);
	}

	@Test
	public void setProb_rounding() {
		Pattern sut = new Pattern(123, dict(F1));
		sut.setProbability(F1, 0.2339995);
		Assert.assertEquals(0.234, sut.getProbability(F1), PREC);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setProb_featureDoesNotExist() {
		Pattern sut = new Pattern(123, arr(0.123), dict(F1));
		sut.setProbability(F2, 0.123);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setProb_tooSmall() {
		Pattern sut = new Pattern(123, arr(0.123), dict(F1));
		sut.setProbability(F2, -0.00001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setProb_tooBig() {
		Pattern sut = new Pattern(123, arr(0.123), dict(F1));
		sut.setProbability(F2, 1.00001);
	}

	@Test
	public void equality() {
		Pattern a = new Pattern(1, arr(0), dict(F1));
		Pattern b = new Pattern(1, arr(0), dict(F1));
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffNum() {
		Pattern a = new Pattern(1, arr(0), dict(F1));
		Pattern b = new Pattern(2, arr(0), dict(F1));
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffProbability() {
		Pattern a = new Pattern(1, arr(0), dict(F1));
		Pattern b = new Pattern(1, arr(0.1), dict(F1));
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffDict() {
		Pattern a = new Pattern(1, arr(0), dict(F1));
		Pattern b = new Pattern(1, arr(0), dict(F2));
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void clones() {
		Pattern a = new Pattern(123, arr(0.123), dict(F1));
		Pattern b = a.clone();
		assertEquals(a, b);
		assertNotSame(a, b);
	}

	@Test
	public void cloneProbability() {
		double[] in = arr(0.123);
		double[] out = new Pattern(123, in, dict(F1)).cloneProbabilities();
		assertArrayEquals(in, out, PREC);
		assertNotSame(in, out);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new Pattern(123, dict(F1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_numPositive_1() {
		new Pattern(0, dict(F1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_dictNull_1() {
		new Pattern(1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_dictEmpty_1() {
		new Pattern(1, new Dictionary<IFeature>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_numPositive_2() {
		new Pattern(0, arr(0), dict(F1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_dictNull_2() {
		new Pattern(1, arr(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_dictEmpty_2() {
		new Pattern(1, arr(), dict());
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_arrNull() {
		new Pattern(1, null, dict());
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_arrSizeNoMatch() {
		new Pattern(1, arr(0.1), dict(F1, F2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_arrProbTooSmall() {
		new Pattern(1, arr(-0.00001), dict(F1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void failInit_arrProbTooBig() {
		new Pattern(1, arr(1.00001), dict(F1));
	}

	private static double[] arr(double... values) {
		return values;
	}

	private static Dictionary<IFeature> dict(IFeature... fs) {
		Dictionary<IFeature> dict = new Dictionary<IFeature>();
		dict.addAll(asList(fs));
		return dict;
	}
}