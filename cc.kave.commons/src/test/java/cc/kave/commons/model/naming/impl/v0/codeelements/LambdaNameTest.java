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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.Names;
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

	@Test
	public void repeatedCallsToParameterNameAreIdentityEqual() {
		ILambdaName l = Names.newLambda("[T, P] ([A, P] a, [B, P] b)");

		List<IParameterName> p1 = l.getParameters();
		List<IParameterName> p2 = l.getParameters();
		Assert.assertTrue(p1 == p2);

		IParameterName a1 = p1.iterator().next();
		IParameterName a2 = p2.iterator().next();
		Assert.assertTrue(a1 == a2);
	}

	@Test
	public void getExplicitMethodName_Action0() {
		String actual = Names.newLambda("[p:void] ()").getExplicitMethodName().getIdentifier();
		String expected = "[p:void] [d:[p:void] [System.Action, mscorlib, 4.0.0.0].()].Invoke()";
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitMethodName_Action1() {
		String actual = Names.newLambda("[p:void] ([p:int] p)").getExplicitMethodName().getIdentifier();
		String expected = "[p:void] [d:[p:void] [System.Action`1[[T -> p:int]], mscorlib, 4.0.0.0].([T] obj)].Invoke([T] obj)";
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitMethodName_Action2() {
		String actual = Names.newLambda("[p:void] ([p:int] p1, [p:double] p2)").getExplicitMethodName().getIdentifier();
		String expected = "[p:void] [d:[p:void] [System.Action`2[[T1 -> p:int],[T2 -> p:double]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)].Invoke([T1] arg1, [T2] arg2)";
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitMethodName_Func1() {
		String actual = Names.newLambda("[p:int] ()").getExplicitMethodName().getIdentifier();
		String expected = "[TResult] [d:[TResult] [System.Func`1[[TResult -> p:int]], mscorlib, 4.0.0.0].()].Invoke()";
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitMethodName_Func2() {
		String actual = Names.newLambda("[p:int] ([p:double] p)").getExplicitMethodName().getIdentifier();
		String expected = "[TResult] [d:[TResult] [System.Func`2[[T -> p:double],[TResult -> p:int]], mscorlib, 4.0.0.0].([T] arg)].Invoke([T] arg)";
		assertEquals(expected, actual);
	}

	@Test
	public void getExplicitMethodName_Func3() {
		String actual = Names.newLambda("[p:int] ([p:double] p1, [p:char] p2)").getExplicitMethodName().getIdentifier();
		String expected = "[TResult] [d:[TResult] [System.Func`3[[T1 -> p:double],[T2 -> p:char],[TResult -> p:int]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)].Invoke([T1] arg1, [T2] arg2)";
		assertEquals(expected, actual);
	}
}