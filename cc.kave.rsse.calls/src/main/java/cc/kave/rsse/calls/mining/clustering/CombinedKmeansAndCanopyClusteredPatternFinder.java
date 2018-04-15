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
import static org.apache.mahout.clustering.canopy.CanopyClusterer.createCanopies;
import static org.apache.mahout.clustering.kmeans.KMeansClusterer.clusterPoints;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;

public class CombinedKmeansAndCanopyClusteredPatternFinder extends PatternFinder {

	private DistanceMeasure distanceMeasure;
	private double t1;
	private double t2;
	private int numIterations;
	private double convergenceThreshold;

	public CombinedKmeansAndCanopyClusteredPatternFinder(VectorBuilder vectorBuilder, Options opts) {
		super(vectorBuilder);
		Asserts.fail("set fields (+ public/final)");
		assertNotNegative(t1);
		assertNotNegative(t2);
		assertGreaterThan(t1, t2);
		assertGreaterThan(numIterations, 0);
		assertNotNegative(convergenceThreshold);
	}

	@Override
	protected List<? extends DistanceMeasureCluster> cluster(List<Vector> vectors) {
		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);

		List<Cluster> clusters = buildClusters(canopies, distanceMeasure);

		List<List<Cluster>> iterations = clusterPoints(vectors, clusters, distanceMeasure, numIterations,
				convergenceThreshold);

		List<Cluster> finalIteration = iterations.get(iterations.size() - 1);
		return finalIteration;
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
		return convergenceThreshold;
	}
}