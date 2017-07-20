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
package cc.kave.commons.model.ssts.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.whileLoop;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.TRUE;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.transformation.loops.ForLoopNormalizationVisitor;
import cc.kave.commons.model.ssts.transformation.StatementNormalizationVisitorBaseTest;

public class ForLoopNormalizationTest extends StatementNormalizationVisitorBaseTest<Void> {
	private ForLoop forLoop;

	@Before
	public void setup() {
		super.setup();
		sut = new ForLoopNormalizationVisitor();
		forLoop = new ForLoop();
		setNormalizing(forLoop);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(;;) {} |..=>..| while(true) {} |
	// |____________|......|________________|
	@Test
	public void testEmptyLoop() {
		setCondition(loopHeader());
		setExpected(whileLoop(TRUE));
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(init;;) {} |..=>..| init; ........ |
	// |________________|......| while(true) {} |
	// ........................|________________|
	@Test
	public void testLoopInit() {
		setCondition(loopHeader());
		setInit(stmt0, stmt1);
		setExpected(stmt0, stmt1, whileLoop(TRUE));
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(;;step) {} |..=>..| while(true) { |
	// |________________|......| ..step; ..... |
	// ........................| } ........... |
	// ........................|_______________|
	@Test
	public void testLoopStep() {
		setCondition(loopHeader());
		setStep(stmt0, stmt1);
		setExpected(whileLoop(TRUE, stmt0, stmt1));
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(;;) { |......| while(true) { |
	// | ..body; . |..=>..| ..body; ..... |
	// | } ....... |......| } ........... |
	// |___________|......|_______________|
	@Test
	public void testLoopBody() {
		setCondition(loopHeader());
		setBody(stmt0, stmt1);
		setExpected(whileLoop(TRUE, stmt0, stmt1));
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(init;cond;step) { |......| init; ....... |
	// | ..body .............. |..=>..| while(cond) { |
	// | } ....................|..... | ..body; ..... |
	// |_______________________|......| ..step; ..... |
	// ...............................| } ........... |
	// ...............................|_______________|
	@Test
	public void testSimpleForToWhile() {
		ILoopHeaderExpression condition = constant("0");
		setCondition(condition);
		setInit(stmt0);
		setBody(stmt1);
		setStep(stmt2);

		setExpected(stmt0, whileLoop(condition, stmt1, stmt2));
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(;cond;step) { |......| while(cond) { |
	// | ..continue; ..... |..=>..| ..step; ..... |
	// | } ................|..... | ..continue; . |
	// |___________________|......| } ........... |
	// ...........................|_______________|
	@Test
	public void testContinueInsideLoopBody() {
		ContinueStatement continueStmt = new ContinueStatement();
		setBody(continueStmt);
		setStep(stmt0);
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(list(stmt0, continueStmt));

		setExpected(whileLoop);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(;a;step) { |......| while(a) { .. |
	// | ..if(b) ...... |..=>..| ..if(b) { ... |
	// | ....continue;. |..... | ....step; ... |
	// | } .............|..... | ....continue; |
	// |________________|......| ..} ......... |
	// ........................| ..step;...... |
	// ........................| } ........... |
	// ........................|_______________|
	@Test
	public void testContinueInsideBodyStatement() {
		ContinueStatement continueStmt = new ContinueStatement();
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setThen(list(continueStmt));

		setBody(ifElseBlock);
		setStep(stmt0);

		IfElseBlock ifElseNormalized = new IfElseBlock();
		ifElseNormalized.setThen(list(stmt0, continueStmt));
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(list(ifElseNormalized, stmt0));

		setExpected(whileLoop);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(i0;c0;s0) . |......| i0; ......... |
	// | ..for(i1;c1;s1) |..=>..| while(c0) { . |
	// | ....body; ..... |......| ..i1;.. ..... |
	// |_________________|......| ..while(c1) { |
	// .........................| ....body;.... |
	// .........................| ....s1;...... |
	// .........................| ..}.......... |
	// .........................| ..s0;........ |
	// .........................| } ........... |
	// .........................|_______________|
	@Test
	public void testCascadedForLoops() {
		// inner for loop
		ISimpleExpression innerCondition = constant("0");
		ForLoop innerFor = new ForLoop();
		innerFor.setInit(list(stmt0));
		innerFor.setStep(list(stmt1));
		innerFor.setBody(list(stmt2));
		innerFor.setCondition(innerCondition);

		// outer for loop
		ISimpleExpression outerCondition = constant("1");
		IStatement stmt4 = dummyStatement(4);
		setInit(stmt3);
		setStep(stmt4);
		setBody(innerFor);
		setCondition(outerCondition);

		IWhileLoop innerWhile = whileLoop(innerCondition, stmt2, stmt1);
		IWhileLoop outerWhile = whileLoop(outerCondition, stmt0, innerWhile, stmt4);

		setExpected(stmt3, outerWhile);
		assertTransformedSST();
	}

	// ---------------------------- helpers -----------------------------------

	private void setBody(IStatement... statements) {
		forLoop.setBody(Lists.newArrayList(statements));
	}

	private void setCondition(ILoopHeaderExpression loopHeader) {
		forLoop.setCondition(loopHeader);
	}

	private void setInit(IStatement... statements) {
		forLoop.setInit(Lists.newArrayList(statements));
	}

	private void setStep(IStatement... statements) {
		forLoop.setStep(Lists.newArrayList(statements));
	}

}
