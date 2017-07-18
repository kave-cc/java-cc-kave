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

import static cc.kave.commons.utils.StringUtils.f;
import static cc.kave.commons.utils.StringUtils.repeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.commons.utils.StringUtils;

public class PredefinedTypeName extends BaseTypeName implements IPredefinedTypeName, IArrayTypeName {

	private static final Map<String, String> IdToFullName = new HashMap<String, String>();

	{
		IdToFullName.put("p:sbyte", "System.SByte");
		IdToFullName.put("p:byte", "System.Byte");
		IdToFullName.put("p:short", "System.Int16");
		IdToFullName.put("p:ushort", "System.UInt16");
		IdToFullName.put("p:int", "System.Int32");
		IdToFullName.put("p:uint", "System.UInt32");
		IdToFullName.put("p:long", "System.Int64");
		IdToFullName.put("p:ulong", "System.UInt64");
		IdToFullName.put("p:char", "System.Char");
		IdToFullName.put("p:float", "System.Single");
		IdToFullName.put("p:double", "System.Double");
		IdToFullName.put("p:bool", "System.Boolean");
		IdToFullName.put("p:decimal", "System.Decimal");

		IdToFullName.put("p:void", "System.Void");

		IdToFullName.put("p:object", "System.Object");
		IdToFullName.put("p:string", "System.String");
	};

	private static final Pattern ValidNameMatcher = Pattern.compile("^p:[a-z]+(\\[,*\\])?$");

	public PredefinedTypeName(String identifier) {
		super(identifier);
		validate(ValidNameMatcher.matcher(identifier).matches(), f("invalid id '%s'", identifier));
		validate(!isArray() || !getArrayBaseType().isVoidType(), "impossible to create void array");
		String baseId = isArray() ? getArrayBaseType().getIdentifier() : getIdentifier();
		validate(IdToFullName.containsKey(baseId), f("unknown id '%s'", identifier));
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
		return new AssemblyName(f("mscorlib, %s", new AssemblyVersion().getIdentifier()));
	}

	@Override
	public INamespaceName getNamespace() {
		return new NamespaceName("System");
	}

	@Override
	public String getFullName() {
		if (isArray()) {
			return f("%s[%s]", IdToFullName.get(getArrayBaseType().getIdentifier()), repeat(',', getRank() - 1));
		}
		return IdToFullName.get(getIdentifier());
	}

	@Override
	public String getName() {
		return getIdentifier().substring(getIdentifier().indexOf(':') + 1);
	}

	@Override
	public boolean isVoidType() {
		return is("p:void");
	}

	@Override
	public boolean isValueType() {
		return isStructType();
	}

	@Override
	public boolean isSimpleType() {
		return is("p:boolean") || isNumeric();
	}

	private boolean is(String id) {
		return id.equals(identifier);
	}

	private boolean isNumeric() {
		return is("p:decimal") || isIntegral() || isFloatingPoint();
	}

	private boolean isFloatingPoint() {
		return is("p:float") || is("p:double");
	}

	private boolean isIntegral() {
		return is("p:sbyte") || is("p:byte") || is("p:short") || is("p:ushort") || is("p:int") || is("p:uint")
				|| is("p:long") || is("p:ulong") || is("p:char");
	}

	@Override
	public boolean isEnumType() {
		return false;
	}

	@Override
	public boolean isStructType() {
		return !isReferenceType();
	}

	@Override
	public boolean isNullableType() {
		return false;
	}

	@Override
	public boolean isReferenceType() {
		return isArray() || isClassType();
	}

	@Override
	public boolean isClassType() {
		return is("p:object") || is("p:string");
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isNestedType() {
		return false;
	}

	@Override
	public ITypeName getDeclaringType() {
		return null;
	}

	@Override
	public boolean isDelegateType() {
		return false;
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		Asserts.fail("impossible");
		return null;
	}

	@Override
	public boolean isArray() {
		return identifier.endsWith("]");
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		Asserts.assertTrue(isArray());
		return this;
	}

	@Override
	public boolean isTypeParameter() {
		return false;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		Asserts.fail("impossible");
		return null;
	}

	@Override
	public boolean isPredefined() {
		return !isArray();
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		Asserts.assertTrue(isPredefined());
		return this;
	}

	@Override
	public ITypeName getFullType() {
		Asserts.assertTrue(!isArray());
		String id = f("%s%s, %s", isStructType() ? "s:" : "", getFullName(), getAssembly().getIdentifier());
		return new TypeName(id);
	}

	@Override
	public int getRank() {
		int openArr = identifier.indexOf('[');
		int closeArr = identifier.indexOf(']');
		return closeArr - openArr;
	}

	@Override
	public ITypeName getArrayBaseType() {
		int openArr = identifier.indexOf('[');
		return new PredefinedTypeName(identifier.substring(0, openArr));
	}

	public static boolean isPredefinedTypeNameIdentifier(String id) {
		return !StringUtils.isNullOrEmpty(id) && ValidNameMatcher.matcher(id).matches();
	}
}