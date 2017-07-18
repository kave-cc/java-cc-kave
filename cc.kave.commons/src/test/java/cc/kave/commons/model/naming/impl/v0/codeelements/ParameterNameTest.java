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
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.TestUtils;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ParameterNameTest {

	private IParameterName sut;

	@Test
	public void DefaultValues() {
		sut = new ParameterName();
		assertEquals("???", sut.getName());
		assertEquals(new TypeName(), sut.getValueType());
		assertTrue(sut.isUnknown());
		assertFalse(sut.isOutput());
		assertFalse(sut.isPassedByReference());
		assertFalse(sut.isExtensionMethodParameter());
		assertFalse(sut.isOptional());
		assertFalse(sut.isParameterArray());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new ParameterName().isUnknown());
		assertTrue(new ParameterName("[?] ???").isUnknown());
		assertFalse(new ParameterName("[T,P] p").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new ParameterName(null);
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void ShouldParseBasicInformation(String typeId) {
		String id = f("[%s] p", typeId);
		sut = new ParameterName(id);

		assertEquals(typeId, sut.getValueType().getIdentifier());
		assertEquals("p", sut.getName());
		assertFalse(sut.isOptional());
		assertFalse(sut.isOutput());
		assertFalse(sut.isParameterArray());
		assertFalse(sut.isExtensionMethodParameter());
	}

	@Test
	public void isPassedByReferenceDepends() {
		assertFalse(new ParameterName("[?] p").isPassedByReference());
		assertFalse(new ParameterName("[p:int] p").isPassedByReference());
		assertTrue(new ParameterName("ref [p:int] p").isPassedByReference());
		assertTrue(new ParameterName("ref [T,P] p").isPassedByReference());
	}

	@Test
	public void ShouldBeOutputParameter() {
		assertFalse(new ParameterName("[T,P] p").isOutput());
		assertTrue(new ParameterName("out [T,P] p").isOutput());
	}

	@Test
	public void ShouldBeParameterArray() {
		assertFalse(new ParameterName("[T, P] p").isParameterArray());
		assertFalse(new ParameterName("[T, P] p").isParameterArray());
		assertFalse(new ParameterName("[T[], P] p").isParameterArray());
		assertTrue(new ParameterName("params [T[], P] p").isParameterArray());
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectParamsWithoutArrayType() {
		new ParameterName("params [T, P] p");
	}

	@Test
	public void ShouldHaveDefaultValue() {
		assertFalse(new ParameterName("[T,P] p").isOptional());
		assertTrue(new ParameterName("opt [T,P] p").isOptional());
	}

	@Test
	public void ShouldBeExtensionMethodParameter() {
		assertFalse(new ParameterName("[T,P] p").isExtensionMethodParameter());
		assertTrue(new ParameterName("this [T,P] p").isExtensionMethodParameter());
	}
}