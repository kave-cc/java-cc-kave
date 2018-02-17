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
package cc.kave.rsse.calls.pbn;

import java.util.List;
import java.util.Set;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.Inject;

import cc.kave.commons.utils.io.Logger;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.FeatureExtractor;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.extraction.features.RareFeatureDropper;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.pbn.clustering.Pattern;
import cc.kave.rsse.calls.pbn.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.usages.Query;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.features.UsageFeature;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.Miner;
import cc.recommenders.mining.calls.ModelBuilder;

public abstract class AbstractPBNMiner<Model> implements Miner<Usage, Query> {

	private final FeatureExtractor<Usage, UsageFeature> featureExtractor;
	private final DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder;
	private final PatternFinderFactory<UsageFeature> patternFinderFactory;
	private final RareFeatureDropper<UsageFeature> dropper;
	private final OptionAwareFeaturePredicate featurePred;
	private final QueryOptions qOpts;
	private final MiningOptions mOpts;

	private int lastNumberOfFeatures = 0;
	private int lastNumberOfPatterns = 0;

	public AbstractPBNMiner(FeatureExtractor<Usage, UsageFeature> featureExtractor,
			DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder,
			PatternFinderFactory<UsageFeature> patternFinderFactory, QueryOptions qOpts, MiningOptions mOpts,
			RareFeatureDropper<UsageFeature> dropper, OptionAwareFeaturePredicate featurePred) {
		this.featureExtractor = featureExtractor;
		this.dictionaryBuilder = dictionaryBuilder;
		this.patternFinderFactory = patternFinderFactory;
		this.qOpts = qOpts;
		this.mOpts = mOpts;
		this.dropper = dropper;
		this.featurePred = featurePred;
	}

	@Override
	public Model learnModel(List<Usage> usages) {
		Logger.debug("extracting features");
		List<List<UsageFeature>> features = extractFeatures(usages);
		Logger.debug("creating dictionary");
		Dictionary<UsageFeature> dictionary = createDictionary(usages, features);

		lastNumberOfFeatures = dictionary.size();

		Logger.debug("mining");
		List<Pattern<UsageFeature>> patterns = patternFinderFactory.createPatternFinder().find(features, dictionary);

		lastNumberOfPatterns = patterns.size();

		Logger.debug("building network");
		Model network = buildModel(patterns, dictionary);
		return network;
	}
	
	protected abstract Model buildModel(List<Pattern<UsageFeature>> patterns, Dictionary<UsageFeature> dictionary);

	protected List<List<UsageFeature>> extractFeatures(List<Usage> usages) {
		return featureExtractor.extract(usages);
	}

	protected Dictionary<UsageFeature> createDictionary(List<Usage> usages, List<List<UsageFeature>> features) {
		Dictionary<UsageFeature> rawDictionary = dictionaryBuilder.newDictionary(usages, featurePred);
		if (mOpts.isFeatureDropping()) {
			Dictionary<UsageFeature> dictionary = dropper.dropRare(rawDictionary, features);
			Set<String> diff = DictionaryHelper.diff(rawDictionary, dictionary);

			Set<UsageFeature> rawClassContexts = new DictionaryHelper(rawDictionary).getClassContexts();
			Set<UsageFeature> classContexts = new DictionaryHelper(dictionary).getClassContexts();
			return dictionary;
		} else {
			return rawDictionary;
		}
	}

	public int getLastNumberOfFeatures() {
		return lastNumberOfFeatures;
	}

	public int getLastNumberOfPatterns() {
		return lastNumberOfPatterns;
	}
}