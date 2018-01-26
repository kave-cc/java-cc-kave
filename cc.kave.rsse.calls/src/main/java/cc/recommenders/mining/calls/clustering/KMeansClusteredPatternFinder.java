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
package cc.recommenders.mining.calls.clustering;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.Pattern;

public class KMeansClusteredPatternFinder<Feature> extends ClusteredPatternFinder<Feature> {

	private final int clusterCount;
	private final int numIterations;
	private final double convergenceTreshold;
	private final DistanceMeasure distanceMeasure;

	private final FeatureWeighter<Feature> weighter;
	private final VectorBuilder<Feature> vectorBuilder;

	public KMeansClusteredPatternFinder(DistanceMeasure distanceMeasure, int clusterCount, int numIterations,
			double convergenceTreshold) {
		this.distanceMeasure = distanceMeasure;
		this.clusterCount = clusterCount;
		this.numIterations = numIterations;
		this.convergenceTreshold = convergenceTreshold;

		// TODO review: WTF?
		weighter = null;
		vectorBuilder = null;
	}

	@Override
	public List<Pattern<Feature>> find(List<List<Feature>> usages, Dictionary<Feature> dictionary) {

		List<Vector> vectors = vectorBuilder.build(usages, dictionary);

		List<Cluster> rndCenters = buildRandomCenters(vectors, clusterCount, distanceMeasure);
		List<List<Cluster>> iterations = KMeansClusterer.clusterPoints(vectors, rndCenters, distanceMeasure,
				numIterations, convergenceTreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		List<Pattern<Feature>> patterns = createPatterns(finalIteration, dictionary);
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

	@Override
	public double getWeight(Feature f) {
		return weighter.getWeight(f);
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
}