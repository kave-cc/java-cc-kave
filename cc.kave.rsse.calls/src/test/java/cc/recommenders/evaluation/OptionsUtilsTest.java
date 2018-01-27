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
package cc.recommenders.evaluation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionsUtilsTest {
	private String actual;
	private String expected;

	@Test
	public void getOptions() {
		String actual = OptionsUtils.getOptions("XXX", false, false, false);
		String expected = "XXX+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableClass() {
		String actual = OptionsUtils.getOptions("YYY", true, false, false);
		String expected = "YYY+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]+CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableDef() {
		String actual = OptionsUtils.getOptions("XXX", false, true, false);
		String expected = "XXX+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD+DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableParams() {
		String actual = OptionsUtils.getOptions("XXX", false, false, true);
		String expected = "XXX+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF+PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void getOptionsWithInit() {
		String actual = OptionsUtils.getOptionsWithInit("ZZZ", false, false, false);
		String expected = "ZZZ+W[0.00; 0.00; 0.00; 0.00]+INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableClassWithInit() {
		String actual = OptionsUtils.getOptionsWithInit("YYY", true, false, false);
		String expected = "YYY+W[0.00; 0.00; 0.00; 0.00]+INIT-DROP+Q[NM]+CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableDefWithInit() {
		String actual = OptionsUtils.getOptionsWithInit("XXX", false, true, false);
		String expected = "XXX+W[0.00; 0.00; 0.00; 0.00]+INIT-DROP+Q[NM]-CLASS+METHOD+DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void enableParamsWithInit() {
		String actual = OptionsUtils.getOptionsWithInit("XXX", false, false, true);
		String expected = "XXX+W[0.00; 0.00; 0.00; 0.00]+INIT-DROP+Q[NM]-CLASS+METHOD-DEF+PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderQueryTypeDefault() {
		String actual = OptionsUtils.bmn().get();
		String expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderQueryTypeNM() {
		String actual = OptionsUtils.bmn().q0().qNM().get();
		String expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderQueryType0() {
		String actual = OptionsUtils.bmn().qNM().q0().get();
		String expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[ZERO]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderBmnDefaults() {
		String actual = OptionsUtils.bmn().get();
		String expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderPbnDefaults() {
		String actual = OptionsUtils.pbn(0).get();
		String expected = "CANOPY[0.002; 0.001]+COSINE+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderPbn7Defaults() {
		String actual = OptionsUtils.pbn(7).get();
		String expected = "CANOPY[0.071; 0.07]+COSINE+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingInit() {
		actual = OptionsUtils.bmn().init(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().init(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]+INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingClass() {
		actual = OptionsUtils.bmn().c(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().c(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]+CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingDef() {
		actual = OptionsUtils.bmn().d(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().d(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD+DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingParams() {
		actual = OptionsUtils.bmn().p(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().p(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF+PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingDouble() {
		actual = OptionsUtils.bmn().useDouble().useFloat().get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE-DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().useFloat().useDouble().get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingIgnore() {
		actual = OptionsUtils.bmn().ignore(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS+IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().ignore(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderFlippingDrop() {
		actual = OptionsUtils.bmn().dropRareFeatures(true).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT+DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().dropRareFeatures(false).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
	}

	@Test
	public void builderSettingMinPropability() {
		actual = OptionsUtils.bmn().get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE";
		assertEquals(expected, actual);
		actual = OptionsUtils.bmn().min(13).get();
		expected = "BMN+MANHATTAN+W[0.00; 0.00; 0.00; 0.00]-INIT-DROP+Q[NM]-CLASS+METHOD-DEF-PARAMS-IGNORE+DOUBLE+MIN13";
		assertEquals(expected, actual);
	}
}