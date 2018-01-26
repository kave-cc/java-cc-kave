/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls;

import static cc.recommenders.mining.calls.QueryOptions.newQueryOptions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.mining.calls.QueryOptions.QueryType;

public class QueryOptionsTest {

	private QueryOptions sut;

	@Test
	public void defaultOptions() {
		sut = new QueryOptions();
		assertTrue(sut.useClassContext);
		assertTrue(sut.useMethodContext);
		assertTrue(sut.useDefinition);
		assertTrue(sut.useParameterSites);
		assertEquals(0.0, sut.minProbability, 0.001);
		assertFalse(sut.isIgnoringAfterFullRecall);
		assertTrue(sut.useDoublePrecision);
		assertEquals(QueryType.NM, sut.queryType);
	}

	@Test
	public void emptyStringResultsInDefaultOptions() {
		assertEquals(new QueryOptions(), newQueryOptions(""));
	}

	@Test(expected = AssertionException.class)
	public void checkForNull() {
		QueryOptions.newQueryOptions(null);
	}

	@Test
	public void shortcutForSetting() {
		String options = "+Q[ZERO]+CLASS+METHOD+DEF+PARAMS-IGNORE+DOUBLE";
		QueryOptions a = new QueryOptions();
		QueryOptions b = new QueryOptions();

		a.setFrom(QueryOptions.newQueryOptions(options));
		b.setFrom(options);
		assertEquals(a, b);
	}

	@Test
	public void classSwitchIsCorrectlyParsed() {
		sut = newQueryOptions("+CLASS");
		assertTrue(sut.useClassContext);
		sut = newQueryOptions("-CLASS");
		assertFalse(sut.useClassContext);
	}

	@Test
	public void precisionIsCorrectlyParsed() {
		sut = newQueryOptions("+DOUBLE");
		assertTrue(sut.useDoublePrecision);
		sut = newQueryOptions("-DOUBLE");
		assertFalse(sut.useDoublePrecision);
	}

	@Test
	public void methodSwitchIsCorrectlyParsed() {
		sut = newQueryOptions("+METHOD");
		assertTrue(sut.useMethodContext);
		sut = newQueryOptions("-METHOD");
		assertFalse(sut.useMethodContext);
	}

	@Test
	public void definitionSwitchIsCorrectlyParsed() {
		sut = newQueryOptions("+DEF");
		assertTrue(sut.useDefinition);
		sut = newQueryOptions("-DEF");
		assertFalse(sut.useDefinition);
	}

	@Test
	public void parameterSwitchIsCorrectlyParsed() {
		sut = newQueryOptions("+PARAMS");
		assertTrue(sut.useParameterSites);
		sut = newQueryOptions("-PARAMS");
		assertFalse(sut.useParameterSites);
	}

	@Test
	public void minIsCorrectlyParsed() {
		sut = newQueryOptions("+MIN13");
		assertEquals(0.13, sut.minProbability, 0.001);
	}

	@Test
	public void ignoreIsCorrectlyParsed() {
		sut = newQueryOptions("+IGNORE");
		assertTrue(sut.isIgnoringAfterFullRecall);
		sut = newQueryOptions("-IGNORE");
		assertFalse(sut.isIgnoringAfterFullRecall);
	}

	@Test
	public void queryTypeIsCorrectlyParsed() {
		sut = newQueryOptions("+Q[ZERO]");
		assertEquals(QueryType.ZERO, sut.queryType);
		sut = newQueryOptions("+Q[NM]");
		assertFalse(sut.isIgnoringAfterFullRecall);
	}

	@Test
	public void equalObjects() {
		QueryOptions a = newQueryOptions("-DEF");
		QueryOptions b = newQueryOptions("-DEF");
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void differentObjects() {
		QueryOptions a = newQueryOptions("-DEF");
		QueryOptions b = newQueryOptions("-CLASS");
		assertFalse(a.equals(b));
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void setFromImpliesEquality() {
		sut = new QueryOptions();
		QueryOptions other = buildWithAllNonDefaultValues();
		assertTrue(sut.setFrom(other).equals(other));
	}

	@Test
	public void setFromYieldsSameHashCode() {
		sut = new QueryOptions();
		QueryOptions other = buildWithAllNonDefaultValues();
		assertTrue(sut.setFrom(other).hashCode() == other.hashCode());
	}

	@Test
	public void severalSerializationTests() {
		ensureEqualSerialization("+Q[ZERO]+CLASS+METHOD+DEF+PARAMS-IGNORE+DOUBLE");
		ensureEqualSerialization("+Q[NM]-CLASS-METHOD-DEF-PARAMS-IGNORE-DOUBLE+MIN12");
		ensureEqualSerialization("+Q[ZERO]+CLASS-METHOD+DEF-PARAMS-IGNORE+DOUBLE");
		ensureEqualSerialization("+Q[NM]+CLASS+METHOD-DEF-PARAMS-IGNORE-DOUBLE+MIN1");
		ensureEqualSerialization("+Q[ZERO]+CLASS+METHOD-DEF-PARAMS+IGNORE+DOUBLE");
	}

	@Test
	public void informalFormatCanBeParsed() {
		QueryOptions sut = newQueryOptions("CANOPY[0;0]+METHOD+COSINE+W[0;0;0;0]+L0-CLASS-PARAMS+MIN5-DEFINITION");
		assertFalse(sut.useClassContext);
		assertTrue(sut.useMethodContext);
		assertFalse(sut.useDefinition);
		assertFalse(sut.useParameterSites);
		assertEquals(0.05, sut.minProbability, 0.0001);
	}

	private QueryOptions buildWithAllNonDefaultValues() {
		QueryOptions other = new QueryOptions();
		other.useDoublePrecision = false;
		other.useClassContext = false;
		other.useMethodContext = false;
		other.useDefinition = false;
		other.useParameterSites = false;
		other.minProbability = 0.5;
		other.isIgnoringAfterFullRecall = true;
		other.queryType = QueryType.ZERO;
		return other;
	}

	private static void ensureEqualSerialization(String expected) {
		String actual = newQueryOptions(expected).toString();
		assertEquals(expected, actual);
	}
}