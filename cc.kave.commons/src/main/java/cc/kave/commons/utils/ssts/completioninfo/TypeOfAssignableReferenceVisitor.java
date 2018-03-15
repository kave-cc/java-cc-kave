/**
 * Copyright 2018 University of Zurich
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
package cc.kave.commons.utils.ssts.completioninfo;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

/**
 * Gives you the value type that can be assigned.
 */
public class TypeOfAssignableReferenceVisitor extends AbstractThrowingNodeVisitor<VariableScope<ITypeName>, ITypeName> {

	@Override
	public ITypeName visit(IEventReference ref, VariableScope<ITypeName> vars) {
		return ref.getEventName().getValueType();
	}

	@Override
	public ITypeName visit(IFieldReference ref, VariableScope<ITypeName> vars) {
		return ref.getFieldName().getValueType();
	}

	@Override
	public ITypeName visit(IIndexAccessReference ref, VariableScope<ITypeName> vars) {
		IIndexAccessExpression expr = ref.getExpression();
		String id = expr.getReference().getIdentifier();
		int numIndices = expr.getIndices().size();

		if (!vars.isDeclared(id)) {
			return Names.getUnknownType();
		}

		ITypeName type = vars.get(id);
		if (!type.isArray()) {
			return Names.getUnknownType();
		}
		IArrayTypeName arrType = type.asArrayTypeName();

		int remainingDimensions = arrType.getRank() - numIndices;

		if (remainingDimensions > 0) {
			return Names.newArrayType(remainingDimensions, arrType.getArrayBaseType());
		} else {
			return arrType.getArrayBaseType();
		}
	}

	@Override
	public ITypeName visit(IPropertyReference ref, VariableScope<ITypeName> vars) {
		return ref.getPropertyName().getValueType();
	}

	@Override
	public ITypeName visit(IUnknownReference ref, VariableScope<ITypeName> vars) {
		return Names.getUnknownType();
	}

	@Override
	public ITypeName visit(IVariableReference ref, VariableScope<ITypeName> vars) {
		String id = ref.getIdentifier();
		return vars.isDeclared(id) ? vars.get(id) : Names.getUnknownType();
	}
}