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
package cc.kave.rsse.calls.mining;

import static cc.kave.rsse.calls.model.Constants.UNKNOWN_CCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_DF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_MCF;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class VectorBuilderTest {

	private static final double DELTA = 0.00000001;

	private static final TypeFeature TF1 = mock(TypeFeature.class);
	private static final ClassContextFeature CCF1 = mock(ClassContextFeature.class);
	private static final MethodContextFeature MCF1 = mock(MethodContextFeature.class);
	private static final DefinitionFeature DF1 = mock(DefinitionFeature.class);
	private static final UsageSiteFeature CALL1 = call(1);
	private static final UsageSiteFeature PARAM1 = param(1);
	private static final UsageSiteFeature MEMBER1 = member(1);

	private Dictionary<IFeature> dict;
	private VectorBuilder sut;

	@Before
	public void setup() {
		setup(b -> b.cCtx(0.1).mCtx(0.2).def(0.3).calls(0.4).params(0.5).members(0.6));
		dict = new Dictionary<IFeature>();
		dict(UNKNOWN_CCF, UNKNOWN_MCF, UNKNOWN_DF);
	}

	private void setup(Consumer<OptionsBuilder> c) {
		OptionsBuilder b = new OptionsBuilder("...");
		b.cCtx(false).mCtx(false).def(false).calls(false).params(false).members(false);
		c.accept(b);
		sut = new VectorBuilder(b.get());
	}

	private void dict(IFeature... fs) {
		dict.addAll(asList(fs));
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_cannotDisableAllUsageSites() {
		setup(b -> b.calls(false).params(false).members(false));
	}

	@Test
	public void happyPath_toX() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);

		assertDouble(in, v(0, 0, 0, 1, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6));
		assertBool(in, v("0001111111"));
	}

	@Test
	public void repetitionIsHandled() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, MCF1, DF1, CALL1, CALL1, CALL1);

		assertDouble(in, v(0, 0, 0, 1, 0.1, 0.2, 0.3, 0.4, 0, 0));
		assertBool(in, v("0001111100"));
	}

	@Test
	public void unknownsAreSetWithCorrectWeight() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, UNKNOWN_CCF, UNKNOWN_MCF, UNKNOWN_DF, CALL1);

		assertDouble(in, v(0.1, 0.2, 0.3, 1, 0, 0, 0, 0.4, 0, 0));
		assertBool(in, v("1111000100"));
	}

	@Test
	public void unknownCCtxIsSetOnDemand() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, mock(ClassContextFeature.class), MCF1, DF1, CALL1);

		assertDouble(in, v(0.1, 0, 0, 1, 0, 0.2, 0.3, 0.4, 0, 0));
		assertBool(in, v("1001011100"));
	}

	@Test
	public void unknownMCtxIsSetOnDemand() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, mock(MethodContextFeature.class), DF1, CALL1);

		assertDouble(in, v(0, 0.2, 0, 1, 0.1, 0, 0.3, 0.4, 0, 0));
		assertBool(in, v("0101101100"));
	}

	@Test
	public void unknownDefIsSetOnDemand() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, MCF1, mock(DefinitionFeature.class), CALL1);

		assertDouble(in, v(0, 0, 0.3, 1, 0.1, 0.2, 0, 0.4, 0, 0));
		assertBool(in, v("0011110100"));
	}

	@Test
	public void unknownUsageSiteIsIgnored() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, MCF1, DF1, CALL1, mock(UsageSiteFeature.class));

		assertDouble(in, v(0, 0, 0, 1, 0.1, 0.2, 0.3, 0.4, 0, 0));
		assertBool(in, v("0001111100"));
	}

	@Test
	public void dropCasesThatDoNotHaveUsageSitesAtTheEnd_Double() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> in = asList(TF1, CCF1, MCF1, DF1, mock(UsageSiteFeature.class));

		assertNoDouble(in);
		assertNoBool(in);
	}

	@Test
	public void happyPath_toPattern() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);

		double[] in = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
		double[] expected = v(0, 0, 0, 1, 0.1 / 0.1, 0.1 / 0.2, 0.1 / 0.3, 0.1 / 0.4, 0.1 / 0.5, 0.1 / 0.6);
		assertPattern(in, expected);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toPattern_failsWithInvalidArrSize() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] in = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1);
		sut.toPattern(1, in, dict);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toPattern_failsWithInvalidCount() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] in = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
		sut.toPattern(-1, in, dict);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toPatternFailsWithInvalidWeight_tooSmall() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] in = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, -0.00001);
		sut.toPattern(1, in, dict);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toPatternFailsWithInvalidWeight_tooBig() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] in = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 1.000001 * 0.6);
		sut.toPattern(1, in, dict);
	}

	@Test
	public void illegal_severalParams() {
		// valid params
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> fs = asList(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] arr = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);

		// dict is null
		assertFail(() -> sut.toDoubleArrays(asList(fs), null), IllegalArgumentException.class);
		assertFail(() -> sut.toDoubleArray(fs, null), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArrays(asList(fs), null), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArray(fs, null), IllegalArgumentException.class);
		assertFail(() -> sut.toPattern(1, arr, null), IllegalArgumentException.class);

		// main param is null
		assertFail(() -> sut.toDoubleArrays(null, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toDoubleArray(null, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArrays(null, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArray(null, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toPattern(1, null, dict), IllegalArgumentException.class);

		// empty lists
		assertFail(() -> sut.toDoubleArrays(asList(), dict), IllegalArgumentException.class);
		assertFail(() -> sut.toDoubleArray(asList(), dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArrays(asList(), dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArray(asList(), dict), IllegalArgumentException.class);
	}

	@Test
	public void illegal_dictIsMissingUnknownCCF() {
		dict = new Dictionary<IFeature>();
		dict(UNKNOWN_MCF, UNKNOWN_DF);
		testDict();
	}

	@Test
	public void illegal_dictIsMissingUnknownMCF() {
		dict = new Dictionary<IFeature>();
		dict(UNKNOWN_CCF, UNKNOWN_DF);
		testDict();
	}

	@Test
	public void illegal_dictIsMissingUnknownDF() {
		dict = new Dictionary<IFeature>();
		dict(UNKNOWN_CCF, UNKNOWN_MCF);
		testDict();
	}

	private void testDict() {
		dict(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		List<IFeature> fs = asList(TF1, CCF1, MCF1, DF1, CALL1, PARAM1, MEMBER1);
		double[] arr = v(0, 0, 0, 1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);

		assertFail(() -> sut.toDoubleArrays(asList(fs), dict), IllegalArgumentException.class);
		assertFail(() -> sut.toDoubleArray(fs, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArrays(asList(fs), dict), IllegalArgumentException.class);
		assertFail(() -> sut.toBoolArray(fs, dict), IllegalArgumentException.class);
		assertFail(() -> sut.toPattern(1, arr, dict), IllegalArgumentException.class);
	}

	private void assertBool(List<IFeature> usage, boolean[] expected) {
		boolean[] actual = sut.toBoolArray(usage, dict).get();
		if (!Arrays.equals(expected, actual)) {
			fail(format("Arrays not equal. Expected\n%s\nbut was\n%s\n", Arrays.toString(expected),
					Arrays.toString(actual)));
		}

		List<boolean[]> actuals = sut.toBoolArrays(asList(usage), dict);
		assertEquals(1, actuals.size());
		actual = actuals.get(0);
		assertTrue(Arrays.equals(expected, actual));
	}

	private void assertDouble(List<IFeature> usage, double[] expected) {
		double[] actual = sut.toDoubleArray(usage, dict).get();
		Assert.assertArrayEquals(expected, actual, DELTA);

		List<double[]> actuals = sut.toDoubleArrays(asList(usage), dict);
		assertEquals(1, actuals.size());
		actual = actuals.get(0);
		assertArrayEquals(expected, actual, DELTA);
	}

	private void assertNoBool(List<IFeature> usage) {
		Assert.assertFalse(sut.toBoolArray(usage, dict).isPresent());
		List<boolean[]> actuals = sut.toBoolArrays(asList(usage), dict);
		assertEquals(0, actuals.size());
	}

	private void assertNoDouble(List<IFeature> usage) {
		Assert.assertFalse(sut.toDoubleArray(usage, dict).isPresent());
		List<double[]> actuals = sut.toDoubleArrays(asList(usage), dict);
		assertEquals(0, actuals.size());
	}

	private void assertPattern(double[] weightedArr, double[] expectedArr) {
		Pattern actual = sut.toPattern(13, weightedArr, dict);
		Pattern expected = new Pattern(13, expectedArr, dict);
		assertEquals(expected, actual);
	}

	private static double[] v(double... arr) {
		return arr;
	}

	private static boolean[] v(String s) {
		boolean[] arr = new boolean[s.length()];
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case '0':
				arr[i] = false;
				break;
			case '1':
				arr[i] = true;
				break;
			default:
				throw new IllegalArgumentException(format("Unexpected char '%c' in string '%s'.", s.charAt(i), s));
			}
		}
		return arr;
	}

	private static <T extends Exception> void assertFail(Runnable r, Class<T> classOfT) {
		boolean hasFailed = false;
		try {
			r.run();
		} catch (Exception e) {
			if (classOfT.isInstance(e)) {
				hasFailed = true;
			} else {
				String myMsg = String.format("Unexpected exception thrown. Expected '%s', but caught '%s' instead.",
						classOfT.getSimpleName(), e.getClass().getSimpleName());
				throw new AssertionError(myMsg, e);
			}
		}
		if (!hasFailed) {
			fail("Unexpected, runnable did not throw an exception.");
		}
	}

	private static UsageSiteFeature call(int i) {
		return new UsageSiteFeature(UsageSites.call(format("[p:void] [p:int].m%d()", i)));
	}

	private static UsageSiteFeature param(int i) {
		return new UsageSiteFeature(callParameter(format("set get [p:int] [T, P].m%d([p:int] p)", i), 0));
	}

	private static UsageSiteFeature member(int i) {
		return new UsageSiteFeature(fieldAccess(format("[p:int] [T, P]._f%d", i)));
	}
}