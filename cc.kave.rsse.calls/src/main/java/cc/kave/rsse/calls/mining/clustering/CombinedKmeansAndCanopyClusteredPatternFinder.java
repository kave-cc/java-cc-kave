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

import static org.apache.mahout.clustering.canopy.CanopyClusterer.createCanopies;
import static org.apache.mahout.clustering.kmeans.KMeansClusterer.clusterPoints;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public class CombinedKmeansAndCanopyClusteredPatternFinder extends PatternFinder {

	private final VectorBuilder vectorBuilder;
	private final FeatureWeighter weighter;

	private final DistanceMeasure distanceMeasure;
	private final double t1;
	private final double t2;
	private final int numIterations;
	private final double convergenceTreshold;

	public CombinedKmeansAndCanopyClusteredPatternFinder(VectorBuilder vectorBuilder,
			FeatureWeighter weighter, DistanceMeasure distanceMeasure, double t1, double t2,
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
	public List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {

		List<Vector> vectors = vectorBuilder.toVectors(usages, dictionary);
		List<Vector> vectors2 = new LinkedList<Vector>();
		vectors2.addAll(vectors);

		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);

		List<Cluster> clusters = buildClusters(canopies, distanceMeasure);

		List<List<Cluster>> iterations = clusterPoints(vectors2, clusters, distanceMeasure, numIterations,
				convergenceTreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		List<Pattern> patterns = createPatterns(finalIteration, dictionary);
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

	@Override
	protected double getWeight(IFeature f) {
		return weighter.getWeight(f);
	}
}