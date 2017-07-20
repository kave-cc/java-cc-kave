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
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainVar;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.getNegated;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;

public class TestAbsorption extends BooleanNormalizationVisitorBaseTest {

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | a && (a || b) --> a |
	// |_____________________|
	@Test
	public void testAbsorptionAndRight() {
		testAbsorption(BinaryOperator.And, false);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | (a || b) && a --> a |
	// |_____________________|
	@Test
	public void testAbsorptionAndLeft() {
		testAbsorption(BinaryOperator.And, true);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | a || (a && b) --> a |
	// |_____________________|
	@Test
	public void testAbsorptionOrRight() {
		testAbsorption(BinaryOperator.Or, false);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | (a && b) || a --> a |
	// |_____________________|
	@Test
	public void testAbsorptionOrLeft() {
		testAbsorption(BinaryOperator.Or, true);
	}

	private void testAbsorption(BinaryOperator op, boolean left) {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> innerBinary = binary(a, b, getNegated(op));
		List<IStatement> outerBinary = left ? binary(a, mainCondition(innerBinary), op)
				: binary(mainCondition(innerBinary), a, op);
		
		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(innerBinary);
		toNormalize.addAll(outerBinary);

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.add(assign(mainVar(toNormalize), a));

		assertTransformation(toNormalize, expected);
	}
}
