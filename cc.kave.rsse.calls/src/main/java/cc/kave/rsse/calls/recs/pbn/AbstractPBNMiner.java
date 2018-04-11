/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.rsse.calls.recs.pbn;

import java.util.List;

import cc.kave.commons.utils.io.Logger;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.OptionAwareFeatureFilter;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.usages.IUsage;

public abstract class AbstractPBNMiner<Model> {

	private final FeatureExtractor featureExtractor;
	private final DictionaryBuilder dictionaryBuilder;
	private final PatternFinderFactory patternFinderFactory;
	private final OptionAwareFeatureFilter featurePred;
	private final QueryOptions qOpts;
	private final MiningOptions mOpts;

	private int lastNumberOfFeatures = 0;
	private int lastNumberOfPatterns = 0;

	public AbstractPBNMiner(FeatureExtractor featureExtractor, DictionaryBuilder dictionaryBuilder,
			PatternFinderFactory patternFinderFactory, QueryOptions qOpts, MiningOptions mOpts,
			OptionAwareFeatureFilter featurePred) {
		this.featureExtractor = featureExtractor;
		this.dictionaryBuilder = dictionaryBuilder;
		this.patternFinderFactory = patternFinderFactory;
		this.qOpts = qOpts;
		this.mOpts = mOpts;
		this.featurePred = featurePred;
	}

	public Model learnModel(List<IUsage> usages) {
		Logger.debug("extracting features");
		List<List<IFeature>> features = extractFeatures(usages);
		Logger.debug("creating dictionary");
		Dictionary<IFeature> dictionary = dictionaryBuilder.newDictionary(features);

		lastNumberOfFeatures = dictionary.size();

		Logger.debug("mining");
		List<Pattern> patterns = patternFinderFactory.createPatternFinder().find(features, dictionary);

		lastNumberOfPatterns = patterns.size();

		Logger.debug("building network");
		Model network = buildModel(patterns, dictionary);
		return network;
	}

	protected abstract Model buildModel(List<Pattern> patterns, Dictionary<IFeature> dictionary);

	protected List<List<IFeature>> extractFeatures(List<IUsage> usages) {
		return featureExtractor.extract(usages);
	}

	public int getLastNumberOfFeatures() {
		return lastNumberOfFeatures;
	}

	public int getLastNumberOfPatterns() {
		return lastNumberOfPatterns;
	}
}