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
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainVar;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.getNegated;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;

public class TestRelational extends BooleanNormalizationVisitorBaseTest {

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a == b) --> a != b |
	// |______________________|
	@Test
	public void testNegatedEqual() {
		testNegatedBinaryArithmetical(BinaryOperator.Equal);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a != b) --> a == b |
	// |______________________|
	@Test
	public void testNegatedNotEqual() {
		testNegatedBinaryArithmetical(BinaryOperator.NotEqual);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a > b) --> a <= b |
	// |_____________________|
	@Test
	public void testNegatedGreaterThan() {
		testNegatedBinaryArithmetical(BinaryOperator.GreaterThan);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a >= b) --> a < b |
	// |_____________________|
	@Test
	public void testNegatedGreaterThanOrEqual() {
		testNegatedBinaryArithmetical(BinaryOperator.GreaterThanOrEqual);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a < b) --> a >= b |
	// |_____________________|
	@Test
	public void testNegatedLessThan() {
		testNegatedBinaryArithmetical(BinaryOperator.LessThan);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a <= b) --> a > b |
	// |_____________________|
	@Test
	public void testNegatedLessThanOrEqual() {
		testNegatedBinaryArithmetical(BinaryOperator.LessThanOrEqual);
	}

	private void testNegatedBinaryArithmetical(BinaryOperator op) {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> toNormalize = not(binary(a, b, op));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.add(assign(mainVar(toNormalize), binExpr(getNegated(op), a, b)));

		assertTransformation(toNormalize, expected);
	}
}
