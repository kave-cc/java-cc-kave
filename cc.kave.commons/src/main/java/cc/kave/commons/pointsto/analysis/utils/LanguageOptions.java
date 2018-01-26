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

import java.util.List;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;

public abstract class LanguageOptions {

	private static final LanguageOptions instance = new CSharpLanguageOptions();

	public static LanguageOptions getInstance() {
		return instance;
	}

	public abstract String getThisName();

	public abstract String getSuperName();

	public abstract ITypeName getSuperType(ITypeHierarchy typeHierarchy);

	public abstract ITypeName getTopClass();

	public abstract String getPropertyParameterName();

	public boolean isAutoImplementedProperty(IPropertyDeclaration propertyDecl) {
		return propertyDecl.getGet().isEmpty();
	}

	public abstract IFieldName propertyToField(IPropertyName property);

	public abstract IMethodName addLambda(IMethodName method);

	public abstract IMethodName removeLambda(IMethodName method);

	public abstract ITypeName addLambda(ITypeName type);

	public boolean isLambdaName(IName name) {
		return LambdaNameHelper.isLambdaName(name.getIdentifier());
	}

	public abstract boolean isDelegateInvocation(IMethodName invokedMethod);

	public abstract <T extends IMemberName> T resolveVirtual(T staticMethod, ITypeName dynamicType);

	public int countOptionalParameters(List<IParameterName> parameters) {
		int count = 0;
		for (IParameterName parameter : parameters) {
			if (parameter.isOptional() || parameter.isParameterArray()) {
				++count;
			}
		}
		return count;
	}

	public abstract List<IStatement> emulateForEachVariableAssignment(IVariableReference dest, IVariableReference src,
			ITypeName srcType);
}