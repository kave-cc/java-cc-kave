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

import static cc.kave.commons.utils.io.Logger.debug;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.clustering.DistanceMeasureCluster;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public abstract class PatternFinder {

	private VectorBuilder vb;

	public PatternFinder(VectorBuilder vb) {
		this.vb = vb;
	}

	public List<Pattern> find(List<List<IFeature>> usages, Dictionary<IFeature> dictionary) {
		List<Vector> vectors = vb.toVectors(usages, dictionary);
		List<? extends DistanceMeasureCluster> clusters = cluster(vectors);
		List<Pattern> patterns = createPatterns(clusters, dictionary);
		return patterns;
	}

	protected abstract List<? extends DistanceMeasureCluster> cluster(List<Vector> vectors);

	private List<Pattern> createPatterns(List<? extends DistanceMeasureCluster> clusters,
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

	private Pattern createPattern(int i, DistanceMeasureCluster canopy, Dictionary<IFeature> dict) {

		Pattern pattern = new Pattern("p" + i, (int) canopy.getNumPoints());

		Vector centroid = canopy.computeCentroid();

		for (Iterator<Element> it = centroid.iterateNonZero(); it.hasNext();) {
			Element element = it.next();

			IFeature f = dict.getEntry(element.index());
			double propability = vb.unweight(f, element.get());
			pattern.setProbability(f, propability);
		}

		return pattern;
	}
}