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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.names.CoReMethodName;

public class CallSiteTest {

	private static final ICoReMethodName METHOD1 = CoReMethodName.get("LT.m1()V");
	private static final ICoReMethodName METHOD2 = CoReMethodName.get("LT.m2()V");

	@Test
	public void defaultValues() {
		CallSite c = new CallSite();
		assertEquals(0, c.getArgIndex());
		assertNull(c.getKind());
		assertNull(c.getMethod());
	}

	@Test
	public void equalsAndHashCode_nullValues() {
		CallSite a = new CallSite();
		CallSite b = new CallSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_equals() {
		CallSite a = create();
		CallSite b = create();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_kindDifferent() {
		CallSite a = create();
		a.setKind(CallSiteKind.RECEIVER);
		CallSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_argIdxDifferent() {
		CallSite a = create();
		a.setArgIndex(3);
		CallSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_methodDifferent() {
		CallSite a = create();
		a.setMethod(METHOD2);
		CallSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	private CallSite create() {
		CallSite c = new CallSite();
		c.setKind(CallSiteKind.PARAMETER);
		c.setMethod(METHOD1);
		c.setArgIndex(2);
		return c;
	}
}