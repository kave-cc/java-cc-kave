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

import static cc.kave.rsse.calls.model.Constants.DUMMY_CCF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_DF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_MCF;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;

public class DictionaryBuilder {

	private Options opts;

	public DictionaryBuilder(Options opts) {
		this.opts = opts;
	}

	public Dictionary<IFeature> build(List<List<IFeature>> llf) {
		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();
		addDummies(dictionary);

		Map<IFeature, Integer> counts = new LinkedHashMap<>();
		for (List<IFeature> lf : llf) {
			for (IFeature f : new LinkedHashSet<>(lf)) {
				if (counts.containsKey(f)) {
					counts.put(f, counts.get(f) + 1);
				} else {
					counts.put(f, 1);
				}
			}
		}
		for (IFeature f : counts.keySet()) {
			int count = counts.get(f);
			if (f instanceof TypeFeature || count >= opts.keepOnlyFeaturesWithAtLeastOccurrences) {
				dictionary.add(f);
			}
		}

		return dictionary;
	}

	private void addDummies(Dictionary<IFeature> d) {
		d.add(DUMMY_CCF);
		d.add(DUMMY_MCF);
		d.add(DUMMY_DF);
	}
}