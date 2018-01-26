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

import java.util.List;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cc.recommenders.datastructures.Dictionary;

public class VectorBuilder<Feature> {

	private final FeatureWeighter<Feature> weighter;

	@Inject
	public VectorBuilder(FeatureWeighter<Feature> weighter) {
		this.weighter = weighter;
	}

	public List<Vector> build(List<List<Feature>> usages, Dictionary<Feature> dictionary) {

		List<Vector> vectors = Lists.newArrayList();

		for (List<Feature> usage : usages) {
			final Vector vector = new RandomAccessSparseVector(dictionary.size());

			for (Feature f : usage) {
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
}