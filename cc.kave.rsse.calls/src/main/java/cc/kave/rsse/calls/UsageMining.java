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

import static cc.kave.rsse.calls.mining.MiningOptions.newMiningOptions;
import static cc.kave.rsse.calls.mining.QueryOptions.newQueryOptions;
import static com.google.common.base.Predicates.alwaysTrue;

import java.util.List;

import com.google.common.base.Predicates;

import cc.kave.commons.utils.SublistSelector;
import cc.kave.commons.utils.io.Logger;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.OptionAwareFeatureFilter;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.mining.clustering.DistanceMeasureFactory;
import cc.kave.rsse.calls.mining.clustering.PatternFinder;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.recs.bmn.BMNMiner;
import cc.kave.rsse.calls.recs.bmn.BMNModel;
import cc.kave.rsse.calls.recs.bmn.BMNRecommender;
import cc.kave.rsse.calls.recs.freq.FreqModel;
import cc.kave.rsse.calls.recs.pbn.BayesianNetwork;
import cc.kave.rsse.calls.recs.pbn.PBNModelBuilder;
import cc.kave.rsse.calls.utils.OptionsUtils;

public class UsageMining {

	private static final int MAX_NUM_USAGES = 15000;

	private static final boolean USE_CLASS_CONTEXT = false;
	private static final boolean USE_DEFINITION = true;
	private static final boolean USE_PARAMETERS = false;

	public static Object minePBN(List<IUsage> usages) {
		String opts = OptionsUtils.pbn(15).c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS)
				.dropRareFeatures(true).min(1).get();
		MiningOptions mOpts = newMiningOptions(opts);
		QueryOptions qOpts = newQueryOptions(opts);

		DictionaryBuilder db = new DictionaryBuilder(mOpts, qOpts);
		List<List<IFeature>> ufs = new FeatureExtractor(mOpts).extract(usages);
		Dictionary<IFeature> uf = db.newDictionary(ufs);


		FeatureWeighter weigher = new FeatureWeighter(mOpts);

		PatternFinder pf = new PatternFinderFactory(weigher, new VectorBuilder(weigher), mOpts,
				new DistanceMeasureFactory(mOpts)).createPatternFinder();

		List<Pattern> patterns = pf.find(ufs, uf);

		BayesianNetwork model = new PBNModelBuilder().build(patterns, uf);
		return null; // TODO create new model builder
	}

	public static BMNModel mineBMN(List<IUsage> usages) {

		if (usages.size() > MAX_NUM_USAGES) {
			Logger.log("More than %d usages, picking random subset", MAX_NUM_USAGES);
			usages = SublistSelector.pickRandomSublist(usages, MAX_NUM_USAGES);
		}

		String opts = OptionsUtils.bmn().c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS).dropRareFeatures(true)
				.min(1).get();
		MiningOptions mOpts = newMiningOptions(opts);
		QueryOptions qOpts = newQueryOptions(opts);

		DictionaryBuilder db = new DictionaryBuilder(mOpts, qOpts);

		BMNMiner miner = new BMNMiner(mOpts, qOpts, new FeatureExtractor(mOpts), db);
		BMNModel model = miner.learnModel(usages);
		return model;
	}

	public static BMNRecommender getBMNRecommender(BMNModel model) {
		String opts = OptionsUtils.bmn().c(USE_CLASS_CONTEXT).d(USE_DEFINITION).p(USE_PARAMETERS).dropRareFeatures(true)
				.min(1).get();
		MiningOptions mOpts = newMiningOptions(opts);
		QueryOptions qOpts = newQueryOptions(opts);

		return new BMNRecommender(new FeatureExtractor(mOpts), model, qOpts);
	}

	public static FreqModel mineFreq(List<IUsage> usages) {
		// TODO Auto-generated method stub
		return null;
	}
}