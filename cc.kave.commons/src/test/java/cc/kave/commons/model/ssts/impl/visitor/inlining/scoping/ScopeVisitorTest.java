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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningBaseTest;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.NameScopeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class ScopeVisitorTest extends InliningBaseTest {

	NameScopeVisitor visitor = new NameScopeVisitor();

	@Test
	public void testSimpleScoping() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		IMethodDeclaration method = declareEntryPoint("m1", declareVar("a"), declareVar("b"), loop);
		Set<IVariableReference> refs = new HashSet<>();
		method.accept(visitor, refs);
		assertThat(refs,
				equalTo(Sets.newHashSet(ref("j"), ref("i"), ref("e"), ref("d"), ref("b"), ref("a"), ref("f2"))));
	}

	@Test
	public void testCompleteMethodScope() {
		Set<IVariableReference> refs = new HashSet<>();
		getExampleMethod().accept(visitor, refs);
		assertThat(refs, equalTo(getExampleReference()));
	}

	private IMethodDeclaration getExampleMethod() {
		return declareEntryPoint("m1", //
				declareVar("a"), //
				forLoop("b", loopHeader(assign(ref("c"), constant("1"))), declareVar("d")), //
				expr(compose(ref("e"), ref("f"))), //
				expr(refExpr("g")), //
				label("label", returnStatement(refExpr("h"), false)), //
				doLoop(loopHeader(expr(constant("true")), declareVar("i"))), //
				forEachLoop("j", "l", assign(ref("m"), constant("1"))), //
				simpleIf(Lists.newArrayList(declareVar("n")), refExpr("o"), declareVar("p")), //
				lockBlock("q", declareVar("r")), //
				switchBlock("s", caseBlock("1", declareVar("t"))), //
				tryBlock(declareVar("u"), declareVar("v"), catchBlock(declareVar("w"))), //
				uncheckedBlock(declareVar("x")), //
				usingBlock(ref("y"), declareVar("z")), //
				whileLoop(loopHeader(expr(constant("true"))), declareVar("ö")), //
				expr(ifElseExpr(refExpr("ä"), refExpr("ü"), refExpr("a1"))), //
				invocationStatement("m1", ref("b1"), refExpr("c1")), //
				expr(lambdaExpr(declareVar("d1"))), //
				assign(refField("k"), constant("1")), //
				expr(completionExpr("e1")), //
				expr(new CompletionExpression()));
	}

	private Set<IVariableReference> getExampleReference() {
		return Sets.newHashSet(ref("a"), ref("b"), ref("c"), ref("d"), ref("e"), ref("f"), ref("g"), ref("h"), ref("i"),
				ref("j"), ref("k"), ref("l"), ref("m"), ref("n"), ref("o"), ref("p"), ref("q"), ref("r"), ref("s"),
				ref("t"), ref("u"), ref("v"), ref("w"), ref("x"), ref("y"), ref("z"), ref("ä"), ref("ü"), ref("ö"),
				ref("a1"), ref("b1"), ref("c1"), ref("d1"), ref("e1"));
	}
}
