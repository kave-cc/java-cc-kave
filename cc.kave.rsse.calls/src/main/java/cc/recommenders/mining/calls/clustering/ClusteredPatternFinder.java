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

import static cc.kave.commons.utils.io.Logger.debug;
import static cc.recommenders.mining.calls.Pattern.newPattern;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.mining.calls.PatternFinder;

public abstract class ClusteredPatternFinder<Feature> implements PatternFinder<Feature> {

	public abstract double getWeight(Feature f);

	protected List<Pattern<Feature>> createPatterns(List<? extends DistanceMeasureCluster> clusters,
			Dictionary<Feature> dictionary) {

		List<Pattern<Feature>> patterns = new LinkedList<Pattern<Feature>>();

		int i = 0;
		for (DistanceMeasureCluster cluster : clusters) {
			Pattern<Feature> p = createPattern(i++, cluster, dictionary);
			patterns.add(p);
		}

		debug("PatternFinder: %d patterns found\n", patterns.size());

		return patterns;
	}

	protected Pattern<Feature> createPattern(int i, DistanceMeasureCluster canopy, Dictionary<Feature> dictionary) {

		Pattern<Feature> pattern = newPattern("p" + i, (int) canopy.getNumPoints());

		Vector centroid = canopy.computeCentroid();

		for (Iterator<Element> it = centroid.iterateNonZero(); it.hasNext();) {
			Element element = it.next();

			Feature feature = dictionary.getEntry(element.index());
			double weight = getWeight(feature);
			double propability = element.get() / weight;

			pattern.setProbability(feature, propability);
		}

		return pattern;
	}
}