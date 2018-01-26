/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.inclusion;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ExprAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraphBuilder;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;

public class SimpleExpressionReader extends FailSafeNodeVisitor<ConstraintGraphBuilder, SetVariable> {

	private final ConstraintGraphBuilder builder;
	private final PropertyAsFieldPredicate treatPropertyAsField;

	public SimpleExpressionReader(ConstraintGraphBuilder builder, PropertyAsFieldPredicate treatPropertyAsField) {
		this.builder = builder;
		this.treatPropertyAsField = treatPropertyAsField;
	}

	public SetVariable read(ISimpleExpression expr) {
		return expr.accept(this, builder);
	}

	public SetVariable read(IReference ref) {
		return ref.accept(this, builder);
	}

	public List<SetVariable> read(List<ISimpleExpression> expressions) {
		List<SetVariable> variables = new ArrayList<>(expressions.size());
		for (ISimpleExpression expr : expressions) {
			variables.add(read(expr));
		}
		return variables;
	}

	@Override
	public SetVariable visit(INullExpression expr, ConstraintGraphBuilder context) {
		return null;
	}

	@Override
	public SetVariable visit(IConstantValueExpression expr, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.allocate(temp, new ExprAllocationSite(expr));
		return temp;
	}

	@Override
	public SetVariable visit(IReferenceExpression expr, ConstraintGraphBuilder context) {
		return expr.getReference().accept(this, context);
	}

	@Override
	public SetVariable visit(IUnknownExpression unknownExpr, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.allocate(temp, new ExprAllocationSite(unknownExpr));
		return temp;
	}

	@Override
	public SetVariable visit(IUnknownReference unknownRef, ConstraintGraphBuilder context) {
		return null;
	}

	@Override
	public SetVariable visit(IVariableReference varRef, ConstraintGraphBuilder context) {
		return context.getVariable(varRef);
	}

	@Override
	public SetVariable visit(IEventReference eventRef, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.readMember(temp, eventRef, eventRef.getEventName());
		return temp;
	}

	@Override
	public SetVariable visit(IFieldReference fieldRef, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.readMember(temp, fieldRef, fieldRef.getFieldName());
		return temp;
	}

	@Override
	public SetVariable visit(IIndexAccessReference indexAccessRef, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.readArray(temp, indexAccessRef);
		return temp;
	}

	@Override
	public SetVariable visit(IMethodReference methodRef, ConstraintGraphBuilder context) {
		SetVariable temp = context.createTemporaryVariable();
		context.storeFunction(temp, methodRef);
		return temp;
	}

	@Override
	public SetVariable visit(IPropertyReference propertyRef, ConstraintGraphBuilder context) {
		IPropertyName property = propertyRef.getPropertyName();
		SetVariable temp = context.createTemporaryVariable();

		if (treatPropertyAsField.test(property)) {
			context.readMember(temp, propertyRef, property);
		} else {
			builder.invokeGetProperty(temp, propertyRef);
		}

		return temp;
	}
}