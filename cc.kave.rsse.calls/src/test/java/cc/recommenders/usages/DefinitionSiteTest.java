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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;

public class DefinitionSiteTest {

	private static final ICoReFieldName FIELD1 = CoReFieldName.get("Lsome/Type.name;Lfield1/Type");
	private static final ICoReFieldName FIELD2 = CoReFieldName.get("Lsome/Type.name;Lfield2/Type");

	private static final ICoReMethodName METHOD1 = CoReMethodName.get("Lsome/Type.m1()V");
	private static final ICoReMethodName METHOD2 = CoReMethodName.get("Lsome/Type.m2()V");

	// toString implementation is tested in DefinitionSitesTest

	@Test
	public void defaultValues() {
		DefinitionSite sut = new DefinitionSite();
		assertEquals(-1, sut.getArgIndex());
		assertEquals(null, sut.getField());
		assertEquals(null, sut.getKind());
		assertEquals(null, sut.getMethod());
	}

	@Test
	public void eqAndHashCodeOnNullValues() {
		DefinitionSite a = new DefinitionSite();
		DefinitionSite b = new DefinitionSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsHashCode_equals() {
		DefinitionSite a = newDefinitionSite();
		DefinitionSite b = newDefinitionSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsHashCode_argIdxDifferent() {
		DefinitionSite a = newDefinitionSite();
		a.setArgIndex(0);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsHashCode_fieldDifferent() {
		DefinitionSite a = newDefinitionSite();
		a.setField(FIELD2);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsHashCode_kindDifferent() {
		DefinitionSite a = newDefinitionSite();
		a.setKind(DefinitionSiteKind.NEW);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsHashCode_methodDifferent() {
		DefinitionSite a = newDefinitionSite();
		a.setMethod(METHOD2);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	private DefinitionSite newDefinitionSite() {
		DefinitionSite d = new DefinitionSite();
		d.setArgIndex(13);
		d.setField(FIELD1);
		d.setKind(DefinitionSiteKind.CONSTANT);
		d.setMethod(METHOD1);
		return d;
	}
}