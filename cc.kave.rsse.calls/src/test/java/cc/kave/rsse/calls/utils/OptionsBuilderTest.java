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
package cc.kave.rsse.calls.utils;

import static cc.kave.rsse.calls.utils.OptionsBuilder.bmn;
import static cc.kave.rsse.calls.utils.OptionsBuilder.pbn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.function.Consumer;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.mining.Options;

public class OptionsBuilderTest {

	@Test
	public void util_init() {
		Options actual = new OptionsBuilder("xxx").get();
		Options expected = new Options("APP[xxx]+MCTX+CALLS");
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void fail_initNull() {
		new OptionsBuilder(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_initEmpty() {
		new OptionsBuilder("");
	}

	@Test(expected = AssertionException.class)
	public void fail_initClosingBracket() {
		new OptionsBuilder("]");
	}

	@Test
	public void util_bmnDefault() {
		Options actual = bmn().get();
		Options expected = new Options("APP[bmn]+MCTX+CALLS");
		assertEquals(expected, actual);
	}

	@Test
	public void util_pbn0Default() {
		Options actual = pbn(0).get();
		Options expected = new Options(
				"APP[pbn]+MCTX+CALLS+OPTS[algo:CANOPY;dist:COSINE;t1:0.002;t2:0.001;prec:double]");
		assertEquals(expected, actual);
	}

	@Test
	public void util_pbn7Defaults() {
		Options actual = pbn(7).get();
		Options expected = new Options(
				"APP[pbn]+MCTX+CALLS+OPTS[algo:CANOPY;dist:COSINE;t1:0.071;t2:0.07;prec:double]");
		assertEquals(expected, actual);
	}

	@Test
	public void builder_classCtx() {
		assertOptions(b -> b.cCtx(false), "");
		assertOptions(b -> b.cCtx(true), "+CCTX");
		assertOptions(b -> b.cCtx(0), "");
		assertOptions(b -> b.cCtx(0.001), "");
		assertOptions(b -> b.cCtx(0.33499), "+CCTX(0.33)");
		assertOptions(b -> b.cCtx(0.335), "+CCTX(0.34)");
		assertOptions(b -> b.cCtx(0.5), "+CCTX(0.50)");
		assertOptions(b -> b.cCtx(0.995), "+CCTX");
		assertOptions(b -> b.cCtx(1), "+CCTX");
		assertOptions(b -> b.cCtx(1).cCtx(0.5), "+CCTX(0.50)");
		assertFail(b -> b.cCtx(-0.1));
		assertFail(b -> b.cCtx(1.1));
	}

	@Test
	public void builder_methodCtx() {
		assertOptions(b -> b.mCtx(false), "");
		assertOptions(b -> b.mCtx(true), "+MCTX");
		assertOptions(b -> b.mCtx(0), "");
		assertOptions(b -> b.mCtx(0.001), "");
		assertOptions(b -> b.mCtx(0.33499), "+MCTX(0.33)");
		assertOptions(b -> b.mCtx(0.335), "+MCTX(0.34)");
		assertOptions(b -> b.mCtx(0.5), "+MCTX(0.50)");
		assertOptions(b -> b.mCtx(0.995), "+MCTX");
		assertOptions(b -> b.mCtx(1), "+MCTX");
		assertOptions(b -> b.mCtx(1).mCtx(0.5), "+MCTX(0.50)");
		assertFail(b -> b.mCtx(-0.1));
		assertFail(b -> b.mCtx(1.1));
	}

	@Test
	public void builder_def() {
		assertOptions(b -> b.def(false), "");
		assertOptions(b -> b.def(true), "+DEF");
		assertOptions(b -> b.def(0), "");
		assertOptions(b -> b.def(0.001), "");
		assertOptions(b -> b.def(0.33499), "+DEF(0.33)");
		assertOptions(b -> b.def(0.335), "+DEF(0.34)");
		assertOptions(b -> b.def(0.5), "+DEF(0.50)");
		assertOptions(b -> b.def(0.995), "+DEF");
		assertOptions(b -> b.def(1), "+DEF");
		assertOptions(b -> b.def(1).def(0.5), "+DEF(0.50)");
		assertFail(b -> b.def(-0.1));
		assertFail(b -> b.def(1.1));
	}

	@Test
	public void builder_calls() {
		assertOptions(b -> b.calls(false), "");
		assertOptions(b -> b.calls(true), "+CALLS");
		assertOptions(b -> b.calls(0), "");
		assertOptions(b -> b.calls(0.001), "");
		assertOptions(b -> b.calls(0.33499), "+CALLS(0.33)");
		assertOptions(b -> b.calls(0.335), "+CALLS(0.34)");
		assertOptions(b -> b.calls(0.5), "+CALLS(0.50)");
		assertOptions(b -> b.calls(0.995), "+CALLS");
		assertOptions(b -> b.calls(1), "+CALLS");
		assertOptions(b -> b.calls(1).calls(0.5), "+CALLS(0.50)");
		assertFail(b -> b.calls(-0.1));
		assertFail(b -> b.calls(1.1));
	}

	@Test
	public void builder_params() {
		assertOptions(b -> b.params(false), "");
		assertOptions(b -> b.params(true), "+PARAMS");
		assertOptions(b -> b.params(0), "");
		assertOptions(b -> b.params(0.001), "");
		assertOptions(b -> b.params(0.33499), "+PARAMS(0.33)");
		assertOptions(b -> b.params(0.335), "+PARAMS(0.34)");
		assertOptions(b -> b.params(0.5), "+PARAMS(0.50)");
		assertOptions(b -> b.params(0.995), "+PARAMS");
		assertOptions(b -> b.params(1), "+PARAMS");
		assertOptions(b -> b.params(1).params(0.5), "+PARAMS(0.50)");
		assertFail(b -> b.params(-0.1));
		assertFail(b -> b.params(1.1));
	}

	@Test
	public void builder_members() {
		assertOptions(b -> b.members(false), "");
		assertOptions(b -> b.members(true), "+MEMBERS");
		assertOptions(b -> b.members(0), "");
		assertOptions(b -> b.members(0.001), "");
		assertOptions(b -> b.members(0.33499), "+MEMBERS(0.33)");
		assertOptions(b -> b.members(0.335), "+MEMBERS(0.34)");
		assertOptions(b -> b.members(0.5), "+MEMBERS(0.50)");
		assertOptions(b -> b.members(0.995), "+MEMBERS");
		assertOptions(b -> b.members(1), "+MEMBERS");
		assertOptions(b -> b.members(1).members(0.5), "+MEMBERS(0.50)");
		assertFail(b -> b.members(-0.1));
		assertFail(b -> b.members(1.1));
	}

	@Test
	public void builder_atLeast() {
		assertOptions(b -> b.atLeast(1), "");
		assertOptions(b -> b.atLeast(2), "+ATLEAST(2)");
		assertFail(b -> b.atLeast(0));
	}

	@Test
	public void builder_minProbability() {
		assertOptions(b -> b.minProbability(0), "");
		assertOptions(b -> b.minProbability(0.5), "+P_MIN(0.50)");
		assertOptions(b -> b.minProbability(0.3449), "+P_MIN(0.34)");
		assertOptions(b -> b.minProbability(0.345), "+P_MIN(0.35)");
		assertOptions(b -> b.minProbability(0.5).noMinProbability(), "");
		assertFail(b -> b.minProbability(-0.1));
		assertFail(b -> b.minProbability(1));
	}

	@Test
	public void builder_opts() {
		assertOptions(b -> b.option("a", "b"), "+OPTS[a:b]");
		assertOptions(b -> b.option("a", "b").option("b", "c"), "+OPTS[a:b;b:c]");
		assertOptions(b -> b.option("a", "b").option("a", "b2"), "+OPTS[a:b2]");
		assertOptions(b -> b.option("a", "b").optionDel("a"), "");

		for (String notAllowed : new String[] { null, "", ";", ":", "[", "]", "\n", "\t", " " }) {
			assertFail(b -> b.option(notAllowed, "b"));
			assertFail(b -> b.option("a", notAllowed));
		}
	}

	private void assertOptions(Consumer<OptionsBuilder> c, String expectedDelta) {
		OptionsBuilder b = new OptionsBuilder("...");
		b.mCtx(false);
		b.calls(false);
		c.accept(b);
		String expected = "APP[...]" + expectedDelta;
		assertEquals(expected, b.get().toString());
	}

	private void assertFail(Consumer<OptionsBuilder> c) {
		try {
			OptionsBuilder b = new OptionsBuilder("xxx");
			c.accept(b);
		} catch (AssertionException e) {
			return;
		}
		fail();
	}
}