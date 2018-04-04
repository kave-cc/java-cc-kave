/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.usages;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.usages.UsageAccess;
import cc.kave.rsse.calls.usages.UsageAccessType;
import cc.kave.rsse.calls.usages.UsageAccesses;

public class CallSitesTest {

	@Test(expected = AssertionException.class)
	public void nullValuesParam1() {
		UsageAccesses.createCallParameter((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesParam2() {
		UsageAccesses.createCallParameter((IMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex1() {
		UsageAccesses.createCallParameter("", -1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex2() {
		UsageAccesses.createCallParameter(mock(IMethodName.class), -1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall1() {
		UsageAccesses.createCallReceiver((String) null);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall2() {
		UsageAccesses.createCallReceiver((IMethodName) null);
	}

	@Test
	public void nullValues_toString() {
		String actual = new UsageAccess().toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesKind_toString() {
		UsageAccess cs = UsageAccesses.createCallReceiver("[p:int] [T,P].m()");
		cs.setKind(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesMethod_toString() {
		UsageAccess cs = UsageAccesses.createCallReceiver("[p:int] [T,P].m()");
		cs.setMethod(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void parameter() {
		UsageAccess actual = UsageAccesses.createCallParameter("[p:int] [T,P].m()", 234);
		UsageAccess expected = new UsageAccess();
		expected.setKind(UsageAccessType.CALL_PARAMETER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		expected.setArgIndex(234);
		assertEquals(expected, actual);
	}

	@Test
	public void parameter_toString() {
		String actual = UsageAccesses.createCallParameter("[p:int] [T,P].m()", 234).toString();
		String expected = "PARAM(234):T.m";
		assertEquals(expected, actual);
	}

	@Test
	public void receiver() {
		UsageAccess actual = UsageAccesses.createCallReceiver("[p:int] [T,P].m()");
		UsageAccess expected = new UsageAccess();
		expected.setKind(UsageAccessType.CALL_RECEIVER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		assertEquals(expected, actual);
	}

	@Test
	public void receiver_toString() {
		String actual = UsageAccesses.createCallReceiver("[p:int] [T,P].m()").toString();
		String expected = "CALL:T.m";
		assertEquals(expected, actual);
	}

	private static IMethodName m(String method) {
		return Names.newMethod(method);
	}
}