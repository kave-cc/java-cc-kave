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

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertNotNegative;

import com.google.inject.Inject;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class PatternFinderFactory {

	private final FeatureWeighter weighter;
	private final VectorBuilder vectorBuilder;
	private final Options options;
	private final DistanceMeasureFactory distanceMeasureFactory;

	@Inject
	public PatternFinderFactory(FeatureWeighter weighter, VectorBuilder vectorBuilder, Options options,
			DistanceMeasureFactory distanceMeasureFactory) {
		this.weighter = weighter;
		this.vectorBuilder = vectorBuilder;
		this.options = options;
		this.distanceMeasureFactory = distanceMeasureFactory;
	}

	public PatternFinder createPatternFinder() {

		switch (options.getOptAsEnum("algo", Algorithm.class)) {
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

	private CanopyClusteredPatternFinder createCanopyClusterer() {

		double t1 = options.getOptAsDouble("t1");
		double t2 = options.getOptAsDouble("t2");

		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);

		return new CanopyClusteredPatternFinder(vectorBuilder, weighter, distanceMeasureFactory.get(), t1, t2);
	}

	private PatternFinder createKmeansClusterer() {

		int clusterCount = -1;// miningOptions.getClusterCount();
		int numIterations = -1;// miningOptions.getNumberOfIterations();
		double convergenceThreshold = -1;// miningOptions.getConvergenceThreshold();

		assertGreaterThan(clusterCount, 0);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceThreshold);

		return new KMeansClusteredPatternFinder(weighter, vectorBuilder, distanceMeasureFactory.get(), clusterCount,
				numIterations, convergenceThreshold);
	}

	private PatternFinder createCombinedClusterer() {

		double t1 = options.getOptAsDouble("t1");
		double t2 = options.getOptAsDouble("t2");
		int numIterations = -1;// miningOptions.getNumberOfIterations();
		double convergenceThreshold = -1;// miningOptions.getConvergenceThreshold();

		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceThreshold);

		return new CombinedKmeansAndCanopyClusteredPatternFinder(vectorBuilder, weighter, distanceMeasureFactory.get(),
				t1, t2, numIterations, convergenceThreshold);
	}

	private PatternFinder createCallGroupFinder() {

		// TODO write tests
		Asserts.fail("test that before using it");

		FeatureWeighter cgWeighter = new FeatureWeighter(options) {

			private static final double VERY_SMALL = 0.0000000001;

			private boolean isCallFeature(IFeature f) {
				Asserts.fail("check for UsageSiteType");
				return f instanceof UsageSiteFeature;
			}

			@Override
			public double getWeight(IFeature f) {
				if (isCallFeature(f)) {
					return 1;
				} else {
					return VERY_SMALL;
				}
			}

			@Override
			public double getUnweighted(IFeature f, double value) {
				return value / getWeight(f);
			}
		};
		return new CanopyClusteredPatternFinder(new VectorBuilder(cgWeighter), cgWeighter, distanceMeasureFactory.get(),
				0.1, 0.05);
	}
}