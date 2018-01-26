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

import java.util.List;

import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.Pattern;

public class CanopyClusteredPatternFinder<Feature> extends ClusteredPatternFinder<Feature> {

	private final DistanceMeasure distanceMeasure;
	private final FeatureWeighter<Feature> weighter;
	private final VectorBuilder<Feature> vectorBuilder;

	private final double t1;
	private final double t2;

	public CanopyClusteredPatternFinder(VectorBuilder<Feature> vectorBuilder, FeatureWeighter<Feature> weighter,
			DistanceMeasure distanceMeasure, double t1, double t2) {
		this.vectorBuilder = vectorBuilder;
		this.weighter = weighter;
		this.distanceMeasure = distanceMeasure;

		this.t1 = t1;
		this.t2 = t2;
	}

	public double getT1() {
		return t1;
	}

	public double getT2() {
		return t2;
	}

	@Override
	public List<Pattern<Feature>> find(List<List<Feature>> usages, Dictionary<Feature> dictionary) {
		List<Vector> vectors = vectorBuilder.build(usages, dictionary);
		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);
		List<Pattern<Feature>> patterns = createPatterns(canopies, dictionary);
		return patterns;
	}

	@Override
	public double getWeight(Feature f) {
		return weighter.getWeight(f);
	}
}