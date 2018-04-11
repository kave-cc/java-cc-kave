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
package cc.kave.rsse.calls.model.usages.impl;

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CONSTANT;
import static cc.kave.rsse.calls.model.usages.DefinitionType.UNKNOWN;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definition;

public class DefinitionTest {

	private static final IFieldName MEMBER = Names.newField("[p:int] [T,P]._f");

	@Test
	public void defaultValues() {
		IDefinition sut = new Definition();
		assertEquals(UNKNOWN, sut.getKind());
		assertEquals(null, sut.getMember(IMemberName.class));
		assertEquals(-1, sut.getArgIndex());
	}

	@Test
	public void customConstructor() {
		IDefinition sut = new Definition(CONSTANT);
		assertEquals(CONSTANT, sut.getKind());
		assertEquals(null, sut.getMember(IMemberName.class));
		assertEquals(-1, sut.getArgIndex());
	}

	@Test
	public void settingValues() {
		Definition sut = new Definition();
		sut.kind = CONSTANT;
		sut.member = MEMBER;
		sut.argIndex = 13;

		assertEquals(CONSTANT, sut.getKind());
		assertEquals(MEMBER, sut.getMember(IFieldName.class));
		assertEquals(13, sut.getArgIndex());
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new Definition(), new Definition());
	}

	@Test
	public void equality_realValues() {
		Definition a = new Definition();
		a.kind = CONSTANT;
		a.member = MEMBER;
		a.argIndex = 13;

		Definition b = new Definition();
		b.kind = CONSTANT;
		b.member = MEMBER;
		b.argIndex = 13;

		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffKind() {
		Definition a = new Definition();
		a.kind = CONSTANT;

		assertNotEqualDataStructures(a, new Definition());
	}

	@Test
	public void equality_diffMember() {
		Definition a = new Definition();
		a.member = MEMBER;

		assertNotEqualDataStructures(a, new Definition());
	}

	@Test
	public void equality_diffArgsIdx() {
		Definition a = new Definition();
		a.argIndex = 13;

		assertNotEqualDataStructures(a, new Definition());
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new Definition());
	}
}