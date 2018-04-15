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
package cc.kave.rsse.calls.mining;

import java.util.List;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;

public class VectorBuilder {

	private Options opts;

	public VectorBuilder(Options opts) {
		this.opts = opts;
	}

	public double getWeight(IFeature f) {
		return 0.0;
	}

	public double unweight(IFeature f, double in) {
		return in / getWeight(f);
	}

	public List<Vector> toVectors(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {

		// check wether
		// ... new DenseVector(toArrays(usages, dictionary));
		// would work too

		List<Vector> vectors = Lists.newArrayList();

		for (List<IFeature> usage : usages) {
			final Vector vector = new RandomAccessSparseVector(dictionary.size());

			for (IFeature f : usage) {
				int index = dictionary.getId(f);
				boolean isValidFeature = index >= 0;
				if (isValidFeature) {
					double value = 1;
					vector.set(index, value);
				}
			}

			vectors.add(vector);
		}

		return vectors;
	}

	public List<Vector> toVector(List<IFeature> usage, Dictionary<IFeature> dictionary) {
		return null;
	}

	public List<double[]> toArrays(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {
		return null;
	}

	public double[] toArray(List<IFeature> usage, Dictionary<IFeature> dictionary) {
		return null;
	}

	public List<boolean[]> toBoolArrays(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {
		return null;
	}

	public boolean[] toBoolArray(List<IFeature> usage, Dictionary<IFeature> dictionary) {
		return null;
	}
}