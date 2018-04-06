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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.usages.UsageSite;
import cc.kave.rsse.calls.usages.UsageAccessType;

public class CallSiteTest {

	private static final IMethodName METHOD1 = Names.newMethod("[p:int] [T,P].m1()");
	private static final IMethodName METHOD2 = Names.newMethod("[p:int] [T,P].m2()");

	@Test
	public void defaultValues() {
		UsageSite c = new UsageSite();
		assertEquals(0, c.getArgIndex());
		assertNull(c.getKind());
		assertNull(c.getMethod());
	}

	@Test
	public void equalsAndHashCode_nullValues() {
		UsageSite a = new UsageSite();
		UsageSite b = new UsageSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_equals() {
		UsageSite a = create();
		UsageSite b = create();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_kindDifferent() {
		UsageSite a = create();
		a.setKind(UsageAccessType.CALL_RECEIVER);
		UsageSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_argIdxDifferent() {
		UsageSite a = create();
		a.setArgIndex(3);
		UsageSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_methodDifferent() {
		UsageSite a = create();
		a.setMethod(METHOD2);
		UsageSite b = create();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	private UsageSite create() {
		UsageSite c = new UsageSite();
		c.setKind(UsageAccessType.CALL_PARAMETER);
		c.setMethod(METHOD1);
		c.setArgIndex(2);
		return c;
	}
}