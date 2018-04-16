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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.mahout.clustering.AbstractCluster;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public abstract class PatternFinder {

	private VectorBuilder vb;

	public PatternFinder(VectorBuilder vb) {
		this.vb = vb;
	}

	public List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dict) {
		List<Vector> vectors = toVectors(usages, dict);
		List<? extends AbstractCluster> clusters = cluster(vectors);
		List<Pattern> patterns = new ArrayList<>(clusters.size());
		int i = 0;
		for (AbstractCluster cluster : clusters) {
			patterns.add(toPattern(i++, cluster, dict));
		}
		return patterns;
	}

	public List<Vector> toVectors(List<List<IFeature>> usages, Dictionary<IFeature> dict) {
		List<Vector> vectors = new ArrayList<>(usages.size());
		for (List<IFeature> usage : usages) {
			Optional<double[]> arr = vb.toDoubleArray(usage, dict);
			if (arr.isPresent()) {
				Vector v = new DenseVector(arr.get());
				vectors.add(v);
			}
		}
		return vectors;
	}

	protected abstract List<? extends AbstractCluster> cluster(List<Vector> vectors);

	public Pattern toPattern(int num, AbstractCluster cluster, Dictionary<IFeature> dict) {

		Vector centroid = cluster.computeCentroid();
		long numPoints = cluster.getNumPoints();

		double[] wArr = new double[centroid.size()];
		for (int i = 0; i < wArr.length; i++) {
			wArr[i] = centroid.get(i);
		}
		return vb.toPattern((int) numPoints, wArr, dict);
	}
}