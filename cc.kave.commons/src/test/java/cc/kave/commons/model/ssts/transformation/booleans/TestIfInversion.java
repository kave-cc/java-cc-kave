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
package cc.kave.commons.model.ssts.transformation.booleans;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.ifElseBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.ifElseExpr;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;

public class TestIfInversion extends BooleanNormalizationVisitorBaseTest {
	
	//@formatter:off
	/* |‾‾‾‾‾‾‾‾‾‾|      |‾‾‾‾‾‾‾‾‾‾|
	   | if(!x)   |      | if(x)    |
	   |   stmt0; |  =>  |   stmt1; |
	   | else     |      | else     |
	   |   stmt1; |      |   stmt0; |
	   |__________|      |__________|*/
	//@formatter:on
	@Test
	public void testNegatedIfCondition() {
		List<IStatement> cond = not(a);
		List<IStatement> thenBefore = list(stmt0);
		List<IStatement> elseBefore = list(stmt1);

		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(cond);
		toNormalize.add(ifElseBlock(mainCondition(cond), thenBefore, elseBefore));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(cond);
		expected.add(ifElseBlock(a, elseBefore, thenBefore));

		assertTransformation(toNormalize, expected);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !a ? b : c --> a ? c : b |
	// |__________________________|
	@Test
	public void testNegatedIfExprCondition() {
		List<IStatement> cond = not(a);

		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(cond);
		toNormalize.add(assign(v0, ifElseExpr(mainCondition(cond), b, c)));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(cond);
		expected.add(assign(v0, ifElseExpr(a, c, b)));

		assertTransformation(toNormalize, expected);
	}
}
