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
package cc.kave.rsse.calls.analysis;

import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachy;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.isDeclaredInSameType;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.propertyAccess;

import java.util.List;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSite;

public class UsageExtractionVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	private final ITypeShape typeShape;
	private final AbstractObjectToUsageMapper usages;
	private final UsageExtractionAssignmentDefinitionVisitor defVisitor;

	public final List<ILambdaExpression> lambdaQueue;

	public UsageExtractionVisitor(AbstractObjectToUsageMapper usages, ITypeShape typeShape,
			UsageExtractionAssignmentDefinitionVisitor defVisitor, List<ILambdaExpression> lambdaQueue) {
		this.usages = usages;
		this.typeShape = typeShape;
		this.defVisitor = defVisitor;
		this.lambdaQueue = lambdaQueue;
	}

	@Override
	public List<Void> visit(List<IStatement> body, Void context) {
		return super.visit(body, context);
	}

	// declaration

	@Override
	public Void visit(ITryBlock block, Void context) {
		for (ICatchBlock cb : block.getCatchBlocks()) {
			if (cb.getKind() == CatchBlockKind.General) {
				IParameterName parameter = cb.getParameter();
				Usage u = usages.get(parameter);
				ITypeName t = cb.getParameter().getValueType();
				u.type = t;
				u.definition = definedByCatchParameter();
			}
		}
		return super.visit(block, context);
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Void context) {
		Usage q = usages.get(stmt.getReference());
		q.type = stmt.getType();
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, Void context) {
		Usage q = usages.get(stmt.getReference());
		q.definition = stmt.getExpression().accept(defVisitor, null);
		return super.visit(stmt, null);
	}

	// usage

	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		IMethodName m = expr.getMethodName();

		if (isDeclaredInSameType(m, typeShape)) {
			m = findFirstOccurrenceInHierachy(m, typeShape);
		}

		if (!m.isStatic()) {
			IReference r = expr.getReference();
			usages.get(r).getUsageSites().add(call(m));
		}

		int argNum = 0;
		for (ISimpleExpression e : expr.getParameters()) {
			argNum++;
			if (e instanceof IReferenceExpression) {
				IReference r = ((IReferenceExpression) e).getReference();
				UsageSite cs = callParameter(m, argNum);
				usages.get(r).getUsageSites().add(cs);
			}
		}

		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Void context) {
		Usage u = usages.get(expr.getReference());
		IReference ref = expr.getReference();

		if (ref instanceof IFieldReference) {
			IFieldReference fr = (IFieldReference) ref;
			u.usageSites.add(fieldAccess(fr.getFieldName()));
		} else if (ref instanceof IPropertyReference) {
			IPropertyReference pr = (IPropertyReference) ref;
			u.usageSites.add(propertyAccess(pr.getPropertyName()));
		}

		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Void context) {
		lambdaQueue.add(expr);
		return null;
	}
}