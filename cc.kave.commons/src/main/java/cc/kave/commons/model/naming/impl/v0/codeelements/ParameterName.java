/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.naming.impl.v0.codeelements;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;

public class ParameterName extends BaseName implements IParameterName {

	private static final String UNKNOWN_PARAMETER_NAME = "[?] ???";

	public static final String PassByReferenceModifier = "ref";
	public static final String OutputModifier = "out";
	public static final String VarArgsModifier = "params";
	public static final String OptionalModifier = "opt";
	public static final String ExtensionMethodModifier = "this";

	public ParameterName() {
		this(UNKNOWN_PARAMETER_NAME);
	}

	public ParameterName(String identifier) {
		super(identifier);
		if (isParameterArray() && !getValueType().isArray()) {
			throw new ValidationException("the params keyword requires array type");
		}
	}

	public ITypeName getValueType() {
		int startOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int endOfValueTypeIdentifier = identifier.lastIndexOf(']');
		return TypeUtils.createTypeName(identifier.substring(startOfValueTypeIdentifier, endOfValueTypeIdentifier));
	}

	public String getName() {
		return identifier.substring(identifier.lastIndexOf(' ') + 1);
	}

	public boolean isPassedByReference() {
		return getValueType().isReferenceType() || Modifiers().contains(PassByReferenceModifier);
	}

	private String Modifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	public boolean isOutput() {
		return Modifiers().contains(OutputModifier);
	}

	public boolean isParameterArray() {
		return Modifiers().contains(VarArgsModifier);
	}

	public boolean isOptional() {
		return Modifiers().contains(OptionalModifier);
	}

	public boolean isExtensionMethodParameter() {
		return Modifiers().contains(ExtensionMethodModifier);
	}

	public boolean isUnknown() {
		return UNKNOWN_PARAMETER_NAME.equals(identifier);
	}
}