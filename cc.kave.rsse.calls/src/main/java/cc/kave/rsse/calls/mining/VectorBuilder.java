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
package cc.kave.rsse.calls.mining;

import java.util.List;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;

public class VectorBuilder {

	private final FeatureWeighter weighter;

	public VectorBuilder(FeatureWeighter weighter) {
		this.weighter = weighter;
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
					double value = weighter.getWeight(f);
					vector.set(index, value);
				}
			}

			vectors.add(vector);
		}

		return vectors;
	}

	public List<double[]> toArrays(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {
		return null;
	}

	public double[] toArray(List<IFeature> usage, Dictionary<IFeature> dictionary) {
		return null;
	}

	public boolean[] toBoolArray(List<IFeature> usage, Dictionary<IFeature> dictionary) {
		return null;
	}
}