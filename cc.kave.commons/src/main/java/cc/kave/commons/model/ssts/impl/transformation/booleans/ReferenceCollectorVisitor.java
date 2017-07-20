/**
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation.booleans;

import static cc.kave.commons.model.ssts.impl.transformation.booleans.RefLookup.UNKNOWN;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

public class ReferenceCollectorVisitor extends AbstractTraversingNodeVisitor<ReferenceCollectorContext, MethodLookup> {

	protected MethodLookup methodLookup;

	public ReferenceCollectorVisitor() {
		methodLookup = new MethodLookup();
	}

	/* Get method lookup for whole sst. */
	public MethodLookup visit(ISST sst) {
		return visit(sst, new ReferenceCollectorContext());
	}

	/* Get reference lookup for single method. */
	public RefLookup visit(IMethodDeclaration decl) {
		methodLookup.put(decl, new RefLookup());
		super.visit(decl, new ReferenceCollectorContext(decl, false));
		return methodLookup.get(decl);
	}

	@Override
	public MethodLookup visit(ISST sst, ReferenceCollectorContext context) {
		for (IMethodDeclaration decl : sst.getMethods())
			decl.accept(this, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IMethodDeclaration decl, ReferenceCollectorContext context) {
		methodLookup.put(decl, new RefLookup());
		super.visit(decl, new ReferenceCollectorContext(decl, false));
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IAssignment stmt, ReferenceCollectorContext context) {
		RefLookup refLookup = methodLookup.get(context.getMethod());
		IAssignableReference ref = stmt.getReference();
		IAssignableExpression expr = stmt.getExpression();

		if (ref instanceof IVariableReference) {
			IVariableReference varRef = (IVariableReference) ref;
			/* put unknown expression if already assigned or inside conditional */
			boolean isUnknown = (refLookup.containsKey(varRef) || context.insideConditional());
			IAssignableExpression assignedExpr = isUnknown ? UNKNOWN : expr;
			refLookup.put(varRef, assignedExpr);
			methodLookup.put(context.getMethod(), refLookup);
		}

		return methodLookup;
	}

	@Override
	public MethodLookup visit(IIfElseBlock block, ReferenceCollectorContext context) {
		super.visit(block, new ReferenceCollectorContext(context.getMethod(), true));
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ILockBlock stmt, ReferenceCollectorContext context) {
		super.visit(stmt, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ISwitchBlock block, ReferenceCollectorContext context) {
		super.visit(block, new ReferenceCollectorContext(context.getMethod(), true));
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ITryBlock block, ReferenceCollectorContext context) {
		super.visit(block, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUncheckedBlock block, ReferenceCollectorContext context) {
		super.visit(block, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUnsafeBlock block, ReferenceCollectorContext context) {
		super.visit(block, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUsingBlock block, ReferenceCollectorContext context) {
		super.visit(block, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ICompletionExpression entity, ReferenceCollectorContext context) {
		super.visit(entity, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IComposedExpression expr, ReferenceCollectorContext context) {
		super.visit(expr, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IIfElseExpression expr, ReferenceCollectorContext context) {
		super.visit(expr, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ILoopHeaderBlockExpression expr, ReferenceCollectorContext context) {
		super.visit(expr, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(ITypeCheckExpression expr, ReferenceCollectorContext context) {
		super.visit(expr, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IEventReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IMethodReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IPropertyReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IVariableReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IIndexAccessReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUnknownReference ref, ReferenceCollectorContext context) {
		super.visit(ref, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUnknownExpression unknownExpr, ReferenceCollectorContext context) {
		super.visit(unknownExpr, context);
		return methodLookup;
	}

	@Override
	public MethodLookup visit(IUnknownStatement unknownStmt, ReferenceCollectorContext context) {
		super.visit(unknownStmt, context);
		return methodLookup;
	}

}
