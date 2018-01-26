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
package cc.recommenders.mining.calls.pbn;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.Inject;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.mining.calls.PatternFinderFactory;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.OptionAwareFeaturePredicate;
import cc.recommenders.mining.features.RareFeatureDropper;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;

public class PBNMiner extends AbstractPBNMiner<BayesianNetwork> {

	private ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder;
	private QueryOptions qOpts;

	@Inject
	public PBNMiner(FeatureExtractor<Usage, UsageFeature> featureExtractor,
			DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder,
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
	public ICallsRecommender<Query> createRecommender(List<Usage> in) {
		BayesianNetwork network = learnModel(in);
		return new PBNRecommender(network, qOpts);
	}
}