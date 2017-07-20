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

import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.not;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.switchBlock;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IBreakStatement;

public class SwitchBlockNormalizationVisitor
		extends AbstractStatementNormalizationVisitor<SwitchBlockNormalizationContext> {

	private int createdVariables;

	public SwitchBlockNormalizationVisitor() {
		createdVariables = 0;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, SwitchBlockNormalizationContext context) {
		// normalize inner switch blocks
		super.visit(block, context);

		IVariableReference ref = block.getReference();
		List<ICaseBlock> sections = block.getSections();
		List<IStatement> defaultSection = block.getDefaultSection();

		if (sections.isEmpty())
			return normalizeEmptySwitch(block, context);

		ICaseBlock section = sections.get(0);
		ISimpleExpression label = section.getLabel();

		boolean fallthrough = fallthrough(section);
		boolean fallthroughToDefault = fallthrough && sections.size() == 1;
		boolean fallthroughToCase = fallthrough && !fallthroughToDefault;

		List<IStatement> conditionStatements = conditionDefinition(ref, label, context);
		IReferenceExpression condition = mainCondition(conditionStatements);
		List<IStatement> fallthroughCondition = new ArrayList<IStatement>();

		if (fallthrough)
			fallthroughCondition = fallthroughConditionStatements(section, condition);

		List<ICaseBlock> remainingSections = sections.subList(1, sections.size());
		SwitchBlockNormalizationContext updatedContext = updatedContext(fallthroughCondition, condition, context,
				fallthrough);
		List<IStatement> thenPart = getThenPart(section);
		List<IStatement> remainingPart = getElsePart(remainingSections, defaultSection, ref, updatedContext);

		List<IStatement> ifElseStatements = assembleIfElse(condition, thenPart, remainingPart, fallthrough);
		boolean emptyIf = ifElseStatements.equals(remainingPart);
		List<IStatement> normalized = new ArrayList<IStatement>();

		if (!emptyIf || fallthroughToCase) {
			normalized.addAll(conditionStatements);
			normalized.addAll(fallthroughCondition);
		}
		normalized.addAll(ifElseStatements);
		return normalized;

	}

	private List<IStatement> assembleIfElse(IReferenceExpression condition, List<IStatement> thenPart,
			List<IStatement> remainingPart, boolean fallthrough) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(condition);
		ifElse.setThen(thenPart);

		List<IStatement> statements = new ArrayList<IStatement>();
		statements.add(ifElse);

		/* in case of fall-through, put remaining statements after if-block */
		if (fallthrough) {
			statements.addAll(remainingPart);
		} else {
			ifElse.setElse(remainingPart);
		}

		boolean emptyThen = ifElse.getThen().isEmpty();
		boolean emptyElse = ifElse.getElse().isEmpty();

		/* no if-block necessary if both branches are empty */
		if (emptyThen && emptyElse) {
			statements.remove(0);
		}

		/*
		 * if then-branch is empty but else-branch is not, switch branches and
		 * negate condition
		 */
		else if (emptyThen) {
			List<IStatement> negatedCond = negCond(condition);
			statements.addAll(0, negatedCond);
			ifElse.setCondition(mainCondition(negatedCond));
			ifElse.setThen(ifElse.getElse());
			ifElse.setElse(thenPart);
		}

		return statements;
	}

	private List<IStatement> normalizeEmptySwitch(ISwitchBlock switchBlock, SwitchBlockNormalizationContext context) {
		IReferenceExpression fallthroughCondition = context.getFallthroughCondition();
		List<IReferenceExpression> labelConditions = context.getLabelConditions();
		List<IStatement> defaultStatements = resolveBreakStatements(switchBlock.getDefaultSection());

		boolean emptyDefault = defaultStatements.isEmpty();
		boolean emptyLabels = labelConditions.isEmpty();
		boolean conditionalFallthrough = context.getConditionalFallthrough();

		if (emptyDefault || emptyLabels || !conditionalFallthrough)
			return defaultStatements;

		/*
		 * Handle the special case where we fall through to the default section
		 * under certain conditions. In this case we may neither put the default
		 * part as else branch, nor simply append it to our list of statements.
		 */
		List<IStatement> normalized = new ArrayList<IStatement>();
		ISimpleExpression condition = null;

		/* conjunction of negated expressions */
		for (IReferenceExpression cond : labelConditions) {
			List<IStatement> negatedStmts = negCond(cond);
			IReferenceExpression negatedCondition = mainCondition(negatedStmts);
			normalized.addAll(negatedStmts);
			if (condition == null) {
				condition = negatedCondition;
			} else {
				List<IStatement> andStmts = andCond(condition, negatedCondition);
				normalized.addAll(andStmts);
				condition = mainCondition(andStmts);
			}
		}

		List<IStatement> orStmts = orCond(fallthroughCondition, condition);
		normalized.addAll(orStmts);

		IfElseBlock ifBlock = new IfElseBlock();
		ifBlock.setCondition(mainCondition(orStmts));
		ifBlock.setThen(defaultStatements);
		normalized.add(ifBlock);
		return normalized;
	}

	// -------------------------- then part -----------------------------------

	private List<IStatement> getThenPart(ICaseBlock section) {
		return resolveBreakStatements(section.getBody());
	}

	// -------------------------- else part -----------------------------------

	private List<IStatement> getElsePart(List<ICaseBlock> remainingSections, List<IStatement> defaultSection,
			IVariableReference ref, SwitchBlockNormalizationContext context) {
		return visit(switchBlock(ref, remainingSections, defaultSection), context);
	}

	// -------------------------- condition -----------------------------------

	private List<IStatement> conditionDefinition(IVariableReference ref, ISimpleExpression label,
			SwitchBlockNormalizationContext context) {
		List<IStatement> statements = eqCond(refExpr(ref), label);
		IReferenceExpression condition = mainCondition(statements);
		IReferenceExpression fallthroughCondition = context.getFallthroughCondition();
		context.addLabelCondition(condition);
		if (fallthroughCondition != null) {
			statements.addAll(orCond(fallthroughCondition, condition));
		}
		return statements;
	}

	/**
	 * Collect all conditions under which a fall-through occurs in the given
	 * case block.
	 */
	private List<IStatement> fallthroughConditionStatements(ICaseBlock caseBlock, ISimpleExpression condition) {
		return fallthroughConditionStatements(caseBlock.getBody(), condition);
	}

	private List<IStatement> fallthroughConditionStatements(List<IStatement> statements, ISimpleExpression condition) {
		List<IStatement> res = new ArrayList<IStatement>();
		List<IIfElseBlock> ifBlocks = statements.stream().filter(s -> s instanceof IIfElseBlock)
				.map(s -> (IIfElseBlock) s).collect(Collectors.toList());

		ISimpleExpression orCond = null;
		for (IIfElseBlock ifBlock : ifBlocks) {
			if (generalFallthrough(ifBlock))
				continue;

			ISimpleExpression collectedCondition = collectFallthroughCondition(ifBlock, res);
			if (orCond == null) {
				orCond = collectedCondition;
			} else {
				res.addAll(orCond(orCond, collectedCondition));
				orCond = mainCondition(res);
			}
		}

		if (orCond != null) {
			res.addAll(andCond(condition, orCond));
		}
		return res;
	}

	/**
	 * Collect all conditions under which a fall-through occurs in a given if
	 * block.
	 */
	private ISimpleExpression collectFallthroughCondition(IIfElseBlock ifBlock, List<IStatement> res) {
		ISimpleExpression ifCondition = ifBlock.getCondition();
		List<IStatement> thenPart = ifBlock.getThen();
		List<IStatement> elsePart = ifBlock.getElse();

		ISimpleExpression collectedCondition = null;
		ISimpleExpression condThen = null;
		ISimpleExpression condElse = null;

		if (fallthrough(thenPart)) {
			condThen = collectCondition(thenPart, ifCondition, res);
			collectedCondition = condThen;
		}

		if (fallthrough(elsePart)) {
			res.addAll(negCond(ifCondition));
			condElse = collectCondition(elsePart, mainCondition(res), res);
			collectedCondition = condElse;
		}

		if (condThen != null && condElse != null) {
			res.addAll(orCond(condThen, condElse));
			collectedCondition = mainCondition(res);
		}

		return collectedCondition;
	}

	private ISimpleExpression collectCondition(List<IStatement> statements, ISimpleExpression cond,
			List<IStatement> res) {
		List<IStatement> collected = fallthroughConditionStatements(statements, cond);
		if (!collected.isEmpty()) {
			res.addAll(collected);
			res.addAll(andCond(cond, mainCondition(collected)));
			cond = mainCondition(res);
		}
		return cond;
	}

	// -------------------------- fall-through --------------------------------

	/**
	 * Determine whether we have a fall-through under some condition.
	 */
	private boolean fallthrough(List<IStatement> statements) {
		boolean outerFallthrough = statements.stream().noneMatch(s -> s instanceof IBreakStatement);
		boolean innerFallthrough = true;

		for (IStatement s : statements)
			if (s instanceof IIfElseBlock)
				innerFallthrough |= fallthrough((IIfElseBlock) s);

		return outerFallthrough && innerFallthrough;
	}

	private boolean fallthrough(ICaseBlock section) {
		return fallthrough(section.getBody());
	}

	private boolean fallthrough(IIfElseBlock ifBlock) {
		return fallthrough(ifBlock.getThen()) || fallthrough(ifBlock.getElse());
	}

	/**
	 * Determine whether we have a general fall-through (under *every*
	 * condition).
	 */
	private boolean generalFallthrough(List<IStatement> statements) {
		boolean noOuterBreak = statements.stream().noneMatch(s -> s instanceof IBreakStatement);
		boolean noInnerBreak = true;

		for (IStatement s : statements)
			if (s instanceof IIfElseBlock)
				noInnerBreak &= generalFallthrough((IIfElseBlock) s);

		return noOuterBreak && noInnerBreak;
	}

	private boolean generalFallthrough(IIfElseBlock ifBlock) {
		return generalFallthrough(ifBlock.getThen()) && generalFallthrough(ifBlock.getElse());
	}

	// ------------------------ handle breaks ---------------------------------

	private List<IStatement> resolveBreakStatements(List<IStatement> statements) {
		List<IStatement> resolved = new ArrayList<IStatement>();

		for (IStatement s : statements) {
			if (s instanceof IBreakStatement)
				break;
			else if (s instanceof IfElseBlock) {
				IfElseBlock ifElse = (IfElseBlock) s;
				ifElse.setThen(resolveBreakStatements(ifElse.getThen()));
				ifElse.setElse(resolveBreakStatements(ifElse.getElse()));
				if (!isEmpty(ifElse))
					resolved.add(ifElse);
			} else
				resolved.add(s);
		}
		return resolved;
	}

	// ----------------------- update context ---------------------------------

	private SwitchBlockNormalizationContext updatedContext(List<IStatement> fallthroughCondition,
			IReferenceExpression condition, SwitchBlockNormalizationContext context, boolean fallthrough) {
		boolean noneCollected = fallthroughCondition.isEmpty();
		boolean noConditionNecessary = !fallthrough;
		IReferenceExpression collectedCondition = mainCondition(fallthroughCondition);
		context.updateConditionalFallthrough(fallthrough, !noneCollected);
		context.setFallthroughCondition(noConditionNecessary ? null : noneCollected ? condition : collectedCondition);
		return context;
	}

	// --------------------------- helper -------------------------------------

	private boolean isEmpty(IfElseBlock ifElse) {
		return ifElse.getThen().isEmpty() && ifElse.getElse().isEmpty();
	}
	
	private List<IStatement> newCondition(IAssignableExpression expr) {
		return define(createdVariables++, expr);
	}

	private List<IStatement> binCond(ISimpleExpression lhs, ISimpleExpression rhs, BinaryOperator op) {
		return newCondition(binExpr(op, lhs, rhs));
	}

	private List<IStatement> eqCond(ISimpleExpression lhs, ISimpleExpression rhs) {
		return binCond(lhs, rhs, BinaryOperator.Equal);
	}

	private List<IStatement> orCond(ISimpleExpression lhs, ISimpleExpression rhs) {
		return binCond(lhs, rhs, BinaryOperator.Or);
	}

	private List<IStatement> andCond(ISimpleExpression lhs, ISimpleExpression rhs) {
		return binCond(lhs, rhs, BinaryOperator.And);
	}

	private List<IStatement> negCond(ISimpleExpression cond) {
		return newCondition(not(cond));
	}

}