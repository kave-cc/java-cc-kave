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

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.StringUtils;

public abstract class MemberName extends BaseName implements IMemberName {

	protected static final String UnknownMemberIdentifier = "[?] [?].???";
	public static final String StaticModifier = "static";

	protected MemberName(String identifier) {
		super(identifier);
	}

	protected String getModifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	@Override
	public boolean isStatic() {
		return getModifiers().contains(StaticModifier);
	}

	@Override
	public String getName() {
		String nameWithBraces = identifier.substring(identifier.lastIndexOf('.') + 1);
		int endIdx = nameWithBraces.indexOf('(');
		return endIdx != -1 ? nameWithBraces.substring(0, endIdx) : nameWithBraces;
	}

	private String _fullName;

	@Override
	public String getFullName() {
		if (_fullName == null) {
			_fullName = getFullNameImpl();
		}
		return _fullName;
	}

	private String getFullNameImpl() {
		String id = identifier;
		int openR = id.indexOf("[");
		int closeR = StringUtils.FindCorrespondingCloseBracket(id, openR);

		int openD = StringUtils.FindNext(id, closeR, '[');
		int closeD = StringUtils.FindCorrespondingCloseBracket(id, openD);

		int dot = StringUtils.FindNext(id, closeD, '.');
		int openP = StringUtils.FindNext(id, dot, '(', '`');
		if (openP != -1 && id.charAt(openP) == '`') {
			int openGen = StringUtils.FindNext(id, openP, '[');
			int closeGen = StringUtils.FindCorrespondingCloseBracket(id, openGen);
			openP = StringUtils.FindNext(id, closeGen, '(');
		}

		int start = dot + 1;
		int end = openP == -1 ? id.length() : openP;
		return id.substring(start, end).trim();
	}

	private ITypeName valueType;

	@Override
	public ITypeName getValueType() {
		if (valueType != null) {
			return valueType;
		}
		int openValType = StringUtils.FindNext(identifier, 0, '[');
		int closeValType = StringUtils.FindCorrespondingCloseBracket(identifier, openValType);
		// ignore open bracket
		openValType++;
		String declTypeIdentifier = identifier.substring(openValType, closeValType);
		return valueType = TypeUtils.createTypeName(declTypeIdentifier);
	}

	private ITypeName declaringType;

	@Override
	public ITypeName getDeclaringType() {
		if (declaringType != null) {
			return declaringType;
		}
		int openValType = StringUtils.FindNext(identifier, 0, '[');
		int closeValType = StringUtils.FindCorrespondingCloseBracket(identifier, openValType);
		int openDeclType = StringUtils.FindNext(identifier, closeValType, '[');
		int closeDeclType = StringUtils.FindCorrespondingCloseBracket(identifier, openDeclType);
		// ignore open bracket
		openDeclType++;
		String declTypeIdentifier = identifier.substring(openDeclType, closeDeclType);
		return declaringType = TypeUtils.createTypeName(declTypeIdentifier);
	}
}