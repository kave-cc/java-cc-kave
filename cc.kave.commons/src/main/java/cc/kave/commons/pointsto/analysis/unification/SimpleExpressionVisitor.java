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
package cc.kave.commons.pointsto.analysis.unification;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;
import cc.kave.commons.utils.io.Logger;

class SimpleExpressionVisitor extends FailSafeNodeVisitor<ContextReferencePair, Void> {

	private ReferenceAssignmentHandler referenceAssignmentHandler = new ReferenceAssignmentHandler();

	@Override
	public Void visit(IConstantValueExpression expr, ContextReferencePair context) {
		context.getAnalysisContext().allocate(context.getReference());
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, ContextReferencePair context) {
		IReference srcRef = expr.getReference();

		if (srcRef instanceof IUnknownReference) {
			Logger.err("Ignoring an unknown reference");
		} else {
			referenceAssignmentHandler.setContext(context.getAnalysisContext());
			referenceAssignmentHandler.process(context.getReference(), srcRef);
		}

		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, ContextReferencePair context) {
		// not officially a simple expression but processed similarly 
		referenceAssignmentHandler.setContext(context.getAnalysisContext());
		referenceAssignmentHandler.process(context.getReference(), SSTBuilder.indexAccessReference(expr));
		return null;
	}

	@Override
	public Void visit(INullExpression expr, ContextReferencePair context) {
		// has no impact on the analysis
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, ContextReferencePair context) {
		context.getAnalysisContext().allocate(context.getReference());
		return null;
	}
}
