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

import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.exceptions.MissingVariableException;
import cc.kave.commons.pointsto.analysis.exceptions.UndeclaredVariableException;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.StmtAllocationSite;
import cc.kave.commons.pointsto.analysis.unification.EventSubscriptionAssignment;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.pointsto.analysis.visitors.ScopingVisitor;
import cc.kave.commons.utils.io.Logger;

public class ConstraintGenerationVisitor extends ScopingVisitor<ConstraintGenerationVisitorContext, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, ConstraintGenerationVisitorContext context) {
		context.enterMember(stmt.getName());
		try {
			return super.visit(stmt, context);
		} finally {
			context.leaveMember();
		}
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, ConstraintGenerationVisitorContext context) {
		context.enterMember(stmt.getName());
		try {
			return super.visit(stmt, context);
		} finally {
			context.leaveMember();
		}
	}

	@Override
	public Void visit(IVariableDeclaration stmt, ConstraintGenerationVisitorContext context) {
		super.visit(stmt, context);

		// structs have an implicit constructor call at declaration
		if (stmt.getType().isStructType()) {
			context.getBuilder().allocate(stmt.getReference(), new StmtAllocationSite(stmt));
		}

		return null;
	}

	@Override
	public Void visit(IAssignment stmt, ConstraintGenerationVisitorContext context) {
		context.setLastAssignment(stmt);

		IAssignableReference destRef = stmt.getReference();
		if (destRef instanceof IUnknownReference) {
			Logger.err("Ignoring assignment to unknown reference");
			return null;
		}

		IAssignableExpression srcExpr = stmt.getExpression();
		try {
			if (srcExpr instanceof ISimpleExpression) {
				context.assign(destRef, (ISimpleExpression) srcExpr);
				return null;
			} else if (srcExpr instanceof IIndexAccessExpression) {
				context.assign(destRef, SSTBuilder.indexAccessReference((IIndexAccessExpression) srcExpr));
				return null;
			}

			return super.visit(stmt, context);
		} catch (UndeclaredVariableException | MissingVariableException ex) {
			Logger.err("Failed to process an assignment: {}", ex.getMessage());
			return null;
		}
	}

	@Override
	public Void visit(IInvocationExpression entity, ConstraintGenerationVisitorContext context) {
		IMethodName method = entity.getMethodName();

		if (method.isUnknown()) {
			Logger.err("Ignoring an unknown method");
			return null;
		}

		// protect against isConstructor/getSignature bug in MethodName
		try {
			context.invoke(entity);
		} catch (RuntimeException ex) {
			if (ex.getMessage().startsWith("Invalid Signature Syntax")) {
				Logger.err("Ignoring invocation expression due to MethodName.getSignature bug");
				return null;
			} else {
				throw ex;
			}
		}

		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, ConstraintGenerationVisitorContext context) {
		ILambdaName lambda = expr.getName();
		context.enterScope();
		try {
			for (IParameterName parameter : lambda.getParameters()) {
				context.declareParameter(parameter, expr);
			}

			context.enterLambda(expr);
			try {
				visitStatements(expr.getBody(), context);
			} finally {
				context.leaveLambda();
			}
		} finally {
			context.leaveScope();
		}

		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, ConstraintGenerationVisitorContext context) {
		try {
			super.visit(stmt, context);
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err("Failed to process an expression statement: {}", ex.getMessage());
		}

		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, ConstraintGenerationVisitorContext context) {
		// a flow-insensitive analysis is only interested in adding observers
		if (stmt.getOperation() == EventSubscriptionOperation.Add) {
			IAssignment assignment = new EventSubscriptionAssignment(stmt);
			return assignment.accept(this, context);
		}

		return null;
	}

	@Override
	public Void visit(IForEachLoop block, ConstraintGenerationVisitorContext context) {
		context.enterScope();
		block.getLoopedReference().accept(this, context);
		context.declareVariable(block.getDeclaration());

		try {
			IVariableReference srcRef = block.getLoopedReference();
			IVariableReference destRef = block.getDeclaration().getReference();
			context.assignForEachVariable(destRef, srcRef, this);

			visitStatements(block.getBody(), context);
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err("Failed to process a for each loop: {}", ex.getMessage());
		} finally {
			context.leaveScope();
		}

		return null;
	}

	@Override
	public Void visit(ICastExpression expr, ConstraintGenerationVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);
		if (destRef != null) {
			context.assign(destRef, expr.getReference());
		}

		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, ConstraintGenerationVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);
		if (destRef != null) {
			context.assign(destRef, expr.getThenExpression());
			context.assign(destRef, expr.getElseExpression());
		}

		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, ConstraintGenerationVisitorContext context) {
		context.expressionAllocation(expr);
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, ConstraintGenerationVisitorContext context) {
		context.expressionAllocation(expr);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, ConstraintGenerationVisitorContext context) {
		try {
			context.registerReturnedExpression(stmt.getExpression());
		} catch (MissingVariableException | UndeclaredVariableException e) {
			Logger.err("Failed to process return statement: {}", e.getMessage());
		}
		return null;
	}
}