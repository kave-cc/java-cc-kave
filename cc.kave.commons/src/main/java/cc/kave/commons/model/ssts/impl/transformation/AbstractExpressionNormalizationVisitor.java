/*
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
package cc.kave.commons.model.ssts.impl.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public abstract class AbstractExpressionNormalizationVisitor<TContext>
		extends AbstractThrowingNodeVisitor<TContext, IAssignableExpression> {
	@Override
	public IAssignableExpression visit(ISST sst, TContext context) {
		for (IDelegateDeclaration decl : sst.getDelegates()) {
			decl.accept(this, context);
		}
		for (IEventDeclaration decl : sst.getEvents()) {
			decl.accept(this, context);
		}
		for (IFieldDeclaration decl : sst.getFields()) {
			decl.accept(this, context);
		}
		for (IMethodDeclaration decl : sst.getMethods()) {
			decl.accept(this, context);
		}
		for (IPropertyDeclaration decl : sst.getProperties()) {
			decl.accept(this, context);
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IDelegateDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IEventDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IFieldDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IMethodDeclaration decl, TContext context) {
		visit(decl.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IPropertyDeclaration decl, TContext context) {
		visit(decl.getGet(), context);
		visit(decl.getSet(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IVariableDeclaration stmt, TContext context) {
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IAssignment stmt, TContext context) {
		stmt.getReference().accept(this, context);
		IAssignableExpression normalized = stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof Assignment)
			((Assignment) stmt).setExpression(normalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IBreakStatement stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IContinueStatement stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IEventSubscriptionStatement stmt, TContext context) {
		stmt.getReference().accept(this, context);
		IAssignableExpression normalized = stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof EventSubscriptionStatement)
			((EventSubscriptionStatement) stmt).setExpression(normalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IExpressionStatement stmt, TContext context) {
		IAssignableExpression normalized = stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof ExpressionStatement)
			((ExpressionStatement) stmt).setExpression(normalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IGotoStatement stmt, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(ILabelledStatement stmt, TContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IReturnStatement stmt, TContext context) {
		ISimpleExpression normalized = (ISimpleExpression) stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof ReturnStatement)
			((ReturnStatement) stmt).setExpression(normalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IThrowStatement stmt, TContext context) {
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IDoLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IForEachLoop block, TContext context) {
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IForLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getInit(), context);
		visit(block.getStep(), context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseBlock block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getThen(), context);
		visit(block.getElse(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ILockBlock stmt, TContext context) {
		stmt.getReference().accept(this, context);
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ISwitchBlock block, TContext context) {
		block.getReference().accept(this, context);
		for (ICaseBlock cb : block.getSections()) {
			ISimpleExpression normalized = (ISimpleExpression) cb.getLabel().accept(this, context);
			if (normalized != null && cb instanceof CaseBlock)
				((CaseBlock) cb).setLabel(normalized);
			visit(cb.getBody(), context);
		}
		visit(block.getDefaultSection(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ITryBlock block, TContext context) {
		visit(block.getBody(), context);
		for (ICatchBlock cb : block.getCatchBlocks()) {
			visit(cb.getBody(), context);
		}
		visit(block.getFinally(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IUncheckedBlock block, TContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IUnsafeBlock block, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IUsingBlock block, TContext context) {
		block.getReference().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IWhileLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ICompletionExpression entity, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IComposedExpression expr, TContext context) {
		for (IVariableReference varRef : expr.getReferences()) {
			varRef.accept(this, context);
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseExpression expr, TContext context) {
		expr.getCondition().accept(this, context);

		ISimpleExpression thenNormalized = (ISimpleExpression) expr.getThenExpression().accept(this, context);
		if (thenNormalized != null && expr instanceof IfElseExpression)
			((IfElseExpression) expr).setThenExpression(thenNormalized);

		ISimpleExpression elseNormalized = (ISimpleExpression) expr.getElseExpression().accept(this, context);
		if (elseNormalized != null && expr instanceof IfElseExpression)
			((IfElseExpression) expr).setElseExpression(elseNormalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IInvocationExpression expr, TContext context) {
		expr.getReference().accept(this, context);

		List<ISimpleExpression> paramsNormalized = expr.getParameters().stream().map(p -> {
			ISimpleExpression pNormalized = (ISimpleExpression) p.accept(this, context);
			return pNormalized != null ? pNormalized : p;
		}).collect(Collectors.toList());
		expr.getParameters().clear();
		expr.getParameters().addAll(paramsNormalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(ILambdaExpression expr, TContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ILoopHeaderBlockExpression expr, TContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IConstantValueExpression expr, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(INullExpression expr, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IReferenceExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(ICastExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IIndexAccessExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		List<ISimpleExpression> indicesNormalized = new ArrayList<ISimpleExpression>();
		for (ISimpleExpression idx : expr.getIndices()) {
			idx.accept(this, context);
			ISimpleExpression idxNormalized = (ISimpleExpression) idx.accept(this, context);
			if (idxNormalized != null) {
				indicesNormalized.add(idxNormalized);
			} else {
				indicesNormalized.add(idx);
			}
		}
		expr.getIndices().clear();
		expr.getIndices().addAll(indicesNormalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(ITypeCheckExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IBinaryExpression expr, TContext context) {
		ISimpleExpression leftOperandNormalized = (ISimpleExpression) expr.getLeftOperand().accept(this, context);
		if (leftOperandNormalized != null && expr instanceof BinaryExpression)
			((BinaryExpression) expr).setLeftOperand(leftOperandNormalized);

		ISimpleExpression rightOperandNormalized = (ISimpleExpression) expr.getRightOperand().accept(this, context);
		if (rightOperandNormalized != null && expr instanceof BinaryExpression)
			((BinaryExpression) expr).setRightOperand(rightOperandNormalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IUnaryExpression expr, TContext context) {
		IAssignableExpression operandNormalized = expr.getOperand().accept(this, context);
		if (operandNormalized instanceof ISimpleExpression && expr instanceof UnaryExpression)
			((UnaryExpression) expr).setOperand((ISimpleExpression) operandNormalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IEventReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IFieldReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IMethodReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IPropertyReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public IAssignableExpression visit(IVariableReference ref, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IIndexAccessReference ref, TContext context) {
		IIndexAccessExpression normalized = (IIndexAccessExpression) ref.getExpression().accept(this, context);
		if (normalized != null && ref instanceof IndexAccessReference)
			((IndexAccessReference) ref).setExpression(normalized);
		return null;
	}

	@Override
	public IAssignableExpression visit(IUnknownReference ref, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IUnknownExpression unknownExpr, TContext context) {
		return null;
	}

	@Override
	public IAssignableExpression visit(IUnknownStatement unknownStmt, TContext context) {
		return null;
	}

	/**
	 * Helper method to normalize list of statements.
	 */
	public void visit(List<IStatement> statements, TContext context) {
		for (IStatement stmt : statements)
			stmt.accept(this, context);
	}
}
