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

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.NameUtils;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.utils.StringUtils;

public class MethodName extends MemberName implements IMethodName {

	private static final String UnknownMethodIdentifier = UnknownMemberIdentifier + "()";

	public MethodName() {
		this(UnknownMethodIdentifier);
	}

	public MethodName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return UnknownMethodIdentifier.equals(identifier);
	}

	private String _name;

	@Override
	public String getName() {
		if (_name == null) {
			if (isUnknown()) {
				_name = UNKNOWN_NAME_IDENTIFIER;
			} else {
				int openR = identifier.indexOf('[');
				int closeR = StringUtils.FindCorrespondingCloseBracket(identifier, openR);
				int openD = StringUtils.FindNext(identifier, closeR, '[');
				int closeD = StringUtils.FindCorrespondingCloseBracket(identifier, openD);
				int startName = StringUtils.FindNext(identifier, closeD, '.') + 1;
				int endName = StringUtils.FindNext(identifier, startName, '`', '(');
				_name = identifier.substring(startName, endName);
			}
		}
		return _name;
	}

	private List<ITypeParameterName> _typeParameters;

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		if (_typeParameters == null) {
			if (getFullName().contains("`")) {
				int start = getFullName().indexOf('[');
				int end = getFullName().lastIndexOf(']');
				_typeParameters = NameUtils.ParseTypeParameterList(getFullName(), start, end);
			} else {
				_typeParameters = Lists.newLinkedList();
			}
		}

		return _typeParameters;
	}

	@Override
	public boolean hasTypeParameters() {
		return getTypeParameters().size() > 0;
	}

	private List<IParameterName> _parameters;

	@Override
	public List<IParameterName> getParameters() {
		if (_parameters == null) {
			int endOfParameters = identifier.lastIndexOf(')');
			int startOfParameters = StringUtils.FindCorrespondingOpenBracket(identifier, endOfParameters);
			_parameters = NameUtils.GetParameterNamesFromSignature(identifier, startOfParameters, endOfParameters);
		}

		return _parameters;
	}

	@Override
	public boolean hasParameters() {
		return getParameters().size() > 0;
	}

	@Override
	public boolean isConstructor() {
		return getName().equals(".ctor") || getName().equals(".cctor");
	}

	@Override
	public boolean isInit() {
		return getName().equals(".init") || getName().equals(".cinit");
	}

	@Override
	public ITypeName getReturnType() {
		return getValueType();
	}

	@Override
	public boolean isExtensionMethod() {
		return isStatic() && hasParameters() && getParameters().get(0).isExtensionMethodParameter();
	}
}