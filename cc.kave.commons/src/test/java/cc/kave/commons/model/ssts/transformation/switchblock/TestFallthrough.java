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

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.breakStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.caseBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.or;

import org.junit.Test;

import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class TestFallthrough extends SwitchBlockNormalizationVisitorBaseTest {
	
	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾|
	// | switch(x) { ..... |..=>..| stmt; |
	// | ..case 0: ....... |......|_______|
	// | ..default: stmt;. |
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionDefaultFallthrough() {
		setSections(new CaseBlock());
		setDefaultSection(stmt0);
		setExpected(stmt0);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ); |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; ..... |
	// | ..default: stmt1; |......| stmt1; ....... |
	// | } ............... |......|________________|
	// |___________________|
	@Test
	public void testNonEmptySectionDefaultFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		setSections(case0);
		setDefaultSection(stmt1);

		IAssignment assign0 = assign(cond0, equal(label0));
		IfElseBlock ifBlock = ifCond(cond0);
		setThen(ifBlock, stmt0);

		setExpected(dec(cond0), assign0, ifBlock, stmt1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 || x == 1); |
	// | ..case 0: ....... |..=>..| ..stmt0; .............. |
	// | ..case 1: stmt0;. |......| else .................. |
	// | ....break; ...... |......| ..stmt1; .............. |
	// | ..default: stmt1; |......|_________________________|
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionFallthrough() {
		ICaseBlock case0 = caseBlock(label0);
		ICaseBlock case1 = caseBlock(label1, stmt0, breakStatement());
		setSections(case0, case1);
		setDefaultSection(stmt1);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, equal(label1));
		IAssignment assign2 = assign(cond2, or(cond0, cond1));

		IfElseBlock ifElse = ifCond(cond2);
		setThen(ifElse, stmt0);
		setElse(ifElse, stmt1);

		setExpected(dec(cond0), assign0, dec(cond1), assign1, dec(cond2), assign2, ifElse);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) ......... |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; .............. |
	// | ..case 1: stmt1;. |......| if ( x == 0 || x == 1 ) |
	// | ....break; ...... |......| ..stmt1; .............. |
	// | ..default: stmt2; |......| else .................. |
	// | } ............... |......| ..stmt2; .............. |
	// |___________________|......|_________________________|
	@Test
	public void testNonEmptySectionFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1, breakStatement());
		setSections(case0, case1);
		setDefaultSection(stmt2);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, equal(label1));
		IAssignment assign2 = assign(cond2, or(cond0, cond1));

		IfElseBlock ifElse0 = ifCond(cond0);
		IfElseBlock ifElse1 = ifCond(cond2);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setElse(ifElse1, stmt2);

		setExpected(dec(cond0), assign0, ifElse0, dec(cond1), assign1, dec(cond2), assign2, ifElse1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) ..................... |
	// | ..case 0: stmt0.. |......| ..stmt0; .......................... |
	// | ..case 1: stmt1;. |......| if ( x == 0 || x == 1 ) ........... |
	// | ..case 2: stmt2;. |..=>..| ..stmt1; .......................... |
	// | ....break; ...... |......| if ( x == 0 || x == 1 || x == 2 ).. |
	// | ..default: stmt3; |......| ..stmt2; .......................... |
	// | } ............... |......| else .............................. |
	// |___________________|......| ..stmt3; .......................... |
	// ...........................|_____________________________________|
	@Test
	public void testMultipleNonEmptySectionsFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1);
		ICaseBlock case2 = caseBlock(label2, stmt2, breakStatement());
		setSections(case0, case1, case2);
		setDefaultSection(stmt3);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, equal(label1));
		IAssignment assign2 = assign(cond2, or(cond0, cond1));
		IAssignment assign3 = assign(cond3, equal(label2));
		IAssignment assign4 = assign(cond4, or(cond2, cond3));

		IfElseBlock ifElse0 = ifCond(cond0);
		IfElseBlock ifElse1 = ifCond(cond2);
		IfElseBlock ifElse2 = ifCond(cond4);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setThen(ifElse2, stmt2);
		setElse(ifElse2, stmt3);

		setExpected(dec(cond0), assign0, ifElse0, dec(cond1), assign1, dec(cond2), assign2, ifElse1, dec(cond3),
				assign3, dec(cond4), assign4, ifElse2);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) ................... |
	// | ..case 0: stmt0.. |......| ..stmt0; ........................ |
	// | ..case 1: stmt1;. |..=>..| if ( x == 0 || x == 1 ) ......... |
	// | ..case 2: stmt2;. |......| ..stmt1; ........................ |
	// | ..default: stmt3; |......| if ( x == 0 || x == 1 || x == 2 ) |
	// | } ............... |......| ..stmt2; ........................ |
	// |___________________|......| stmt3; .......................... |
	// ...........................|___________________________________|
	@Test
	public void testMultipleNonEmptySectionsDefaultFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1);
		ICaseBlock case2 = caseBlock(label2, stmt2);
		setSections(case0, case1, case2);
		setDefaultSection(stmt3);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, equal(label1));
		IAssignment assign2 = assign(cond2, or(cond0, cond1));
		IAssignment assign3 = assign(cond3, equal(label2));
		IAssignment assign4 = assign(cond4, or(cond2, cond3));

		IfElseBlock ifElse0 = ifCond(cond0);
		IfElseBlock ifElse1 = ifCond(cond2);
		IfElseBlock ifElse2 = ifCond(cond4);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setThen(ifElse2, stmt2);

		setExpected(dec(cond0), assign0, ifElse0, dec(cond1), assign1, dec(cond2), assign2, ifElse1, dec(cond3),
				assign3, dec(cond4), assign4, ifElse2, stmt3);
		assertTransformedSST();
	}
}
