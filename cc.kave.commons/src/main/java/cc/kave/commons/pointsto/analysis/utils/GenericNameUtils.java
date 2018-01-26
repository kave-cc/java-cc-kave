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
package cc.kave.commons.pointsto.analysis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;

public class GenericNameUtils {

	private static final String INSTANTIATION_ARROW = "->";

	@SuppressWarnings("unchecked")
	public static <T extends IMemberName> T eraseGenericInstantiations(T member) {
		if (member instanceof IMethodName) {
			return (T) eraseGenericInstantiations((IMethodName) member);
		} else if (member instanceof IPropertyName) {
			return (T) eraseGenericInstantiations((IPropertyName) member);
		} else if (member instanceof IFieldName) {
			return (T) eraseGenericInstantiations((IFieldName) member);
		} else if (member instanceof IEventName) {
			return (T) eraseGenericInstantiations((IEventName) member);
		} else {
			throw new UnexpectedNameException(member);
		}
	}

	public static IEventName eraseGenericInstantiations(IEventName event) {
		if (!event.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return event;
		}

		return Names.newEvent(replaceTypes(event.getIdentifier(), event.getHandlerType(), event.getDeclaringType()));
	}

	public static IFieldName eraseGenericInstantiations(IFieldName field) {
		if (!field.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return field;
		}

		return Names.newField(replaceTypes(field.getIdentifier(), field.getValueType(), field.getDeclaringType()));
	}

	public static IPropertyName eraseGenericInstantiations(IPropertyName property) {
		if (!property.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return property;
		}

		return Names.newProperty(
				replaceTypes(property.getIdentifier(), property.getValueType(), property.getDeclaringType()));
	}

	public static IMethodName eraseGenericInstantiations(IMethodName method) {
		if (!method.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return method;
		}

		List<IParameterName> parameters = method.getParameters();
		List<ITypeName> parameterTypes = new ArrayList<>(parameters.size());
		for (IParameterName parameter : parameters) {
			parameterTypes.add(parameter.getValueType());
		}

		return Names.newMethod(replaceTypes(method.getIdentifier(),
				Iterables.concat(Arrays.asList(method.getReturnType(), method.getDeclaringType()), parameterTypes)));
	}

	public static ITypeName eraseGenericInstantiations(ITypeName type) {
		String id = type.getIdentifier();
		if (!id.contains(INSTANTIATION_ARROW)) {
			return type;
		}

		for (ITypeParameterName typeParam : type.getTypeParameters()) {
			String name = typeParam.getTypeParameterShortName();

			// guard against a bug in TypeName.getTypeParameters producing
			// invalid type parameters
			if (Strings.isNullOrEmpty(name)) {
				return type;
			}

			String pattern = Pattern
					.quote(name + " " + INSTANTIATION_ARROW + " " + typeParam.getTypeParameterType().getIdentifier());
			id = id.replaceAll(pattern, name);
		}
		return Names.newType(id);
	}

	private static String replaceTypes(String id, ITypeName... types) {
		return replaceTypes(id, Arrays.asList(types));
	}

	private static String replaceTypes(String id, Iterable<ITypeName> types) {
		for (ITypeName type : types) {
			String oldType = type.getIdentifier();
			String newType = eraseGenericInstantiations(type).getIdentifier();
			if (!oldType.equals(newType)) {
				id = id.replace("[" + oldType + "]", "[" + newType + "]");
			}
		}
		return id;
	}
}