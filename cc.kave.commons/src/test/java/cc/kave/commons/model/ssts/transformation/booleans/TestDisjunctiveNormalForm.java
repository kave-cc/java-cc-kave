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
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainVar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTUtil;

public class TestDisjunctiveNormalForm extends BooleanNormalizationVisitorBaseTest {

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | (a || b) && c --> (a && c) || (b && c) |
	// |________________________________________|
	@Test
	public void testDisjunctiveNormalFormLeft() {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> toNormalize = and(or(a, b), c);
		List<IStatement> lhs = and(a, c);
		List<IStatement> rhs = and(b, c);
		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.addAll(lhs);
		expected.addAll(rhs);
		expected.add(assign(mainVar(toNormalize), SSTUtil.or(mainCondition(lhs), mainCondition(rhs))));

		assertTransformation(toNormalize, expected);
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | a && (b || c) --> (b && a) || (c && a) |
	// |________________________________________|
	@Test
	public void testDisjunctiveNormalFormRight() {
		/* we create 2 variables before transformation */
		counter -= 2;
		List<IStatement> toNormalize = and(a, or(b, c));
		List<IStatement> lhs = and(b, a);
		List<IStatement> rhs = and(c, a);
		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.addAll(lhs);
		expected.addAll(rhs);
		expected.add(assign(mainVar(toNormalize), SSTUtil.or(mainCondition(lhs), mainCondition(rhs))));

		assertTransformation(toNormalize, expected);
	}

	//@formatter:off
	/* |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	   | (a || b) && (c || d)                              |
	   | --(1)--> (a && (c || d)) || (b && (c || d))       | 
	   | --(2)--> (c && a || d && a) || (c && b || d && b) |
	   | --(3)--> ((c && a || d && a) || c && b) || d && b |
	   |___________________________________________________| 
	   (1) and (2) are intermediary steps, 
	   (3) the actual result 
	*/
	//@formatter:on
	@Test
	public void testDisjunctiveNormalFormCombined() {
		/* we create 3 variables before transformation */
		counter -= 3;
		List<IStatement> lhs = or(a, b);
		List<IStatement> rhs = or(c, d);
		List<IStatement> toNormalize = and(lhs, rhs);

		List<IStatement> def0 = and(a, mainCondition(rhs));
		List<IStatement> def1 = and(c, a);
		List<IStatement> def2 = and(d, a);
		List<IStatement> def3 = and(b, mainCondition(rhs));
		List<IStatement> def4 = and(c, b);
		List<IStatement> def5 = and(d, b);
		List<IStatement> def6 = defineNew(SSTUtil.or(mainCondition(def0), mainCondition(def4)));

		List<IStatement> expected = new ArrayList<IStatement>();
		expected.addAll(toNormalize.subList(0, toNormalize.size() - 1));
		expected.addAll(def1);
		expected.addAll(def2);
		expected.addAll(define(mainVar(def0), SSTUtil.or(mainCondition(def1), mainCondition(def2))));
		expected.addAll(def4);
		expected.addAll(def5);
		expected.addAll(define(mainVar(def3), SSTUtil.or(mainCondition(def4), mainCondition(def5))));
		expected.addAll(def6);
		expected.add(assign(mainVar(toNormalize), SSTUtil.or(mainCondition(def6), mainCondition(def5))));

		assertTransformation(toNormalize, expected);
	}
}
