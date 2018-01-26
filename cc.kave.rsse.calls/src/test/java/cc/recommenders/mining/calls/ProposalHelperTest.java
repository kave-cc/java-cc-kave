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
package cc.recommenders.mining.calls;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Sets;

public class ProposalHelperTest {

	private Set<Tuple<Integer, Double>> actuals;
	private Set<Tuple<Integer, Double>> expecteds;

	@Before
	public void setup() {
		actuals = ProposalHelper.createSortedSet();
		expecteds = Sets.newLinkedHashSet();
	}

	// TODO fix tests!
	@Test
	@Ignore
	public void firstElemIsNUllAndSecondIsEqual() {
		actuals.add(_(1, 1.0));
		actuals.add(_(null, 1.0));
	}
	@Test
	@Ignore
	public void firstElemIsNUllAndSecondIsEqual_2() {
		actuals.add(_(null, 1.0));
		actuals.add(_(1, 1.0));
	}
	
	@Test
	public void isInDescendingOrder() {
		actuals.add(_(1, 0.1));
		actuals.add(_(2, 0.2));
		actuals.add(_(3, 0.3));

		expecteds.add(_(3, 0.3));
		expecteds.add(_(2, 0.2));
		expecteds.add(_(1, 0.1));

		assertSets();
	}

	@Test
	public void equalValuesAreSortedByFirstKey() {
		actuals.add(_(1, 0.1));
		actuals.add(_(3, 0.1));
		actuals.add(_(2, 0.1));

		expecteds.add(_(1, 0.1));
		expecteds.add(_(2, 0.1));
		expecteds.add(_(3, 0.1));

		assertSets();
	}

	@Test
	public void mixedCase() {
		actuals.add(_(1, 0.1));
		actuals.add(_(0, 0.0));
		actuals.add(_(3, 0.1));
		actuals.add(_(4, 0.4));
		actuals.add(_(2, 0.1));

		expecteds.add(_(4, 0.4));
		expecteds.add(_(1, 0.1));
		expecteds.add(_(2, 0.1));
		expecteds.add(_(3, 0.1));
		expecteds.add(_(0, 0.0));

		assertSets();
	}

	private void assertSets() {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Tuple<Integer, Double>> itA = expecteds.iterator();
		Iterator<Tuple<Integer, Double>> itB = actuals.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
	}

	private Tuple<Integer, Double> _(Integer i, double d) {
		return Tuple.newTuple(i, d);
	}
}