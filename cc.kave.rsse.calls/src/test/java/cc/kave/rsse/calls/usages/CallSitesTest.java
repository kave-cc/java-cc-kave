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
import cc.kave.rsse.calls.usages.UsageSite;
import cc.kave.rsse.calls.usages.UsageAccessType;
import cc.kave.rsse.calls.usages.UsageSites;

public class CallSitesTest {

	@Test(expected = AssertionException.class)
	public void nullValuesParam1() {
		UsageSites.methodParameter((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesParam2() {
		UsageSites.methodParameter((IMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex1() {
		UsageSites.methodParameter("", -1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex2() {
		UsageSites.methodParameter(mock(IMethodName.class), -1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall1() {
		UsageSites.methodCall((String) null);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall2() {
		UsageSites.methodCall((IMethodName) null);
	}

	@Test
	public void nullValues_toString() {
		String actual = new UsageSite().toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesKind_toString() {
		UsageSite cs = UsageSites.methodCall("[p:int] [T,P].m()");
		cs.setKind(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesMethod_toString() {
		UsageSite cs = UsageSites.methodCall("[p:int] [T,P].m()");
		cs.setMethod(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void parameter() {
		UsageSite actual = UsageSites.methodParameter("[p:int] [T,P].m()", 234);
		UsageSite expected = new UsageSite();
		expected.setKind(UsageAccessType.CALL_PARAMETER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		expected.setArgIndex(234);
		assertEquals(expected, actual);
	}

	@Test
	public void parameter_toString() {
		String actual = UsageSites.methodParameter("[p:int] [T,P].m()", 234).toString();
		String expected = "PARAM(234):T.m";
		assertEquals(expected, actual);
	}

	@Test
	public void receiver() {
		UsageSite actual = UsageSites.methodCall("[p:int] [T,P].m()");
		UsageSite expected = new UsageSite();
		expected.setKind(UsageAccessType.CALL_RECEIVER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		assertEquals(expected, actual);
	}

	@Test
	public void receiver_toString() {
		String actual = UsageSites.methodCall("[p:int] [T,P].m()").toString();
		String expected = "CALL:T.m";
		assertEquals(expected, actual);
	}

	private static IMethodName m(String method) {
		return Names.newMethod(method);
	}
}