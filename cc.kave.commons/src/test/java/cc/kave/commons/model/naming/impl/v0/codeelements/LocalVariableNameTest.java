/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import static cc.kave.commons.utils.StringUtils.f;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.codeelements.ILocalVariableName;
import cc.kave.commons.model.naming.impl.v0.TestUtils;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class LocalVariableNameTest {

	private ILocalVariableName sut;

	@Test
	public void DefaultValues() {
		sut = new LocalVariableName();
		assertEquals("???", sut.getName());
		assertEquals(new TypeName(), sut.getValueType());
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new LocalVariableName().isUnknown());
		assertTrue(new LocalVariableName("[?] ???").isUnknown());
		assertFalse(new LocalVariableName("[T,P] o").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new LocalVariableName(null);
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void ShouldParseType(String typeId) {
		String id = f("[%s] id", typeId);
		sut = new LocalVariableName(id);

		ITypeName actual = sut.getValueType();
		ITypeName expected = TypeUtils.createTypeName(typeId);
		assertEquals(expected, actual);
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void ShouldParseVariableName(String typeId) {
		String id = f("[%s] id", typeId);
		sut = new LocalVariableName(id);

		String actual = sut.getName();
		String expected = "id";
		assertEquals(expected, actual);
	}

	@Test
	public void ShouldNotStripNameIfNoWhitespaceIsUsed() {
		sut = new LocalVariableName("[T]t");
		assertEquals(TypeUtils.createTypeName("T"), sut.getValueType());
		assertEquals("t", sut.getName());
	}
}