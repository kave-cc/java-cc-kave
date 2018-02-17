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
import cc.kave.rsse.calls.usages.CallSite;
import cc.kave.rsse.calls.usages.CallSiteKind;
import cc.kave.rsse.calls.usages.CallSites;

public class CallSitesTest {

	@Test(expected = AssertionException.class)
	public void nullValuesParam1() {
		CallSites.createParameterCallSite((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesParam2() {
		CallSites.createParameterCallSite((IMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex1() {
		CallSites.createParameterCallSite("", -1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex2() {
		CallSites.createParameterCallSite(mock(IMethodName.class), -1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall1() {
		CallSites.createReceiverCallSite((String) null);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall2() {
		CallSites.createReceiverCallSite((IMethodName) null);
	}

	@Test
	public void nullValues_toString() {
		String actual = new CallSite().toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesKind_toString() {
		CallSite cs = CallSites.createReceiverCallSite("[p:int] [T,P].m()");
		cs.setKind(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesMethod_toString() {
		CallSite cs = CallSites.createReceiverCallSite("[p:int] [T,P].m()");
		cs.setMethod(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void parameter() {
		CallSite actual = CallSites.createParameterCallSite("[p:int] [T,P].m()", 234);
		CallSite expected = new CallSite();
		expected.setKind(CallSiteKind.PARAMETER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		expected.setArgIndex(234);
		assertEquals(expected, actual);
	}

	@Test
	public void parameter_toString() {
		String actual = CallSites.createParameterCallSite("[p:int] [T,P].m()", 234).toString();
		String expected = "PARAM(234):T.m";
		assertEquals(expected, actual);
	}

	@Test
	public void receiver() {
		CallSite actual = CallSites.createReceiverCallSite("[p:int] [T,P].m()");
		CallSite expected = new CallSite();
		expected.setKind(CallSiteKind.RECEIVER);
		expected.setMethod(m("[p:int] [T,P].m()"));
		assertEquals(expected, actual);
	}

	@Test
	public void receiver_toString() {
		String actual = CallSites.createReceiverCallSite("[p:int] [T,P].m()").toString();
		String expected = "CALL:T.m";
		assertEquals(expected, actual);
	}

	private static IMethodName m(String method) {
		return Names.newMethod(method);
	}
}