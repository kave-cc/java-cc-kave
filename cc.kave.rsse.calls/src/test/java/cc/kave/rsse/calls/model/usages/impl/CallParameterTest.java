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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.testing.ToStringAsserts;

public class CallParameterTest {

	private static final IMethodName M1 = Names.newMethod("[p:void] [T, P].m1([p:int] p1,[p:int] p2)");
	private static final IMethodName M2 = Names.newMethod("[p:void] [T, P].m2([p:int] p1,[p:int] p2)");

	@Test(expected = IllegalArgumentException.class)
	public void fail_callParam_String() {
		new CallParameter((String) null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_callParam_String2() {
		new CallParameter("", 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_callParam_MethodName() {
		new CallParameter((IMethodName) null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_callParam_indexTooLow() {
		new CallParameter(M1, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_callParam_indexTooHigh() {
		new CallParameter(M1, 2);
	}

	@Test
	public void defaults() {
		assertEquals(M1, new CallParameter(M1, 0).method);
		assertEquals(M1, new CallParameter(M1, 0).getMethod());

		assertEquals(0, new CallParameter(M1, 0).argIndex);
		assertEquals(1, new CallParameter(M1, 1).getArgIndex());
	}

	@Test
	public void defaults_customCtor() {
		assertEquals(M1, new CallParameter(M1.getIdentifier(), 0).method);
		assertEquals(M1, new CallParameter(M1.getIdentifier(), 0).getMethod());

		assertEquals(0, new CallParameter(M1.getIdentifier(), 0).argIndex);
		assertEquals(1, new CallParameter(M1.getIdentifier(), 1).getArgIndex());
	}

	@Test
	public void toStingIsImplemented() {
		ToStringAsserts.assertToStringUtils(new CallParameter(M1, 0));
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new CallParameter(M1, 0), new CallParameter(M1, 0));
	}

	@Test
	public void equality_difMethod() {
		assertNotEqualDataStructures(new CallParameter(M1, 0), new CallParameter(M2, 0));
	}

	@Test
	public void equality_difIdx() {
		assertNotEqualDataStructures(new CallParameter(M1, 0), new CallParameter(M1, 1));
	}
}