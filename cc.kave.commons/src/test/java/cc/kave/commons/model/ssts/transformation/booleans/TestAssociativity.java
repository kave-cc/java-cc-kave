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
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainVar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;

public class TestAssociativity extends BooleanNormalizationVisitorBaseTest {
	
	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | a || (b || c) --> (a || b) || c |
	// |_________________________________|
	@Test
	public void testToLeftAssociativeOr() {
		testToLeftAssociative(BinaryOperator.Or);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | a && (b && c) --> (a && b) && c |
	// |_________________________________|
	@Test
	public void testToLeftAssociativeAnd() {
		testToLeftAssociative(BinaryOperator.And);
	}

	private void testToLeftAssociative(BinaryOperator op) {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> rhs = binary(b, c, op);
		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(rhs);
		toNormalize.addAll(binary(a, mainCondition(rhs), op));

		List<IStatement> lhsAfter = binary(a, b, op);
		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.addAll(lhsAfter);
		expected.add(assign(mainVar(toNormalize), binExpr(op, mainCondition(lhsAfter), c)));

		assertTransformation(toNormalize, expected);
	}
}
