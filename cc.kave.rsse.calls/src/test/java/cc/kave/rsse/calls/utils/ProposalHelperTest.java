/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.rsse.calls.utils;

import static cc.kave.commons.utils.ssts.SSTUtils.ACTION1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;

public class ProposalHelperTest {

	private Set<Pair<IMemberName, Double>> actuals;
	private Set<Pair<IMemberName, Double>> expecteds;

	@Before
	public void setup() {
		actuals = ProposalHelper.createSortedSet();
		expecteds = Sets.newLinkedHashSet();
	}

	@Test
	public void isInDescendingOrder() {
		actuals.add(p(1, 0.1));
		actuals.add(p(2, 0.2));
		actuals.add(p(3, 0.3));

		expecteds.add(p(3, 0.3));
		expecteds.add(p(2, 0.2));
		expecteds.add(p(1, 0.1));

		assertSets();
	}

	@Test
	public void equalValuesAreSortedByFirstKey() {
		actuals.add(p(1, 0.1));
		actuals.add(p(3, 0.1));
		actuals.add(p(2, 0.1));

		expecteds.add(p(1, 0.1));
		expecteds.add(p(2, 0.1));
		expecteds.add(p(3, 0.1));

		assertSets();
	}

	@Test
	public void mixedCase() {
		actuals.add(p(1, 0.1));
		actuals.add(p(0, 0.0));
		actuals.add(p(3, 0.1));
		actuals.add(p(4, 0.4));
		actuals.add(p(2, 0.1));

		expecteds.add(p(4, 0.4));
		expecteds.add(p(1, 0.1));
		expecteds.add(p(2, 0.1));
		expecteds.add(p(3, 0.1));
		expecteds.add(p(0, 0.0));

		assertSets();
	}

	@Test
	public void worksAcrossMembers() {
		IEventName x = Names.newEvent("[%s] [T0, P].m3", ACTION1.getIdentifier());
		IEventName e3 = Names.newEvent("[%s] [T0, P].m3", ACTION1.getIdentifier());
		IFieldName f2 = Names.newField("[p:int] [T1, P].m2");
		IMethodName m0 = Names.newMethod("[p:int] [T2, P].m0()");
		IPropertyName p1 = Names.newProperty("get set [p:int] [T4, P].m1()");

		actuals.add(Pair.of(x, 0.5));
		actuals.add(Pair.of(e3, 0.4));
		actuals.add(Pair.of(f2, 0.4));
		actuals.add(Pair.of(m0, 0.4));
		actuals.add(Pair.of(p1, 0.4));

		expecteds.add(Pair.of(x, 0.5));
		expecteds.add(Pair.of(m0, 0.4));
		expecteds.add(Pair.of(p1, 0.4));
		expecteds.add(Pair.of(f2, 0.4));
		expecteds.add(Pair.of(e3, 0.4));

		assertSets();
	}

	@Test
	public void forSimilarMemberNamesTheWholeIdentifierIsUsed() {
		IMethodName m0 = Names.newMethod("[p:int] [T0, P].m()");
		IMethodName m2 = Names.newMethod("[p:int] [T2, P].m()");
		IMethodName m1 = Names.newMethod("[p:int] [T1, P].m()");

		actuals.add(Pair.of(m0, 0.4));
		actuals.add(Pair.of(m2, 0.4));
		actuals.add(Pair.of(m1, 0.4));

		expecteds.add(Pair.of(m0, 0.4));
		expecteds.add(Pair.of(m1, 0.4));
		expecteds.add(Pair.of(m2, 0.4));

		assertSets();
	}

	private void assertSets() {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Pair<IMemberName, Double>> itA = expecteds.iterator();
		Iterator<Pair<IMemberName, Double>> itB = actuals.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
		assertFalse(itB.hasNext());
	}

	private Pair<IMemberName, Double> p(Integer i, double d) {
		IMemberName m = Names.newMethod("[p:void] [T, P].m%d()", i);
		return Pair.of(m, d);
	}
}