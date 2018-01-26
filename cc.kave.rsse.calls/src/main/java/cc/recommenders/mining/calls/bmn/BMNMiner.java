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
package cc.recommenders.mining.calls.bmn;

import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.Miner;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.MiningOptions.Algorithm;
import cc.recommenders.mining.calls.MiningOptions.DistanceMeasure;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.OptionAwareFeaturePredicate;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;

import com.google.inject.Inject;

public class BMNMiner implements Miner<Usage, Query> {

	private final QueryOptions qOpts;
	private final DictionaryBuilder<Usage, UsageFeature> dictBuilder;
	private final FeatureExtractor<Usage, UsageFeature> extractor;
	private MiningOptions mOpts;

	@Inject
	public BMNMiner(MiningOptions mOpts, QueryOptions qOpts, DictionaryBuilder<Usage, UsageFeature> dictBuilder,
			FeatureExtractor<Usage, UsageFeature> extractor) {
		this.mOpts = mOpts;
		this.qOpts = qOpts;
		this.dictBuilder = dictBuilder;
		this.extractor = extractor;
	}

	@Override
	public BMNModel learnModel(List<Usage> in) {
		Asserts.assertTrue(Algorithm.BMN.equals(mOpts.getAlgorithm()));
		Asserts.assertTrue(DistanceMeasure.MANHATTAN.equals(mOpts.getDistanceMeasure()));

		BMNModel bmnModel = new BMNModel();
		bmnModel.dictionary = dictBuilder.newDictionary(in, new OptionAwareFeaturePredicate(qOpts));

		bmnModel.table = new Table(bmnModel.dictionary.size());

		for (Usage u : in) {
			boolean[] uArr = convert(u, bmnModel.dictionary);
			bmnModel.table.add(uArr);
		}

		return bmnModel;
	}

	private boolean[] convert(Usage u, Dictionary<UsageFeature> dict) {
		boolean[] uArr = new boolean[dict.size()];
		List<UsageFeature> fs = extractor.extract(u);
		for (int i = 0; i < dict.size(); i++) {
			UsageFeature f = dict.getEntry(i);
			uArr[i] = fs.contains(f);
		}
		return uArr;
	}

	@Override
	public BMNRecommender createRecommender(List<Usage> in) {
		BMNModel model = learnModel(in);
		return new BMNRecommender(extractor, model, qOpts);
	}
}