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

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static cc.kave.rsse.calls.recs.rep.RepetitionModel.PRECISION;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.function.Consumer;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;

public class RepetitionModelTest {

	private static final ITypeName TYPE = mock(ITypeName.class);
	private static final IMemberName MEMBER = mock(IMemberName.class);

	@Test
	public void defaults() {
		RepetitionModel sut = new RepetitionModel();
		assertNull(sut.type);
		assertEquals(new HashSet<>(), sut.getRepetitionKeys());
		assertFalse(sut.hasRepetitionProbability(MEMBER));

		assertEquals(0.000001, RepetitionModel.PRECISION, 0.000000001);
		assertEquals(6, RepetitionModel.PRECISION_SCALE);
	}

	@Test
	public void values() {
		RepetitionModel sut = new RepetitionModel(TYPE);
		sut.setRepetitionProbability(MEMBER, 0.123);

		assertSame(TYPE, sut.type);
		assertEquals(new HashSet<IMemberName>(asList(MEMBER)), sut.getRepetitionKeys());
		assertTrue(sut.hasRepetitionProbability(MEMBER));
		assertEquals(0.123, sut.getRepetitionProbability(MEMBER), PRECISION * 0.1);
	}

	@Test
	public void unknownProbability() {
		RepetitionModel sut = new RepetitionModel(TYPE);
		assertEquals(0, sut.getRepetitionProbability(MEMBER), 0.0000001);
	}

	@Test
	public void zeroProbabilitiesAreNotAdded() {
		RepetitionModel sut = new RepetitionModel(TYPE);
		sut.setRepetitionProbability(MEMBER, 0);
		assertFalse(sut.hasRepetitionProbability(MEMBER));
	}

	@Test
	public void precisionIsRounded_up() {
		RepetitionModel sut = new RepetitionModel();
		sut.setRepetitionProbability(MEMBER, 0.1234567);
		assertEquals(0.123457, sut.getRepetitionProbability(MEMBER), PRECISION * 0.1);
	}

	@Test
	public void precisionIsRounded_down() {
		RepetitionModel sut = new RepetitionModel();
		sut.setRepetitionProbability(MEMBER, 0.7654321);
		assertEquals(0.765432, sut.getRepetitionProbability(MEMBER), PRECISION * 0.1);
	}

	@Test
	public void equality_default() {
		RepetitionModel a = new RepetitionModel();
		RepetitionModel b = new RepetitionModel();
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_values() {
		RepetitionModel a = new RepetitionModel();
		a.type = Names.newType("T, P");
		a.setRepetitionProbability(MEMBER, 0.123);
		RepetitionModel b = new RepetitionModel();
		b.type = Names.newType("T, P");
		b.setRepetitionProbability(MEMBER, 0.123);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffType1() {
		RepetitionModel a = new RepetitionModel();
		a.type = Names.newType("T, P");
		RepetitionModel b = new RepetitionModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffType2() {
		RepetitionModel a = new RepetitionModel();
		RepetitionModel b = new RepetitionModel();
		b.type = Names.newType("T, P");
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffProbs() {
		RepetitionModel a = new RepetitionModel();
		a.setRepetitionProbability(MEMBER, 0.123);
		RepetitionModel b = new RepetitionModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new RepetitionModel());
	}

	@Test
	public void fail() {
		assertFail(sut -> new RepetitionModel(null));
		assertFail(sut -> sut.setRepetitionProbability(null, 0.123));
		assertFail(sut -> sut.setRepetitionProbability(MEMBER, -0.00001));
		assertFail(sut -> sut.setRepetitionProbability(MEMBER, 1.00001));
		assertFail(sut -> sut.hasRepetitionProbability(null));
		assertFail(sut -> sut.getRepetitionProbability(null));
	}

	public void assertFail(Consumer<RepetitionModel> c) {
		boolean failed = false;
		try {
			c.accept(new RepetitionModel());
		} catch (IllegalArgumentException e) {
			failed = true;
		}
		assertTrue(failed);
	}
}