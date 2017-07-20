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

import static cc.kave.commons.model.ssts.impl.SSTUtil.and;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.not;
import static cc.kave.commons.model.ssts.impl.SSTUtil.or;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.FALSE;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.TRUE;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.getNegated;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.isLogical;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.isRelational;

import java.util.List;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.transformation.AbstractExpressionNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class ExpressionNormalizationVisitor extends AbstractExpressionNormalizationVisitor<RefLookup> {
	private ReferenceCollectorVisitor referenceCollector;
	private StatementRegistry createdStatements;
	private int createdVariables;

	public ExpressionNormalizationVisitor() {
		referenceCollector = new ReferenceCollectorVisitor();
		createdStatements = new StatementRegistry();
		createdVariables = 0;
	}

	public List<IStatement> clearCreatedStatements() {
		return createdStatements.clearContent();
	}

	private List<IStatement> defineNew(IAssignableExpression expr, RefLookup context) {
		List<IStatement> definition = define(createdVariables++, expr);
		registerNewDeclarations(definition, context);
		return definition;
	}

	private void registerNewDeclarations(List<IStatement> statements, RefLookup context) {
		/* normalize and add newly created statements */
		visit(statements, context);
		createdStatements.addAll(statements);
	}

	@Override
	public IAssignableExpression visit(IMethodDeclaration decl, RefLookup context) {
		RefLookup lookup = referenceCollector.visit(decl);
		visit(decl.getBody(), lookup);
		return null;
	}

	@Override
	public IAssignableExpression visit(IAssignment stmt, RefLookup context) {
		IAssignableExpression normalized = stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof Assignment) {
			Assignment assignment = (Assignment) stmt;
			assignment.setExpression(normalized);
			IAssignableReference ref = assignment.getReference();

			/* update context */
			if (ref instanceof IVariableReference) {
				context.put((IVariableReference) ref, normalized);
			}
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseBlock block, RefLookup context) {
		super.visit(block, context);
		IAssignableExpression expr = context.tryLookup(block.getCondition());
		if (expr instanceof IUnaryExpression) {
			IUnaryExpression unary = (IUnaryExpression) expr;

			/* if condition is negated, remove negation and switch branches */
			if (isNegated(unary) && block instanceof IfElseBlock) {
				IfElseBlock ifElse = (IfElseBlock) block;
				List<IStatement> thenPart = ifElse.getThen();
				List<IStatement> elsePart = ifElse.getElse();
				ifElse.setCondition(unary.getOperand());
				ifElse.setThen(elsePart);
				ifElse.setElse(thenPart);
			}
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseExpression expr, RefLookup context) {
		super.visit(expr, context);
		IAssignableExpression referenced = context.tryLookup(expr.getCondition());
		if (referenced instanceof IUnaryExpression) {
			IUnaryExpression unary = (IUnaryExpression) referenced;

			/* if condition is negated, remove negation and switch parts */
			if (isNegated(unary) && expr instanceof IfElseExpression) {
				IfElseExpression ifElse = (IfElseExpression) expr;
				ISimpleExpression thenPart = ifElse.getThenExpression();
				ISimpleExpression elsePart = ifElse.getElseExpression();
				ifElse.setCondition(unary.getOperand());
				ifElse.setThenExpression(elsePart);
				ifElse.setElseExpression(thenPart);
			}
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IReferenceExpression expr, RefLookup context) {
		IAssignableExpression referencedExpr = context.get(expr.getReference());
		IAssignableExpression normalized = null;

		/* inline constant */
		if (referencedExpr instanceof IConstantValueExpression) {
			normalized = referencedExpr;
		}

		/* inline reference */
		else if (referencedExpr instanceof IReferenceExpression) {
			IReferenceExpression referencedRefExpr = (IReferenceExpression) referencedExpr;
			IAssignableExpression referencedExprNormalized = referencedExpr.accept(this, context);
			if (referencedExprNormalized instanceof IConstantValueExpression) {
				normalized = referencedExprNormalized;
			} else {
				IReference newRef = referencedRefExpr.getReference();
				/* only inline references that are assigned exactly once */
				if (context.isKnown(newRef) && expr instanceof ReferenceExpression) {
					((ReferenceExpression) expr).setReference(newRef);
				}
			}
		}
		return normalized;
	}

	// ----------------------- binary expressions -----------------------------

	@Override
	public IAssignableExpression visit(IBinaryExpression expr, RefLookup context) {
		// normalize operands first
		super.visit(expr, context);
		ISimpleExpression normalized0 = idempotence(expr, context);
		if (normalized0 != null) {
			return normalized0;
		}
		ISimpleExpression normalized1 = constantOperand(expr, context);
		if (normalized1 != null) {
			return normalized1;
		}
		ISimpleExpression normalized2 = absorption(expr, context);
		if (normalized2 != null) {
			return normalized2;
		}
		IBinaryExpression normalized3 = disjunctiveNormalForm(expr, context);
		IBinaryExpression normalized4 = normalized3 == null ? toLeftAssociative(expr, context)
				: toLeftAssociative(normalized3, context);
		return normalized4 != null ? normalized4 : normalized3;
	}

	/**
	 * or(x, x) -> x, and(x, x) -> x
	 */
	private ISimpleExpression idempotence(IBinaryExpression expr, RefLookup context) {
		ISimpleExpression normalized = null;
		if (isDisjunction(expr) || isConjunction(expr)) {
			ISimpleExpression lhs = expr.getLeftOperand();
			ISimpleExpression rhs = expr.getRightOperand();
			IAssignableExpression leftReferenced = context.tryLookup(lhs);
			IAssignableExpression rightReferenced = context.tryLookup(rhs);
			boolean referencedEqual = leftReferenced != null && rightReferenced != null
					&& leftReferenced.equals(rightReferenced);

			if (lhs.equals(rhs) || referencedEqual) {
				normalized = lhs;
			}
		}
		return normalized;
	}

	/**
	 * e.g. or(x, false) -> x, or(x, true) -> true
	 */
	private ISimpleExpression constantOperand(IBinaryExpression expr, RefLookup context) {
		boolean isDisjunction = isDisjunction(expr);
		boolean isConjunction = isConjunction(expr);
		if (!(isDisjunction || isConjunction))
			return null;

		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = context.tryLookup(lhs);
		IAssignableExpression rightReferenced = context.tryLookup(rhs);

		boolean leftTrue = isTrue(lhs) || isTrue(leftReferenced);
		boolean rightTrue = isTrue(rhs) || isTrue(rightReferenced);
		boolean leftFalse = isFalse(lhs) || isFalse(leftReferenced);
		boolean rightFalse = isFalse(rhs) || isFalse(rightReferenced);

		ISimpleExpression normalized = null;
		if (leftTrue) {
			normalized = isDisjunction ? TRUE : rhs;
		} else if (rightTrue) {
			normalized = isDisjunction ? TRUE : lhs;
		} else if (leftFalse) {
			normalized = isDisjunction ? rhs : FALSE;
		} else if (rightFalse) {
			normalized = isDisjunction ? lhs : FALSE;
		}
		return normalized;
	}

	/**
	 * and(x, or(x, y)) -> x, or(x, and(x, y)) -> x
	 */
	private ISimpleExpression absorption(IBinaryExpression expr, RefLookup context) {
		BinaryOperator negatedOp = getNegated(expr.getOperator());

		if (!(isDisjunction(expr) || isConjunction(expr))) {
			return null;
		}
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();

		boolean mayAbsorbLeft = mayAbsorb(lhs, rhs, negatedOp, context);
		boolean mayAbsorbRight = mayAbsorb(rhs, lhs, negatedOp, context);

		ISimpleExpression normalized = mayAbsorbLeft ? rhs : mayAbsorbRight ? lhs : null;
		return normalized;
	}

	private boolean mayAbsorb(ISimpleExpression maybeAbsorbable, ISimpleExpression expr, BinaryOperator op,
			RefLookup context) {
		IAssignableExpression referenced = context.tryLookup(maybeAbsorbable);
		IBinaryExpression binary = (referenced instanceof IBinaryExpression) ? (IBinaryExpression) referenced : null;
		return binary != null && binary.getOperator().equals(op) && containsMember(binary, expr, context);
	}

	/**
	 * Determine whether a given expression is a member of a
	 * disjunctive/conjunctive chain.
	 */
	private boolean containsMember(IBinaryExpression expr, ISimpleExpression member, RefLookup context) {
		BinaryOperator op = expr.getOperator();
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = context.tryLookup(lhs);
		IAssignableExpression rightReferenced = context.tryLookup(rhs);

		if (lhs.equals(member) || rhs.equals(member) || leftReferenced != null && leftReferenced.equals(member)
				|| rightReferenced != null && rightReferenced.equals(member))
			return true;

		if (leftReferenced instanceof IBinaryExpression) {
			IBinaryExpression leftReferencedBinary = (IBinaryExpression) leftReferenced;
			if (leftReferencedBinary.getOperator().equals(op) && containsMember(leftReferencedBinary, member, context))
				return true;
		}
		if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightReferencedBinary = (IBinaryExpression) rightReferenced;
			if (rightReferencedBinary.getOperator().equals(op)
					&& containsMember(rightReferencedBinary, member, context))
				return true;
		}
		return false;

	}

	/**
	 * Convert binary expression into its left associative equivalent.
	 * 
	 * (e.g. or(a, or(b, c)) -> or(or(a, b), c)
	 */
	private IBinaryExpression toLeftAssociative(IBinaryExpression expr, RefLookup context) {
		BinaryOperator op = expr.getOperator();
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression rightReferenced = context.tryLookup(rhs);
		IBinaryExpression normalized = null;

		if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightReferencedBinary = (IBinaryExpression) rightReferenced;
			if (rightReferencedBinary.getOperator().equals(op)) {
				List<IStatement> innerBinary = defineNew(binExpr(op, lhs, rightReferencedBinary.getLeftOperand()),
						context);
				normalized = binExpr(op, mainCondition(innerBinary), rightReferencedBinary.getRightOperand());
			}
		}
		return normalized;
	}

	/**
	 * Convert binary expression into its disjunctive normal form.
	 * 
	 * (e.g. and(a, or(b, c)) -> or(and(a, b), and(a, c))
	 */
	private IBinaryExpression disjunctiveNormalForm(IBinaryExpression expr, RefLookup context) {
		/* already in DNF, or no logical expression --> nothing to do */
		if (!isConjunction(expr))
			return null;

		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = context.tryLookup(lhs);
		IAssignableExpression rightReferenced = context.tryLookup(rhs);
		IBinaryExpression normalized = null;

		if (leftReferenced instanceof IBinaryExpression) {
			IBinaryExpression leftBinary = (IBinaryExpression) leftReferenced;
			if (isDisjunction(leftBinary)) {
				normalized = applyDistributivity(leftBinary, rhs, context);
			}
		}

		else if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightBinary = (IBinaryExpression) rightReferenced;
			if (isDisjunction(rightBinary)) {
				normalized = applyDistributivity(rightBinary, lhs, context);
			}
		}
		return normalized;
	}

	private IBinaryExpression applyDistributivity(IBinaryExpression disjunction, ISimpleExpression simple,
			RefLookup context) {
		List<IStatement> left = defineNew(and(disjunction.getLeftOperand(), simple), context);
		List<IStatement> right = defineNew(and(disjunction.getRightOperand(), simple), context);
		return or(mainCondition(left), mainCondition(right));
	}

	// ------------------------ unary expressions -----------------------------

	@Override
	public IAssignableExpression visit(IUnaryExpression expr, RefLookup context) {
		// normalize operand first
		super.visit(expr, context);
		IAssignableExpression normalized = handleNegation(expr, context);
		return normalized;
	}

	private IAssignableExpression handleNegation(IUnaryExpression unaryExpr, RefLookup context) {
		IAssignableExpression normalized = null;
		if (isNegated(unaryExpr)) {
			IAssignableExpression operandExpr = context.tryLookup(unaryExpr.getOperand());

			if (operandExpr instanceof IUnaryExpression) {
				normalized = handleNegatedUnaryExpression((IUnaryExpression) operandExpr);
			} else if (operandExpr instanceof IBinaryExpression) {
				normalized = handleNegatedBinaryExpression((IBinaryExpression) operandExpr, context);
			}
		}
		return normalized;
	}

	private IAssignableExpression handleNegatedUnaryExpression(IUnaryExpression negatedExpr) {
		/* handle double negation */
		if (isNegated(negatedExpr))
			return negatedExpr.getOperand();
		return null;
	}

	private IAssignableExpression handleNegatedBinaryExpression(IBinaryExpression negatedExpr, RefLookup context) {
		BinaryOperator op = negatedExpr.getOperator();
		BinaryOperator negatedOp = getNegated(op);
		ISimpleExpression lhs = negatedExpr.getLeftOperand();
		ISimpleExpression rhs = negatedExpr.getRightOperand();
		IBinaryExpression result = null;

		if (isLogical(op)) {
			/* apply De Morgan's laws */
			List<IStatement> negatedLeft = defineNew(not(lhs), context);
			List<IStatement> negatedRight = defineNew(not(rhs), context);
			result = binExpr(negatedOp, mainCondition(negatedLeft), mainCondition(negatedRight));
		}

		else if (isRelational(op)) {
			/* negate operator */
			result = binExpr(negatedOp, lhs, rhs);
		}
		return result;
	}

	// ---------------------------- helpers -----------------------------------

	private boolean isNegated(IUnaryExpression unaryExpr) {
		return unaryExpr.getOperator().equals(UnaryOperator.Not);
	}

	private boolean isConjunction(IBinaryExpression expr) {
		return expr.getOperator().equals(BinaryOperator.And);
	}

	private boolean isDisjunction(IBinaryExpression expr) {
		return expr.getOperator().equals(BinaryOperator.Or);
	}

	private boolean isTrue(IAssignableExpression expr) {
		return expr != null && expr.equals(TRUE);
	}

	private boolean isFalse(IAssignableExpression expr) {
		return expr != null && expr.equals(FALSE);
	}
}