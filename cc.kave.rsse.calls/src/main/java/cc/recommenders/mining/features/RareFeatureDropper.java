/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.features;

import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.datastructures.Dictionary;

public class RareFeatureDropper<Feature> {

	private int threshold = 2;

	public int getTreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		Asserts.assertGreaterThan(threshold, 1);
		this.threshold = threshold;
	}

	public Dictionary<Feature> dropRare(Dictionary<Feature> dictionary, List<List<Feature>> usages) {
		Asserts.assertGreaterThan(usages.size(), 0);
		Dictionary<Feature> d = new Dictionary<Feature>();
		Feature last = null;

		for (Feature f : dictionary.getAllEntries()) {
			int count = 0;

			for (List<Feature> usage : usages) {
				for (Feature uf : usage) {
					if (f.equals(uf)) {
						count++;
						break;
					}
				}
			}

			if (count >= threshold) {
				d.add(f);
			}
			last = f;
		}

		if (d.size() == 0) {
			d.add(last);
		}

		return d;
	}
}