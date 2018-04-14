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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;

public class DictionaryBuilderTest {

	private Options opts;

	private List<List<IFeature>> in;

	@Before
	public void setup() {
		opts = new Options("");
		in = Lists.newLinkedList();
	}

	private void add(IFeature... fs) {
		in.add(new LinkedList<>(asList(fs)));
	}

	private void assertDict(IFeature... ts) {
		DictionaryBuilder sut = new DictionaryBuilder(opts);
		Dictionary<IFeature> actual = sut.newDictionary(in);
		Dictionary<IFeature> expected = new Dictionary<>();
		expected.addAll(Arrays.asList(ts));
		assertEquals(expected, actual);
	}

	@Test
	public void testMe() {
		add(tf(1));
		assertDict(tf(1));
	}

	@Test
	public void dropRare() {
		fail();
	}

	@Test
	public void dropLocal() {
		fail();
	}

	@Test
	public void someFeatureTypesMustAlwaysExist() {
		// e.g., kicking out defsites, means, we need to add unknown
		fail();
	}

	@Test
	public void addsDummyValuesToPreventInvalidNetworks() {
		fail();
	}

	@Test
	public void unknownsAreNotAdded() {
		fail();
	}

	private static TypeFeature tf(int i) {
		return null;
	}

	private static ClassContextFeature ccf(int i) {
		return null;
	}

	private static MethodContextFeature mcf(int i) {
		return null;
	}

	private static DefinitionFeature df(int i) {
		return null;
	}

	private static UsageSiteFeature usf(int i) {
		return null;
	}

	private static ITypeName t(int i) {
		return null;
	}

	private static IMethodName m(int i) {
		return null;
	}
}