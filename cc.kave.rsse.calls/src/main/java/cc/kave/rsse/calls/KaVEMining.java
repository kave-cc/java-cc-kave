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
package cc.kave.rsse.calls;

import static cc.kave.rsse.calls.options.MiningOptions.newMiningOptions;
import static cc.kave.rsse.calls.options.QueryOptions.newQueryOptions;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import cc.kave.rsse.calls.bmn.BMNMiner;
import cc.kave.rsse.calls.bmn.BMNModel;
import cc.kave.rsse.calls.bmn.BMNRecommender;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.UsageFeatureExtractor;
import cc.kave.rsse.calls.extraction.features.UsageFeatureWeighter;
import cc.kave.rsse.calls.freq.FreqModel;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.OptionsUtils;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.pbn.PBNModel;
import cc.kave.rsse.calls.pbn.PBNModelBuilder;
import cc.kave.rsse.calls.pbn.clustering.Pattern;
import cc.kave.rsse.calls.pbn.clustering.PatternFinder;
import cc.kave.rsse.calls.pbn.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.features.UsageFeature;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.DistanceMeasureFactory;

public class KaVEMining {

	private static final boolean USE_CLASS_CONTEXT = false;
	private static final boolean USE_DEFINITION = true;
	private static final boolean USE_PARAMETERS = false;

	public static PBNModel minePBN(List<Usage> usages) {
		String opts = OptionsUtils.pbn(15).c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS).get();
		MiningOptions mOpts = newMiningOptions(opts);

		DictionaryBuilder<Usage, UsageFeature> db = new DictionaryBuilder<>(new UsageFeatureExtractor(mOpts));
		Dictionary<UsageFeature> uf = db.newDictionary(usages);

		List<List<UsageFeature>> ufs = new UsageFeatureExtractor(mOpts).extract(usages);

		PatternFinder<UsageFeature> pf = new PatternFinderFactory<>(new UsageFeatureWeighter(mOpts), mOpts,
				new DistanceMeasureFactory(mOpts)).createPatternFinder();

		List<Pattern<UsageFeature>> patterns = pf.find(ufs, uf);

		BayesianNetwork model = new PBNModelBuilder().build(patterns, uf);
		return null; // TODO create new model builder
	}

	public static BMNModel mineBMN(List<Usage> usages) {
		String opts = OptionsUtils.bmn().c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS).get();
		MiningOptions mOpts = newMiningOptions(opts);
		QueryOptions qOpts = newQueryOptions(opts);

		DictionaryBuilder<Usage, UsageFeature> db = new DictionaryBuilder<>(new UsageFeatureExtractor(mOpts));

		BMNMiner miner = new BMNMiner(mOpts, qOpts, db, new UsageFeatureExtractor(mOpts));
		BMNModel model = miner.learnModel(usages);
		return model;
	}

	public static BMNRecommender getBMNRecommender(BMNModel model) {
		String opts = OptionsUtils.bmn().c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS).get();
		MiningOptions mOpts = newMiningOptions(opts);
		QueryOptions qOpts = newQueryOptions(opts);

		return new BMNRecommender(new UsageFeatureExtractor(mOpts), model, qOpts);
	}

	public static FreqModel mineFreq(List<Usage> usages) {
		// TODO Auto-generated method stub
		return null;
	}
}