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
package cc.kave.commons.model.ssts.transformation.switchblock;

import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.booleanDeclaration;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.newVar;

import org.junit.Before;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.transformation.switchblock.SwitchBlockNormalizationContext;
import cc.kave.commons.model.ssts.impl.transformation.switchblock.SwitchBlockNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.transformation.StatementNormalizationVisitorBaseTest;

public class SwitchBlockNormalizationVisitorBaseTest
		extends StatementNormalizationVisitorBaseTest<SwitchBlockNormalizationContext> {
	protected SwitchBlock switchBlock;
	protected IVariableReference var;
	IConstantValueExpression label0, label1, label2;
	IVariableReference cond0, cond1, cond2, cond3, cond4;

	@Before
	public void setup() {
		super.setup();
		sut = new SwitchBlockNormalizationVisitor();
		context = new SwitchBlockNormalizationContext();
		var = variableReference("x");
		switchBlock = new SwitchBlock();
		switchBlock.setReference(var);
		setNormalizing(switchBlock);

		label0 = constant("0");
		label1 = constant("1");
		label2 = constant("2");

		cond0 = newVar(0);
		cond1 = newVar(1);
		cond2 = newVar(2);
		cond3 = newVar(3);
		cond4 = newVar(4);
	}

	// ---------------------------- helpers -----------------------------------

	protected void setSections(ICaseBlock... caseBlocks) {
		switchBlock.setSections(Lists.newArrayList(caseBlocks));
	}

	protected void setDefaultSection(IStatement... defaultStatements) {
		switchBlock.setDefaultSection(Lists.newArrayList(defaultStatements));
	}

	protected IfElseBlock ifCond(IVariableReference ref) {
		return ifCond(refExpr(ref));
	}

	protected IfElseBlock ifCond(ISimpleExpression condition) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(condition);
		return ifElse;
	}

	protected void setThen(IfElseBlock ifElse, IStatement... statements) {
		ifElse.setThen(Lists.newArrayList(statements));
	}

	protected void setElse(IfElseBlock ifElse, IStatement... statements) {
		ifElse.setElse(Lists.newArrayList(statements));
	}

	protected IVariableDeclaration dec(IVariableReference ref) {
		return booleanDeclaration(ref);
	}

	protected IBinaryExpression equal(ISimpleExpression label) {
		return binExpr(BinaryOperator.Equal, refExpr(var), label);
	}

}