/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.commons.model.naming.impl.v0.types;

import static cc.kave.commons.assertions.Asserts.assertFalse;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;
import static cc.kave.commons.utils.StringUtils.FindPrevious;
import static cc.kave.commons.utils.StringUtils.f;
import static cc.kave.commons.utils.StringUtils.insert;
import static cc.kave.commons.utils.StringUtils.remove;

import java.util.regex.Pattern;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class ArrayTypeName extends BaseTypeName implements IArrayTypeName {

	public ArrayTypeName(String identifier) {
		super(identifier);
		assertFalse(TypeUtils.isUnknownTypeIdentifier(identifier));
	}

	@Override
	public ITypeName getArrayBaseType() {
		// can not be TypeParameter)

		String id = getIdentifier();
		String newId;

		if (id.startsWith("d:")) // base is delegate
		{
			newId = id.substring(0, id.lastIndexOf(')') + 1);
			return TypeUtils.createTypeName(newId);
		}

		int openArr = findArrayMarkerIndex(id);
		assertFalse(openArr == -1);
		int closeArr = FindCorrespondingCloseBracket(id, openArr);
		assertFalse(closeArr == -1);

		newId = remove(id, openArr, closeArr - openArr + 1);
		return TypeUtils.createTypeName(newId);
	}

	@Override
	public int getRank() {
		return getArrayRank(this);
	}

	@Override
	public String getName() {
		return getArrayBaseType().getName() + createArrayMarker(getRank());
	}

	@Override
	public String getFullName() {
		return getArrayBaseType().getFullName() + createArrayMarker(getRank());
	}

	@Override
	public INamespaceName getNamespace() {
		return getArrayBaseType().getNamespace();
	}

	@Override
	public IAssemblyName getAssembly() {
		return getArrayBaseType().getAssembly();
	}

	@Override
	public boolean isNestedType() {
		return false;
	}

	@Override
	public ITypeName getDeclaringType() {
		return null;
	}

	private static final Pattern UnknownArrayMatcher = Pattern.compile("^\\?\\[,*\\]$");

	public static boolean isArrayTypeNameIdentifier(String id) {
		if (TypeUtils.isUnknownTypeIdentifier(id)) {
			return false;
		}
		if (UnknownArrayMatcher.matcher(id).matches()) {
			return true;
		}
		if (id.startsWith("d:")) {
			int idx = id.lastIndexOf(')');
			return idx != -1 && FindNext(id, idx, '[') != -1;
		}
		if (TypeParameterName.isTypeParameterNameIdentifier(id)) {
			return false;
		}
		if (id.startsWith("p:")) {
			return false;
		}

		return findArrayMarkerIndex(id) != -1;
	}

	private static int getArrayRank(ITypeName typeName) {
		String id = typeName.getIdentifier();
		int arrOpen = findArrayMarkerIndex(id);
		if (arrOpen == -1) {
			return 0;
		}
		int arrClose = FindCorrespondingCloseBracket(id, arrOpen);
		return arrClose - arrOpen;
	}

	private static int findArrayMarkerIndex(String id) {
		int closeBracket = id.lastIndexOf(']');
		if (closeBracket == -1) {
			return -1;
		}
		int cur = closeBracket;

		// regular (multi-dimensional) array
		while (cur - 1 > 0 && id.charAt(--cur) == ',') {
		}
		if (id.charAt(cur) == '[') {
			return cur;
		}

		// generic
		int openGeneric = FindCorrespondingOpenBracket(id, closeBracket);
		if (openGeneric == -1) {
			return -1;
		}
		int tick = FindPrevious(id, openGeneric, '`');
		if (tick == -1) {
			return -1;
		}
		int openArr = FindNext(id, tick, '[');
		if (openArr == openGeneric) {
			return -1;
		}
		return openArr;
	}

	public static IArrayTypeName from(ITypeName baseType, int rank) {
		Asserts.assertTrue(rank > 0, "rank smaller than 1");
		rank = baseType.isArray() ? baseType.asArrayTypeName().getRank() + rank : rank;
		baseType = baseType.isArray() ? baseType.asArrayTypeName().getArrayBaseType() : baseType;
		String arrMarker = createArrayMarker(rank);

		if (baseType.isTypeParameter()) {
			if (baseType.asTypeParameterName().isBound()) {
				ITypeName paramType = baseType.asTypeParameterName().getTypeParameterType();
				return new TypeParameterName(f("%s%s -> %s", baseType.getName(), arrMarker, paramType.getIdentifier()));
			}
			return new TypeParameterName(f("%s%s", baseType.getName(), arrMarker));
		}

		if (baseType.isPredefined()) {
			return new PredefinedTypeName(baseType.getIdentifier() + arrMarker);
		}

		if (baseType.isDelegateType() || baseType.isUnknown()) {
			return new ArrayTypeName(baseType.getIdentifier() + arrMarker);
		}

		return new ArrayTypeName(insertMarkerAfterRawName(baseType, arrMarker));
	}

	private static String createArrayMarker(int rank) {
		Asserts.assertTrue(rank > 0);
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < rank - 1; i++) {
			sb.append(',');
		}
		return sb.append("]").toString();
	}

	private static String insertMarkerAfterRawName(ITypeName baseType, String arrayMarker) {
		assertFalse(baseType.isArray());
		assertFalse(baseType.isDelegateType());
		assertFalse(baseType.isTypeParameter());
		assertFalse(baseType.isPredefined());

		String id = baseType.getIdentifier();
		int arrIdx = -1;

		if (baseType.hasTypeParameters()) {
			int closeGeneric = id.lastIndexOf("]");
			arrIdx = FindCorrespondingOpenBracket(id, closeGeneric);
		} else {
			String asmId = baseType.getAssembly().getIdentifier();
			int beforeAssembly = id.length() - asmId.length();
			int comma = FindPrevious(id, beforeAssembly, ',');
			arrIdx = comma;
		}

		assertFalse(arrIdx == -1);
		return insert(id, arrIdx, arrayMarker);
	}
}