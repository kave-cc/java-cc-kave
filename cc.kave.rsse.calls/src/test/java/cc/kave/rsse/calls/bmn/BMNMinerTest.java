/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.bmn;

import static cc.kave.rsse.calls.options.QueryOptions.newQueryOptions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.rsse.calls.bmn.BMNMiner;
import cc.kave.rsse.calls.bmn.BMNModel;
import cc.kave.rsse.calls.bmn.Table;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.extraction.features.FeatureExtractor;
import cc.kave.rsse.calls.extraction.features.OptionAwareFeaturePredicate;
import cc.kave.rsse.calls.options.MiningOptions;
import cc.kave.rsse.calls.options.QueryOptions;
import cc.kave.rsse.calls.options.MiningOptions.Algorithm;
import cc.kave.rsse.calls.options.MiningOptions.DistanceMeasure;
import cc.kave.rsse.calls.usages.Usage;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.SuperMethodFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;
import cc.recommenders.mining.calls.DictionaryBuilder;

public class BMNMinerTest {

	@Mock
	private TypeFeature type1;
	@Mock
	private ClassFeature class1;
	@Mock
	private ClassFeature class2;
	@Mock
	private SuperMethodFeature super1;
	@Mock
	private SuperMethodFeature super2;
	@Mock
	private FirstMethodFeature method1;
	@Mock
	private FirstMethodFeature method2;
	@Mock
	private DefinitionFeature def1;
	@Mock
	private DefinitionFeature def2;
	@Mock
	private ParameterFeature param1;
	@Mock
	private ParameterFeature param2;
	@Mock
	private CallFeature call1;
	@Mock
	private CallFeature call2;
	@Mock
	private CallFeature call3;

	@Mock
	private DictionaryBuilder<Usage, UsageFeature> dictBuilder;
	@Mock
	private FeatureExtractor<Usage, UsageFeature> extractor;

	private Dictionary<UsageFeature> dict;
	private MiningOptions mOpts;
	private QueryOptions qOpts;
	private List<Usage> usages;
	private BMNMiner sut;
	private OptionAwareFeaturePredicate featurePredicate;

	@Before
	public void setup() {
		initMocks(this);

		mOpts = MiningOptions.newMiningOptions("BMN+MANHATTAN+W[0;0;0;0]-INIT-DROP");
		qOpts = new QueryOptions();
		dict = new Dictionary<UsageFeature>();
		usages = Lists.newLinkedList();
		featurePredicate = new OptionAwareFeaturePredicate(qOpts);

		when(dictBuilder.newDictionary(eq(usages))).thenReturn(dict);
		when(dictBuilder.newDictionary(eq(usages), any(OptionAwareFeaturePredicate.class))).thenReturn(dict);
		sut = new BMNMiner(mOpts, qOpts, dictBuilder, extractor);
	}

	@Test(expected = AssertionException.class)
	public void distanceMeasureIsValidated() {
		mOpts.setAlgorithm(Algorithm.BMN);
		mOpts.setDistanceMeasure(DistanceMeasure.COSINE);
		sut.createRecommender(usages);
	}

	@Test(expected = AssertionException.class)
	public void algorithmIsValidated() {
		mOpts.setAlgorithm(Algorithm.CANOPY);
		mOpts.setDistanceMeasure(DistanceMeasure.MANHATTAN);
		sut.createRecommender(usages);
	}

	@Test
	public void integrationTestOfLearning() {
		addUsage(method1, call1);
		addUsage(method1, call1, call2);
		addUsage(method1, call1, call2, call3);
		addUsage(method2, call2, call3);

		BMNModel model = sut.learnModel(usages);

		assertEquals(dict, model.dictionary);

		Table actual = model.table;

		// dict: ctx1, call1, call2, call3, ctx2
		boolean[][] table = new boolean[][] { q(1, 1, 0, 0, 0), q(1, 1, 1, 0, 0), q(1, 1, 1, 1, 0), q(0, 0, 1, 1, 1), };

		int[] freqs = new int[] { 1, 1, 1, 1 };

		Table expected = new Table(table, freqs);

		assertEquals(expected, actual);
	}

	@Test
	public void integrationTestOfRecommenderInstantiation() {
		// TODO add tests here
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_min() {
		setOpts("-CLASS-METHOD-DEF-PARAMS");
		addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_CLASS() {
		setOpts("+CLASS-METHOD-DEF-PARAMS");
		addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, class1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_METHOD() {
		setOpts("-CLASS+METHOD-DEF-PARAMS");
		addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, method1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_DEF() {
		setOpts("-CLASS-METHOD+DEF-PARAMS");
		addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, def1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_PARAM() {
		setOpts("-CLASS-METHOD-DEF+PARAMS");
		addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, param1);
	}

	@Test
	public void complexExampleForFeatureCreation() {
		setOpts("+CLASS+METHOD+DEF+PARAMS");
		addUsage(class1, method1, super1, def1, param1);
		addUsage(class2, method2, super2, def2, param2);
		learnModelAndExpectInDictionary(class1, class2, method1, method2, def1, def2, param1, param2);
	}

	private void learnModelAndExpectInDictionary(UsageFeature... fs) {
		BMNModel model = sut.learnModel(usages);
		Dictionary<UsageFeature> dict = model.dictionary;
		assertEquals(fs.length, dict.size());
		for (UsageFeature f : fs) {
			assertTrue(dict.contains(f));
		}
		boolean[] firstRow = model.table.getBMNTable()[0];
		assertEquals(fs.length, firstRow.length);
	}

	private void setOpts(String in) {
		qOpts.setFrom(newQueryOptions(in));
	}

	private void addUsage(UsageFeature... featureArr) {
		Usage u1 = mock(Usage.class);
		List<UsageFeature> featurelist = Lists.newLinkedList();
		for (UsageFeature f : featureArr) {
			featurelist.add(f);
			if (featurePredicate.apply(f)) {
				dict.add(f);
			}
		}
		when(extractor.extract(u1)).thenReturn(featurelist);
		usages.add(u1);
	}

	private static boolean[] q(int... values) {
		boolean[] res = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i] == 1;
		}
		return res;
	}
}