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

import java.util.List;

import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public class CanopyClusteredPatternFinder extends PatternFinder {

	private final DistanceMeasure distanceMeasure;
	private final FeatureWeighter weighter;
	private final VectorBuilder vectorBuilder;

	private final double t1;
	private final double t2;

	public CanopyClusteredPatternFinder(VectorBuilder vectorBuilder, FeatureWeighter weighter,
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
	public List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {
		List<Vector> vectors = vectorBuilder.toVectors(usages, dictionary);
		List<Canopy> canopies = createCanopies(vectors, distanceMeasure, t1, t2);
		List<Pattern> patterns = createPatterns(canopies, dictionary);
		return patterns;
	}

	@Override
	public double getWeight(IFeature f) {
		return weighter.getWeight(f);
	}
}