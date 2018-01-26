/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.visitor.inlining.scoping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningBaseTest;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnsVisitor;

public class CountReturnVisitorTest extends InliningBaseTest {

	@Test
	public void testReturnStatementCount() {
		IMethodDeclaration method = declareEntryPoint("m1", //
				declareVar("a"), //
				forLoop("i", loopHeader(expr(constant("true"))), //
						simpleIf(Lists.newArrayList(returnStatement(constant("5"), false)), constant("true"),
								returnStatement(constant("5"), false))),
				simpleIf(Lists.newArrayList(returnStatement(constant("6"), false)), constant("true"),
						returnStatement(constant("7"), true)));
		assertReturns(method, 4);
	}

	@Test
	public void testLoopConditionReturn() {
		IMethodDeclaration method = declareEntryPoint("m1", //
				forLoop("i", loopHeader(returnStatement(refExpr("a"), false))));
		assertReturns(method, 0);
	}

	private void assertReturns(IMethodDeclaration method, int count) {
		CountReturnContext context = new CountReturnContext();
		method.accept(new CountReturnsVisitor(), context);
		assertThat(count, equalTo(context.returnCount));
	}
}
