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

import static cc.kave.commons.utils.StringUtils.f;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.commons.utils.StringUtils;

public class TypeParameterName extends BaseName implements ITypeParameterName, IArrayTypeName {

	public static final String ParameterNameTypeSeparator = " -> ";

	public TypeParameterName(String identifier) {
		super(identifier);
		if (TypeUtils.isUnknownTypeIdentifier(identifier)) {
			throw new ValidationException("must not be unknown");
		}
	}

	@Override
	public boolean isUnknown() {
		return false;
	}

	@Override
	public boolean hasTypeParameters() {
		return false;
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		return Lists.newLinkedList();
	}

	@Override
	public IAssemblyName getAssembly() {
		return new AssemblyName();
	}

	@Override
	public INamespaceName getNamespace() {
		return new NamespaceName();
	}

	@Override
	public ITypeName getDeclaringType() {
		return null;
	}

	@Override
	public String getFullName() {
		return getTypeParameterShortName();
	}

	@Override
	public String getName() {
		return getTypeParameterShortName();
	}

	@Override
	public boolean isVoidType() {
		return false;
	}

	@Override
	public boolean isValueType() {
		return false;
	}

	@Override
	public boolean isSimpleType() {
		return false;
	}

	@Override
	public boolean isEnumType() {
		return false;
	}

	@Override
	public boolean isStructType() {
		return false;
	}

	@Override
	public boolean isNullableType() {
		return false;
	}

	@Override
	public boolean isReferenceType() {
		return isArray();
	}

	@Override
	public boolean isClassType() {
		return false;
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isDelegateType() {
		return false;
	}

	@Override
	public boolean isNestedType() {
		return false;
	}

	@Override
	public boolean isArray() {
		return getTypeParameterShortName().contains("[") && getTypeParameterShortName().contains("]");
	}

	@Override
	public boolean isTypeParameter() {
		return !isArray();
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		Asserts.fail("impossible");
		return null;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		Asserts.assertTrue(isArray());
		return this;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		Asserts.assertTrue(isTypeParameter());
		return this;
	}

	@Override
	public boolean isPredefined() {
		return false;
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		Asserts.fail("impossible");
		return null;
	}

	@Override
	public String getTypeParameterShortName() {
		int endOfTypeParameterName = identifier.indexOf(ParameterNameTypeSeparator);
		return endOfTypeParameterName == -1 ? identifier : identifier.substring(0, endOfTypeParameterName);
	}

	@Override
	public boolean isBound() {
		return identifier.contains(ParameterNameTypeSeparator);
	}

	@Override
	public ITypeName getTypeParameterType() {
		int startOfTypeName = getTypeParameterShortName().length() + ParameterNameTypeSeparator.length();
		return startOfTypeName >= identifier.length() ? new TypeName()
				: TypeUtils.createTypeName(identifier.substring(startOfTypeName));
	}

	@Override
	public int getRank() {
		Asserts.assertTrue(isArray());
		int start = getTypeParameterShortName().indexOf("[");
		int end = getTypeParameterShortName().indexOf("]");
		return end - start;
	}

	@Override
	public ITypeName getArrayBaseType() {
		Asserts.assertTrue(isArray());
		String tpn = identifier.substring(0, getTypeParameterShortName().indexOf("["));
		String rest = identifier.substring(getTypeParameterShortName().length());
		return new TypeParameterName(f("%s%s", tpn, rest));
	}

	private static final Pattern FreeTypeParameterMatcher = Pattern
			.compile("^[^ ,0-9\\[\\](){}<>-][^ ,\\[\\](){}<>-]*$");

	public static boolean isTypeParameterNameIdentifier(String identifier) {
		if (TypeUtils.isUnknownTypeIdentifier(identifier)) {
			return false;
		}
		if (identifier.startsWith("?")) // e.g., unknown arrays
		{
			return false;
		}
		if (identifier.startsWith("p:")) {
			return false;
		}
		int idxArrow = identifier.indexOf(ParameterNameTypeSeparator);
		if (idxArrow != -1) {
			String before = identifier.substring(0, idxArrow);
			return isTypeParameterNameIdentifier(before);
		}

		return FreeTypeParameterMatcher.matcher(identifier).matches() || isTypeParameterArray(identifier);
	}

	private static boolean isTypeParameterArray(String id) {
		id = id.trim();

		int arrClose = id.lastIndexOf(']');
		if (arrClose == -1) {
			return false;
		}
		int cur = arrClose;
		while (cur - 1 > 0 && id.charAt(--cur) == ',') {
		}
		if (id.charAt(cur) != '[') {
			return false;
		}

		String before = id.substring(0, cur);
		if (StringUtils.containsAny(before, " ", ",") || arrClose != id.length() - 1) {
			return false;
		}
		return FreeTypeParameterMatcher.matcher(before.trim()).matches();
	}
}