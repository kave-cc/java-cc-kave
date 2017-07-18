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
 * distributed under the License instanceof distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.naming.impl.v0.types;

import static cc.kave.commons.assertions.Asserts.assertFalse;
import static cc.kave.commons.assertions.Asserts.assertTrue;
import static cc.kave.commons.model.naming.impl.v0.NameUtils.ParseTypeParameterList;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;
import static cc.kave.commons.utils.StringUtils.FindPrevious;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public abstract class BaseTypeName extends BaseName implements ITypeName {

	public static final String UnknownTypeIdentifier = "?";
	public static final String PrefixEnum = "e:";
	public static final String PrefixInterface = "i:";
	public static final String PrefixStruct = "s:";
	public static final String PrefixDelegate = "d:";

	protected BaseTypeName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return UnknownTypeIdentifier.equals(identifier);
	}

	@Override
	public abstract String getName();

	@Override
	public abstract String getFullName();

	@Override
	public abstract IAssemblyName getAssembly();

	@Override
	public abstract INamespaceName getNamespace();

	@Override
	public abstract boolean isNestedType();

	@Override
	public abstract ITypeName getDeclaringType();

	@Override
	public boolean isVoidType() {
		return false;
	}

	@Override
	public boolean isSimpleType() {
		return false;
	}

	@Override
	public boolean isNullableType() {
		return identifier.startsWith("s:System.Nullable`1[[");
	}

	@Override
	public boolean isValueType() {
		return isStructType() || isEnumType();
	}

	@Override
	public boolean isReferenceType() {
		return isClassType() || isInterfaceType() || isArray() || isDelegateType();
	}

	@Override
	public boolean isClassType() {
		return !isValueType() && !isInterfaceType() && !isArray() && !isDelegateType() && !isUnknown();
	}

	@Override
	public boolean isEnumType() {
		return !isArray() && identifier.startsWith(PrefixEnum);
	}

	@Override
	public boolean isInterfaceType() {
		return !isArray() && identifier.startsWith(PrefixInterface);
	}

	@Override
	public boolean isStructType() {
		return !isArray() && identifier.startsWith(PrefixStruct);
	}

	@Override
	public boolean hasTypeParameters() {
		return getTypeParameters().size() > 0;
	}

	private List<ITypeParameterName> _typeParameters;

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		if (_typeParameters == null) {
			assertFalse(isDelegateType());
			int close = FindPrevious(getFullName(), getFullName().length() - 1, '+', ']');
			if (isArray() || close == -1 || getFullName().charAt(close) == '+') {
				_typeParameters = Lists.newLinkedList();
			} else {
				int open = FindCorrespondingOpenBracket(getFullName(), close);
				_typeParameters = ParseTypeParameterList(getFullName(), open, close);
			}
		}
		return _typeParameters;
	}

	@Override
	public boolean isDelegateType() {
		return this instanceof IDelegateTypeName;
	}

	@Override
	public boolean isArray() {
		return this instanceof IArrayTypeName;
	}

	@Override
	public boolean isTypeParameter() {
		return this instanceof ITypeParameterName;
	}

	@Override
	public boolean isPredefined() {
		return this instanceof IPredefinedTypeName;
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		assertTrue(this instanceof IDelegateTypeName);
		return (IDelegateTypeName) this;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		assertTrue(this instanceof IArrayTypeName);
		return (IArrayTypeName) this;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		assertTrue(this instanceof ITypeParameterName);
		return (ITypeParameterName) this;
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		assertTrue(this instanceof IPredefinedTypeName);
		return (IPredefinedTypeName) this;
	}
}