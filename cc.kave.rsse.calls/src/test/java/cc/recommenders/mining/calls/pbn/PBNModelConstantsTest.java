/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls.pbn;

import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_FIELD;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_METHOD;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_TYPE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.UNKNOWN_DEFINITION;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newCallSite;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newClassContext;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newDefinition;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newMethodContext;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newParameterSite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;

public class PBNModelConstantsTest {

	@Test
	public void dummyValuesExist() {
		assertNotNull(DUMMY_TYPE);
		assertNotNull(DUMMY_METHOD);
		assertNotNull(DUMMY_FIELD);
		assertNotNull(UNKNOWN_DEFINITION);
	}

	@Test
	public void classContext() {
		ICoReTypeName type = CoReTypeName.get("Lorg/bla/Blubb");
		String actual = newClassContext(type);
		String expected = type.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void methodContext() {
		ICoReMethodName method = CoReMethodName.get("Lorg/bla/Blubb.method()V");
		String actual = newMethodContext(method);
		String expected = method.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void initDefinition() {
		DefinitionSite defSite = createInitDefinitionSite("Lorg/bla/Blubb.<init>()V");
		String actual = newDefinition(defSite);
		String expected = "INIT:Lorg/bla/Blubb.<init>()V";
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition() {
		DefinitionSite defSite = createMethodReturnDefinitionSite("Lorg/bla/Blubb.m1()V");
		String actual = newDefinition(defSite);
		String expected = "RETURN:Lorg/bla/Blubb.m1()V";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition() {
		DefinitionSite defSite = createParameterDefinitionSite("LType.method(LOtherType;)V", 345);
		String actual = newDefinition(defSite);
		String expected = "PARAM(345):LType.method(LOtherType;)V";
		assertEquals(expected, actual);
	}

	@Test
	public void fieldDefinition() {
		DefinitionSite defSite = createFieldDefinitionSite("LType.name;LOtherType");
		String actual = newDefinition(defSite);
		String expected = "FIELD:LType.name;LOtherType";
		assertEquals(expected, actual);
	}

	@Test
	public void unknownDefinition() {
		String actual = newDefinition(createUnknownDefinitionSite());
		String expected = "UNKNOWN";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterSite() {
		ICoReMethodName method = CoReMethodName.get("Lorg/bla/Blubb.m1()V");
		int argNum = 2345;
		String actual = newParameterSite(method, argNum);
		String expected = "P_Lorg/bla/Blubb.m1()V#2345";
		assertEquals(expected, actual);
	}

	@Test
	public void callSite() {
		ICoReMethodName method = CoReMethodName.get("Lorg/bla/Blubb.m2()V");
		String actual = newCallSite(method);
		String expected = "C_Lorg/bla/Blubb.m2()V";
		assertEquals(expected, actual);
	}

	private static DefinitionSite createInitDefinitionSite(String method) {
		return DefinitionSites.createDefinitionByConstructor(CoReMethodName.get(method));
	}

	private static DefinitionSite createMethodReturnDefinitionSite(String method) {
		return DefinitionSites.createDefinitionByReturn(CoReMethodName.get(method));
	}

	private static DefinitionSite createFieldDefinitionSite(String fieldName) {
		return DefinitionSites.createDefinitionByField(CoReFieldName.get(fieldName));
	}

	private static DefinitionSite createParameterDefinitionSite(String method, int argNum) {
		return DefinitionSites.createDefinitionByParam(CoReMethodName.get(method), argNum);
	}

	private static DefinitionSite createUnknownDefinitionSite() {
		return DefinitionSites.createUnknownDefinitionSite();
	}
}