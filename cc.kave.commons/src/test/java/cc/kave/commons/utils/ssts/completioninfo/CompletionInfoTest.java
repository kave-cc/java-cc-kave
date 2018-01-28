/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.ssts.completioninfo;

import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.assign;
import static cc.kave.commons.utils.ssts.SSTUtils.completionExpr;
import static cc.kave.commons.utils.ssts.SSTUtils.completionStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.varDecl;
import static cc.kave.commons.utils.ssts.SSTUtils.varRef;
import static cc.kave.commons.utils.ssts.completioninfo.CompletionInfo.extractCompletionInfoFrom;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class CompletionInfoTest {

	@Test(expected = AssertionException.class)
	public void cannotBeCalledWithNull() {
		CompletionInfo.extractCompletionInfoFrom(null);
	}

	@Test
	public void noInfoAvailable() {
		SST sst = sst(new ContinueStatement());
		Assert.assertFalse(extractCompletionInfoFrom(sst).isPresent());
	}

	@Test
	public void infoAvailable() {
		IAssignableExpression expr = new CompletionExpression();
		SST sst = sst(assign(varRef("x"), expr));
		Assert.assertTrue(extractCompletionInfoFrom(sst).isPresent());
	}

	@Test
	public void captureCompletionExpr() {
		IAssignableExpression expr = new CompletionExpression();
		ICompletionInfo sut = assertFound(assign(varRef("x"), expr));
		Assert.assertSame(expr, sut.getCompletionExpr());
	}

	@Test
	public void findTargetType_TriggeredOnType() {
		ICompletionInfo sut = assertFound( //
				completionStmt(INT, "ge"));

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_varDecl() {
		ICompletionInfo sut = assertFound( //
				varDecl("i", INT), //
				completionStmt("i", "ge"));

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_varDeclUndefined() {
		ICompletionInfo sut = assertFound( //
				completionStmt("i", "ge"));

		Assert.assertEquals(Names.getUnknownType(), sut.getTriggeredType());
	}

	@Test
	public void findTargetType_methodParam() {
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[p:void] [C,P].M([p:int] x)"));
		md.getBody().add( //
				exprStmt(completionExpr("x", "")));
		SST sst = new SST();
		sst.getMethods().add(md);

		ICompletionInfo sut = extractCompletionInfoFrom(sst).get();

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_foreach() {

		ForEachLoop forEach = new ForEachLoop();
		forEach.setDeclaration(varDecl("i", INT));
		forEach.getBody().add(completionStmt("i", "ge"));

		ICompletionInfo sut = assertFound(forEach);

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_for() {

		ForLoop l = new ForLoop();
		l.getInit().add(varDecl("i", INT));
		l.getBody().add(completionStmt("i", "ge"));

		ICompletionInfo sut = assertFound(l);

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_tryCatch() {

		CatchBlock catchBlock = new CatchBlock();
		// unnamed and general declare ??? as a variable, but this does not matter
		catchBlock.setKind(CatchBlockKind.Default);
		catchBlock.setParameter(Names.newParameter("[p:int] i"));
		catchBlock.getBody().add(completionStmt("i", "ge"));

		TryBlock tryBlock = new TryBlock();
		tryBlock.getCatchBlocks().add(catchBlock);

		ICompletionInfo sut = assertFound(tryBlock);

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_lambdaParam() {

		LambdaExpression lambdaExpr = new LambdaExpression();
		lambdaExpr.setName(Names.newLambda("[?] [?].([p:int] i)"));
		lambdaExpr.getBody().add(completionStmt("i", ""));
		IAssignment stmt = assign(varRef("x"), lambdaExpr);

		ICompletionInfo sut = assertFound(stmt);

		Assert.assertEquals(INT, sut.getTriggeredType());
	}

	@Test
	public void findTargetType_unknown() {
		ICompletionInfo sut = assertFound(completionStmt("i", ""));
		Assert.assertEquals(Names.getUnknownType(), sut.getTriggeredType());
	}

	@Test
	public void completionOnlyToken() {

		ICompletionInfo sut = assertFound( //
				exprStmt(completionExpr((String) null, "ge")));

		Assert.assertFalse(sut.hasExpectedType());
	}

	@Test
	public void completionWithoutExpectation() {

		ICompletionInfo sut = assertFound( //
				exprStmt(completionExpr("i", "ge")));

		Assert.assertFalse(sut.hasExpectedType());
	}

	@Test(expected = AssertionException.class)
	public void completionWithoutExpectation_failWithoutCheck() {

		ICompletionInfo sut = assertFound( //
				exprStmt(completionExpr("i", "ge")));

		sut.getExpectedType();
	}

	@Test
	public void completionWithExpectation() {

		ICompletionInfo sut = assertFound( //
				varDecl("i", INT), //
				assign("i", completionExpr("this", "ge")));

		Assert.assertTrue(sut.hasExpectedType());
		Assert.assertEquals(INT, sut.getExpectedType());
	}

	private static ICompletionInfo assertFound(IStatement... body) {
		SST sst = sst(body);
		Optional<CompletionInfo> info = extractCompletionInfoFrom(sst);
		Assert.assertTrue(info.isPresent());
		return info.get();
	}

	private static SST sst(IStatement... body) {
		MethodDeclaration md = new MethodDeclaration();
		md.setBody(Lists.newArrayList(body));
		SST sst = new SST();
		sst.getMethods().add(md);
		return sst;
	}
}