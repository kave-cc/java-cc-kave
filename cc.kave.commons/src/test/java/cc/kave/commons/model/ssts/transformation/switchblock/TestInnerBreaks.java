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

import static cc.kave.commons.model.ssts.impl.SSTUtil.and;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.breakStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.caseBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.not;
import static cc.kave.commons.model.ssts.impl.SSTUtil.or;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class TestInnerBreaks extends SwitchBlockNormalizationVisitorBaseTest {

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { . |......| if( x == 0 && !c || x == 1 ) |
	// | ..case 0: ... |..=>..| ..stmt0; ................... |
	// | ....if(c) ... |......| else ....................... |
	// | ......break;. |......| ..stmt1; ................... |
	// | ..case 1: ... |......|______________________________|
	// | ....stmt0; .. |
	// | ....break; .. |
	// | ..default: .. |
	// | ....stmt1; .. |
	// |_______________|
	@Test
	public void testInnerBreak0() {
		ISimpleExpression c = label2;
		IfElseBlock innerIfBefore = ifCond(c);
		setThen(innerIfBefore, breakStatement());

		ICaseBlock case0 = caseBlock(label0, innerIfBefore);
		ICaseBlock case1 = caseBlock(label1, stmt0, breakStatement());
		setSections(case0, case1);
		setDefaultSection(stmt1);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, not(c));
		IAssignment assign2 = assign(cond2, and(cond0, cond1));
		IAssignment assign3 = assign(cond3, equal(label1));
		IAssignment assign4 = assign(cond4, or(cond2, cond3));

		IfElseBlock innerIfAfter = ifCond(c);
		setThen(innerIfAfter, stmt0);

		IfElseBlock if1 = ifCond(cond4);
		setThen(if1, stmt0);
		setElse(if1, stmt1);

		setExpected(dec(cond0), assign0, dec(cond1), assign1, dec(cond2), assign2, dec(cond3), assign3, dec(cond4),
				assign4, if1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { . |......| if( x == 0 ) .............. |
	// | ..case 0: ... |......| ..if(c) ................... |
	// | ....if(c) ... |......| ....stmt0; ................ |
	// | ......stmt0;. |..=>..| if( x == 0 && c || x == 1 ) |
	// | ....else .... |......| ..stmt1; .................. |
	// | ......break;. |......| else ...................... |
	// | ..case 1: ... |......| ..stmt2; .................. |
	// | ....stmt1; .. |......|_____________________________|
	// | ....break; .. |
	// | ..default: .. |
	// | ....stmt2; .. |
	// |_______________|
	@Test
	public void testInnerBreak1() {
		ISimpleExpression c = label2;
		IfElseBlock innerIfBefore = ifCond(c);
		setThen(innerIfBefore, stmt0);
		setElse(innerIfBefore, breakStatement());

		ICaseBlock case0 = caseBlock(label0, innerIfBefore);
		ICaseBlock case1 = caseBlock(label1, stmt1, breakStatement());
		setSections(case0, case1);
		setDefaultSection(stmt2);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, and(refExpr(cond0), c));
		IAssignment assign2 = assign(cond2, equal(label1));
		IAssignment assign3 = assign(cond3, or(cond1, cond2));

		IfElseBlock innerIfAfter = ifCond(c);
		setThen(innerIfAfter, stmt0);

		IfElseBlock if0 = ifCond(cond0);
		setThen(if0, innerIfAfter);
		IfElseBlock if1 = ifCond(cond3);
		setThen(if1, stmt1);
		setElse(if1, stmt2);

		setExpected(dec(cond0), assign0, dec(cond1), assign1, if0, dec(cond2), assign2, dec(cond3), assign3, if1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { . |......| if( x == 0 ) ............... |
	// | ..case 0: ... |......| ..if(c) .................... |
	// | ....if(c) ... |..=>..| ....stmt0; ................. |
	// | ......stmt0;. |......| if( x == 0 && c || !x == 0 ) |
	// | ....else .... |......| ..stmt1; ................... |
	// | ......break;. |......|______________________________|
	// | ..default: .. |
	// | ....stmt1; .. |
	// |_______________|
	@Test
	public void testInnerBreakDefaultFallthrough() {
		ISimpleExpression c = label1;
		IfElseBlock innerIfBefore = ifCond(c);
		setThen(innerIfBefore, stmt0);
		setElse(innerIfBefore, breakStatement());

		ICaseBlock case0 = caseBlock(label0, innerIfBefore);
		setSections(case0);
		setDefaultSection(stmt1);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, and(refExpr(cond0), c));
		IAssignment assign2 = assign(cond2, not(cond0));
		IAssignment assign3 = assign(cond3, or(cond1, cond2));

		IfElseBlock innerIfAfter = ifCond(c);
		setThen(innerIfAfter, stmt0);

		IfElseBlock if0 = ifCond(cond0);
		setThen(if0, innerIfAfter);
		IfElseBlock if1 = ifCond(cond3);
		setThen(if1, stmt1);

		setExpected(dec(cond0), assign0, dec(cond1), assign1, if0, dec(cond2), assign2, dec(cond3), assign3, if1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ........ |......| boolean $c = exp; ........... |
	// | ..case 0: .......... |......| if( x == 0 ) { .............. |
	// | ....boolean c = exp; |......| ..boolean c = exp; .......... |
	// | ....if(c) .......... |......| ..if(c) ..................... |
	// | ......stmt0; ....... |..=>..| ....stmt0; .................. |
	// | ....else ........... |......| } ........................... |
	// | ......break; ....... |......| if( x == 0 && $c || !x == 0 ) |
	// | ..default: ......... |......| ..stmt1; .................... |
	// | ....stmt1; ......... |......|_______________________________|
	// |______________________|
	@Ignore("Special case which is not yet handled. (Need to consider scope of condition.)")
	@Test
	public void testInnerBreakConditionScope() {
		IAssignableExpression exp = refExpr(dummyVar(10));
		IVariableReference c = dummyVar(20);
		IAssignment assignC = assign(c, exp);
		IfElseBlock innerIfBefore = ifCond(c);
		setThen(innerIfBefore, stmt0);
		setElse(innerIfBefore, breakStatement());

		ICaseBlock case0 = caseBlock(label0, dec(c), assignC, innerIfBefore);
		setSections(case0);
		setDefaultSection(stmt1);

		IVariableReference c0 = variableReference("$" + c.getIdentifier());
		IAssignment assignC0 = assign(c0, exp);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, and(cond0, c));
		IAssignment assign2 = assign(cond2, not(cond0));
		IAssignment assign3 = assign(cond3, or(cond1, cond2));

		IfElseBlock innerIfAfter = ifCond(c);
		setThen(innerIfAfter, stmt0);

		IfElseBlock if0 = ifCond(cond0);
		setThen(if0, innerIfAfter);
		IfElseBlock if1 = ifCond(cond3);
		setThen(if1, stmt1);

		setExpected(dec(c0), assignC0, dec(cond0), assign0, dec(cond1), assign1, if0, dec(cond2), assign2, dec(cond3),
				assign3, if1);
		assertTransformedSST();
	}
}
