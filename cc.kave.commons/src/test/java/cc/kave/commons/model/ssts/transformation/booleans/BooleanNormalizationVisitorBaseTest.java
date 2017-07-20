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

import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.transformation.booleans.BooleanNormalizationVisitor;
import cc.kave.commons.model.ssts.impl.transformation.booleans.RefLookup;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.transformation.StatementNormalizationVisitorBaseTest;

public class BooleanNormalizationVisitorBaseTest extends StatementNormalizationVisitorBaseTest<RefLookup> {
	protected int counter;
	protected ISimpleExpression a, b, c, d;
	protected IVariableReference v0, v1, v2, v3;

	@Before
	public void setup() {
		super.setup();
		sut = new BooleanNormalizationVisitor();
		counter = 0;

		a = refExpr("0");
		b = refExpr("1");
		c = refExpr("2");
		d = refExpr("3");

		v0 = dummyVar(0);
		v1 = dummyVar(1);
		v2 = dummyVar(2);
		v3 = dummyVar(3);
	}

	// ---------------------------- helpers -----------------------------------

	protected List<IStatement> defineNew(IAssignableExpression expr) {
		return define(counter++, expr);
	}

	protected List<IStatement> or(ISimpleExpression lhs, ISimpleExpression rhs) {
		return binary(lhs, rhs, BinaryOperator.Or);
	}

	protected List<IStatement> and(ISimpleExpression lhs, ISimpleExpression rhs) {
		return binary(lhs, rhs, BinaryOperator.And);
	}

	protected List<IStatement> and(ISimpleExpression lhs, List<IStatement> rhs) {
		List<IStatement> result = new ArrayList<IStatement>();
		result.addAll(rhs);
		result.addAll(and(lhs, mainCondition(rhs)));
		return result;
	}

	protected List<IStatement> and(List<IStatement> lhs, ISimpleExpression rhs) {
		List<IStatement> result = new ArrayList<IStatement>();
		result.addAll(lhs);
		result.addAll(and(mainCondition(lhs), rhs));
		return result;
	}

	protected List<IStatement> and(List<IStatement> lhs, List<IStatement> rhs) {
		List<IStatement> result = new ArrayList<IStatement>();
		result.addAll(lhs);
		result.addAll(rhs);
		result.addAll(and(mainCondition(lhs), mainCondition(rhs)));
		return result;
	}

	protected List<IStatement> not(ISimpleExpression cond) {
		return defineNew(SSTUtil.not(cond));
	}

	protected List<IStatement> not(List<IStatement> cond) {
		List<IStatement> result = new ArrayList<IStatement>();
		result.addAll(cond);
		result.addAll(not(mainCondition(cond)));
		return result;
	}

	protected List<IStatement> binary(ISimpleExpression lhs, ISimpleExpression rhs, BinaryOperator op) {
		return defineNew(binExpr(op, lhs, rhs));
	}
}