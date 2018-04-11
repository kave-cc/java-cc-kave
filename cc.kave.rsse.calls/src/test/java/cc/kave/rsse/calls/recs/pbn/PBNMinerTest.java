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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.OptionAwareFeatureFilter;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.clustering.PatternFinder;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.recs.pbn.BayesianNetwork;
import cc.kave.rsse.calls.recs.pbn.PBNMiner;
import cc.kave.rsse.calls.recs.pbn.PBNModelBuilder;

public class PBNMinerTest {

	private PBNMiner sut;

	private FeatureExtractor extractor;
	private DictionaryBuilder dictionaryBuilder;
	private PBNModelBuilder modelBuilder;
	private PatternFinder patternFinder;
	private PatternFinderFactory patternFinderFactory;

	private List<IUsage> usages;

	private Dictionary<IFeature> dictionary;
	private List<Pattern> patterns;
	private BayesianNetwork network;

	private List<List<IFeature>> features;

	private QueryOptions queryOptions;

	private MiningOptions miningOptions;

	private Dictionary<IFeature> filteredDictionary;

	private OptionAwareFeatureFilter featurePred;

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
		featurePred = mock(OptionAwareFeatureFilter.class);

		features = newArrayList();
		dictionary = createDict("a", "b", "x", "c");
		filteredDictionary = createDict("a", "b", "c");
		queryOptions = new QueryOptions();
		miningOptions = new MiningOptions();

		// when(dictionaryBuilder.newDictionary(eq(usages),
		// eq(featurePred))).thenReturn(dictionary);

		when(extractor.extract(eq(usages))).thenReturn(features);

		when(patternFinderFactory.createPatternFinder()).thenReturn(patternFinder);
		when(patternFinder.find(eq(features), any(Dictionary.class))).thenReturn(patterns);

		when(modelBuilder.build(eq(patterns), any(Dictionary.class))).thenReturn(network);

		sut = new PBNMiner(extractor, dictionaryBuilder, patternFinderFactory, modelBuilder, queryOptions,
				miningOptions, featurePred);
	}

	private Dictionary<IFeature> createDict(String... names) {
		Dictionary<IFeature> d = new Dictionary<IFeature>();
		for (String name : names) {
			d.add(new MethodContextFeature(Names.newMethod("[p:void] [some.Type, P]." + name + "()")));
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
		// verify(dictionaryBuilder).newDictionary(usages, featurePred);
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