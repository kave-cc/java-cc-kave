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
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.recs.bmn.BMNMiner;
import cc.kave.rsse.calls.recs.bmn.BMNModel;
import cc.kave.rsse.calls.recs.bmn.BMNModelStore;
import cc.kave.rsse.calls.recs.bmn.BMNRecommender;
import cc.kave.rsse.calls.recs.freq.FreqModel;

public class UsageMining {

	private static final int MAX_NUM_USAGES = 40000;

	public static Object minePBN(List<IUsage> usages) {
		// Options opts = setup(OptionsBuilder.pbn(15));
		//
		// DictionaryBuilder db = new DictionaryBuilder(opts);
		// List<List<IFeature>> ufs = new FeatureExtractor(opts).extract(usages);
		// Dictionary<IFeature> uf = db.build(ufs);
		//
		// PatternFinder pf = new PatternFinderFactory(new VectorBuilder(opts),
		// opts).createPatternFinder();
		//
		// List<Pattern> patterns = pf.find(ufs, uf);
		//
		// BayesianNetwork model = new PBNModelBuilder().build(patterns, uf);
		return null; // TODO create new model builder
	}

	public static BMNModel mineBMN(List<IUsage> usages, Options opts) {

		if (usages.size() > MAX_NUM_USAGES) {
			Logger.log("More than %d usages, picking random subset", MAX_NUM_USAGES);
			usages = SublistSelector.pickRandomSublist(usages, MAX_NUM_USAGES);
		}

		DictionaryBuilder db = new DictionaryBuilder(opts);
		BMNMiner miner = new BMNMiner(new FeatureExtractor(opts), db, new VectorBuilder(opts));
		BMNModel model = miner.learnModel(usages);
		return model;
	}

	public static BMNRecommender getBMNRecommender(BMNModelStore store, Options opts) {
		return new BMNRecommender(new FeatureExtractor(opts), store, opts);
	}

	public static FreqModel mineFreq(List<IUsage> usages) {
		return null;
	}
}