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
package cc.kave.rsse.calls.recs.bmn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;

import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IUsage;

@Ignore
public class BMNMinerTest {

	@Mock
	private TypeFeature type1;
	@Mock
	private ClassContextFeature class1;
	@Mock
	private ClassContextFeature class2;
	// @Mock
	// private SuperMethodFeature super1;
	// @Mock
	// private SuperMethodFeature super2;
	@Mock
	private MethodContextFeature method1;
	@Mock
	private MethodContextFeature method2;
	@Mock
	private DefinitionFeature def1;
	@Mock
	private DefinitionFeature def2;
	// @Mock
	// private ParameterFeature param1;
	// @Mock
	// private ParameterFeature param2;
	@Mock
	private UsageSiteFeature call1;
	@Mock
	private UsageSiteFeature call2;
	@Mock
	private UsageSiteFeature call3;

	@Mock
	private DictionaryBuilder dictBuilder;
	@Mock
	private FeatureExtractor extractor;

	private Dictionary<IFeature> dict;
	private Options opts;
	private List<IUsage> usages;
	private BMNMiner sut;

	@Before
	public void setup() {
		initMocks(this);

		opts = new Options("BMN+MANHATTAN+W[0;0;0;0]-INIT-DROP");
		dict = new Dictionary<IFeature>();
		usages = Lists.newLinkedList();

		// when(dictBuilder.newDictionary(eq(usages),
		// any(OptionAwareFeatureFilter.class))).thenReturn(dict);
		sut = new BMNMiner(opts, extractor, dictBuilder);
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
		// addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_CLASS() {
		setOpts("+CLASS-METHOD-DEF-PARAMS");
		// addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, class1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_METHOD() {
		setOpts("-CLASS+METHOD-DEF-PARAMS");
		// addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, method1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_DEF() {
		setOpts("-CLASS-METHOD+DEF-PARAMS");
		// addUsage(type1, class1, method1, super1, def1, call1, param1);
		learnModelAndExpectInDictionary(type1, call1, def1);
	}

	@Test
	public void unnecessaryFeaturesDoNotCreateDimensions_PARAM() {
		setOpts("-CLASS-METHOD-DEF+PARAMS");
		// addUsage(type1, class1, method1, super1, def1, call1, param1);
		// learnModelAndExpectInDictionary(type1, call1, param1);
	}

	@Test
	public void complexExampleForFeatureCreation() {
		setOpts("+CLASS+METHOD+DEF+PARAMS");
		// addUsage(class1, method1, super1, def1, param1);
		// addUsage(class2, method2, super2, def2, param2);
		// learnModelAndExpectInDictionary(class1, class2, method1, method2, def1, def2,
		// param1, param2);
	}

	private void setOpts(String string) {
		fail();
		opts = new Options(string);
	}

	private void learnModelAndExpectInDictionary(IFeature... fs) {
		BMNModel model = sut.learnModel(usages);
		Dictionary<IFeature> dict = model.dictionary;
		assertEquals(fs.length, dict.size());
		for (IFeature f : fs) {
			assertTrue(dict.contains(f));
		}
		boolean[] firstRow = model.table.getBMNTable()[0];
		assertEquals(fs.length, firstRow.length);
	}

	private void addUsage(IFeature... featureArr) {
		IUsage u1 = mock(IUsage.class);
		List<IFeature> featurelist = Lists.newLinkedList();
		for (IFeature f : featureArr) {
			featurelist.add(f);
			dict.add(f);
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