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
package cc.kave.commons.model.ssts.impl.visitor.inlining.util;

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
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
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

public class CountReturnsVisitor extends AbstractThrowingNodeVisitor<CountReturnContext, Void> {

	@Override
	public Void visit(ISST sst, CountReturnContext context) {
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, CountReturnContext context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, CountReturnContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		context.returnCount++;
		context.isVoid = stmt.isVoid();
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, CountReturnContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, CountReturnContext context) {
		visit(block.getBody(), context);
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, CountReturnContext context) {
		visit(block.getInit(), context);
		visit(block.getBody(), context);
		visit(block.getStep(), context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, CountReturnContext context) {
		block.getCondition().accept(this, context);
		visit(block.getElse(), context);
		visit(block.getThen(), context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, CountReturnContext context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, CountReturnContext context) {
		visit(block.getDefaultSection(), context);
		block.getReference().accept(this, context);
		for (ICaseBlock caseBlock : block.getSections())
			visit(caseBlock.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		visit(block.getFinally(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks())
			visit(catchBlock.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		block.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, CountReturnContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, CountReturnContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(INullExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, CountReturnContext context) {
		return null;
	}

	public Void visit(IEventSubscriptionStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, CountReturnContext context) {
		expr.getLeftOperand().accept(this, context);
		expr.getRightOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, CountReturnContext context) {
		expr.getOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, CountReturnContext context) {
		for (ISimpleExpression e : expr.getIndices()) {
			e.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ITypeCheckExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, CountReturnContext context) {
		indexAccessRef.getExpression().accept(this, context);
		return null;
	}

	public void visit(List<IStatement> body, CountReturnContext context) {
		for (IStatement statement : body) {
			statement.accept(this, context);
		}
	}

}
