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
package cc.recommenders.mining.calls;

import java.util.Collection;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.features.FeatureExtractor;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.inject.Inject;

public class DictionaryBuilder<Usage, Feature> {

	private final FeatureExtractor<Usage, Feature> featureExtractor;

	@Inject
	public DictionaryBuilder(FeatureExtractor<Usage, Feature> featureExtractor) {
		this.featureExtractor = featureExtractor;

	}

	public Dictionary<Feature> newDictionary(Collection<Usage> usages, Predicate<Feature> useFeature) {
		Dictionary<Feature> dictionary = new Dictionary<Feature>();

		for (Usage u : usages) {
			for (Feature f : featureExtractor.extract(u)) {
				if (useFeature.apply(f)) {
					dictionary.add(f);
				}
			}
		}

		return dictionary;
	}

	public Dictionary<Feature> newDictionary(Collection<Usage> usages) {
		Predicate<Feature> alwaysTrue = Predicates.alwaysTrue();
		return newDictionary(usages, alwaysTrue);
	}
}