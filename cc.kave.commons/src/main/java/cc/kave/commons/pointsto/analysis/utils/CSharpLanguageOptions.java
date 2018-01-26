/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.utils;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.assignment;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.indexAccessReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.invocationExpr;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.propertyReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.referenceExpr;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableDeclaration;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.Arrays;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.PropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;

public class CSharpLanguageOptions extends LanguageOptions {

	@Override
	public String getThisName() {
		return "this";
	}

	@Override
	public String getSuperName() {
		return "base";
	}

	@Override
	public ITypeName getTopClass() {
		return Names.newType("p:object");
	}

	@Override
	public ITypeName getSuperType(ITypeHierarchy typeHierarchy) {
		if (typeHierarchy.hasSuperclass()) {
			return typeHierarchy.getExtends().getElement();
		} else {
			return this.getTopClass();
		}
	}

	@Override
	public String getPropertyParameterName() {
		return "value";
	}

	@Override
	public IMethodName addLambda(IMethodName method) {
		return Names.newMethod(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}

	@Override
	public ITypeName addLambda(ITypeName type) {
		return Names.newType(LambdaNameHelper.addLambdaToTypeName(type.getIdentifier()));
	}

	@Override
	public IMethodName removeLambda(IMethodName method) {
		return Names.newMethod(LambdaNameHelper.removeLambda(method.getIdentifier()));
	}

	@Override
	public IFieldName propertyToField(IPropertyName property) {
		String propertyIdentifier = property.getIdentifier();
		// remove 'get set' and trailing '()'
		String fieldIdentifier = propertyIdentifier.substring(propertyIdentifier.indexOf('['));
		fieldIdentifier = fieldIdentifier.substring(0, fieldIdentifier.lastIndexOf('('));

		if (property.isStatic()) {
			fieldIdentifier = "static " + fieldIdentifier;
		}

		return Names.newField(fieldIdentifier);
	}

	@Override
	public boolean isDelegateInvocation(IMethodName invokedMethod) {
		return invokedMethod.getName().equals("Invoke") && invokedMethod.getDeclaringType().isDelegateType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IMemberName> T resolveVirtual(T staticMethod, ITypeName dynamicType) {
		String methodName = staticMethod.getName();
		ITypeName staticType = staticMethod.getDeclaringType();
		if (staticType.equals(dynamicType)) {
			return staticMethod;
		}

		String target = "[" + staticType.getIdentifier() + "]." + methodName + "(";
		String replacement = "[" + dynamicType.getIdentifier() + "]." + methodName + "(";
		String oldIdentifier = staticMethod.getIdentifier();
		String newIdentifier = oldIdentifier.replace(target, replacement);

		if (staticMethod.getClass() == MethodName.class) {
			return (T) Names.newMethod(newIdentifier);
		} else if (staticMethod.getClass() == PropertyName.class) {
			return (T) Names.newProperty(newIdentifier);
		} else {
			throw new UnexpectedSSTNodeException();
		}
	}

	@Override
	public List<IStatement> emulateForEachVariableAssignment(IVariableReference dest, IVariableReference src,
			ITypeName srcType) {
		if (srcType != null && srcType.isArray()) {
			return Arrays.asList(assignment(dest, indexAccessReference(src)));
		} else {
			String tmpVar = "$foreachEnumerator";
			ITypeName enumeratorType = Names.newType("System.Collections.IEnumerator, mscorlib");
			IMethodName getEnumeratorName = Names.newMethod(
					"[" + enumeratorType.getIdentifier() + "] [" + srcType.getIdentifier() + "].GetEnumerator()");
			IPropertyName current = Names
					.newProperty("get [System.Object, mscorlib] [" + enumeratorType.getIdentifier() + "].Current()");
			return Arrays.asList(variableDeclaration(enumeratorType, variableReference(tmpVar)),
					assignment(variableReference(tmpVar), invocationExpr(getEnumeratorName, src)),
					assignment(dest, referenceExpr(propertyReference(variableReference(tmpVar), current))));
		}
	}
}