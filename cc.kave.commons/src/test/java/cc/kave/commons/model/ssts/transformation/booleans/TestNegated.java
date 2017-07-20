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
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainVar;
import static cc.kave.commons.model.ssts.impl.transformation.booleans.BinaryOperatorUtil.getNegated;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;

public class TestNegated extends BooleanNormalizationVisitorBaseTest {
	
	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | not(not(a)) --> a |
	// |___________________|
	@Test
	public void testDoubleNegation() {
		List<IStatement> def0 = not(a);
		List<IStatement> def1 = define(v1, SSTUtil.not(mainCondition(def0)));

		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(def0);
		toNormalize.addAll(def1);

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(def0);
		expected.addAll(define(v1, a));

		assertTransformation(toNormalize, expected);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a && b) --> !a || !b |
	// |_________________________|
	@Test
	public void testNegatedConjunction() {
		testNegatedBinaryLogical(BinaryOperator.And);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | !(a || b) --> !a && !b |
	// |_________________________|
	@Test
	public void testNegatedDisjunction() {
		testNegatedBinaryLogical(BinaryOperator.Or);
	}

	private void testNegatedBinaryLogical(BinaryOperator op) {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> toNormalize = not(binary(a, b, op));
		
		List<IStatement> notA = not(a);
		List<IStatement> notB = not(b);
		
		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.addAll(notA);
		expected.addAll(notB);
		expected.add(assign(mainVar(toNormalize), binExpr(getNegated(op), mainCondition(notA), mainCondition(notB))));

		assertTransformation(toNormalize, expected);
	}
}
