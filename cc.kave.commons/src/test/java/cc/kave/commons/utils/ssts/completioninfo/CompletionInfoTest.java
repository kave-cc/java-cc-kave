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

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.utils.ssts.SSTUtils.BOOL;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.assign;
import static cc.kave.commons.utils.ssts.SSTUtils.completionExpr;
import static cc.kave.commons.utils.ssts.SSTUtils.completionStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.varDecl;
import static cc.kave.commons.utils.ssts.SSTUtils.varRef;
import static cc.kave.commons.utils.ssts.completioninfo.CompletionInfo.extractCompletionInfoFrom;

import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling;

public class CompletionInfoTest {

	@BeforeClass
	public static void setupStatic() {
		Assert.assertEquals(ErrorHandling.LOG, CompletionInfo.errorHandlingStrategy);
		CompletionInfo.errorHandlingStrategy = ErrorHandling.THROW;
	}

	@AfterClass
	public static void teardownStatic() {
		CompletionInfo.errorHandlingStrategy = ErrorHandling.LOG;
	}

	@Test(expected = AssertionException.class)
	public void cannotBeCalledWithNull() {
		CompletionInfo.extractCompletionInfoFrom((Context) null);
	}

	@Test
	public void noInfoAvailable() {
		Assert.assertFalse(extractCompletionInfoFrom(new Context()).isPresent());
	}

	@Test
	public void infoAvailable() {
		IStatement stmt = assign(varRef("x"), new CompletionExpression());

		MethodDeclaration md = new MethodDeclaration();
		md.getBody().add(stmt);

		SST sst = new SST();
		sst.getMethods().add(md);

		Context ctx = new Context();
		ctx.setSST(sst);

		Assert.assertTrue(extractCompletionInfoFrom(ctx).isPresent());
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

		ICompletionInfo sut = smokeTest(sst).get();

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
	public void findTargetType_this() {
		IStatement stmt = completionStmt("this", "ge");
		ICompletionInfo sut = assertFound(stmt);
		Assert.assertEquals(Names.newType("ABC, P"), sut.getTriggeredType());
	}

	@Test
	public void findTargetType_baseObject() {
		IStatement stmt = completionStmt("base", "ge");
		ICompletionInfo sut = assertFound(stmt);
		Assert.assertEquals(Names.newType("p:object"), sut.getTriggeredType());
	}

	@Test
	public void findTargetType_base() {
		IStatement stmt = completionStmt("base", "ge");
		ICompletionInfo sut = assertFoundInHierarchy("CDE, P", stmt);
		Assert.assertEquals(Names.newType("CDE, P"), sut.getTriggeredType());
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

	@Test
	public void blocksOpenScopeAndDoNotCrash_properties() {

		PropertyDeclaration pd = new PropertyDeclaration();
		pd.getGet().add(varDecl("x", INT));
		pd.getSet().add(varDecl("x", BOOL));

		SST sst = new SST();
		sst.getProperties().add(pd);

		smokeTest(sst);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_if() {
		IIfElseBlock ie = new IfElseBlock();
		ie.getThen().add(varDecl("x", INT));
		ie.getElse().add(varDecl("x", BOOL));
		smokeTest(ie);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_for() {
		IForLoop f1 = new ForLoop();
		f1.getInit().add(varDecl("x", INT));
		IForLoop f2 = new ForLoop();
		f2.getInit().add(varDecl("x", BOOL));
		smokeTest(f1, f2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_foreach() {
		ForEachLoop f1 = new ForEachLoop();
		f1.setDeclaration(varDecl("x", INT));
		ForEachLoop f2 = new ForEachLoop();
		f2.setDeclaration(varDecl("x", BOOL));
		smokeTest(f1, f2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_do() {
		IDoLoop f1 = new DoLoop();
		f1.getBody().add(varDecl("x", INT));
		IDoLoop f2 = new DoLoop();
		f2.getBody().add(varDecl("x", BOOL));
		smokeTest(f1, f2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_while() {
		IWhileLoop f1 = new WhileLoop();
		f1.getBody().add(varDecl("x", INT));
		IWhileLoop f2 = new WhileLoop();
		f2.getBody().add(varDecl("x", BOOL));
		smokeTest(f1, f2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_try() {
		ITryBlock f1 = new TryBlock();
		f1.getBody().add(varDecl("x", INT));
		f1.getFinally().add(varDecl("x", BOOL));
		smokeTest(f1);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_switch() {

		ISwitchBlock s1 = new SwitchBlock();
		s1.getDefaultSection().add(varDecl("x", INT));
		ISwitchBlock s2 = new SwitchBlock();
		s2.getDefaultSection().add(varDecl("x", BOOL));
		smokeTest(s1, s2);
	}

	@Test(expected = AssertionException.class)
	public void blocksOpenScopeAndDoNotCrash_switch_crashDoubleCase() {

		CaseBlock cb1 = new CaseBlock();
		cb1.getBody().add(varDecl("x", INT));

		CaseBlock cb2 = new CaseBlock();
		cb2.getBody().add(varDecl("x", BOOL));

		ISwitchBlock s = new SwitchBlock();
		s.getSections().add(cb1);
		s.getSections().add(cb2);

		smokeTest(s);
	}

	@Test(expected = AssertionException.class)
	public void blocksOpenScopeAndDoNotCrash_switch_crashCaseAndDefault() {

		CaseBlock cb = new CaseBlock();
		cb.getBody().add(varDecl("x", INT));

		ISwitchBlock s = new SwitchBlock();
		s.getSections().add(cb);
		s.getDefaultSection().add(varDecl("x", BOOL));

		smokeTest(s);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_lock() {

		ILockBlock s1 = new LockBlock();
		s1.getBody().add(varDecl("x", INT));

		ILockBlock s2 = new LockBlock();
		s2.getBody().add(varDecl("x", BOOL));

		smokeTest(s1, s2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_unchecked() {

		IUncheckedBlock s1 = new UncheckedBlock();
		s1.getBody().add(varDecl("x", INT));

		IUncheckedBlock s2 = new UncheckedBlock();
		s2.getBody().add(varDecl("x", BOOL));

		smokeTest(s1, s2);
	}

	@Test
	public void blocksOpenScopeAndDoNotCrash_using() {

		IUsingBlock s1 = new UsingBlock();
		s1.getBody().add(varDecl("x", INT));

		IUsingBlock s2 = new UsingBlock();
		s2.getBody().add(varDecl("x", BOOL));

		smokeTest(s1, s2);
	}

	private static ICompletionInfo assertFound(IStatement... body) {
		TypeHierarchy th = new TypeHierarchy("ABC, P");

		TypeShape ts = new TypeShape();
		ts.setTypeHierarchy(th);

		Context ctx = new Context();
		ctx.setTypeShape(ts);
		ctx.setSST(sst(newType("ABC, P"), body));

		Optional<CompletionInfo> info = extractCompletionInfoFrom(ctx);
		Assert.assertTrue(info.isPresent());
		return info.get();
	}

	private static ICompletionInfo assertFoundInHierarchy(String extTypeId, IStatement... body) {

		TypeHierarchy th = new TypeHierarchy("ABC, P");
		th.setExtends(new TypeHierarchy("CDE, P"));

		TypeShape ts = new TypeShape();
		ts.setTypeHierarchy(th);

		Context ctx = new Context();
		ctx.setTypeShape(ts);
		ctx.setSST(sst(newType("ABC, P"), body));

		Optional<CompletionInfo> info = extractCompletionInfoFrom(ctx);
		Assert.assertTrue(info.isPresent());
		return info.get();
	}

	private static void smokeTest(IStatement... stmts) {
		smokeTest(sst(newType("T, P"), stmts));
	}

	private static Optional<CompletionInfo> smokeTest(ISST sst) {
		Context ctx = new Context();
		ctx.setSST(sst);
		return extractCompletionInfoFrom(ctx);
	}

	private static SST sst(ITypeName encType, IStatement... body) {
		MethodDeclaration md = new MethodDeclaration();
		md.body.addAll(Lists.newArrayList(body));
		SST sst = new SST();
		sst.setEnclosingType(encType);
		sst.getMethods().add(md);
		return sst;
	}
}