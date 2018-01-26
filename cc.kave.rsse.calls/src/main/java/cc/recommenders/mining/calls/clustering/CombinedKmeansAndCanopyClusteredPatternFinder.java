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

import static org.apache.mahout.clustering.canopy.CanopyClusterer.createCanopies;
import static org.apache.mahout.clustering.kmeans.KMeansClusterer.clusterPoints;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.Pattern;

public class CombinedKmeansAndCanopyClusteredPatternFinder<Feature> extends ClusteredPatternFinder<Feature> {

	private final VectorBuilder<Feature> vectorBuilder;
	private final FeatureWeighter<Feature> weighter;

	private final DistanceMeasure distanceMeasure;
	private final double t1;
	private final double t2;
	private final int numIterations;
	private final double convergenceTreshold;

	public CombinedKmeansAndCanopyClusteredPatternFinder(VectorBuilder<Feature> vectorBuilder,
			FeatureWeighter<Feature> weighter, DistanceMeasure distanceMeasure, double t1, double t2,
			int numIterations, double convergenceThreshold) {
		this.vectorBuilder = vectorBuilder;
		this.weighter = weighter;
		this.distanceMeasure = distanceMeasure;

		this.t1 = t1;
		this.t2 = t2;
		this.numIterations = numIterations;
		this.convergenceTreshold = convergenceThreshold;
	}

	@Override
	public List<Pattern<Feature>> find(List<List<Feature>> usages, Dictionary<Feature> dictionary) {

		List<Vector> vectors = vectorBuilder.build(usages, dictionary);
		List<Vector> vectors2 = new LinkedList<Vector>();
		vectors2.addAll(vectors);

		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);

		List<Cluster> clusters = buildClusters(canopies, distanceMeasure);

		List<List<Cluster>> iterations = clusterPoints(vectors2, clusters, distanceMeasure, numIterations,
				convergenceTreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		List<Pattern<Feature>> patterns = createPatterns(finalIteration, dictionary);
		return patterns;
	}

	private List<Cluster> buildClusters(List<Canopy> canopies, DistanceMeasure distanceMeasure) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		int i = 0;
		for (Canopy canopy : canopies) {
			Cluster c = new Cluster(canopy.getCenter(), i, distanceMeasure);
			clusters.add(c);
			i++;
		}
		return clusters;
	}

	@Override
	public double getWeight(Feature f) {
		return weighter.getWeight(f);
	}

	public double getT1() {
		return t1;
	}

	public double getT2() {
		return t2;
	}

	public double getNumberOfIterations() {
		return numIterations;
	}

	public double getConvergenceThreshold() {
		return convergenceTreshold;
	}
}