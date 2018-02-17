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

import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSiteKind;
import cc.kave.rsse.calls.usages.DefinitionSites;

public class DefinitionSitesTest {

	@Test
	public void nullValues_toString() {
		String actual = new DefinitionSite().toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesKind_toString() {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(null);
		String actual = def.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesField_toString() {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(DefinitionSiteKind.FIELD);
		String actual = def.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesInit_toString() {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(DefinitionSiteKind.NEW);
		String actual = def.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesParam_toString() {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(DefinitionSiteKind.PARAM);
		String actual = def.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void nullValuesReturn_toString() {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(DefinitionSiteKind.RETURN);
		String actual = def.toString();
		String expected = "INVALID";
		assertEquals(expected, actual);
	}

	@Test
	public void constantDefinition() {
		DefinitionSite actual = DefinitionSites.createDefinitionByConstant();
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.CONSTANT);
		assertEquals(expected, actual);
	}

	@Test
	public void constantDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByConstant().toString();
		String expected = "CONSTANT";
		assertEquals(expected, actual);
	}

	@Test
	public void fieldDefinition() {
		String field = "LType.name;LOtherType";
		DefinitionSite actual = DefinitionSites.createDefinitionByField(field);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.FIELD);
		expected.setField(f(field));
		assertEquals(expected, actual);
	}

	@Test
	public void fieldDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByField("[p:int] [T,P]._f").toString();
		String expected = "FIELD:[p:int] [T,P]._f";
		assertEquals(expected, actual);
	}

	@Test
	public void propertyDefinition() {
		String id = "get [p:int] [T,P].P()";
		DefinitionSite actual = DefinitionSites.createDefinitionByProperty(id);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.PROPERTY);
		expected.setProperty(Names.newProperty(id));
		assertEquals(expected, actual);
	}

	@Test
	public void propertyDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByProperty("get [p:int] [T,P].P()").toString();
		String expected = "PROPERTY:get [p:int] [T,P].P()";
		assertEquals(expected, actual);
	}

	@Test
	public void initDefinition() {
		String id = "[p:void] [T,P]..ctor()";
		DefinitionSite actual = DefinitionSites.createDefinitionByConstructor(id);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.NEW);
		expected.setMethod(m(id));
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void initDefinitionMustBeCalledWithInit() {
		String method = "[p:void] [T,P].m()";
		DefinitionSites.createDefinitionByConstructor(method);
	}

	@Test
	public void initDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByConstructor("[p:void] [T,P]..ctor()").toString();
		String expected = "INIT:[p:void] [T,P]..ctor()";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition() {
		String id = "[p:int] [T,P].m()";
		DefinitionSite actual = DefinitionSites.createDefinitionByParam(id, 345);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.PARAM);
		expected.setArgIndex(345);
		expected.setMethod(m(id));
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByParam("[p:int] [T,P].m()", 345).toString();
		String expected = "PARAM(345):[p:int] [T,P].m()";
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition() {
		String id = "[p:int] [T,P].m()";
		DefinitionSite actual = DefinitionSites.createDefinitionByReturn(id);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.RETURN);
		expected.setMethod(m(id));
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByReturn("[p:int] [T,P].m()").toString();
		String expected = "RETURN:[p:int] [T,P].m()";
		assertEquals(expected, actual);
	}

	@Test
	public void thisDefinition() {
		DefinitionSite actual = DefinitionSites.createDefinitionByThis();
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.THIS);
		assertEquals(expected, actual);
	}

	@Test
	public void thisDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByThis().toString();
		String expected = "THIS";
		assertEquals(expected, actual);
	}

	@Test
	public void unknownDefinition() {
		DefinitionSite actual = DefinitionSites.createUnknownDefinitionSite();
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.UNKNOWN);
		assertEquals(expected, actual);
	}

	@Test
	public void unknownDefinition_toString() {
		String actual = DefinitionSites.createUnknownDefinitionSite().toString();
		String expected = "UNKNOWN";
		assertEquals(expected, actual);
	}

	private static IFieldName f(String field) {
		return Names.newField(field);
	}

	private static IMethodName m(String method) {
		return Names.newMethod(method);
	}
}