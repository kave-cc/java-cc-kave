/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.naming.impl.v0.types;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.StringUtils;

public class TypeUtils {

	private static final ITypeName UnknownTypeInstance = new TypeName();

	public static ITypeName createTypeName(String identifier) {
		if (isUnknownTypeIdentifier(identifier)) {
			return UnknownTypeInstance;
		}
		if (PredefinedTypeName.isPredefinedTypeNameIdentifier(identifier)) {
			return new PredefinedTypeName(identifier);
		}
		if (TypeParameterName.isTypeParameterNameIdentifier(identifier)) {
			return new TypeParameterName(identifier);
		}
		if (ArrayTypeName.isArrayTypeNameIdentifier(identifier)) {
			return new ArrayTypeName(identifier);
		}
		if (DelegateTypeName.isDelegateTypeNameIdentifier(identifier)) {
			return new DelegateTypeName(identifier);
		}
		if (TypeName.isTypeNameIdentifier(identifier)) {
			return new TypeName(identifier);
		}

		return UnknownTypeInstance;
	}

	public static boolean isUnknownTypeIdentifier(String identifier) {
		return StringUtils.isNullOrEmpty(identifier) || BaseTypeName.UnknownTypeIdentifier.equals(identifier);
	}
}