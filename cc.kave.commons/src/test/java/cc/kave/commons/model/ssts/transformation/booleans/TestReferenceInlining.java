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

import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;

public class TestReferenceInlining extends BooleanNormalizationVisitorBaseTest {

	// |‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾|
	// | x = a; |......| x = a; |
	// | y = x; |..=>..| y = x; |
	// | z = y; |......| z = x; |
	// |________|......|________|
	// (a references an unknown value, x is assigned only once)
	@Test
	public void testReferenceExpression() {
		List<IStatement> toNormalize = new ArrayList<IStatement>();
		toNormalize.addAll(define(v0, a));
		toNormalize.addAll(define(v1, refExpr(v0)));
		toNormalize.addAll(define(v2, refExpr(v1)));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(define(v0, a));
		expected.addAll(define(v1, refExpr(v0)));
		expected.addAll(define(v2, refExpr(v0)));

		assertTransformation(toNormalize, expected);
	}
	
	// |‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾|
	// | x = 0; |......| x = 0; |
	// | y = x; |..=>..| y = 0; |
	// | z = y; |......| z = 0; |
	// |________|......|________|
	// (x is assigned only once)
	@Test
	public void testConstantInlining() {
		List<IStatement> toNormalize = new ArrayList<IStatement>();
		IConstantValueExpression constant = SSTUtil.constant("0");
		toNormalize.addAll(define(v0, constant));
		toNormalize.addAll(define(v1, refExpr(v0)));
		toNormalize.addAll(define(v2, refExpr(v1)));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(define(v0, constant));
		expected.addAll(define(v1, constant));
		expected.addAll(define(v2, constant));

		assertTransformation(toNormalize, expected);
	}
}
