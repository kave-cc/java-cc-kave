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

import static cc.kave.commons.assertions.Asserts.assertGreaterOrEqual;
import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertNotNull;
import static cc.kave.commons.assertions.Asserts.assertTrue;
import static cc.kave.commons.model.naming.Names.newEvent;
import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CAST;
import static cc.kave.rsse.calls.model.usages.DefinitionType.CONSTANT;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LAMBDA_DECL;
import static cc.kave.rsse.calls.model.usages.DefinitionType.LAMBDA_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.MEMBER_ACCESS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.METHOD_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.OUT_PARAMETER;
import static cc.kave.rsse.calls.model.usages.DefinitionType.THIS;
import static cc.kave.rsse.calls.model.usages.DefinitionType.UNKNOWN;

import javax.annotation.Nonnull;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;

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

	public static IDefinition definedByCast() {
		return new Definition(CAST);
	}

	public static Definition definedByThis() {
		return new Definition(THIS);
	}

	public static Definition definedByLambdaParameter() {
		return new Definition(LAMBDA_PARAMETER);
	}

	public static Definition definedByLambdaDecl() {
		return new Definition(LAMBDA_DECL);
	}

	public static Definition definedByMethodParameter(@Nonnull String id, int argIndex) {
		assertNotNull(id);
		return definedByMethodParameter(Names.newMethod(id), argIndex);
	}

	public static Definition definedByMethodParameter(@Nonnull IMethodName m, int argIndex) {
		assertNotNull(m);
		assertGreaterOrEqual(argIndex, 0);
		assertGreaterThan(m.getParameters().size(), argIndex);

		final Definition d = new Definition(METHOD_PARAMETER);
		d.member = m;
		d.argIndex = argIndex;
		return d;
	}

	public static Definition definedByLoopHeader() {
		return new Definition(DefinitionType.LOOP_HEADER);
	}

	public static Definition definedByCatchParameter() {
		return new Definition(DefinitionType.CATCH_PARAMETER);
	}

	public static Definition definedByConstructor(@Nonnull String id) {
		assertNotNull(id);
		return definedByConstructor(Names.newMethod(id));
	}

	public static Definition definedByConstructor(@Nonnull IMethodName ctor) {
		assertNotNull(ctor);
		assertTrue(ctor.isConstructor());
		final Definition d = new Definition();
		d.type = DefinitionType.NEW;
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
		d.type = DefinitionType.RETURN_VALUE;
		d.member = m;
		return d;
	}

	public static IDefinition definedByOutParameter(String id) {
		assertNotNull(id);
		return definedByOutParameter(Names.newMethod(id));
	}

	public static IDefinition definedByOutParameter(IMethodName n) {
		assertNotNull(n);
		boolean hasOutParams = false;
		for (IParameterName p : n.getParameters()) {
			if (p.isOutput()) {
				hasOutParams = true;
			}
		}
		Asserts.assertTrue(hasOutParams);
		Definition d = new Definition();
		d.type = OUT_PARAMETER;
		d.member = n;
		return d;
	}

	public static Definition definedByMemberAccess(@Nonnull IMemberName n) {
		assertNotNull(n);
		Definition d = new Definition();
		d.type = MEMBER_ACCESS;
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