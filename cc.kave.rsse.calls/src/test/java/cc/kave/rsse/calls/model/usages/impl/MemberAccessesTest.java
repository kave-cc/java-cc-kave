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

import static cc.kave.rsse.calls.model.usages.impl.MemberAccesses.methodCall;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.utils.ssts.SSTUtils;
import cc.kave.rsse.calls.model.usages.MemberAccessType;

public class MemberAccessesTest {

	@Test
	public void initForCodeCoverage() {
		new MemberAccesses();
	}

	@Test(expected = AssertionException.class)
	public void fail_call_String() {
		methodCall((String) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_call_MethodName() {
		methodCall((IMethodName) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_memberAccess() {
		MemberAccesses.memberRef(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_memberAccess_event() {
		MemberAccesses.memberRefToEvent(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_memberAccess_field() {
		MemberAccesses.memberRefToField(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_memberAccess_methdo() {
		MemberAccesses.memberRefToMethod(null);
	}

	@Test(expected = AssertionException.class)
	public void fail_memberAccess_property() {
		MemberAccesses.memberRefToProperty(null);
	}

	@Test
	public void testCall() {
		IMethodName m = Names.newMethod("[p:void] [T, P].m()");
		MemberAccess expected = new MemberAccess();
		expected.type = MemberAccessType.METHOD_CALL;
		expected.member = m;

		assertEquals(expected, methodCall(m));
		assertEquals(expected, methodCall(m.getIdentifier()));
	}

	@Test
	public void testMemberAccess_event() {
		IEventName n = Names.newEvent("[%s] [T, P].E", SSTUtils.ACTION1);
		MemberAccess expected = new MemberAccess();
		expected.type = MemberAccessType.MEMBER_REFERENCE;
		expected.member = n;

		assertEquals(expected, MemberAccesses.memberRef(n));
		assertEquals(expected, MemberAccesses.memberRefToEvent(n.getIdentifier()));
	}

	@Test
	public void testMemberAccess_field() {
		IFieldName f = Names.newField("[p:int] [T, P]._f");
		MemberAccess expected = new MemberAccess();
		expected.type = MemberAccessType.MEMBER_REFERENCE;
		expected.member = f;

		assertEquals(expected, MemberAccesses.memberRef(f));
		assertEquals(expected, MemberAccesses.memberRefToField(f.getIdentifier()));
	}

	@Test
	public void testMemberAccess_method() {
		IMethodName n = Names.newMethod("[p:int] [T, P].m()");
		MemberAccess expected = new MemberAccess();
		expected.type = MemberAccessType.MEMBER_REFERENCE;
		expected.member = n;

		assertEquals(expected, MemberAccesses.memberRef(n));
		assertEquals(expected, MemberAccesses.memberRefToMethod(n.getIdentifier()));
	}

	@Test
	public void testMemberAccess_property() {
		IPropertyName n = Names.newProperty("get [p:int] [T, P].P()");
		MemberAccess expected = new MemberAccess();
		expected.type = MemberAccessType.MEMBER_REFERENCE;
		expected.member = n;

		assertEquals(expected, MemberAccesses.memberRef(n));
		assertEquals(expected, MemberAccesses.memberRefToProperty(n.getIdentifier()));
	}
}