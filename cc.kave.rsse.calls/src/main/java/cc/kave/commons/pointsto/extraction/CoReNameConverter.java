/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.extraction;

import java.util.stream.Collectors;

import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.commons.pointsto.analysis.exceptions.MissingTypeNameException;
import cc.kave.commons.pointsto.analysis.utils.LambdaNameHelper;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReName;

public class CoReNameConverter {

	private static final String VOID_NAME = "LSystem/Void";
	private static final String OBJECT_NAME = "LSystem/Object";
	private static final String UNKNOWN_NAME = "LUnknown";

	private static final LanguageOptions LANGUAGE_OPTIONS = LanguageOptions.getInstance();

	private static String toName(INamespaceName namespace) {
		if (namespace.isGlobalNamespace()) {
			return "";
		}

		INamespaceName parentNamespace = namespace.getParentNamespace();
		if (parentNamespace == null) {
			return "";
		}

		return toName(parentNamespace) + namespace.getName() + "/";
	}

	private static String toName(ITypeName sstType) {
		if (sstType.isTypeParameter()) {
			return OBJECT_NAME;
		}
		if (sstType.isArray()) {
			return "[" + toName(sstType.asArrayTypeName().getArrayBaseType());
		}
		if (sstType.isNestedType()) {
			return toName(sstType.getDeclaringType()) + "$" + sstType.getName();
		}
		if (sstType.isUnknown()) {
			return UNKNOWN_NAME;
		}

		return "L" + toName(sstType.getNamespace()) + sstType.getName();
	}

	public static cc.recommenders.names.ICoReTypeName convert(ITypeName sstType) {
		if (sstType == null) {
			return null;
		}

		return CoReTypeName.get(toName(sstType));
	}

	public static cc.recommenders.names.ICoReFieldName convert(IFieldName sstField) {
		if (sstField == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(toName(sstField.getDeclaringType()));
		builder.append('.');
		builder.append(sstField.isUnknown() ? "unknown" : sstField.getName());
		builder.append(';');
		builder.append(toName(sstField.getValueType()));
		return CoReFieldName.get(builder.toString());
	}

	public static cc.recommenders.names.ICoReFieldName convert(IPropertyName sstProperty) {
		if (sstProperty == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(toName(sstProperty.getDeclaringType()));
		builder.append('.');
		builder.append(sstProperty.isUnknown() ? "unknown" : sstProperty.getName());
		builder.append(';');
		builder.append(toName(sstProperty.getValueType()));
		return CoReFieldName.get(builder.toString());
	}

	public static cc.recommenders.names.ICoReMethodName convert(IMethodName sstMethod) {
		if (sstMethod == null) {
			return null;
		}

		boolean isLambda = LANGUAGE_OPTIONS.isLambdaName(sstMethod);
		if (isLambda) {
			sstMethod = LANGUAGE_OPTIONS.removeLambda(sstMethod);
		}

		StringBuilder builder = new StringBuilder();
		ITypeName declaringType = sstMethod.getDeclaringType();
		if (declaringType.getName().equals("")) {
			throw new MissingTypeNameException(sstMethod);
		}
		builder.append(toName(declaringType));

		if (sstMethod.isConstructor()) {
			builder.append(".<init>(");
			builder.append(sstMethod.getParameters().stream().map(parameter -> toName(parameter.getValueType()) + ";")
					.collect(Collectors.joining()));
			builder.append(')');
			builder.append(VOID_NAME);
			builder.append(';');
		} else {
			builder.append('.');
			// TODO replace with isUnknown once fixed
			builder.append(sstMethod.isUnknown() ? "unknown" : sstMethod.getName());
			builder.append('(');
			builder.append(sstMethod.getParameters().stream().map(parameter -> toName(parameter.getValueType()) + ";")
					.collect(Collectors.joining()));
			builder.append(')');
			builder.append(toName(sstMethod.getReturnType()));
			builder.append(';');
		}

		String identifier = builder.toString();
		if (isLambda) {
			return addLambda(CoReMethodName.get(identifier));
		}
		return CoReMethodName.get(identifier);
	}

	public static boolean isUnknown(cc.recommenders.names.ICoReTypeName type) {
		return type.getIdentifier().equals(UNKNOWN_NAME);
	}

	public static boolean isLambdaName(ICoReName name) {
		return LambdaNameHelper.isLambdaName(name.getIdentifier());
	}

	public static cc.recommenders.names.ICoReMethodName addLambda(cc.recommenders.names.ICoReMethodName method) {
		return CoReMethodName.get(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}

	public static cc.recommenders.names.ICoReTypeName addLambda(cc.recommenders.names.ICoReTypeName type) {
		return CoReTypeName.get(LambdaNameHelper.addLambdaToTypeName(type.getIdentifier()));
	}

	public static cc.recommenders.names.ICoReMethodName removeLambda(cc.recommenders.names.ICoReMethodName method) {
		return CoReMethodName.get(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}
}