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

import static cc.kave.commons.utils.ssts.SSTUtils.ACTION;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CONSTANT;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.impl.Definition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class DefinitionsTest {

	@Test
	public void initForCodeCoverage() {
		new Definitions();
	}

	@Test
	public void defineUnknown() {
		Assert.assertEquals(new Definition(), definedByUnknown());
	}

	@Test
	public void defineConstant() {
		Assert.assertEquals(new Definition(CONSTANT), Definitions.definedByConstant());
	}

	@Test
	public void defineThis() {
		Assert.assertEquals(new Definition(DefinitionType.THIS), Definitions.definedByThis());
	}

	@Test
	public void defineMethodParameter() {
		Definition expected = new Definition(DefinitionType.METHOD_PARAMETER);
		expected.member = m(1);
		expected.argIndex = 1;
		Assert.assertEquals(expected, Definitions.definedByMethodParameter(m(1), 1));
		Assert.assertEquals(expected, Definitions.definedByMethodParameter(m(1).getIdentifier(), 1));
	}

	@Test
	public void defineLoopHeader() {
		Definition expected = new Definition(DefinitionType.LOOP_HEADER);
		expected.member = mLoop(t());
		expected.argIndex = 1;
		Assert.assertEquals(expected, Definitions.definedByLoopHeader(t()));
		Assert.assertEquals(expected, Definitions.definedByLoopHeader(t().getIdentifier()));
	}

	@Test
	public void defineCatchParameter() {
		Definition expected = new Definition(DefinitionType.CATCH_PARAMETER);
		expected.member = mCatch(t());
		expected.argIndex = 1;
		Assert.assertEquals(expected, Definitions.definedByCatchParameter(t()));
		Assert.assertEquals(expected, Definitions.definedByCatchParameter(t().getIdentifier()));
	}

	@Test
	public void defineConstructor() {
		IMethodName ctor = Names.newMethod("[p:void] [T, P]..ctor()");
		Definition expected = new Definition(DefinitionType.NEW);
		expected.member = ctor;
		Assert.assertEquals(expected, Definitions.definedByConstructor(ctor));
		Assert.assertEquals(expected, Definitions.definedByConstructor(ctor.getIdentifier()));
	}

	@Test
	public void defineReturnValue() {
		IMethodName m = Names.newMethod("[p:int] [T, P].m()");
		Definition expected = new Definition(DefinitionType.RETURN_VALUE);
		expected.member = m;
		Assert.assertEquals(expected, Definitions.definedByReturnValue(m));
		Assert.assertEquals(expected, Definitions.definedByReturnValue(m.getIdentifier()));
	}

	@Test
	public void defineMemberAccess() {
		Definition expected = new Definition(DefinitionType.MEMBER_ACCESS);
		expected.member = m(0);
		Assert.assertEquals(expected, Definitions.definedByMemberAccess(m(0)));
	}

	@Test
	public void defineMemberAccessToEvent() {
		Definition expected = new Definition(DefinitionType.MEMBER_ACCESS);
		expected.member = Names.newEvent("[%s] [T, P].E", ACTION.getIdentifier());
		Assert.assertEquals(expected, Definitions.definedByMemberAccessToEvent(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToField() {
		Definition expected = new Definition(DefinitionType.MEMBER_ACCESS);
		expected.member = Names.newField("[p:int] [T, P]._f");
		Assert.assertEquals(expected, Definitions.definedByMemberAccessToField(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToMethod() {
		Definition expected = new Definition(DefinitionType.MEMBER_ACCESS);
		expected.member = m(0);
		Assert.assertEquals(expected, Definitions.definedByMemberAccessToMethod(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToProperty() {
		Definition expected = new Definition(DefinitionType.MEMBER_ACCESS);
		expected.member = Names.newProperty("set get [p:int] [T, P].P()");
		Assert.assertEquals(expected, Definitions.definedByMemberAccessToProperty(expected.member.getIdentifier()));
	}

	@Test(expected = AssertionException.class)
	public void null_defineMethodParameter_String() {
		Definitions.definedByMethodParameter((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMethodParameter_MethodName() {
		Definitions.definedByMethodParameter((IMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void null_defineLoopHeader_String() {
		Definitions.definedByLoopHeader((String) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineLoopHeader_TypeName() {
		Definitions.definedByLoopHeader((ITypeName) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineCatchParam_String() {
		Definitions.definedByCatchParameter((String) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineCatchParam_TypeName() {
		Definitions.definedByCatchParameter((ITypeName) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineCtor_String() {
		Definitions.definedByConstructor((String) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineCtor_MethodName() {
		Definitions.definedByConstructor((IMethodName) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineReturn_String() {
		Definitions.definedByReturnValue((String) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineReturn_MethodName() {
		Definitions.definedByReturnValue((IMethodName) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMemberAccess() {
		Definitions.definedByMemberAccess(null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMemberAccessToEvent() {
		Definitions.definedByMemberAccessToEvent(null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMemberAccessToField() {
		Definitions.definedByMemberAccessToField(null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMemberAccessToMethod() {
		Definitions.definedByMemberAccessToMethod(null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineMemberAccessToProperty() {
		Definitions.definedByMemberAccessToProperty(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_defineMethodParam_argTooLow() {
		Definitions.definedByMethodParameter(m(1), 0);
	}

	@Test(expected = AssertionException.class)
	public void fail_defineMethodParam_argTooHigh() {
		Definitions.definedByMethodParameter(m(1), 2);
	}

	@Test(expected = AssertionException.class)
	public void fail_defineConstructor_noCtor() {
		Definitions.definedByConstructor(m(1));
	}

	@Test(expected = AssertionException.class)
	public void fail_defineReturn_ctor() {
		// technically, an invalid ctor, but used to distinguish from the next test case
		Definitions.definedByReturnValue(Names.newMethod("[p:int] [T, P]..ctor()"));
	}

	@Test(expected = AssertionException.class)
	public void fail_defineReturn_void() {
		Definitions.definedByReturnValue(Names.newMethod("[p:void] [T, P].m()"));
	}

	private static IMethodName m(int numParams) {
		StringBuilder sb = new StringBuilder("[p:void] [T, P].m(");
		for (int i = 0; i < numParams; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("[p:int] p").append(i);
		}
		sb.append(")");
		return Names.newMethod(sb.toString());
	}

	private ITypeName t() {
		return Names.newType("T, P");
	}

	private IMemberName mLoop(ITypeName t) {
		return Names.newMethod("[p:void] [System.Object, mscorlib, 4.0.0.0].loopHeader([%s] e)", t.getIdentifier());
	}

	private IMemberName mCatch(ITypeName t) {
		return Names.newMethod("[p:void] [System.Exception, mscorlib, 4.0.0.0].catch([%s] e)", t.getIdentifier());
	}
}