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

import static cc.kave.commons.utils.io.Logger.debug;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public abstract class PatternFinder {

	protected abstract double getWeight(IFeature f);

	protected List<Pattern> createPatterns(List<? extends DistanceMeasureCluster> clusters,
			Dictionary<IFeature> dictionary) {

		List<Pattern> patterns = new LinkedList<>();

		int i = 0;
		for (DistanceMeasureCluster cluster : clusters) {
			Pattern p = createPattern(i++, cluster, dictionary);
			patterns.add(p);
		}

		debug("PatternFinder: %d patterns found\n", patterns.size());

		return patterns;
	}

	protected Pattern createPattern(int i, DistanceMeasureCluster canopy, Dictionary<IFeature> dict) {

		Pattern pattern = new Pattern("p" + i, (int) canopy.getNumPoints());

		Vector centroid = canopy.computeCentroid();

		for (Iterator<Element> it = centroid.iterateNonZero(); it.hasNext();) {
			Element element = it.next();

			IFeature feature = dict.getEntry(element.index());
			double weight = getWeight(feature);
			double propability = element.get() / weight;

			pattern.setProbability(feature, propability);
		}

		return pattern;
	}

	public abstract List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dictionary);
}