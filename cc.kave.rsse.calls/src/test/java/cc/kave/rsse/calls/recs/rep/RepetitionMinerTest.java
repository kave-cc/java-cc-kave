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
package cc.kave.rsse.calls.recs.rep;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class RepetitionMinerTest {

	private static final ITypeName TYPE = mock(ITypeName.class);
	private static final IMemberName MEMBER1 = mock(IMemberName.class);
	private static final IMemberName MEMBER2 = mock(IMemberName.class);

	private List<IUsage> in;
	private RepetitionModel expected;

	@Before
	public void setup() {
		in = new LinkedList<>();
		expected = new RepetitionModel(TYPE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_null() {
		new RepetitionMiner().learnModel(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_empty() {
		new RepetitionMiner().learnModel(new LinkedList<>());
	}

	@Test
	public void typeIsSet() {
		in.add(u());
		assertModel();
	}

	@Test
	public void singleOccurrencesAreNotCounted() {
		in.add(u(MEMBER1));
		in.add(u(MEMBER1));
		assertModel();
	}

	@Test
	public void doubleOccurrencesAreCounted() {
		in.add(u(MEMBER1, MEMBER1));
		expected.setRepetitionProbability(MEMBER1, 1);
		assertModel();
	}

	@Test
	public void tripleOccurrencesAreNotCounted() {
		in.add(u(MEMBER1, MEMBER1, MEMBER1));
		expected.setRepetitionProbability(MEMBER1, 1);
		assertModel();
	}

	@Test
	public void probabilityCalculationIsCorrect() {
		in.add(u(MEMBER1, MEMBER1));
		in.add(u(MEMBER1));
		expected.setRepetitionProbability(MEMBER1, 0.5);
		assertModel();
	}

	@Test
	public void probabilityConsidersOnlyCasesInWhichMemberWasSeen() {
		in.add(u(MEMBER1, MEMBER1));
		in.add(u(MEMBER2));
		expected.setRepetitionProbability(MEMBER1, 1);
		assertModel();
	}

	private void assertModel() {
		RepetitionMiner sut = new RepetitionMiner();
		RepetitionModel actual = sut.learnModel(in);
		assertEquals(expected, actual);
	}

	private IUsage u(IMemberName... ms) {
		Usage u = new Usage();
		u.type = TYPE;
		for (IMemberName m : ms) {
			UsageSite us = new UsageSite();
			us.member = m;
			u.usageSites.add(us);
		}
		return u;
	}
}