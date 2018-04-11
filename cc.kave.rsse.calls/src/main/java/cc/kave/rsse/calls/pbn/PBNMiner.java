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
package cc.kave.rsse.calls.pbn;

import java.util.List;

import com.google.inject.Inject;

import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.FeatureExtractor;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.extraction.features.RareFeatureDropper;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.ModelBuilder;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.pbn.clustering.Pattern;
import cc.kave.rsse.calls.pbn.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.pbn.model.BayesianNetwork;
import cc.kave.rsse.calls.usages.features.UsageFeature;
import cc.kave.rsse.calls.usages.model.IUsage;
import cc.kave.rsse.calls.usages.model.impl.Usage;

public class PBNMiner extends AbstractPBNMiner<BayesianNetwork> {

	private ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder;
	private QueryOptions qOpts;

	@Inject
	public PBNMiner(FeatureExtractor<IUsage, UsageFeature> featureExtractor,
			DictionaryBuilder<IUsage, UsageFeature> dictionaryBuilder,
			PatternFinderFactory<UsageFeature> patternFinderFactory,
			ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder, QueryOptions qOpts, MiningOptions mOpts,
			RareFeatureDropper<UsageFeature> dropper, OptionAwareFeaturePredicate featurePred) {

		super(featureExtractor, dictionaryBuilder, patternFinderFactory, qOpts, mOpts, dropper, featurePred);
		this.modelBuilder = modelBuilder;
		this.qOpts = qOpts;
	}

	@Override
	protected BayesianNetwork buildModel(List<Pattern<UsageFeature>> patterns, Dictionary<UsageFeature> dictionary) {
		return modelBuilder.build(patterns, dictionary);
	}

	@Override
	public ICallsRecommender<Usage> createRecommender(List<IUsage> in) {
		BayesianNetwork network = learnModel(in);
		return new PBNRecommender(network, qOpts);
	}
}