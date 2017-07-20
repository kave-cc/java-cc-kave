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
package cc.kave.commons.model.ssts.impl.transformation.switchblock;

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
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public abstract class AbstractNodeFinderVisitor extends AbstractThrowingNodeVisitor<Void, Boolean> {

	protected abstract boolean match(ISSTNode node);

	protected boolean ignore(ISSTNode node) {
		return false;
	}

	private Boolean mayFind(ISSTNode node, Void context) {
		return (!ignore(node) && node.accept(this, context));
	}

	private Boolean mayFind(List<IStatement> statements, Void context) {
		for (IStatement s : statements) {
			if (mayFind(s, context))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visit(ISST sst, Void context) {
		for (IDelegateDeclaration decl : sst.getDelegates()) {
			if (mayFind(decl, context))
				return true;
		}
		for (IEventDeclaration decl : sst.getEvents()) {
			if (mayFind(decl, context))
				return true;
		}
		for (IFieldDeclaration decl : sst.getFields()) {
			if (mayFind(decl, context))
				return true;
		}
		for (IMethodDeclaration decl : sst.getMethods()) {
			if (mayFind(decl, context))
				return true;
		}
		for (IPropertyDeclaration decl : sst.getProperties()) {
			if (mayFind(decl, context))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visit(IDelegateDeclaration stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(IEventDeclaration stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(IFieldDeclaration stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(IMethodDeclaration decl, Void context) {
		return match(decl) || mayFind(decl.getBody(), context);
	}

	@Override
	public Boolean visit(IPropertyDeclaration decl, Void context) {
		return match(decl) || mayFind(decl.getGet(), context) || mayFind(decl.getSet(), context);
	}

	@Override
	public Boolean visit(IVariableDeclaration stmt, Void context) {
		return match(stmt) || mayFind(stmt.getReference(), context);
	}

	@Override
	public Boolean visit(IAssignment stmt, Void context) {
		return match(stmt) || mayFind(stmt.getReference(), context) || mayFind(stmt.getExpression(), context);
	}

	@Override
	public Boolean visit(IBreakStatement stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(IContinueStatement stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(IEventSubscriptionStatement stmt, Void context) {
		return match(stmt) || mayFind(stmt.getReference(), context) || mayFind(stmt.getExpression(), context);
	}

	@Override
	public Boolean visit(IExpressionStatement stmt, Void context) {
		return match(stmt) || mayFind(stmt.getExpression(), context);
	}

	@Override
	public Boolean visit(IGotoStatement stmt, Void context) {
		return match(stmt);
	}

	@Override
	public Boolean visit(ILabelledStatement stmt, Void context) {
		return match(stmt) || mayFind(stmt.getStatement(), context);
	}

	@Override
	public Boolean visit(IReturnStatement stmt, Void context) {
		return match(stmt) || mayFind(stmt.getExpression(), context);
	}

	@Override
	public Boolean visit(IThrowStatement stmt, Void context) {
		return match(stmt) || mayFind(stmt.getReference(), context);
	}

	@Override
	public Boolean visit(IDoLoop block, Void context) {
		return match(block) || mayFind(block.getCondition(), context) || mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(IForEachLoop block, Void context) {
		return match(block) || mayFind(block.getDeclaration(), context) || mayFind(block.getLoopedReference(), context)
				|| mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(IForLoop block, Void context) {
		return match(block) || mayFind(block.getInit(), context) || mayFind(block.getCondition(), context)
				|| mayFind(block.getStep(), context) || mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(IIfElseBlock block, Void context) {
		return match(block) || mayFind(block.getCondition(), context) || mayFind(block.getThen(), context)
				|| mayFind(block.getElse(), context);
	}

	@Override
	public Boolean visit(ILockBlock stmt, Void context) {
		return match(stmt) || mayFind(stmt.getReference(), context) || mayFind(stmt.getBody(), context);
	}

	@Override
	public Boolean visit(ISwitchBlock block, Void context) {
		if (match(block) || mayFind(block.getReference(), context))
			return true;
		for (ICaseBlock cb : block.getSections()) {
			if (mayFind(cb.getLabel(), context) || mayFind(cb.getBody(), context))
				return true;
		}
		return mayFind(block.getDefaultSection(), context);
	}

	@Override
	public Boolean visit(ITryBlock block, Void context) {
		if (match(block) || mayFind(block.getBody(), context))
			return true;
		for (ICatchBlock cb : block.getCatchBlocks()) {
			if (mayFind(cb.getBody(), context))
				return true;
		}
		return mayFind(block.getFinally(), context);
	}

	@Override
	public Boolean visit(IUncheckedBlock block, Void context) {
		return match(block) || mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(IUnsafeBlock block, Void context) {
		return match(block);
	}

	@Override
	public Boolean visit(IUsingBlock block, Void context) {
		return match(block) || mayFind(block.getReference(), context) || mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(IWhileLoop block, Void context) {
		return match(block) || mayFind(block.getCondition(), context) || mayFind(block.getBody(), context);
	}

	@Override
	public Boolean visit(ICompletionExpression entity, Void context) {
		return match(entity);
	}

	@Override
	public Boolean visit(IComposedExpression expr, Void context) {
		if (match(expr))
			return true;
		for (IVariableReference varRef : expr.getReferences()) {
			if (mayFind(varRef, context))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visit(IIfElseExpression expr, Void context) {
		if (match(expr))
			return true;
		return mayFind(expr.getCondition(), context) || mayFind(expr.getThenExpression(), context)
				|| mayFind(expr.getElseExpression(), context);
	}

	@Override
	public Boolean visit(IInvocationExpression expr, Void context) {
		if (match(expr) || mayFind(expr.getReference(), context))
			return true;
		for (ISimpleExpression p : expr.getParameters()) {
			if (mayFind(p, context))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visit(ILambdaExpression expr, Void context) {
		return match(expr) || mayFind(expr.getBody(), context);
	}

	@Override
	public Boolean visit(ILoopHeaderBlockExpression expr, Void context) {
		return match(expr) || mayFind(expr.getBody(), context);
	}

	@Override
	public Boolean visit(IConstantValueExpression expr, Void context) {
		return match(expr);
	}

	@Override
	public Boolean visit(INullExpression expr, Void context) {
		return match(expr);
	}

	@Override
	public Boolean visit(IReferenceExpression expr, Void context) {
		return match(expr) || mayFind(expr.getReference(), context);
	}

	@Override
	public Boolean visit(ICastExpression expr, Void context) {
		return match(expr) || mayFind(expr.getReference(), context);
	}

	@Override
	public Boolean visit(IIndexAccessExpression expr, Void context) {
		if (match(expr) || mayFind(expr.getReference(), context))
			return true;
		for (ISimpleExpression idx : expr.getIndices()) {
			if (mayFind(idx, context))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visit(ITypeCheckExpression expr, Void context) {
		return match(expr) || mayFind(expr.getReference(), context);
	}

	@Override
	public Boolean visit(IBinaryExpression expr, Void context) {
		return match(expr) || mayFind(expr.getLeftOperand(), context) || mayFind(expr.getRightOperand(), context);
	}

	@Override
	public Boolean visit(IUnaryExpression expr, Void context) {
		return match(expr) || mayFind(expr.getOperand(), context);
	}

	@Override
	public Boolean visit(IEventReference ref, Void context) {
		return match(ref) || mayFind(ref.getReference(), context);
	}

	@Override
	public Boolean visit(IFieldReference ref, Void context) {
		return match(ref) || mayFind(ref.getReference(), context);
	}

	@Override
	public Boolean visit(IMethodReference ref, Void context) {
		return match(ref) || mayFind(ref.getReference(), context);
	}

	@Override
	public Boolean visit(IPropertyReference ref, Void context) {
		return match(ref) || mayFind(ref.getReference(), context);
	}

	@Override
	public Boolean visit(IVariableReference ref, Void context) {
		return match(ref);
	}

	@Override
	public Boolean visit(IIndexAccessReference ref, Void context) {
		return match(ref) || mayFind(ref.getExpression(), context);
	}

	@Override
	public Boolean visit(IUnknownReference ref, Void context) {
		return match(ref);
	}

	@Override
	public Boolean visit(IUnknownExpression unknownExpr, Void context) {
		return match(unknownExpr);
	}

	@Override
	public Boolean visit(IUnknownStatement unknownStmt, Void context) {
		return match(unknownStmt);
	}

}
