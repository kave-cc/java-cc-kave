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

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class DelegateTypeName extends BaseTypeName implements IDelegateTypeName {

	private static final String UnknownDelegateIdentifier = "d:[?] [?].()";

	public DelegateTypeName() {
		this(UnknownDelegateIdentifier);
	}

	public DelegateTypeName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return UnknownDelegateIdentifier.equals(getIdentifier());
	}

	@Override
	public String getName() {
		return getDelegateType().getName();
	}

	@Override
	public String getFullName() {
		return getDelegateType().getFullName();
	}

	private IMethodName delegateMethod;

	private IMethodName getDelegateMethod() {
		if (delegateMethod == null) {
			delegateMethod = new MethodName(getIdentifier().substring(PrefixDelegate.length()));
		}
		return delegateMethod;
	}

	@Override
	public ITypeName getDelegateType() {
		return getDelegateMethod().getDeclaringType();
	}

	@Override
	public boolean isNestedType() {
		return getDelegateType().isNestedType();
	}

	@Override
	public ITypeName getDeclaringType() {
		return getDelegateType().getDeclaringType();
	}

	@Override
	public INamespaceName getNamespace() {
		return getDelegateType().getNamespace();
	}

	@Override
	public IAssemblyName getAssembly() {
		return getDelegateType().getAssembly();
	}

	@Override
	public boolean hasParameters() {
		return getDelegateMethod().hasParameters();
	}

	@Override
	public boolean isRecursive() {
		if (getDelegateType().isUnknown()) {
			return false;
		}
		boolean hasRecursiveReturn = getDelegateMethod().getReturnType().getIdentifier()
				.contains(getDelegateType().getIdentifier());
		return hasRecursiveReturn || hasRecursiveParam();
	}

	private boolean hasRecursiveParam() {
		String delegateId = getDelegateType().getIdentifier();
		for (IParameterName p : getDelegateMethod().getParameters()) {
			if (p.getIdentifier().contains(delegateId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IParameterName> getParameters() {
		List<IParameterName> ps = Lists.newLinkedList();
		for (IParameterName p : getDelegateMethod().getParameters()) {
			boolean isRecursive = !p.getValueType().isUnknown()
					&& p.getIdentifier().contains(getDelegateType().getIdentifier());
			if (isRecursive) {
				String id = p.getIdentifier().replace(getDelegateType().getIdentifier(), getIdentifier());
				ps.add(Names.newParameter(id));
			} else {
				ps.add(p);
			}
		}
		return ps;
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		return getDelegateType().getTypeParameters();
	}

	@Override
	public ITypeName getReturnType() {
		ITypeName rt = getDelegateMethod().getReturnType();

		// simple case
		if (rt.isUnknown() || !rt.getIdentifier().contains(getDelegateType().getIdentifier())) {
			return rt;
		}

		// recursive case
		String nrtId = rt.getIdentifier().replace(getDelegateType().getIdentifier(), getIdentifier());
		return TypeUtils.createTypeName(nrtId);
	}

	public static boolean isDelegateTypeNameIdentifier(String identifier) {
		if (TypeUtils.isUnknownTypeIdentifier(identifier)) {
			return false;
		}
		boolean startsWithD = identifier.startsWith(PrefixDelegate);
		boolean isArrayTypeNameIdentifier = ArrayTypeName.isArrayTypeNameIdentifier(identifier);
		return startsWithD && !isArrayTypeNameIdentifier;
	}
}