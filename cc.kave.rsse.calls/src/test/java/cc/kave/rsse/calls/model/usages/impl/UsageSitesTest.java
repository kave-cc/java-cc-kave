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

import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.propertyAccess;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.rsse.calls.model.usages.UsageSiteType;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

public class UsageSitesTest {

	@Test
	public void initForCodeCoverage() {
		new UsageSites();
	}

	@Test(expected = AssertionException.class)
	public void fail_call_String() {
		call((String) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_call_MethodName() {
		call((IMethodName) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_callParam_String() {
		callParameter((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void fail_callParam_MethodName() {
		callParameter((IMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void fail_callParam_indexTooLow() {
		callParameter("[p:void] [T, P].m([p:int] i)", 0);
	}

	@Test(expected = AssertionException.class)
	public void fail_callParam_indexTooHigh() {
		callParameter("[p:void] [T, P].m([p:int] i)", 2);
	}

	@Test(expected = AssertionException.class)
	public void fail_fieldAccess_String() {
		fieldAccess((String) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_fieldAccess_FieldName() {
		fieldAccess((IFieldName) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_propertyAccess_String() {
		propertyAccess((String) null);
	}

	@Test(expected = AssertionException.class)
	public void fail_propertyAccess_PropertyName() {
		propertyAccess((IPropertyName) null);
	}

	@Test
	public void testCall() {
		IMethodName m = Names.newMethod("[p:void] [T, P].m()");
		UsageSite expected = new UsageSite();
		expected.type = UsageSiteType.CALL_RECEIVER;
		expected.member = m;

		assertEquals(expected, call(m));
		assertEquals(expected, call(m.getIdentifier()));
	}

	@Test
	public void testCallParameter() {
		IMethodName m = Names.newMethod("[p:void] [T, P].m([p:int] i)");
		UsageSite expected = new UsageSite();
		expected.type = UsageSiteType.CALL_PARAMETER;
		expected.member = m;
		expected.argIndex = 1;

		assertEquals(expected, callParameter(m, 1));
		assertEquals(expected, callParameter(m.getIdentifier(), 1));
	}

	@Test
	public void testFieldAccess() {
		IFieldName f = Names.newField("[p:int] [T, P]._f");
		UsageSite expected = new UsageSite();
		expected.type = UsageSiteType.FIELD_ACCESS;
		expected.member = f;

		assertEquals(expected, fieldAccess(f));
		assertEquals(expected, fieldAccess(f.getIdentifier()));
	}

	@Test
	public void testPropertyAccess() {
		IPropertyName p = Names.newProperty("set get [p:int] [T, P].P()");
		UsageSite expected = new UsageSite();
		expected.type = UsageSiteType.PROPERTY_ACCESS;
		expected.member = p;

		assertEquals(expected, propertyAccess(p));
		assertEquals(expected, propertyAccess(p.getIdentifier()));
	}
}