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

import org.junit.Test;

import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.DefinitionSites;
import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;

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
		String actual = DefinitionSites.createDefinitionByField("LType.name;LOtherType").toString();
		String expected = "FIELD:LType.name;LOtherType";
		assertEquals(expected, actual);
	}

	@Test
	public void initDefinition() {
		String method = "Lorg/bla/Blubb.<init>()V";
		DefinitionSite actual = DefinitionSites.createDefinitionByConstructor(method);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.NEW);
		expected.setMethod(m(method));
		assertEquals(expected, actual);
	}

	@Test(expected = AssertionException.class)
	public void initDefinitionMustBeCalledWithInit() {
		String method = "Lorg/bla/Blubb.noInit()V";
		DefinitionSites.createDefinitionByConstructor(method);
	}

	@Test
	public void initDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByConstructor("Lorg/bla/Blubb.<init>()V").toString();
		String expected = "INIT:Lorg/bla/Blubb.<init>()V";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition() {
		String method = "LType.method(LOtherType;)V";
		DefinitionSite actual = DefinitionSites.createDefinitionByParam(method, 345);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.PARAM);
		expected.setArgIndex(345);
		expected.setMethod(m(method));
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByParam("LType.method(LOtherType;)V", 345).toString();
		String expected = "PARAM(345):LType.method(LOtherType;)V";
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition() {
		String method = "Lorg/bla/Blubb.m1()V";
		DefinitionSite actual = DefinitionSites.createDefinitionByReturn(method);
		DefinitionSite expected = new DefinitionSite();
		expected.setKind(DefinitionSiteKind.RETURN);
		expected.setMethod(m(method));
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition_toString() {
		String actual = DefinitionSites.createDefinitionByReturn("Lorg/bla/Blubb.m1()V").toString();
		String expected = "RETURN:Lorg/bla/Blubb.m1()V";
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

	private static ICoReFieldName f(String field) {
		return CoReFieldName.get(field);
	}

	private static ICoReMethodName m(String method) {
		return CoReMethodName.get(method);
	}
}