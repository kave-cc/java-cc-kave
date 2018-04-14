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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createQuery;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createQueryWithAllCallsSet;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createQueryWithUnobservedData;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createResult;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createSampleNetwork;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.createTuple;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.newDoubleRecommender;
import static cc.kave.rsse.calls.recs.pbn.PBNRecommenderFixture.newFloatRecommender;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.mining.Options;

@Ignore
public class PBNRecommenderTest {

	private BayesianNetwork network;
	private PBNRecommender recommender;

	@Before
	public void setup() {
		// a genie file with that network is provided in resource folder
		network = createSampleNetwork();
		recommender = new PBNRecommender(network, new Options("+CLASS+METHOD+DEF+PARAMS"));
	}

	@Test
	public void normalUse() {
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.3571),
				createTuple("LC.m3()V", 0.3128));
		assertEqualSet(expected, actual);
	}

	@Test
	public void excludeResultsWithLowProbability() {
		recommender = new PBNRecommender(network, new Options("+MIN35"));
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.3571));
		assertEqualSet(expected, actual);
	}

	@Test
	public void queriedCallsAreCleared() {
		// first query with all calls set
		recommender.query(createQueryWithAllCallsSet());
		// then do a "normal query"
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.3571),
				createTuple("LC.m3()V", 0.3128));
		assertEqualSet(expected, actual);
	}

	@Test
	public void doNotQueryClass() {
		recommender = new PBNRecommender(network, new Options("-CLASS+METHOD+DEF+PARAMS"));
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.3170),
				createTuple("LC.m3()V", 0.2968));
		assertEqualSet(expected, actual);
	}

	@Test
	public void doNotQueryMethod() {
		recommender = new PBNRecommender(network, new Options("+CLASS-METHOD+DEF+PARAMS"));
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.4894),
				createTuple("LC.m3()V", 0.3657));
		assertEqualSet(expected, actual);
	}

	@Test
	public void doNotQueryDefinition() {
		recommender = new PBNRecommender(network, new Options("+CLASS+METHOD-DEF+PARAMS"));
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.4272),
				createTuple("LC.m3()V", 0.3409));
		assertEqualSet(expected, actual);
	}

	@Test
	public void doNotQueryParameter() {
		recommender = new PBNRecommender(network, new Options("+CLASS+METHOD+DEF-PARAMS"));
		Set<Pair<IMethodName, Double>> actual = recommender.query(createQuery());
		Set<Pair<IMethodName, Double>> expected = createResult(createTuple("LC.m2()V", 0.4619),
				createTuple("LC.m3()V", 0.3547));
		assertEqualSet(expected, actual);
	}

	@Test
	public void queryWithUnobservatedDataDoesNotThrowException() {
		recommender.query(createQueryWithUnobservedData());
	}

	@Test
	public void size_floatMinimal() {
		int actual = newFloatRecommender().getSize();
		int expected = 4 * (2 + 4 + 4 + 4);
		assertEquals(expected, actual);
	}

	@Test
	public void size_floatBigger() {
		int actual = newFloatRecommender().patterns(2).inClass(3).inMethod(4).def(5).methods(6).params(7).getSize();
		int expected = 4 * (2 + 2 * 3 + 2 * 4 + 2 * 5 + 6 * (2 * 2) + 7 * (2 * 2));
		assertEquals(expected, actual);
	}

	@Test
	public void size_doubleMinimal() {
		int actual = newDoubleRecommender().getSize();
		int expected = 8 * (2 + 4 + 4 + 4);
		assertEquals(expected, actual);
	}

	@Test
	public void size_doubleBigger() {
		int actual = newDoubleRecommender().patterns(2).inClass(3).inMethod(4).def(5).methods(6).params(7).getSize();
		int expected = 8 * (2 + 2 * 3 + 2 * 4 + 2 * 5 + 6 * (2 * 2) + 7 * (2 * 2));
		assertEquals(expected, actual);
	}

	private static <T> void assertEqualSet(Set<Pair<T, Double>> a, Set<Pair<T, Double>> b) {
		assertTrue(a.size() == b.size());
		Iterator<Pair<T, Double>> itA = a.iterator();
		Iterator<Pair<T, Double>> itB = b.iterator();

		while (itA.hasNext()) {
			Pair<T, Double> tA = itA.next();
			Pair<T, Double> tB = itB.next();

			assertEquals(tA.getLeft(), tB.getLeft());
			assertEquals(tA.getRight(), tB.getRight(), 0.001);
		}
	}
}