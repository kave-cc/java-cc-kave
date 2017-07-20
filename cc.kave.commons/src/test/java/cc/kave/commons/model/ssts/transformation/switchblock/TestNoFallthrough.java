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
import static cc.kave.commons.model.ssts.impl.SSTUtil.not;

import org.junit.Test;

import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class TestNoFallthrough extends SwitchBlockNormalizationVisitorBaseTest {
	
	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾|
	// | switch(x) { .... |..=>..| stmt; |
	// | ..default: stmt; |......|_______|
	// | } .............. |
	// |__________________|
	@Test
	public void testOnlyDefaultSection() {
		setDefaultSection(stmt0);
		setExpected(stmt0);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |..=>..| if ( ! x == 0 ) |
	// | ..case 0: break;. |......| ..stmt; ....... |
	// | ..default: stmt;. |......|_________________|
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, breakStatement());
		setSections(case0);
		setDefaultSection(stmt0);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, not(cond0));

		IfElseBlock ifElseBlock = ifCond(cond1);
		setThen(ifElseBlock, stmt0);

		setExpected(dec(cond0), assign0, dec(cond1), assign1, ifElseBlock);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if( x == 0 ) |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; ... |
	// | .....break; ..... |......| else ....... |
	// | ..default: stmt1; |......| ..stmt1; ... |
	// | } ............... |......|______________|
	// |___________________|
	@Test
	public void testOneSectionNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0, breakStatement());
		setSections(case0);
		setDefaultSection(stmt1);

		IAssignment assign0 = assign(cond0, equal(label0));
		IfElseBlock ifElseBlock = ifCond(cond0);
		setThen(ifElseBlock, stmt0);
		setElse(ifElseBlock, stmt1);

		setExpected(dec(cond0), assign0, ifElseBlock);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) .... |
	// | ..case 0: stmt0;. |......| ..stmt0; ......... |
	// | .....break; ..... |..=>..| else if ( x == 1 ) |
	// | ..case 1: stmt1;. |......| ..stmt1; ......... |
	// | .....break; ..... |......| else ............. |
	// | ..default: stmt2; |......| ..stmt2; ......... |
	// | } ............... |......|____________________|
	// |___________________|
	@Test
	public void testTwoSectionsNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0, breakStatement());
		ICaseBlock case1 = caseBlock(label1, stmt1, breakStatement());
		setSections(case0, case1);
		setDefaultSection(stmt2);

		IAssignment assign0 = assign(cond0, equal(label0));
		IAssignment assign1 = assign(cond1, equal(label1));

		IfElseBlock ifElseInner = ifCond(cond1);
		setThen(ifElseInner, stmt1);
		setElse(ifElseInner, stmt2);

		IfElseBlock ifElseOuter = ifCond(cond0);
		setThen(ifElseOuter, stmt0);
		setElse(ifElseOuter, dec(cond1), assign1, ifElseInner);

		setExpected(dec(cond0), assign0, ifElseOuter);
		assertTransformedSST();
	}
}
