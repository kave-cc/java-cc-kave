/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.recs.bmn;

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.Constants.DUMMY_CCF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_DF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_MCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_CCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_DF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_MCF;
import static cc.kave.rsse.calls.model.usages.impl.MemberAccesses.methodCall;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;

import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.MemberAccessFeature;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class BMNMinerTest {

	private TypeFeature TYPE = new TypeFeature(newType("T0, P"));

	private MemberAccessFeature CALL1 = new MemberAccessFeature(methodCall(newMethod("[p:void] [T1, P].m([p:object] o)")));
	private MemberAccessFeature CALL2 = new MemberAccessFeature(methodCall(newMethod("[p:void] [T2, P].m([p:object] o)")));
	private MemberAccessFeature CALL3 = new MemberAccessFeature(methodCall(newMethod("[p:void] [T3, P].m([p:object] o)")));

	@Mock
	private FeatureExtractor featureExtractor;

	private Options opts;
	private List<IUsage> usages;
	private List<List<IFeature>> features;
	private BMNMiner sut;

	@Before
	public void setup() {
		initMocks(this);
		setup(b -> {
		});
		usages = Lists.newLinkedList();
		features = Lists.newLinkedList();
	}

	private void setup(Consumer<OptionsBuilder> c) {
		OptionsBuilder b = OptionsBuilder.bmn().cCtx(false).mCtx(false).def(false).calls(true).members(false);
		c.accept(b);
		opts = b.get();
		sut = new BMNMiner(featureExtractor, new DictionaryBuilder(opts), new VectorBuilder(opts));
	}

	@Test
	public void integrationTest() {
		addUsage(TYPE, CALL1);
		addUsage(TYPE, CALL1, CALL2);
		addUsage(TYPE, CALL1, CALL2, CALL3);
		addUsage(TYPE, CALL1, CALL2, CALL3);

		BMNModel actual = sut.learnModel(usages);

		BMNModel expected = new BMNModel();
		expected.dictionary = dict(TYPE, CALL1, CALL2, CALL3);
		expected.table = new Table(10);
		expected.table.add(r(1, 1, 0, 0));
		expected.table.add(r(1, 1, 1, 0));
		expected.table.add(r(1, 1, 1, 1));
		expected.table.add(r(1, 1, 1, 1));

		assertEquals(expected, actual);
	}

	private void addUsage(IFeature... featureArr) {
		IUsage u1 = mock(IUsage.class);
		usages.add(u1);

		List<IFeature> fs = Lists.newLinkedList();
		features.add(fs);
		for (IFeature f : featureArr) {
			fs.add(f);
		}
		when(featureExtractor.extract(u1)).thenReturn(fs);
		when(featureExtractor.extract(usages)).thenReturn(features);
	}

	private static Dictionary<IFeature> dict(IFeature... fs) {
		Dictionary<IFeature> dict = new Dictionary<IFeature>();
		dict.addAll(asList(UNKNOWN_CCF, UNKNOWN_MCF, UNKNOWN_DF));
		dict.addAll(asList(DUMMY_CCF, DUMMY_MCF, DUMMY_DF));
		dict.addAll(asList(fs));
		return dict;
	}

	private static boolean[] r(int... values) {
		boolean[] res = new boolean[values.length + 6];
		for (int i = 0; i < values.length; i++) {
			res[i + 6] = values[i] == 1;
		}
		return res;
	}
}