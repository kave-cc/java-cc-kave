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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.junit.Before;
import org.junit.Test;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.mining.calls.PatternFinder;
import cc.recommenders.mining.calls.PatternFinderFactory;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.mining.calls.pbn.PBNModelBuilder;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.OptionAwareFeaturePredicate;
import cc.recommenders.mining.features.RareFeatureDropper;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.UsageFeature;

public class PBNMinerTest {

	private PBNMiner sut;

	private FeatureExtractor<Usage, UsageFeature> extractor;
	private DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder;
	private ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder;
	private PatternFinder<UsageFeature> patternFinder;
	private PatternFinderFactory<UsageFeature> patternFinderFactory;

	private List<Usage> usages;

	private Dictionary<UsageFeature> dictionary;
	private List<Pattern<UsageFeature>> patterns;
	private BayesianNetwork network;

	private List<List<UsageFeature>> features;

	private QueryOptions queryOptions;

	private MiningOptions miningOptions;

	private RareFeatureDropper<UsageFeature> rareFeatureDropper;

	private Dictionary<UsageFeature> filteredDictionary;

	private OptionAwareFeaturePredicate featurePred;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		usages = mock(List.class);
		patterns = mock(List.class);
		network = mock(BayesianNetwork.class);
		dictionaryBuilder = mock(DictionaryBuilder.class);
		extractor = mock(FeatureExtractor.class);
		patternFinderFactory = mock(PatternFinderFactory.class);
		patternFinder = mock(PatternFinder.class);
		modelBuilder = mock(PBNModelBuilder.class);
		rareFeatureDropper = mock(RareFeatureDropper.class);
		featurePred = mock(OptionAwareFeaturePredicate.class);

		features = newArrayList();
		dictionary = createDict("a", "b", "x", "c");
		filteredDictionary = createDict("a", "b", "c");
		queryOptions = new QueryOptions();
		miningOptions = new MiningOptions();
		
		when(dictionaryBuilder.newDictionary(eq(usages))).thenReturn(dictionary);
		when(dictionaryBuilder.newDictionary(eq(usages), eq(featurePred))).thenReturn(dictionary);

		when(extractor.extract(eq(usages))).thenReturn(features);

		when(patternFinderFactory.createPatternFinder()).thenReturn(patternFinder);
		when(patternFinder.find(eq(features), any(Dictionary.class))).thenReturn(patterns);

		when(modelBuilder.build(eq(patterns), any(Dictionary.class))).thenReturn(network);

		when(rareFeatureDropper.dropRare(any(Dictionary.class), eq(features))).thenReturn(filteredDictionary);

		sut = new PBNMiner(extractor, dictionaryBuilder, patternFinderFactory, modelBuilder, queryOptions,
				miningOptions, rareFeatureDropper, featurePred);
	}

	private Dictionary<UsageFeature> createDict(String... names) {
		Dictionary<UsageFeature> d = new Dictionary<UsageFeature>();
		for (String name : names) {
			d.add(new FirstMethodFeature(CoReMethodName.get("Lsome/Type." + name + "()V")));
		}
		return d;
	}

	@Test
	public void featuresAreExtracted() {
		sut.learnModel(usages);
		verify(extractor).extract(usages);
	}

	@Test
	public void dictionaryIsCreatedFromAllUsages() {
		sut.learnModel(usages);
		verify(dictionaryBuilder).newDictionary(usages, featurePred);
	}

	@Test
	public void patternsAreCreatedWithVectorsAndDictionary() {
		sut.learnModel(usages);
		verify(patternFinder).find(features, dictionary);
	}

	@Test
	public void modelIsCreatedWithPatterns() {
		sut.learnModel(usages);
		verify(modelBuilder).build(patterns, dictionary);
	}

	@Test
	public void modelIsReturned() {
		BayesianNetwork actual = sut.learnModel(usages);
		BayesianNetwork expected = network;
		assertSame(expected, actual);
	}

	@Test
	public void whatHappensWhenFeatureDroppingIsEnabled() {
		miningOptions.setFeatureDropping(true);

		BayesianNetwork actual = sut.learnModel(usages);
		BayesianNetwork expected = network;
		assertSame(expected, actual);
	}
}