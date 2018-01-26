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
package cc.recommenders.usages;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.usages.CallSites;
import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.names.CoReMethodName;

public class CallSitesTest {

	@Test(expected = AssertionException.class)
	public void nullValuesParam1() {
		CallSites.createParameterCallSite((String) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesParam2() {
		CallSites.createParameterCallSite((ICoReMethodName) null, 1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex1() {
		CallSites.createParameterCallSite("", -1);
	}

	@Test(expected = AssertionException.class)
	public void incorrectArgIndex2() {
		CallSites.createParameterCallSite(mock(ICoReMethodName.class), -1);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall1() {
		CallSites.createReceiverCallSite((String) null);
	}

	@Test(expected = AssertionException.class)
	public void nullValuesCall2() {
		CallSites.createReceiverCallSite((ICoReMethodName) null);
	}

	@Test
	public void nullValues_toString() {
		String actual = new CallSite().toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesKind_toString() {
		CallSite cs = CallSites.createReceiverCallSite("LT.m()V");
		cs.setKind(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesMethod_toString() {
		CallSite cs = CallSites.createReceiverCallSite("LT.m()V");
		cs.setMethod(null);
		String actual = cs.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void parameter() {
		CallSite actual = CallSites.createParameterCallSite("Lsome/Type.m()V", 234);
		CallSite expected = new CallSite();
		expected.setKind(CallSiteKind.PARAMETER);
		expected.setMethod(m("Lsome/Type.m()V"));
		expected.setArgIndex(234);
		assertEquals(expected, actual);
	}

	@Test
	public void parameter_toString() {
		String actual = CallSites.createParameterCallSite("Lsome/Type.m()V", 234).toString();
		String expected = "PARAM(234):Type.m";
		assertEquals(expected, actual);
	}

	@Test
	public void receiver() {
		CallSite actual = CallSites.createReceiverCallSite("Lsome/Type.m()V");
		CallSite expected = new CallSite();
		expected.setKind(CallSiteKind.RECEIVER);
		expected.setMethod(m("Lsome/Type.m()V"));
		assertEquals(expected, actual);
	}

	@Test
	public void receiver_toString() {
		String actual = CallSites.createReceiverCallSite("Lsome/Type.m()V").toString();
		String expected = "CALL:Type.m";
		assertEquals(expected, actual);
	}

	private static ICoReMethodName m(String method) {
		return CoReMethodName.get(method);
	}
}