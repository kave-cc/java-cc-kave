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
package cc.kave.commons.model.naming.impl.v0.types;

import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;
import static cc.kave.commons.utils.StringUtils.FindPrevious;
import static cc.kave.commons.utils.StringUtils.f;

import java.util.regex.Pattern;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class TypeName extends BaseTypeName {

	private static final String[] InvalidIds = { "System.Boolean, mscorlib,", "System.Decimal, mscorlib,",
			"System.SByte, mscorlib,", "System.Byte, mscorlib,", "System.Int16, mscorlib,", "System.UInt16, mscorlib,",
			"System.Int32, mscorlib,", "System.UInt32, mscorlib,", "System.Int64, mscorlib,",
			"System.UInt64, mscorlib,", "System.Char, mscorlib,", "System.Single, mscorlib,",
			"System.Double, mscorlib,",
			// "System.String, mscorlib,",
			// "System.Object, mscorlib,",
			"System.Void, mscorlib" };

	public TypeName() {
		this(UnknownTypeIdentifier);
	}

	public TypeName(String id) {
		super(id);

		if (isUnknown()) {
			return;
		}

		for (String prefix : InvalidIds) {
			if (id.startsWith(prefix)) {
				throw new ValidationException(f("rejecting a predefined type: '%s'", id));
			}
		}

		boolean hasComma = id.contains(",") && id.lastIndexOf(',') > id.lastIndexOf(']');
		if (!hasComma) {
			throw new ValidationException(f("does not contain a correct assembly name: '%s'", id));
		}
	}

	@Override
	public IAssemblyName getAssembly() {
		if (isUnknown()) {
			return new AssemblyName();
		}
		if (isDelegateType()) {
			return getDeclaringType().getAssembly();
		}
		int endOfTypeName = GetLengthOfTypeName();
		// TODO NameUpdate: did trim ',' too, and no +1
		String assemblyIdentifier = identifier.substring(endOfTypeName + 1).trim();
		return new AssemblyName(assemblyIdentifier);
	}

	private int GetLengthOfTypeName() {
		String id = identifier;
		if (TypeUtils.isUnknownTypeIdentifier(id)) {
			return id.length();
		}

		int lastComma = id.lastIndexOf(',');
		int x = FindPrevious(id, lastComma - 1, ',', ']', '+');
		if (x == -1) {
			return lastComma;
		}
		return id.charAt(x) == ',' ? x : FindNext(id, x, ',');
	}

	@Override
	public INamespaceName getNamespace() {
		if (isUnknown()) {
			return new NamespaceName();
		}
		if (isDelegateType()) {
			return getDeclaringType().getNamespace();
		}

		String id = RemoveTypeParameterListButKeepTicks(getFullName());

		int endIndexOfNamespaceIdentifier = id.lastIndexOf('.');
		return endIndexOfNamespaceIdentifier < 0 ? new NamespaceName("")
				: new NamespaceName(id.substring(0, endIndexOfNamespaceIdentifier));
	}

	private String _fullName;

	@Override
	public String getFullName() {
		if (_fullName == null) {
			int length = GetLengthOfTypeName();
			_fullName = identifier.substring(0, length);
			if (isEnumType() || isInterfaceType() || isStructType()) {
				int startIdx = _fullName.indexOf(":") + 1;
				_fullName = _fullName.substring(startIdx);
			}
		}
		return _fullName;
	}

	private static String RemoveTypeParameterListButKeepTicks(String fullName) {
		int startIdx = fullName.indexOf('[');
		if (startIdx != -1) {
			int endIdx = FindCorrespondingCloseBracket(fullName, startIdx);
			String genericInfo = fullName.substring(startIdx, endIdx + 1);
			return fullName.replace(genericInfo, "");
		}
		return fullName;
	}

	@Override
	public String getName() {
		String rawFullName = RemoveTypeParameterListButKeepTicks(getFullName());
		int endOfOutTypeName = rawFullName.lastIndexOf('+');
		if (endOfOutTypeName > -1) {
			rawFullName = rawFullName.substring(endOfOutTypeName + 1);
		}
		int endOfTypeName = rawFullName.lastIndexOf('`');
		if (endOfTypeName > -1) {
			rawFullName = rawFullName.substring(0, endOfTypeName);
		}
		int startIndexOfSimpleName = rawFullName.lastIndexOf('.');
		return rawFullName.substring(startIndexOfSimpleName + 1);
	}

	@Override
	public ITypeName getDeclaringType() {
		int plus = FindPlus(identifier);
		if (plus == -1) {
			return null;
		}

		int start = identifier.startsWith(PrefixEnum) ? PrefixEnum.length()
				: identifier.startsWith(PrefixInterface) ? PrefixInterface.length()
						: identifier.startsWith(PrefixStruct) ? PrefixStruct.length() : 0;

		String declTypeId = identifier.substring(start, plus - start);

		return new TypeName(f("%s, %s", declTypeId, getAssembly().getIdentifier()));
	}

	@Override
	public boolean isNestedType() {
		return FindPlus(identifier) != -1;
	}

	private int FindPlus(String id) {
		int comma = id.length() - getAssembly().getIdentifier().length();
		// unknown type
		if (comma < 0) {
			return -1;
		}
		int plus = FindPrevious(id, comma, '+', ']');
		if (plus == -1) {
			return -1;
		}
		// is generic
		if (id.charAt(plus) == ']') {
			int closeGeneric = FindCorrespondingOpenBracket(id, plus);
			plus = FindPrevious(id, closeGeneric, '+');
		}
		return plus;
	}

	private static final Pattern MissingTickForGenericsMatcher = Pattern.compile("[a-zA-Z]\\[\\[");

	public static boolean isTypeNameIdentifier(String id) {
		if (TypeUtils.isUnknownTypeIdentifier(id) || PredefinedTypeName.isPredefinedTypeNameIdentifier(id)
				|| TypeParameterName.isTypeParameterNameIdentifier(id) || ArrayTypeName.isArrayTypeNameIdentifier(id)
				|| DelegateTypeName.isDelegateTypeNameIdentifier(id)) {
			return false;
		}

		// unbalanced brackets
		for (String pair : new String[] { "[]", "()" }) {
			if (Count(id, pair.charAt(0)) != Count(id, pair.charAt(1))) {
				return false;
			}
		}

		if (MissingTickForGenericsMatcher.matcher(id).matches()) {
			return false;
		}

		return true;
	}

	private static int Count(String id, char needle) {
		int count = 0;
		for (int i = 0; i < id.length(); i++) {
			if (id.charAt(i) == needle) {
				count++;
			}
		}
		return count;
	}
}