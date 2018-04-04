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
package cc.kave.rsse.calls.bmn;

import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.FeatureExtractor;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.Miner;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.options.MiningOptions.Algorithm;
import cc.kave.rsse.calls.options.MiningOptions.DistanceMeasure;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.IUsage;
import cc.kave.rsse.calls.usages.features.UsageFeature;

import com.google.inject.Inject;

public class BMNMiner implements Miner<IUsage, Usage> {

	private final QueryOptions qOpts;
	private final DictionaryBuilder<IUsage, UsageFeature> dictBuilder;
	private final FeatureExtractor<IUsage, UsageFeature> extractor;
	private MiningOptions mOpts;

	@Inject
	public BMNMiner(MiningOptions mOpts, QueryOptions qOpts, DictionaryBuilder<IUsage, UsageFeature> dictBuilder,
			FeatureExtractor<IUsage, UsageFeature> extractor) {
		this.mOpts = mOpts;
		this.qOpts = qOpts;
		this.dictBuilder = dictBuilder;
		this.extractor = extractor;
	}

	@Override
	public BMNModel learnModel(List<IUsage> in) {
		Asserts.assertTrue(Algorithm.BMN.equals(mOpts.getAlgorithm()));
		Asserts.assertTrue(DistanceMeasure.MANHATTAN.equals(mOpts.getDistanceMeasure()));

		BMNModel bmnModel = new BMNModel();
		bmnModel.dictionary = dictBuilder.newDictionary(in, new OptionAwareFeaturePredicate(qOpts));

		bmnModel.table = new Table(bmnModel.dictionary.size());

		for (IUsage u : in) {
			boolean[] uArr = convert(u, bmnModel.dictionary);
			bmnModel.table.add(uArr);
		}

		return bmnModel;
	}

	private boolean[] convert(IUsage u, Dictionary<UsageFeature> dict) {
		boolean[] uArr = new boolean[dict.size()];
		List<UsageFeature> fs = extractor.extract(u);
		for (int i = 0; i < dict.size(); i++) {
			UsageFeature f = dict.getEntry(i);
			uArr[i] = fs.contains(f);
		}
		return uArr;
	}

	@Override
	public BMNRecommender createRecommender(List<IUsage> in) {
		BMNModel model = learnModel(in);
		return new BMNRecommender(extractor, model, qOpts);
	}
}