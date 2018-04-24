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
package cc.kave.rsse.calls.mining;

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.rsse.calls.model.Constants.DUMMY_CCF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_DF;
import static cc.kave.rsse.calls.model.Constants.DUMMY_MCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_CCF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_DF;
import static cc.kave.rsse.calls.model.Constants.UNKNOWN_MCF;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.utils.OptionsBuilder;

public class DictionaryBuilderTest {

	private Options opts;

	private List<List<IFeature>> in;

	@Before
	public void setup() {
		opts = atLeast(1).get();
		in = Lists.newLinkedList();
	}

	private OptionsBuilder atLeast(int numAtLeast) {
		return new OptionsBuilder("...").atLeast(numAtLeast).cCtx(true).mCtx(true).def(true).calls(true).params(true)
				.members(true);
	}

	private void add(IFeature... fs) {
		in.add(new LinkedList<>(asList(fs)));
	}

	private void assertDict(IFeature... ts) {
		DictionaryBuilder sut = new DictionaryBuilder(opts);
		Dictionary<IFeature> actual = sut.build(in);
		Dictionary<IFeature> expected = new Dictionary<>();
		expected.addAll(asList(UNKNOWN_CCF, UNKNOWN_MCF, UNKNOWN_DF));
		expected.addAll(asList(DUMMY_CCF, DUMMY_MCF, DUMMY_DF));
		expected.addAll(asList(ts));
		assertEquals(expected, actual);
	}

	@Test
	public void alwaysContainsBasicFeatures() {
		// We remove features, so we need to make sure that the dictionary preserves
		// "alternatives" for exclusive feature types or some recommender would
		// fail to build their models.
		assertDict(); // defaults are automatically expected
	}

	@Test
	public void add_1() {
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		add(f1);
		assertDict(f1);
	}

	@Test
	public void add_2() {
		opts = atLeast(2).get();
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:string"));
		add(f1);
		add(f2);
		add(f2);
		assertDict(f2);
	}

	@Test
	public void add_doesNotCountTwicePerUsage() {
		opts = atLeast(2).get();
		IFeature f1 = new ClassContextFeature(newType("p:object"));
		IFeature f2 = new ClassContextFeature(newType("p:string"));
		add(f1, f1);
		add(f2);
		add(f2);
		assertDict(f2);
	}

	@Test
	public void add_neverDropTypeFeatures() {
		opts = atLeast(2).get();
		IFeature f1 = new TypeFeature(newType("p:object"));
		add(f1);
		assertDict(f1);
	}
}