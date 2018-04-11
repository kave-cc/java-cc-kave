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
package cc.kave.rsse.calls.mining.clustering;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public class KMeansClusteredPatternFinder extends PatternFinder {

	private final int clusterCount;
	private final int numIterations;
	private final double convergenceTreshold;
	private final DistanceMeasure distanceMeasure;
	private final VectorBuilder vectorBuilder;
	private final FeatureWeighter weighter;

	public KMeansClusteredPatternFinder(FeatureWeighter weighter, VectorBuilder vectorBuilder,
			DistanceMeasure distanceMeasure, int clusterCount, int numIterations, double convergenceTreshold) {
		this.vectorBuilder = vectorBuilder;
		this.distanceMeasure = distanceMeasure;
		this.clusterCount = clusterCount;
		this.numIterations = numIterations;
		this.convergenceTreshold = convergenceTreshold;
		this.weighter = weighter;
	}

	@Override
	public List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {

		List<Vector> vectors = vectorBuilder.toVectors(usages, dictionary);

		List<Cluster> rndCenters = buildRandomCenters(vectors, clusterCount, distanceMeasure);
		List<List<Cluster>> iterations = KMeansClusterer.clusterPoints(vectors, rndCenters, distanceMeasure,
				numIterations, convergenceTreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		List<Pattern> patterns = createPatterns(finalIteration, dictionary);
		return patterns;
	}

	private List<Cluster> buildRandomCenters(List<Vector> vectors, int kmeansClusterCount,
			DistanceMeasure distanceMeasure) {

		List<Cluster> clusters = new ArrayList<Cluster>();

		SecureRandom randomGenerator = new SecureRandom();

		for (int i = 0; i < kmeansClusterCount; i++) {
			int rndIndex = randomGenerator.nextInt();

			if (rndIndex < 0)
				rndIndex = (rndIndex + 1) * -1;

			rndIndex %= vectors.size();
			Vector randomVector = vectors.get(rndIndex);

			Cluster c = new Cluster(randomVector, i, distanceMeasure);
			clusters.add(c);
		}

		return clusters;
	}

	public double getNumberOfIterations() {
		return numIterations;
	}

	public double getClusterCount() {
		return clusterCount;
	}

	public double getConvergenceThreshold() {
		return convergenceTreshold;
	}

	@Override
	protected double getWeight(IFeature f) {
		return weighter.getWeight(f);
	}
}