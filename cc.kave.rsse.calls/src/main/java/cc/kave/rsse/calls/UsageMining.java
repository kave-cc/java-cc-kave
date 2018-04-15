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

import java.util.List;

import cc.kave.commons.utils.SublistSelector;
import cc.kave.commons.utils.io.Logger;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.Options;
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
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class UsageMining {

	private static final int MAX_NUM_USAGES = 15000;

	private static final boolean USE_CLASS_CONTEXT = false;
	private static final boolean USE_DEFINITION = true;
	private static final boolean USE_PARAMETERS = false;

	public static Object minePBN(List<IUsage> usages) {
		Options opts = OptionsBuilder.pbn(15).cCtx(USE_CLASS_CONTEXT).def(USE_DEFINITION).params(USE_PARAMETERS)
				.atLeast(3).minProbability(1).get();

		DictionaryBuilder db = new DictionaryBuilder(opts);
		List<List<IFeature>> ufs = new FeatureExtractor().extract(usages);
		Dictionary<IFeature> uf = db.build(ufs);

		FeatureWeighter weigher = new FeatureWeighter(opts);

		PatternFinder pf = new PatternFinderFactory(weigher, new VectorBuilder(weigher), opts,
				new DistanceMeasureFactory(opts)).createPatternFinder();

		List<Pattern> patterns = pf.find(ufs, uf);

		BayesianNetwork model = new PBNModelBuilder().build(patterns, uf);
		return null; // TODO create new model builder
	}

	public static BMNModel mineBMN(List<IUsage> usages) {

		if (usages.size() > MAX_NUM_USAGES) {
			Logger.log("More than %d usages, picking random subset", MAX_NUM_USAGES);
			usages = SublistSelector.pickRandomSublist(usages, MAX_NUM_USAGES);
		}

		Options opts = OptionsBuilder.bmn().cCtx(USE_CLASS_CONTEXT).def(USE_DEFINITION).params(USE_PARAMETERS)
				.atLeast(3).minProbability(1).get();

		DictionaryBuilder db = new DictionaryBuilder(opts);

		BMNMiner miner = new BMNMiner(opts, new FeatureExtractor(), db);
		BMNModel model = miner.learnModel(usages);
		return model;
	}

	public static BMNRecommender getBMNRecommender(BMNModel model) {
		Options opts = OptionsBuilder.bmn().cCtx(USE_CLASS_CONTEXT).def(USE_DEFINITION).params(USE_PARAMETERS)
				.atLeast(3).minProbability(1).get();

		return new BMNRecommender(new FeatureExtractor(), model, opts);
	}

	public static FreqModel mineFreq(List<IUsage> usages) {
		// TODO Auto-generated method stub
		return null;
	}
}