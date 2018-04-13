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
package cc.kave.rsse.calls.recs.pbn;

import java.util.List;

import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class PBNMiner extends AbstractPBNMiner<BayesianNetwork> {

	private PBNModelBuilder modelBuilder;
	private QueryOptions qOpts;

	public PBNMiner(FeatureExtractor featureExtractor, DictionaryBuilder dictionaryBuilder,
			PatternFinderFactory patternFinderFactory, PBNModelBuilder modelBuilder, QueryOptions qOpts,
			MiningOptions mOpts) {

		super(featureExtractor, dictionaryBuilder, patternFinderFactory, qOpts, mOpts);
		this.modelBuilder = modelBuilder;
		this.qOpts = qOpts;
	}

	@Override
	protected BayesianNetwork buildModel(List<Pattern> patterns, Dictionary<IFeature> dictionary) {
		return modelBuilder.build(patterns, dictionary);
	}

	public ICallsRecommender<Usage> createRecommender(List<IUsage> in) {
		BayesianNetwork network = learnModel(in);
		return new PBNRecommender(network, qOpts);
	}
}