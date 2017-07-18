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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.TestUtils;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class LambdaNameTest {

	private ILambdaName sut;

	@Test
	public void DefaultValues() {
		sut = new LambdaName();
		assertEquals(new TypeName(), sut.getReturnType());
		assertTrue(sut.isUnknown());
		assertFalse(sut.hasParameters());
		assertEquals(Lists.newLinkedList(), sut.getParameters());
	}

	@Test
	public void ShouldRecognizeUnknownName() {
		assertTrue(new LambdaName().isUnknown());
		assertTrue(new LambdaName("???").isUnknown());
		assertFalse(new LambdaName("[?] ()").isUnknown());
	}

	@Test(expected = ValidationException.class)
	public void ShouldAvoidNullParameters() {
		new LambdaName(null);
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void WithoutParameters(String typeId) {
		sut = new LambdaName(f("[%s] ()", typeId));

		assertFalse(sut.hasParameters());
		assertTrue(sut.getParameters().isEmpty());
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void WithParameters(String typeId) {
		sut = new LambdaName(f("[%s] ([%s] p1, [%s] p2)", typeId, typeId, typeId));

		assertTrue(sut.hasParameters());
		assertEquals(
				Lists.newArrayList(new ParameterName(f("[%s] p1", typeId)), new ParameterName(f("[%s] p2", typeId))),
				sut.getParameters());
	}

	@Test
	@Parameters(source = TestUtils.class, method = "provideTypes")
	public void ShouldParseReturnType(String typeId) {
		sut = new LambdaName(f("[%s] ([%s] p1, [%s] p2)", typeId, typeId, typeId));

		assertEquals(TypeUtils.createTypeName(typeId), sut.getReturnType());
	}

	@Test
	public void ParameterParsingIsCached() {
		sut = new LambdaName();
		List<IParameterName> a = sut.getParameters();
		List<IParameterName> b = sut.getParameters();
		assertSame(a, b);
	}
}