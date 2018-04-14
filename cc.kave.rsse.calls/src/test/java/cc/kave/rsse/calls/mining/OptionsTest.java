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

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;

public class OptionsTest {

	private static final double DELTA = 0.0000001;

	@Test
	public void base() {
		Options[] suts = new Options[] { new Options("xxx", 0, 0, 0, 0, 0, 0, 1, 0, new HashMap<>()),
				new Options("APP[xxx]") };

		for (Options sut : suts) {
			assertEquals("xxx", sut.approachName);
			assertEquals(0.0, sut.weightClassCtx, DELTA);
			assertEquals(0.0, sut.weightMethodCtx, DELTA);
			assertEquals(0.0, sut.weightDef, DELTA);
			assertEquals(0.0, sut.weightCalls, DELTA);
			assertEquals(0.0, sut.weightParams, DELTA);
			assertEquals(0.0, sut.weightMembers, DELTA);
			assertEquals(1, sut.keepOnlyFeaturesWithAtLeastOccurrences);
			assertEquals(0.0, sut.minProbability, DELTA);
			assertOptions(sut, "APP[xxx]");
		}
	}

	@Test
	public void casesForWeightParsing() {
		assertEquals(0.0, new Options("APP[...]").weightClassCtx, DELTA);
		assertEquals(1.0, new Options("APP[...]+CCTX").weightClassCtx, DELTA);
		assertEquals(1.0, new Options("APP[...]+CCTX+MCTX").weightClassCtx, DELTA);
		assertEquals(0.5, new Options("APP[...]+CCTX(0.5)").weightClassCtx, DELTA);
		assertEquals(0.5, new Options("APP[...]+CCTX(0.5)+MCTX").weightClassCtx, DELTA);
	}

	@Test
	public void useClassCtx() {
		assertFalse(new Options("APP[...]").useClassCtx());
		assertTrue(new Options("APP[...]+CCTX(0.01)").useClassCtx());
	}

	@Test
	public void useMethodCtx() {
		assertFalse(new Options("APP[...]").useMethodCtx());
		assertTrue(new Options("APP[...]+MCTX(0.01)").useMethodCtx());
	}

	@Test
	public void useDef() {
		assertFalse(new Options("APP[...]").useDef());
		assertTrue(new Options("APP[...]+DEF(0.01)").useDef());
	}

	@Test
	public void useCalls() {
		assertFalse(new Options("APP[...]").useCalls());
		assertTrue(new Options("APP[...]+CALLS(0.01)").useCalls());
	}

	@Test
	public void useParams() {
		assertFalse(new Options("APP[...]").useParams());
		assertTrue(new Options("APP[...]+PARAMS(0.01)").useParams());
	}

	@Test
	public void useMembers() {
		assertFalse(new Options("APP[...]").useMembers());
		assertTrue(new Options("APP[...]+MEMBERS(0.01)").useMembers());
	}

	@Test
	public void getInt() {
		Options sut = new Options("APP[...]+OPTS[i:1]");
		assertEquals(1, sut.getOptAsInt("i"));
	}

	@Test
	public void getDouble() {
		Options sut = new Options("APP[...]+OPTS[d:0.123]");
		assertEquals(0.123, sut.getOptAsDouble("d"), DELTA);
	}

	@Test
	public void getBool() {
		Options sut = new Options("APP[...]+OPTS[b:TrUe]");
		assertTrue(sut.getOptAsBool("b"));
	}

	private enum TestEnum {
		A, B
	}

	@Test
	public void getEnum() {
		Options sut = new Options("APP[...]+OPTS[e:B]");
		assertEquals(TestEnum.B, sut.getOptAsEnum("e", TestEnum.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getEnum_fail() {
		Options sut = new Options("APP[...]+OPTS[e:C]");
		sut.getOptAsEnum("e", TestEnum.class);
	}

	@Test
	public void rounding_down() {
		Options[] suts = new Options[] {
				new Options("xxx", 0.1149, 0.2249, 0.3349, 0.4449, 0.5549, 0.6649, 1, 0.7749, new HashMap<>()),
				new Options(
						"APP[xxx]+CCTX(0.1149)+MCTX(0.2249)+DEF(0.3349)+CALLS(0.4449)+PARAMS(0.5549)+MEMBERS(0.6649)+P_MIN(0.7749)") };

		for (Options sut : suts) {
			assertEquals(0.11, sut.weightClassCtx, DELTA);
			assertEquals(0.22, sut.weightMethodCtx, DELTA);
			assertEquals(0.33, sut.weightDef, DELTA);
			assertEquals(0.44, sut.weightCalls, DELTA);
			assertEquals(0.55, sut.weightParams, DELTA);
			assertEquals(0.66, sut.weightMembers, DELTA);
			assertEquals(0.77, sut.minProbability, DELTA);
			assertOptions(sut,
					"APP[xxx]+CCTX(0.11)+MCTX(0.22)+DEF(0.33)+CALLS(0.44)+PARAMS(0.55)+MEMBERS(0.66)+P_MIN(0.77)");
		}
	}

	@Test
	public void rounding_up() {
		Options[] suts = new Options[] {
				new Options("xxx", 0.115, 0.225, 0.335, 0.445, 0.555, 0.665, 1, 0.775, new HashMap<>()), new Options(
						"APP[xxx]+CCTX(0.115)+MCTX(0.225)+DEF(0.335)+CALLS(0.445)+PARAMS(0.555)+MEMBERS(0.665)+P_MIN(0.775)") };

		for (Options sut : suts) {
			assertEquals(0.12, sut.weightClassCtx, DELTA);
			assertEquals(0.23, sut.weightMethodCtx, DELTA);
			assertEquals(0.34, sut.weightDef, DELTA);
			assertEquals(0.45, sut.weightCalls, DELTA);
			assertEquals(0.56, sut.weightParams, DELTA);
			assertEquals(0.67, sut.weightMembers, DELTA);
			assertEquals(0.78, sut.minProbability, DELTA);
			assertOptions(sut,
					"APP[xxx]+CCTX(0.12)+MCTX(0.23)+DEF(0.34)+CALLS(0.45)+PARAMS(0.56)+MEMBERS(0.67)+P_MIN(0.78)");
		}
	}

	@Test
	public void classCtx() {
		Function<Double, Options> f = d -> new Options("xxx", d, 0, 0, 0, 0, 0, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+CCTX(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+CCTX(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+CCTX(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+CCTX(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+CCTX");
	}

	@Test
	public void methodCtx() {
		Function<Double, Options> f = d -> new Options("xxx", 0, d, 0, 0, 0, 0, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+MCTX(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+MCTX(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+MCTX(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+MCTX(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+MCTX");
	}

	@Test
	public void def() {
		Function<Double, Options> f = d -> new Options("xxx", 0, 0, d, 0, 0, 0, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+DEF(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+DEF(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+DEF(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+DEF(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+DEF");
	}

	@Test
	public void calls() {
		Function<Double, Options> f = d -> new Options("xxx", 0, 0, 0, d, 0, 0, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+CALLS(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+CALLS(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+CALLS(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+CALLS(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+CALLS");
	}

	@Test
	public void params() {
		Function<Double, Options> f = d -> new Options("xxx", 0, 0, 0, 0, d, 0, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+PARAMS(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+PARAMS(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+PARAMS(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+PARAMS(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+PARAMS");
	}

	@Test
	public void members() {
		Function<Double, Options> f = d -> new Options("xxx", 0, 0, 0, 0, 0, d, 1, 0, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+MEMBERS(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+MEMBERS(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+MEMBERS(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+MEMBERS(0.99)");
		assertOptions(f.apply(1.0), "APP[xxx]+MEMBERS");
	}

	@Test
	public void atLeast() {
		Function<Integer, Options> f = num -> new Options("xxx", 0, 0, 0, 0, 0, 0, num, 0, new HashMap<>());
		assertOptions(f.apply(1), "APP[xxx]");
		assertOptions(f.apply(2), "APP[xxx]+ATLEAST(2)");
	}

	@Test
	public void minProbability() {
		Function<Double, Options> f = d -> new Options("xxx", 0, 0, 0, 0, 0, 0, 1, d, new HashMap<>());
		assertOptions(f.apply(0.0), "APP[xxx]");
		assertOptions(f.apply(0.001), "APP[xxx]");
		assertOptions(f.apply(0.3449), "APP[xxx]+P_MIN(0.34)");
		assertOptions(f.apply(0.345), "APP[xxx]+P_MIN(0.35)");
		assertOptions(f.apply(0.5), "APP[xxx]+P_MIN(0.50)");
		assertOptions(f.apply(0.9949), "APP[xxx]+P_MIN(0.99)");
	}

	@Test
	public void opts() {
		Function<Map<String, String>, Options> f = m -> new Options("xxx", 0, 0, 0, 0, 0, 0, 1, 0, m);

		Map<String, String> opts = new LinkedHashMap<>();
		assertOptions(f.apply(opts), "APP[xxx]");
		opts.put("a", "1");
		assertOptions(f.apply(opts), "APP[xxx]+OPTS[a:1]");
		opts.put("b", "2");
		assertOptions(f.apply(opts), "APP[xxx]+OPTS[a:1;b:2]");

		// make sure that order is stable
		StringBuilder sb = new StringBuilder("APP[xxx]+OPTS[a:1;b:2");
		Random rnd = new Random();
		for (int i = 1; i < 10; i++) {
			String key = UUID.randomUUID().toString();
			String value = String.valueOf(rnd.nextInt());
			opts.put(key, value);
			sb.append(';').append(key).append(':').append(value);
		}
		sb.append(']');
		assertOptions(f.apply(opts), sb.toString());
	}

	@Test(expected = AssertionException.class)
	public void fail_str() {
		// this shows that helper is called, all other cases are only provoked with the
		// second constructor that can suffer from more cases and that is easier to test
		new Options("APP[xxx]+CCTX(12.34)");
	}

	@Test
	public void fail_invalidApp() {
		for (String app : new String[] { null, "", ";", ":", "[", "]", "\n", "\t", " " }) {
			assertFail(() -> new Options(app, 0, 0, 0, 0, 0, 0, 1, 0, new HashMap<>()));
		}
	}

	@Test
	public void fail_invalidWeights() {
		for (double i : new double[] { -0.1, 1.1 }) {
			assertFail(() -> new Options("x", i, 0, 0, 0, 0, 0, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, i, 0, 0, 0, 0, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, 0, i, 0, 0, 0, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, 0, 0, i, 0, 0, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, 0, 0, 0, i, 0, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, 0, 0, 0, 0, i, 1, 0, new HashMap<>()));
			assertFail(() -> new Options("x", 0, 0, 0, 0, 0, 0, 1, i, new HashMap<>()));
		}
		// min probability of "1" does not make sense
		assertFail(() -> new Options("x", 0, 0, 0, 0, 0, 0, 1, 1, new HashMap<>()));
	}

	@Test
	public void fail_invalidAtLeast() {
		assertFail(() -> new Options("x", 0, 0, 0, 0, 0, 0, 0, 0, new HashMap<>()));
	}

	private static void assertFail(Runnable r) {
		try {
			r.run();
			fail();
		} catch (AssertionException e) {
			// exception is expected
		}
	}

	private static void assertOptions(Options a, String s) {
		Options b = new Options(s);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(s, a.toString());
		assertEquals(s, b.toString());
	}

	@Test
	public void equality_default() {
		Options a = new Options("xxx", 0, 0, 0, 0, 0, 0, 1, 0, new HashMap<>());
		Options b = new Options("xxx", 0, 0, 0, 0, 0, 0, 1, 0, new HashMap<>());
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_withValues() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 2, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 2, 0.7, values);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffApp() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("yyy", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffCCtx() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.2, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMCtx() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.3, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffDef() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.4, 0.4, 0.5, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffCalls() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.5, 0.5, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffParams() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.6, 0.6, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMember() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.7, 1, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffAtLeast() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 2, 0.7, values);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMinProb() {
		Map<String, String> values = new HashMap<>();
		values.put("a", "b");
		Options a = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.7, values);
		Options b = new Options("xxx", 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 1, 0.8, values);
		assertNotEqualDataStructures(a, b);
	}
}