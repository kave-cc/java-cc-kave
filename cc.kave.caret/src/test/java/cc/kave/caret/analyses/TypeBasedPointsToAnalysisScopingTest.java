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
package cc.kave.caret.analyses;

import static cc.kave.commons.model.naming.Names.newLambda;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.BOOL;
import static cc.kave.commons.utils.ssts.SSTUtils.CHAR;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.OBJECT;
import static cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling.THROW;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
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
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling;
import cc.kave.testcommons.LoggerAsserts;

public class TypeBasedPointsToAnalysisScopingTest extends PathInsensitivePointToAnalysisTestBase {

	@Before
	public void setup() {
		Logger.setCapturing(true);
		sut = new TypeBasedPointsToAnalysis(THROW);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test(expected = AssertionException.class)
	public void fail_duplicateDeclaration_throw() {
		sut = new TypeBasedPointsToAnalysis(ErrorHandling.THROW);
		runWithDuplicateDeclaration();
	}

	@Test
	public void fail_duplicateDeclaration_log() {
		sut = new TypeBasedPointsToAnalysis(ErrorHandling.LOG);
		runWithDuplicateDeclaration();
		LoggerAsserts.assertLogContains(0,
				"Trying to change the value of 'x' from 'PredefinedTypeName(p:int)' to 'PredefinedTypeName(p:char)'.");

	}

	@Test
	public void fail_duplicateDeclaration_ignore() {
		sut = new TypeBasedPointsToAnalysis(ErrorHandling.IGNORE);
		runWithDuplicateDeclaration();
		assertEquals(new LinkedList<>(), Logger.getCapturedLog());
	}

	private void runWithDuplicateDeclaration() {
		VariableDeclaration x1 = varDecl("x", INT);
		VariableDeclaration x2 = varDecl("x", CHAR);
		md1.body.add(x1);
		md1.body.add(x2);
		addUniqueAOs(x1.getReference(), x2.getReference());
		assertAOs();
	}

	@Test
	public void scope_property() {
		VariableDeclaration d1 = varDecl("o", INT);
		VariableDeclaration d2 = varDecl("o", CHAR);

		PropertyDeclaration pd1 = new PropertyDeclaration();
		pd1.getGet().add(d1);
		sst.properties.add(pd1);

		PropertyDeclaration pd2 = new PropertyDeclaration();
		pd2.getGet().add(d2);
		sst.properties.add(pd2);

		addAO(pd1, pd2);
		addUniqueAOs(d1.getReference(), d2.getReference());
		assertAOs();
	}

	@Test
	public void scope_property_accessors() {
		VariableDeclaration d1 = varDecl("o", INT);
		VariableDeclaration d2 = varDecl("o", CHAR);

		PropertyDeclaration pd1 = new PropertyDeclaration();
		pd1.getGet().add(d1);
		pd1.getSet().add(d2);
		sst.properties.add(pd1);

		addUniqueAOs(pd1, d1.getReference(), d2.getReference());
		assertAOs();
	}

	@Test
	public void scope_method() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		MethodDeclaration md2 = new MethodDeclaration();
		sst.methods.add(md2);

		md1.body.add(varDecl(o1, INT));
		md2.body.add(varDecl(o2, CHAR));

		addUniqueAOs(md2, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_do() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		DoLoop l1 = new DoLoop();
		l1.getBody().add(varDecl(o1, INT));
		DoLoop l2 = new DoLoop();
		l2.getBody().add(varDecl(o2, CHAR));

		md1.body.add(l1);
		md1.body.add(l2);

		addUniqueAOs(o1, o2);
		assertAOs();
	}

	@Test
	public void scope_foreach() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference p1 = varRef("p");
		VariableReference p2 = varRef("p");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		ForEachLoop l1 = new ForEachLoop();
		ForEachLoop l2 = new ForEachLoop();
		l1.setLoopedReference(this1);
		l2.setLoopedReference(this2);
		l1.setDeclaration(varDecl(p1, BOOL));
		l2.setDeclaration(varDecl(p2, OBJECT));
		l1.getBody().add(varDecl(o1, INT));
		l2.getBody().add(varDecl(o2, CHAR));

		md1.body.add(l1);
		md1.body.add(l2);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, p1, p2, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_for() {
		VariableReference p1 = varRef("p");
		VariableReference p2 = varRef("p");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		ForLoop l1 = new ForLoop();
		l1.getInit().add(varDecl(p1, BOOL));
		l1.getBody().add(varDecl(o1, INT));
		ForLoop l2 = new ForLoop();
		l2.getInit().add(varDecl(p2, OBJECT));
		l2.getBody().add(varDecl(o2, CHAR));

		md1.body.add(l1);
		md1.body.add(l2);

		addUniqueAOs(p1, p2, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_ifElse() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");
		VariableReference o3 = varRef("o");
		VariableReference o4 = varRef("o");

		IfElseBlock l1 = new IfElseBlock();
		l1.getThen().add(varDecl(o1, BOOL));
		l1.getElse().add(varDecl(o2, INT));
		IfElseBlock l2 = new IfElseBlock();
		l2.getThen().add(varDecl(o3, OBJECT));
		l2.getElse().add(varDecl(o4, CHAR));

		md1.body.add(l1);
		md1.body.add(l2);

		addUniqueAOs(o1, o2, o3, o4);
		assertAOs();
	}

	@Test
	public void scope_lock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		LockBlock l1 = new LockBlock();
		l1.setReference(this1);
		l1.getBody().add(varDecl(o1, BOOL));
		LockBlock l2 = new LockBlock();
		l2.setReference(this2);
		l2.getBody().add(varDecl(o2, INT));

		md1.body.add(l1);
		md1.body.add(l2);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_switch() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		SwitchBlock l1 = new SwitchBlock();
		l1.setReference(this1);
		l1.getDefaultSection().add(varDecl(o1, BOOL));

		SwitchBlock l2 = new SwitchBlock();
		l2.setReference(this2);
		l2.getDefaultSection().add(varDecl(o2, INT));

		md1.body.add(l1);
		md1.body.add(l2);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, o1, o2);
		assertAOs();
	}

	@Test(expected = AssertionException.class)
	public void scope_switchBlock_casesAreNoBlocks() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		CaseBlock cb1 = new CaseBlock();
		cb1.getBody().add(varDecl(o1, BOOL));
		CaseBlock cb2 = new CaseBlock();
		cb2.getBody().add(varDecl(o2, INT));

		SwitchBlock l1 = new SwitchBlock();
		l1.setReference(this1);
		l1.getSections().add(cb1);
		l1.getSections().add(cb2);

		md1.body.add(l1);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_try() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");
		VariableReference o3 = varRef("o");
		VariableReference o4 = varRef("o");

		TryBlock tb = new TryBlock();
		tb.getBody().add(varDecl(o1, BOOL));
		tb._finally.add(varDecl(o2, CHAR));

		TryBlock tb2 = new TryBlock();
		tb2.getBody().add(varDecl(o3, INT));
		tb2._finally.add(varDecl(o4, OBJECT));

		md1.body.add(tb);
		md1.body.add(tb2);

		addUniqueAOs(o1, o2, o3, o4);
		assertAOs();
	}

	@Test
	public void scope_try_catchBlocks() {
		IParameterName e1 = Names.newParameter("[E1, P] e");
		IParameterName e2 = Names.newParameter("[E2, P] e");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");
		VariableReference o3 = varRef("o");
		VariableReference o4 = varRef("o");

		TryBlock tb = new TryBlock();
		tb.getBody().add(varDecl(o1, BOOL));

		CatchBlock cb1 = new CatchBlock();
		cb1.setParameter(e1);
		cb1.getBody().add(varDecl(o2, INT));

		CatchBlock cb2 = new CatchBlock();
		cb2.setParameter(e2);
		cb2.getBody().add(varDecl(o3, OBJECT));

		tb._finally.add(varDecl(o4, CHAR));

		tb.catchBlocks.add(cb1);
		tb.catchBlocks.add(cb2);
		md1.body.add(tb);

		addUniqueAOs(o1, o2, o3, o4, e1, e2);
		assertAOs();
	}

	@Test
	public void scope_unchecked() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		UncheckedBlock ub1 = new UncheckedBlock();
		UncheckedBlock ub2 = new UncheckedBlock();
		ub1.getBody().add(varDecl(o1, INT));
		ub2.getBody().add(varDecl(o2, BOOL));

		md1.body.add(ub1);
		md1.body.add(ub2);

		addUniqueAOs(o1, o2);
		assertAOs();
	}

	@Test
	public void scope_unsafe() {
		md1.body.add(new UnsafeBlock());
		assertAOs();
	}

	@Test
	public void scope_using() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		UsingBlock ub1 = new UsingBlock();
		UsingBlock ub2 = new UsingBlock();
		ub1.setReference(this1);
		ub2.setReference(this2);
		ub1.getBody().add(varDecl(o1, INT));
		ub2.getBody().add(varDecl(o2, BOOL));

		md1.body.add(ub1);
		md1.body.add(ub2);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, o1, o2);
		assertAOs();
	}

	@Test
	public void scope_while() {
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		WhileLoop l1 = new WhileLoop();
		l1.getBody().add(varDecl(o1, INT));
		WhileLoop l2 = new WhileLoop();
		l2.getBody().add(varDecl(o2, CHAR));

		md1.body.add(l1);
		md1.body.add(l2);

		addUniqueAOs(o1, o2);
		assertAOs();
	}

	// ############################################################################

	@Test
	public void scope_lambda() {
		ILambdaName n1 = newLambda("[p:void] ([p:object] p)");
		ILambdaName n2 = newLambda("[p:void] ([p:bool] p)");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		LambdaExpression l1 = new LambdaExpression();
		l1.setName(n1);
		l1.getBody().add(varDecl(o1, INT));

		LambdaExpression l2 = new LambdaExpression();
		l2.setName(n2);
		l2.getBody().add(varDecl(o2, CHAR));

		md1.body.add(exprStmt(l1));
		md1.body.add(exprStmt(l2));

		addAO(l1, l2);
		addUniqueAOs(o1, o2, n1.getParameters().get(0), n2.getParameters().get(0));
		assertAOs();
	}

	@Test
	public void scope_lambdaInlambda() {
		ILambdaName n1 = newLambda("[p:void] ([p:object] p)");
		ILambdaName n2 = newLambda("[p:void] ([p:bool] p)");
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");

		LambdaExpression l1 = new LambdaExpression();
		l1.setName(n1);
		l1.getBody().add(varDecl(o1, INT));

		LambdaExpression l2 = new LambdaExpression();
		l2.setName(n2);
		l2.getBody().add(varDecl(o2, CHAR));

		l1.getBody().add(exprStmt(l2));
		md1.body.add(exprStmt(l1));

		addAO(l1, l2);
		addUniqueAOs(o1, o2, n1.getParameters().get(0), n2.getParameters().get(0));
		assertAOs();
	}
}