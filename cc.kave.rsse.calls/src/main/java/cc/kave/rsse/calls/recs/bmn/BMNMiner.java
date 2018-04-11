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
package cc.kave.rsse.calls.recs.bmn;

import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.MiningOptions.Algorithm;
import cc.kave.rsse.calls.mining.MiningOptions.DistanceMeasure;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.usages.IUsage;

public class BMNMiner {

	private final QueryOptions qOpts;
	private final DictionaryBuilder dictBuilder;
	private final FeatureExtractor extractor;
	private MiningOptions mOpts;

	public BMNMiner(MiningOptions mOpts, QueryOptions qOpts, FeatureExtractor extractor,
			DictionaryBuilder dictBuilder) {
		this.mOpts = mOpts;
		this.qOpts = qOpts;
		this.dictBuilder = dictBuilder;
		this.extractor = extractor;
	}

	public BMNModel learnModel(List<IUsage> in) {
		Asserts.assertTrue(Algorithm.BMN.equals(mOpts.getAlgorithm()));
		Asserts.assertTrue(DistanceMeasure.MANHATTAN.equals(mOpts.getDistanceMeasure()));

		BMNModel bmnModel = new BMNModel();
		// TODO
		// bmnModel.dictionary = dictBuilder.newDictionary();

		bmnModel.table = new Table(bmnModel.dictionary.size());

		for (IUsage u : in) {
			boolean[] uArr = convert(u, bmnModel.dictionary);
			bmnModel.table.add(uArr);
		}

		return bmnModel;
	}

	private boolean[] convert(IUsage u, Dictionary<IFeature> dict) {
		boolean[] uArr = new boolean[dict.size()];
		List<IFeature> fs = extractor.extract(u);
		for (int i = 0; i < dict.size(); i++) {
			IFeature f = dict.getEntry(i);
			uArr[i] = fs.contains(f);
		}
		return uArr;
	}

	public BMNRecommender createRecommender(List<IUsage> in) {
		BMNModel model = learnModel(in);
		return new BMNRecommender(extractor, model, qOpts);
	}
}