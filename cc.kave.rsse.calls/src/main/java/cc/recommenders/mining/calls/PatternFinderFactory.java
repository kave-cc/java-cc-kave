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
package cc.recommenders.mining.calls;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.mining.calls.clustering.CanopyClusteredPatternFinder;
import cc.recommenders.mining.calls.clustering.CombinedKmeansAndCanopyClusteredPatternFinder;
import cc.recommenders.mining.calls.clustering.FeatureWeighter;
import cc.recommenders.mining.calls.clustering.KMeansClusteredPatternFinder;
import cc.recommenders.mining.calls.clustering.VectorBuilder;
import cc.recommenders.usages.features.CallFeature;

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertNotNegative;

import com.google.inject.Inject;

public class PatternFinderFactory<ObjectUsageFeature> {

	private final FeatureWeighter<ObjectUsageFeature> weighter;
	private final MiningOptions miningOptions;
	private final DistanceMeasureFactory distanceMeasureFactory;

	@Inject
	public PatternFinderFactory(FeatureWeighter<ObjectUsageFeature> weighter, MiningOptions miningOptions,
			DistanceMeasureFactory distanceMeasureFactory) {
		this.weighter = weighter;
		this.miningOptions = miningOptions;
		this.distanceMeasureFactory = distanceMeasureFactory;
	}

	public PatternFinder<ObjectUsageFeature> createPatternFinder() {

		switch (miningOptions.getAlgorithm()) {
		case CANOPY:
			return createCanopyClusterer();

		case KMEANS:
			return createKmeansClusterer();

		case COMBINED:
			return createCombinedClusterer();

		default:
			return createCallGroupFinder();
		}

	}

	private CanopyClusteredPatternFinder<ObjectUsageFeature> createCanopyClusterer() {

		double t1 = miningOptions.getT1();
		double t2 = miningOptions.getT2();
		VectorBuilder<ObjectUsageFeature> vectorBuilder = new VectorBuilder<ObjectUsageFeature>(weighter);

		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);

		return new CanopyClusteredPatternFinder<ObjectUsageFeature>(vectorBuilder, weighter,
				distanceMeasureFactory.get(), t1, t2);
	}

	private PatternFinder<ObjectUsageFeature> createKmeansClusterer() {

		int clusterCount = miningOptions.getClusterCount();
		int numIterations = miningOptions.getNumberOfIterations();
		double convergenceThreshold = miningOptions.getConvergenceThreshold();

		assertGreaterThan(clusterCount, 0);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceThreshold);

		return new KMeansClusteredPatternFinder<ObjectUsageFeature>(distanceMeasureFactory.get(), clusterCount,
				numIterations, convergenceThreshold);
	}

	private PatternFinder<ObjectUsageFeature> createCombinedClusterer() {

		double t1 = miningOptions.getT1();
		double t2 = miningOptions.getT2();
		int numIterations = miningOptions.getNumberOfIterations();
		double convergenceThreshold = miningOptions.getConvergenceThreshold();
		VectorBuilder<ObjectUsageFeature> vectorBuilder = new VectorBuilder<ObjectUsageFeature>(weighter);

		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceThreshold);

		return new CombinedKmeansAndCanopyClusteredPatternFinder<ObjectUsageFeature>(vectorBuilder, weighter,
				distanceMeasureFactory.get(), t1, t2, numIterations, convergenceThreshold);
	}

	private PatternFinder<ObjectUsageFeature> createCallGroupFinder() {

		// TODO write tests
		Asserts.fail("test that before using it");

		FeatureWeighter<ObjectUsageFeature> cgWeighter = new FeatureWeighter<ObjectUsageFeature>() {

			private static final double VERY_SMALL = 0.0000000001;

			private boolean isCallFeature(ObjectUsageFeature f) {
				return f instanceof CallFeature;
			}

			@Override
			public double getWeight(ObjectUsageFeature f) {
				if (isCallFeature(f)) {
					return 1;
				} else {
					return VERY_SMALL;
				}
			}

			@Override
			public double getUnweighted(ObjectUsageFeature f, double value) {
				return value / getWeight(f);
			}
		};
		VectorBuilder<ObjectUsageFeature> vectorBuilder = new VectorBuilder<ObjectUsageFeature>(cgWeighter);
		return new CanopyClusteredPatternFinder<ObjectUsageFeature>(vectorBuilder, cgWeighter,
				distanceMeasureFactory.get(), 0.1, 0.05);
	}
}