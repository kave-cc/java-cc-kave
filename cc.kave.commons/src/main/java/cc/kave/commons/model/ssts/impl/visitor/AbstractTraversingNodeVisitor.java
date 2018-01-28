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
package cc.kave.commons.model.ssts.impl.visitor;

import java.util.List;

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
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

/**
 * extend this base class if you would like to reuse the traversing logic
 */
public class AbstractTraversingNodeVisitor<TContext, TReturn> implements ISSTNodeVisitor<TContext, TReturn> {

	@Override
	public TReturn visit(ISST sst, TContext context) {
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
	public TReturn visit(IDelegateDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IEventDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IFieldDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IMethodDeclaration decl, TContext context) {
		visit(decl.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IPropertyDeclaration decl, TContext context) {
		visit(decl.getGet(), context);
		visit(decl.getSet(), context);
		return null;
	}

	@Override
	public TReturn visit(IVariableDeclaration stmt, TContext context) {
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IAssignment stmt, TContext context) {
		stmt.getReference().accept(this, context);
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IBreakStatement stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IContinueStatement stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IEventSubscriptionStatement stmt, TContext context) {
		stmt.getReference().accept(this, context);
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IExpressionStatement stmt, TContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IGotoStatement stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(ILabelledStatement stmt, TContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IReturnStatement stmt, TContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IThrowStatement stmt, TContext context) {
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IDoLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	protected void visit(List<IStatement> body, TContext context) {
		for (IStatement stmt : body) {
			stmt.accept(this, context);
		}
	}

	@Override
	public TReturn visit(IForEachLoop block, TContext context) {
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IForLoop block, TContext context) {
		visit(block.getInit(), context);
		block.getCondition().accept(this, context);
		visit(block.getStep(), context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IIfElseBlock block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getThen(), context);
		visit(block.getElse(), context);
		return null;
	}

	@Override
	public TReturn visit(ILockBlock stmt, TContext context) {
		stmt.getReference().accept(this, context);
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ISwitchBlock block, TContext context) {
		block.getReference().accept(this, context);
		for (ICaseBlock cb : block.getSections()) {
			cb.getLabel().accept(this, context);
			visit(cb.getBody(), context);
		}
		visit(block.getDefaultSection(), context);
		return null;
	}

	@Override
	public TReturn visit(ITryBlock block, TContext context) {
		visit(block.getBody(), context);
		for (ICatchBlock cb : block.getCatchBlocks()) {
			visit(cb.getBody(), context);
		}
		visit(block.getFinally(), context);
		return null;
	}

	@Override
	public TReturn visit(IUncheckedBlock block, TContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IUnsafeBlock block, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IUsingBlock block, TContext context) {
		block.getReference().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IWhileLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ICompletionExpression entity, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IComposedExpression expr, TContext context) {
		for (IVariableReference varRef : expr.getReferences()) {
			varRef.accept(this, context);
		}
		return null;
	}

	@Override
	public TReturn visit(IIfElseExpression expr, TContext context) {
		expr.getCondition().accept(this, context);
		expr.getThenExpression().accept(this, context);
		expr.getElseExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IInvocationExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		for (ISimpleExpression p : expr.getParameters()) {
			p.accept(this, context);
		}
		return null;
	}

	@Override
	public TReturn visit(ILambdaExpression expr, TContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ILoopHeaderBlockExpression expr, TContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IConstantValueExpression expr, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(INullExpression expr, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IReferenceExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(ICastExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IIndexAccessExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		for (ISimpleExpression idx : expr.getIndices()) {
			idx.accept(this, context);
		}
		return null;
	}

	@Override
	public TReturn visit(ITypeCheckExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IBinaryExpression expr, TContext context) {
		expr.getLeftOperand().accept(this, context);
		expr.getRightOperand().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IUnaryExpression expr, TContext context) {
		expr.getOperand().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IEventReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IFieldReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IMethodReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IPropertyReference ref, TContext context) {
		ref.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IVariableReference ref, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IIndexAccessReference ref, TContext context) {
		ref.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IUnknownReference ref, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IUnknownExpression unknownExpr, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IUnknownStatement unknownStmt, TContext context) {
		return null;
	}
}