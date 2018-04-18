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
import static cc.kave.rsse.calls.model.usages.DefinitionType.CAST;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CATCH_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CONSTANT;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LAMBDA_DECL;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LAMBDA_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LOOP_HEADER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.MEMBER_ACCESS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.METHOD_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.NEW;
import static cc.kave.rsse.calls.model.usages.DefinitionType.OUT_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.RETURN_VALUE;
import static cc.kave.rsse.calls.model.usages.DefinitionType.THIS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.UNKNOWN;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstructor;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLambdaDecl;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLambdaParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLoopHeader;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToEvent;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToField;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToMethod;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToProperty;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByOutParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class DefinitionsTest {

	@Test
	public void initForCodeCoverage() {
		new Definitions();
	}

	@Test
	public void defineUnknown() {
		Assert.assertEquals(new Definition(UNKNOWN), definedByUnknown());
	}

	@Test
	public void defineConstant() {
		Assert.assertEquals(new Definition(CONSTANT), definedByConstant());
	}

	@Test
	public void defineCast() {
		Assert.assertEquals(new Definition(CAST), Definitions.definedByCast());
	}

	@Test
	public void defineThis() {
		Assert.assertEquals(new Definition(THIS), definedByThis());
	}

	@Test
	public void defineLambdaParam() {
		Assert.assertEquals(new Definition(LAMBDA_PARAMETER), definedByLambdaParameter());
	}

	@Test
	public void defineLambdaDecl() {
		Assert.assertEquals(new Definition(LAMBDA_DECL), definedByLambdaDecl());
	}

	@Test
	public void defineMethodParameter() {
		Definition expected = new Definition(METHOD_PARAMETER);
		expected.member = m(1);
		expected.argIndex = 0;
		Assert.assertEquals(expected, definedByMethodParameter(m(1), 0));
		Assert.assertEquals(expected, definedByMethodParameter(m(1).getIdentifier(), 0));
	}

	@Test
	public void defineLoopHeader() {
		Assert.assertEquals(new Definition(LOOP_HEADER), definedByLoopHeader());
	}

	@Test
	public void defineCatchParameter() {
		Assert.assertEquals(new Definition(CATCH_PARAMETER), definedByCatchParameter());
	}

	@Test
	public void defineConstructor() {
		IMethodName ctor = Names.newMethod("[p:void] [T, P]..ctor()");
		Definition expected = new Definition(NEW);
		expected.member = ctor;
		Assert.assertEquals(expected, definedByConstructor(ctor));
		Assert.assertEquals(expected, definedByConstructor(ctor.getIdentifier()));
	}

	@Test
	public void defineReturnValue() {
		IMethodName m = Names.newMethod("[p:int] [T, P].m()");
		Definition expected = new Definition(RETURN_VALUE);
		expected.member = m;
		Assert.assertEquals(expected, definedByReturnValue(m));
		Assert.assertEquals(expected, definedByReturnValue(m.getIdentifier()));
	}

	@Test
	public void defineOutParameter() {
		IMethodName m = Names.newMethod("[p:void] [T,P].m(out [p:int] p)");
		Definition expected = new Definition(OUT_PARAMETER);
		expected.member = m;
		Assert.assertEquals(expected, definedByOutParameter(m));
		Assert.assertEquals(expected, definedByOutParameter(m.getIdentifier()));
	}

	@Test
	public void defineMemberAccess() {
		Definition expected = new Definition(MEMBER_ACCESS);
		expected.member = m(0);
		Assert.assertEquals(expected, definedByMemberAccess(m(0)));
	}

	@Test
	public void defineMemberAccessToEvent() {
		Definition expected = new Definition(MEMBER_ACCESS);
		expected.member = Names.newEvent("[%s] [T, P].E", ACTION.getIdentifier());
		Assert.assertEquals(expected, definedByMemberAccessToEvent(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToField() {
		Definition expected = new Definition(MEMBER_ACCESS);
		expected.member = Names.newField("[p:int] [T, P]._f");
		Assert.assertEquals(expected, definedByMemberAccessToField(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToMethod() {
		Definition expected = new Definition(MEMBER_ACCESS);
		expected.member = m(0);
		Assert.assertEquals(expected, definedByMemberAccessToMethod(expected.member.getIdentifier()));
	}

	@Test
	public void defineMemberAccessToProperty() {
		Definition expected = new Definition(MEMBER_ACCESS);
		expected.member = Names.newProperty("set get [p:int] [T, P].P()");
		Assert.assertEquals(expected, definedByMemberAccessToProperty(expected.member.getIdentifier()));
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
	public void null_defineOut_String() {
		Definitions.definedByOutParameter((String) null);
	}

	@Test(expected = AssertionException.class)
	public void null_defineOut_MethodName() {
		Definitions.definedByOutParameter((IMethodName) null);
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
		Definitions.definedByMethodParameter(m(1), -1);
	}

	@Test(expected = AssertionException.class)
	public void fail_defineMethodParam_argTooHigh() {
		Definitions.definedByMethodParameter(m(1), 1);
	}

	@Test(expected = AssertionException.class)
	public void fail_defineConstructor_noCtor() {
		Definitions.definedByConstructor(m(1));
	}

	@Test(expected = AssertionException.class)
	public void fail_defineOutParam_noOutParam() {
		Definitions.definedByOutParameter(m(1));
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
}