/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.visitors;

import java.util.List;

import cc.kave.commons.model.naming.codeelements.IMethodName;
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
import cc.kave.commons.utils.io.Logger;

public class TraversingVisitor<TContext, TReturn> implements ISSTNodeVisitor<TContext, TReturn> {

	protected void visitStatements(List<IStatement> statements, TContext context) {
		for (IStatement stmt : statements) {
			stmt.accept(this, context);
		}
	}

	@Override
	public TReturn visit(ISST sst, TContext context) {
		sst.getDelegates().forEach((IDelegateDeclaration decl) -> decl.accept(this, context));
		sst.getEvents().forEach((IEventDeclaration decl) -> decl.accept(this, context));
		sst.getFields().forEach((IFieldDeclaration decl) -> decl.accept(this, context));
		sst.getProperties().forEach((IPropertyDeclaration decl) -> decl.accept(this, context));
		sst.getMethods().forEach((IMethodDeclaration decl) -> decl.accept(this, context));
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
	public TReturn visit(IMethodDeclaration stmt, TContext context) {
		visitStatements(stmt.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IPropertyDeclaration stmt, TContext context) {
		visitStatements(stmt.getGet(), context);
		visitStatements(stmt.getSet(), context);
		return null;
	}

	@Override
	public TReturn visit(IVariableDeclaration stmt, TContext context) {
		// TODO
		return stmt.getReference().accept(this, context);
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
	public TReturn visit(IExpressionStatement stmt, TContext context) {
		return stmt.getExpression().accept(this, context);
	}

	@Override
	public TReturn visit(IGotoStatement stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(ILabelledStatement stmt, TContext context) {
		return stmt.getStatement().accept(this, context);
	}

	@Override
	public TReturn visit(IReturnStatement stmt, TContext context) {
		return stmt.getExpression().accept(this, context);
	}

	@Override
	public TReturn visit(IThrowStatement stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IEventSubscriptionStatement stmt, TContext context) {
		stmt.getReference().accept(this, context);
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IDoLoop block, TContext context) {
		visitStatements(block.getBody(), context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IForEachLoop block, TContext context) {
		block.getDeclaration().accept(this, context);
		// TODO
		block.getLoopedReference().accept(this, context);
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IForLoop block, TContext context) {
		visitStatements(block.getInit(), context);
		block.getCondition().accept(this, context);
		visitStatements(block.getStep(), context);
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IIfElseBlock block, TContext context) {
		block.getCondition().accept(this, context);
		visitStatements(block.getThen(), context);
		visitStatements(block.getElse(), context);
		return null;
	}

	@Override
	public TReturn visit(ILockBlock block, TContext context) {
		block.getReference().accept(this, context);
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ISwitchBlock block, TContext context) {
		block.getReference().accept(this, context);

		for (ICaseBlock caseBlock : block.getSections()) {
			caseBlock.getLabel().accept(this, context);
			visitStatements(caseBlock.getBody(), context);
		}

		visitStatements(block.getDefaultSection(), context);

		return null;
	}

	@Override
	public TReturn visit(ITryBlock block, TContext context) {
		visitStatements(block.getBody(), context);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			visitStatements(catchBlock.getBody(), context);
		}

		visitStatements(block.getFinally(), context);

		return null;
	}

	@Override
	public TReturn visit(IUncheckedBlock block, TContext context) {
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IUnsafeBlock block, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IUsingBlock block, TContext context) {
		block.getReference().accept(this, context);
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IWhileLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visitStatements(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ICompletionExpression entity, TContext context) {
		if (entity.getVariableReference() != null) {
			return entity.getVariableReference().accept(this, context);
		}
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
	public TReturn visit(IInvocationExpression entity, TContext context) {
		for (ISimpleExpression simpleExpr : entity.getParameters()) {
			simpleExpr.accept(this, context);
		}

		IMethodName method = entity.getMethodName();

		// protect against isConstructor/getSignature bug in MethodName
		try {
			// static methods and constructors do not have a sensible reference
			if (!method.isStatic() && !method.isConstructor()) {
				return entity.getReference().accept(this, context);
			} else {
				return null;
			}
		} catch (RuntimeException ex) {
			if (ex.getMessage().startsWith("Invalid Signature Syntax")) {
				Logger.err("Skipping receiver reference due to MethodName.getSignature bug");
				return null;
			} else {
				throw ex;
			}
		}
	}

	@Override
	public TReturn visit(ILambdaExpression expr, TContext context) {
		visitStatements(expr.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(ILoopHeaderBlockExpression expr, TContext context) {
		visitStatements(expr.getBody(), context);
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
		return expr.getReference().accept(this, context);
	}

	@Override
	public TReturn visit(IEventReference eventRef, TContext context) {
		return eventRef.getReference().accept(this, context);
	}

	@Override
	public TReturn visit(IFieldReference fieldRef, TContext context) {
		return fieldRef.getReference().accept(this, context);
	}

	@Override
	public TReturn visit(IMethodReference methodRef, TContext context) {
		return methodRef.getReference().accept(this, context);
	}

	@Override
	public TReturn visit(IPropertyReference propertyRef, TContext context) {
		return propertyRef.getReference().accept(this, context);
	}

	@Override
	public TReturn visit(IVariableReference varRef, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IUnknownReference unknownRef, TContext context) {
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

	@Override
	public TReturn visit(ICastExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public TReturn visit(IIndexAccessExpression expr, TContext context) {
		expr.getReference().accept(this, context);
		for (ISimpleExpression simpleExpr : expr.getIndices()) {
			simpleExpr.accept(this, context);
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
	public TReturn visit(IIndexAccessReference indexAccessRef, TContext context) {
		indexAccessRef.getExpression().accept(this, context);
		return null;
	}
}