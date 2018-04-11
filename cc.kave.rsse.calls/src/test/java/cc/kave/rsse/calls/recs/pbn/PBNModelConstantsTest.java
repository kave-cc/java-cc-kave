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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstructor;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccessToField;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByReturnValue;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_FIELD;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_METHOD;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_TYPE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.UNKNOWN_DEFINITION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newCallSite;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newClassContext;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newDefinition;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newMethodContext;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newParameterSite;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.IDefinition;

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
		ITypeName type = Names.newType("org.bla.Blubb, P");
		String actual = newClassContext(type);
		String expected = type.getIdentifier();
		assertEquals(expected, actual);
	}

	@Test
	public void methodContext() {
		IMethodName method = Names.newMethod("Lorg/bla/Blubb.method()V");
		String actual = newMethodContext(method);
		String expected = method.getIdentifier();
		assertEquals(expected, actual);
	}

	@Test
	public void initDefinition() {
		IDefinition defSite = definedByConstructor("[p:void] [org.bla.Blubb, P]..ctor()");
		String actual = newDefinition(defSite);
		String expected = "INIT:[p:void] [org.bla.Blubb, P]..ctor()";
		assertEquals(expected, actual);
	}

	@Test
	public void methodReturnDefinition() {
		IDefinition defSite = definedByReturnValue("Lorg/bla/Blubb.m1()V");
		String actual = newDefinition(defSite);
		String expected = "RETURN:Lorg/bla/Blubb.m1()V";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterDefinition() {
		IDefinition defSite = definedByMethodParameter("LType.method(LOtherType;)V", 345);
		String actual = newDefinition(defSite);
		String expected = "PARAM(345):LType.method(LOtherType;)V";
		assertEquals(expected, actual);
	}

	@Test
	public void fieldDefinition() {
		IDefinition defSite = definedByMemberAccessToField("LType.name;LOtherType");
		String actual = newDefinition(defSite);
		String expected = "FIELD:LType.name;LOtherType";
		assertEquals(expected, actual);
	}

	@Test
	public void unknownDefinition() {
		String actual = newDefinition(definedByUnknown());
		String expected = "UNKNOWN";
		assertEquals(expected, actual);
	}

	@Test
	public void parameterSite() {
		IMethodName method = Names.newMethod("[p:void] [org.bla.Blubb, P].m1()");
		int argNum = 2345;
		String actual = newParameterSite(method, argNum);
		String expected = "P:[p:void] [org.bla.Blubb, P].m1()#2345";
		assertEquals(expected, actual);
	}

	@Test
	public void callSite() {
		IMethodName method = Names.newMethod("[p:void] [org.bla.Blubb, P].m2()");
		String actual = newCallSite(method);
		String expected = "C:[p:void] [org.bla.Blubb, P].m2()";
		assertEquals(expected, actual);
	}
}