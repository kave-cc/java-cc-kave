/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.mining.clustering;

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertNotNegative;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;

public class KMeansClusteredPatternFinder extends PatternFinder {

	private int clusterCount;
	private int numIterations;
	private double convergenceTreshold;
	private DistanceMeasure distanceMeasure;

	public KMeansClusteredPatternFinder(VectorBuilder vectorBuilder, Options opts) {
		super(vectorBuilder);
		Asserts.fail("set fields (+ public/final)");
		assertGreaterThan(clusterCount, 0);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceTreshold);
	}

	@Override
	protected List<? extends DistanceMeasureCluster> cluster(List<Vector> vectors) {
		List<Cluster> rndCenters = buildRandomCenters(vectors, clusterCount, distanceMeasure);
		List<List<Cluster>> iterations = KMeansClusterer.clusterPoints(vectors, rndCenters, distanceMeasure,
				numIterations, convergenceTreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		return finalIteration;
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
}