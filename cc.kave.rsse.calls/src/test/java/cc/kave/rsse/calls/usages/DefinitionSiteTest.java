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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;

public class DefinitionSiteTest {

	private static final IFieldName FIELD1 = Names.newField("[p:int] [T,P]._f");
	private static final IFieldName FIELD2 = Names.newField("[p:int] [T,P]._g");

	private static final IMethodName METHOD1 = Names.newMethod("[p:int] [T,P].M()");
	private static final IMethodName METHOD2 = Names.newMethod("[p:int] [T,P].N()");

	private static final IPropertyName PROPERTY1 = Names.newProperty("get [p:int] [T,P].P()");
	private static final IPropertyName PROPERTY2 = Names.newProperty("get [p:int] [T,P].Q()");

	// toString implementation is tested in DefinitionSitesTest

	@Test
	public void defaultValues() {
		DefinitionSite sut = new DefinitionSite();
		assertEquals(null, sut.getKind());
		assertEquals(null, sut.getField());
		assertEquals(null, sut.getMethod());
		assertEquals(null, sut.getProperty());
		assertEquals(-1, sut.getArgIndex());
	}

	@Test
	public void settingValues() {
		DefinitionSite sut = newDefinitionSite();
		assertEquals(DefinitionSiteKind.CONSTANT, sut.getKind());
		assertEquals(FIELD1, sut.getField());
		assertEquals(METHOD1, sut.getMethod());
		assertEquals(PROPERTY1, sut.getProperty());
		assertEquals(13, sut.getArgIndex());
	}

	@Test
	public void equality() {
		DefinitionSite a = new DefinitionSite();
		DefinitionSite b = new DefinitionSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_realValues() {
		DefinitionSite a = newDefinitionSite();
		DefinitionSite b = newDefinitionSite();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_diffKind() {
		DefinitionSite a = newDefinitionSite();
		a.setKind(DefinitionSiteKind.NEW);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_diffArgsIdx() {
		DefinitionSite a = newDefinitionSite();
		a.setArgIndex(0);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_diffField() {
		DefinitionSite a = newDefinitionSite();
		a.setField(FIELD2);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_diffMethod() {
		DefinitionSite a = newDefinitionSite();
		a.setMethod(METHOD2);
		DefinitionSite b = newDefinitionSite();
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equality_diffProperty() {
		DefinitionSite a = newDefinitionSite();
		a.setProperty(PROPERTY2);
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
		d.setProperty(PROPERTY1);
		return d;
	}
}