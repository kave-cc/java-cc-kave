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

import java.util.Set;

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
import cc.kave.commons.model.ssts.impl.references.VariableReference;
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

public class NameScopeVisitor extends AbstractThrowingNodeVisitor<Set<IVariableReference>, Void> {
	@Override
	public Void visit(IAssignment stmt, Set<IVariableReference> context) {
		stmt.getExpression().accept(this, context);
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ISST sst, Set<IVariableReference> context) {
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Set<IVariableReference> context) {
		for (IStatement statement : stmt.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, Set<IVariableReference> context) {
		for (IStatement statement : stmt.getGet())
			statement.accept(this, context);
		for (IStatement statement : stmt.getSet())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Set<IVariableReference> context) {
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, Set<IVariableReference> context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, Set<IVariableReference> context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, Set<IVariableReference> context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, Set<IVariableReference> context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, Set<IVariableReference> context) {
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, Set<IVariableReference> context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		for (IStatement statement : block.getInit())
			statement.accept(this, context);
		for (IStatement statement : block.getStep())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, Set<IVariableReference> context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getElse())
			statement.accept(this, context);
		for (IStatement statement : block.getThen())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, Set<IVariableReference> context) {
		stmt.getReference().accept(this, context);
		for (IStatement statement : stmt.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, Set<IVariableReference> context) {
		block.getReference().accept(this, context);
		for (IStatement statement : block.getDefaultSection())
			statement.accept(this, context);
		for (ICaseBlock caseBlock : block.getSections())
			for (IStatement statement : caseBlock.getBody())
				statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, Set<IVariableReference> context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		for (IStatement statement : block.getFinally())
			statement.accept(this, context);
		for (ICatchBlock catchBlock : block.getCatchBlocks())
			for (IStatement statement : catchBlock.getBody())
				statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, Set<IVariableReference> context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, Set<IVariableReference> context) {
		block.getReference().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, Set<IVariableReference> context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, Set<IVariableReference> context) {
		IVariableReference objectReference = entity.getVariableReference();
		if (objectReference != null)
			objectReference.accept(this, context);
		return null;
		// TODO test
	}

	@Override
	public Void visit(IComposedExpression expr, Set<IVariableReference> context) {
		for (IVariableReference ref : expr.getReferences())
			ref.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, Set<IVariableReference> context) {
		expr.getCondition().accept(this, context);
		expr.getElseExpression().accept(this, context);
		expr.getThenExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, Set<IVariableReference> context) {
		if (entity.getReference() != null)
			entity.getReference().accept(this, context);
		for (ISimpleExpression statement : entity.getParameters())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Set<IVariableReference> context) {
		for (IStatement statement : expr.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, Set<IVariableReference> context) {
		for (IStatement statement : expr.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(INullExpression expr, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Set<IVariableReference> context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, Set<IVariableReference> context) {
		eventRef.getReference().accept(this, context);
		return null;
		// TODO test
	}

	@Override
	public Void visit(IFieldReference fieldRef, Set<IVariableReference> context) {
		fieldRef.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, Set<IVariableReference> context) {
		methodRef.getReference().accept(this, context);
		return null;
		// TODO test
	}

	@Override
	public Void visit(IPropertyReference methodRef, Set<IVariableReference> context) {
		methodRef.getReference().accept(this, context);
		return null;
		// TODO test
	}

	@Override
	public Void visit(IVariableReference varRef, Set<IVariableReference> context) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(varRef.getIdentifier());
		context.add(ref);
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, Set<IVariableReference> context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, Set<IVariableReference> context) {
		return null;
	}

	public Void visit(IEventSubscriptionStatement stmt, Set<IVariableReference> context) {
		stmt.getReference().accept(this, context);
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, Set<IVariableReference> context) {
		expr.getLeftOperand().accept(this, context);
		expr.getRightOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, Set<IVariableReference> context) {
		expr.getOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, Set<IVariableReference> context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, Set<IVariableReference> context) {
		for (ISimpleExpression e : expr.getIndices()) {
			e.accept(this, context);
		}
		expr.getReference().accept(this, context);
		return null;
	}

	public Void visit(IIndexAccessReference indexAccessRef, Set<IVariableReference> context) {
		indexAccessRef.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ITypeCheckExpression expr, Set<IVariableReference> context) {
		expr.getReference().accept(this, context);
		return null;
	}
}
