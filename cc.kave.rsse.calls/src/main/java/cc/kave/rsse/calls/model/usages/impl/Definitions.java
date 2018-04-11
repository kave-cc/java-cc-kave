/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.model.usages.impl;

import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertLessOrEqual;
import static cc.kave.commons.assertions.Asserts.assertNotNull;
import static cc.kave.commons.assertions.Asserts.assertTrue;
import static cc.kave.commons.model.naming.Names.newEvent;
import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CONSTANT;
import static cc.kave.rsse.calls.model.usages.DefinitionType.MEMBER_ACCESS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.METHOD_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.THIS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.UNKNOWN;

import javax.annotation.Nonnull;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.DefinitionType;

/**
 * helper class to instantiate DefinitionSite instances with correct parameters
 */
public class Definitions {

	public static Definition definedByUnknown() {
		return new Definition(UNKNOWN);
	}

	public static Definition definedByConstant() {
		return new Definition(CONSTANT);
	}

	public static Definition definedByThis() {
		return new Definition(THIS);
	}

	public static Definition definedByMethodParameter(@Nonnull String id, int argIndex) {
		assertNotNull(id);
		return definedByMethodParameter(Names.newMethod(id), argIndex);
	}

	public static Definition definedByMethodParameter(@Nonnull IMethodName m, int argIndex) {
		assertNotNull(m);
		assertGreaterThan(argIndex, 0);
		assertLessOrEqual(argIndex, m.getParameters().size());

		final Definition d = new Definition(METHOD_PARAMETER);
		d.member = m;
		d.argIndex = argIndex;
		return d;
	}

	public static Definition definedByLoopHeader(@Nonnull String id) {
		assertNotNull(id);
		return definedByLoopHeader(Names.newType(id));
	}

	public static Definition definedByLoopHeader(@Nonnull ITypeName t) {
		assertNotNull(t);
		Definition d = new Definition(DefinitionType.LOOP_HEADER);
		d.member = Names.newMethod("[p:void] [System.Object, mscorlib, 4.0.0.0].loopHeader([%s] e)", t.getIdentifier());
		d.argIndex = 1;
		return d;
	}

	public static Definition definedByCatchParameter(@Nonnull String id) {
		assertNotNull(id);
		return definedByCatchParameter(Names.newType(id));
	}

	public static Definition definedByCatchParameter(@Nonnull ITypeName t) {
		assertNotNull(t);
		Definition d = new Definition(DefinitionType.CATCH_PARAMETER);
		d.member = Names.newMethod("[p:void] [System.Exception, mscorlib, 4.0.0.0].catch([%s] e)", t.getIdentifier());
		d.argIndex = 1;
		return d;
	}

	public static Definition definedByConstructor(@Nonnull String id) {
		assertNotNull(id);
		return definedByConstructor(Names.newMethod(id));
	}

	public static Definition definedByConstructor(@Nonnull IMethodName ctor) {
		assertNotNull(ctor);
		assertTrue(ctor.isConstructor());
		final Definition d = new Definition();
		d.kind = DefinitionType.NEW;
		d.member = ctor;
		return d;
	}

	public static Definition definedByReturnValue(@Nonnull String id) {
		assertNotNull(id);
		return definedByReturnValue(Names.newMethod(id));
	}

	public static Definition definedByReturnValue(@Nonnull IMethodName m) {
		assertNotNull(m);
		Asserts.assertFalse(m.isConstructor());
		Asserts.assertFalse(m.getReturnType().isVoidType());
		final Definition d = new Definition();
		d.kind = DefinitionType.RETURN_VALUE;
		d.member = m;
		return d;
	}

	public static Definition definedByMemberAccess(@Nonnull IMemberName n) {
		assertNotNull(n);
		Definition d = new Definition(MEMBER_ACCESS);
		d.member = n;
		return d;
	}

	public static Definition definedByMemberAccessToEvent(@Nonnull String id) {
		assertNotNull(id);
		return definedByMemberAccess(newEvent(id));
	}

	public static Definition definedByMemberAccessToField(@Nonnull String id) {
		assertNotNull(id);
		return definedByMemberAccess(newField(id));
	}

	public static Definition definedByMemberAccessToMethod(@Nonnull String id) {
		assertNotNull(id);
		return definedByMemberAccess(newMethod(id));
	}

	public static Definition definedByMemberAccessToProperty(@Nonnull String id) {
		assertNotNull(id);
		return definedByMemberAccess(newProperty(id));
	}
}