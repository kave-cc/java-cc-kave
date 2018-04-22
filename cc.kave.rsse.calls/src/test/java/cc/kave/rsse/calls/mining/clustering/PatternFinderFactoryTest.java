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
package cc.kave.rsse.calls.mining.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.utils.OptionsBuilder;

@Ignore
public class PatternFinderFactoryTest {

	private static final double DELTA = 0.0001;
	private Options options;
	private PatternFinderFactory sut;

	@Before
	public void setup() {

		options = OptionsBuilder.bmn().get();

		sut = new PatternFinderFactory(new VectorBuilder(options), options);
	}

	@Test
	public void canopyClustererIsInstantiatedWithCorrectParameters() {
		double expectedT1 = 13;
		double expectedT2 = 5;

		fail();
		// miningOptions.setAlgorithm(Algorithm.CANOPY);
		// miningOptions.setT1(expectedT1);
		// miningOptions.setT2(expectedT2);
		Object obj = sut.createPatternFinder();
		assertNotNull(obj);
		assertTrue(obj instanceof CanopyClusteredPatternFinder);

		CanopyClusteredPatternFinder actual = cast(sut.createPatternFinder());

		assertEquals(expectedT1, actual.getT1(), DELTA);
		assertEquals(expectedT2, actual.getT2(), DELTA);
	}

	@Test
	public void kmeansClustererIsInstantiatedWithCorrectParameters() {
		double expectedThreshold = 0.123;
		int expectedK = 34;
		int expectedIterations = 17;

		fail();
		// miningOptions.setAlgorithm(Algorithm.KMEANS);
		// miningOptions.setConvergenceThreshold(expectedThreshold);
		// miningOptions.setClusterCount(expectedK);
		// miningOptions.setNumberOfIterations(expectedIterations);
		Object obj = sut.createPatternFinder();
		assertNotNull(obj);
		assertTrue(obj instanceof KMeansClusteredPatternFinder);

		KMeansClusteredPatternFinder actual = cast(sut.createPatternFinder());

		assertEquals(expectedIterations, actual.getNumberOfIterations(), DELTA);
		assertEquals(expectedK, actual.getClusterCount(), DELTA);
		assertEquals(expectedThreshold, actual.getConvergenceThreshold(), DELTA);
	}

	@Test
	public void combinedClustererIsInstantiatedWithCorrectParameters() {
		double expectedT1 = 13;
		double expectedT2 = 5;
		double expectedThreshold = 0.123;
		int expectedIterations = 17;

		fail();
		// miningOptions.setAlgorithm(Algorithm.COMBINED);
		// miningOptions.setT1(expectedT1);
		// miningOptions.setT2(expectedT2);
		// miningOptions.setConvergenceThreshold(expectedThreshold);
		// miningOptions.setNumberOfIterations(expectedIterations);

		Object obj = sut.createPatternFinder();
		assertNotNull(obj);
		assertTrue(obj instanceof CombinedKmeansAndCanopyClusteredPatternFinder);

		CombinedKmeansAndCanopyClusteredPatternFinder actual = cast(sut.createPatternFinder());

		assertEquals(expectedT1, actual.getT1(), DELTA);
		assertEquals(expectedT2, actual.getT2(), DELTA);
		assertEquals(expectedIterations, actual.getNumberOfIterations(), DELTA);
		assertEquals(expectedThreshold, actual.getConvergenceThreshold(), DELTA);
	}

	@Test
	@Ignore
	public void callGroupFinderIsInstantiatedWithCorrectParameters() {

		fail();
		// miningOptions.setAlgorithm(Algorithm.CALLGROUP);
		// miningOptions.setT1(14);
		// miningOptions.setT2(6);
		Object obj = sut.createPatternFinder();
		assertNotNull(obj);
		assertTrue(obj instanceof CanopyClusteredPatternFinder);

		CanopyClusteredPatternFinder actual = cast(sut.createPatternFinder());

		assertEquals(0, actual.getT1(), DELTA);
		assertEquals(0, actual.getT2(), DELTA);
	}

	@SuppressWarnings("unchecked")
	private static <T> T cast(Object o) {
		return (T) o;
	}
}