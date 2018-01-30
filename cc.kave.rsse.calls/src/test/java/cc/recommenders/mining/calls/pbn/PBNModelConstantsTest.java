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
import static cc.recommenders.usages.DefinitionSites.createDefinitionByConstructor;
import static cc.recommenders.usages.DefinitionSites.createDefinitionByField;
import static cc.recommenders.usages.DefinitionSites.createDefinitionByParam;
import static cc.recommenders.usages.DefinitionSites.createDefinitionByReturn;
import static cc.recommenders.usages.DefinitionSites.createUnknownDefinitionSite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.recommenders.usages.DefinitionSite;

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
		ITypeName type = Names.newType("Lorg/bla/Blubb");
		String actual = newClassContext(type);
		String expected = type.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void methodContext() {
		IMethodName method = Names.newMethod("Lorg/bla/Blubb.method()V");
		String actual = newMethodContext(method);
		String expected = method.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void initDefinition() {
		DefinitionSite defSite = createDefinitionByConstructor("Lorg/bla/Blubb.<init>()V");
		String actual = newDefinition(defSite);
		String expected = "INIT:Lorg/bla/Blubb.<init>()V";
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition() {
		DefinitionSite defSite = createDefinitionByReturn("Lorg/bla/Blubb.m1()V");
		String actual = newDefinition(defSite);
		String expected = "RETURN:Lorg/bla/Blubb.m1()V";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition() {
		DefinitionSite defSite = createDefinitionByParam("LType.method(LOtherType;)V", 345);
		String actual = newDefinition(defSite);
		String expected = "PARAM(345):LType.method(LOtherType;)V";
		assertEquals(expected, actual);
	}

	@Test
	public void fieldDefinition() {
		DefinitionSite defSite = createDefinitionByField("LType.name;LOtherType");
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
		IMethodName method = Names.newMethod("Lorg/bla/Blubb.m1()V");
		int argNum = 2345;
		String actual = newParameterSite(method, argNum);
		String expected = "P_Lorg/bla/Blubb.m1()V#2345";
		assertEquals(expected, actual);
	}

	@Test
	public void callSite() {
		IMethodName method = Names.newMethod("Lorg/bla/Blubb.m2()V");
		String actual = newCallSite(method);
		String expected = "C_Lorg/bla/Blubb.m2()V";
		assertEquals(expected, actual);
	}
}