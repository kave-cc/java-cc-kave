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

import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.eventRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.fieldRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.methodRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION1;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION2;
import static cc.kave.commons.utils.ssts.SSTUtils.BOOL;
import static cc.kave.commons.utils.ssts.SSTUtils.STRING;
import static cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling.THROW;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
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
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.utils.ssts.SSTUtils;

public class TypeBasedPointsToAnalysisExistanceTest extends PathInsensitivePointToAnalysisTestBase {

	// The only goal of this suite is to make sure that all possible keys are being
	// registered, it does not check for correctness.

	@Before
	public void setup() {
		sut = new TypeBasedPointsToAnalysis(THROW);
	}

	@Test
	public void decl_sst() {
		sst = new SST();
		resetAOs();
		addUniqueAOs(sst);
		assertAOs();
	}

	@Test
	public void decl_event() {
		EventDeclaration ed = new EventDeclaration();
		ed.setName(Names.newEvent("[E,P] [T,P].e([P2, P] p)"));

		sst = new SST();
		sst.events.add(ed);

		resetAOs();
		addUniqueAOs(sst, ed);
		assertAOs();
	}

	@Test
	public void decl_field() {
		FieldDeclaration fd = new FieldDeclaration();
		fd.setName(Names.newField("[E,P] [T,P].e([P2, P] p)"));

		sst = new SST();
		sst.fields.add(fd);

		resetAOs();
		addUniqueAOs(sst, fd);
		assertAOs();
	}

	@Test
	public void decl_methods() {

		VariableDeclaration vd = SSTUtil.varDecl("x", SSTUtils.INT);

		MethodDeclaration md1 = new MethodDeclaration();
		md1.setName(Names.newMethod("[p:int] [%s].m([p:char] c)", t(1).getIdentifier()));
		IParameterName c = md1.getName().getParameters().get(0);
		md1.body.add(vd);

		sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		resetAOs();
		addUniqueAOs(sst, md1, c, vd.getReference());
		assertAOs();
	}

	@Test
	public void decl_property() {

		VariableDeclaration vd1 = SSTUtil.varDecl("x1", SSTUtils.CHAR);
		VariableDeclaration vd2 = SSTUtil.varDecl("x2", SSTUtils.STRING);

		PropertyDeclaration pd1 = new PropertyDeclaration();
		pd1.setName(Names.newProperty("get set [p:int] [%s].P()", t(1).getIdentifier()));
		IParameterName value = pd1.getName().getSetterValueParam();
		pd1.getGet().add(vd1);
		pd1.getGet().add(vd2);

		sst = new SST();
		sst.enclosingType = t(1);
		sst.properties.add(pd1);

		resetAOs();
		addAO(pd1, value);
		addUniqueAOs(sst, vd1.getReference(), vd2.getReference());
		assertAOs();
	}

	@Test
	public void decl_property_indexer() {

		VariableDeclaration vd1 = varDecl("x1", BOOL);
		VariableDeclaration vd2 = varDecl("x2", STRING);

		IPropertyName pn = newProperty("get set [p:int] [%s].P([p:char] p1)", t(1).getIdentifier());
		IParameterName p1_1 = pn.getExplicitGetterName().getParameters().get(0);
		IParameterName p1_2 = pn.getExplicitSetterName().getParameters().get(0);
		assertSame(p1_1, p1_2);
		IParameterName value = pn.getSetterValueParam();

		PropertyDeclaration pd1 = new PropertyDeclaration();
		pd1.setName(pn);
		pd1.getGet().add(vd1);
		pd1.getGet().add(vd2);

		sst = new SST();
		sst.enclosingType = t(1);
		sst.properties.add(pd1);

		resetAOs();
		addAO(pd1, value);
		addUniqueAOs(sst, p1_1, vd1.getReference(), vd2.getReference());
		assertAOs();
	}

	// ################################################################################

	@Test
	public void block_doLoop() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		DoLoop l = new DoLoop();
		l.getBody().add(exprStmt(refExpr(this1)));
		l.setCondition(refExpr(this2));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_forEachLoop() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference o = varRef("o");

		ForEachLoop l = new ForEachLoop();
		l.setLoopedReference(this1);
		l.setDeclaration(varDecl(o, newType("TWhile, P")));
		l.getBody().add(exprStmt(refExpr(this2)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, o);
		assertAOs();
	}

	@Test
	public void block_forLoop() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference this3 = varRef("this");
		VariableReference this4 = varRef("this");

		ForLoop l = new ForLoop();
		l.getInit().add(exprStmt(refExpr(this1)));
		l.setCondition(refExpr(this2));
		l.getStep().add(exprStmt(refExpr(this3)));
		l.getBody().add(exprStmt(refExpr(this4)));

		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2, this3, this4);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_ifElseBlock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference this3 = varRef("this");

		IfElseBlock l = new IfElseBlock();
		l.setCondition(refExpr(this1));
		l.getThen().add(exprStmt(refExpr(this2)));
		l.getElse().add(exprStmt(refExpr(this3)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2, this3);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_lockBlock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		LockBlock l = new LockBlock();
		l.setReference(this1);
		l.getBody().add(exprStmt(refExpr(this2)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_switchBlock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference this3 = varRef("this");

		CaseBlock cb = new CaseBlock();
		cb.getBody().add(exprStmt(refExpr(this2)));

		SwitchBlock l = new SwitchBlock();
		l.setReference(this1);
		l.getSections().add(cb);
		l.getDefaultSection().add(exprStmt(refExpr(this3)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2, this3);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_tryBlock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference this3 = varRef("this");

		IParameterName pn1 = Names.newParameter("[p:char] p1");
		IParameterName pn2 = Names.newParameter("[p:bool] ?");
		IParameterName pn3; // [?] ???

		CatchBlock cb1 = new CatchBlock();
		cb1.setParameter(pn1);
		cb1.getBody().add(exprStmt(refExpr(this2)));

		CatchBlock cb2 = new CatchBlock();
		cb2.setKind(CatchBlockKind.Unnamed);
		cb2.setParameter(pn2);

		CatchBlock cb3 = new CatchBlock();
		cb3.setKind(CatchBlockKind.General);
		pn3 = cb3.getParameter();

		TryBlock tb = new TryBlock();
		tb.getBody().add(exprStmt(refExpr(this1)));
		tb.catchBlocks.add(cb1);
		tb.catchBlocks.add(cb2);
		tb.catchBlocks.add(cb3);
		tb._finally.add(exprStmt(refExpr(this3)));

		md1.body.add(tb);

		resetAOs();
		addAO(sst, this1, this2, this3);
		addUniqueAOs(md1, pn1, pn2, pn3);
		assertAOs();
	}

	@Test
	public void block_uncheckedBlock() {
		VariableReference this1 = varRef("this");

		UncheckedBlock l = new UncheckedBlock();
		l.getBody().add(exprStmt(refExpr(this1)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_unsafeBlock() {
		md1.body.add(new UnsafeBlock());
		assertAOs();
	}

	@Test
	public void block_usingBlock() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		UsingBlock l = new UsingBlock();
		l.setReference(this1);
		l.getBody().add(exprStmt(refExpr(this2)));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void block_whileLoop() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		WhileLoop l = new WhileLoop();
		l.getBody().add(exprStmt(refExpr(this1)));
		l.setCondition(refExpr(this2));
		md1.body.add(l);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	// ################################################################################

	@Test
	public void stmt_assignment() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		md1.body.add(assign(this1, refExpr(this2)));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void stmt_breakStmt() {
		md1.body.add(new BreakStatement());
		assertAOs();
	}

	@Test
	public void stmt_continueStmt() {
		md1.body.add(new ContinueStatement());
		assertAOs();
	}

	@Test
	public void stmt_eventSubscriptionStmt() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		EventSubscriptionStatement stmt = new EventSubscriptionStatement();
		stmt.setReference(this1);
		stmt.setOperation(EventSubscriptionOperation.Add);
		stmt.setExpression(refExpr(this2));
		md1.body.add(stmt);

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void stmt_expressionStmt() {
		IVariableReference ref = varRef("this");
		md1.body.add(exprStmt(refExpr(ref)));
		resetAOs();
		addAO(sst, ref);
		addAO(md1);
		assertAOs();
	}

	@Test
	public void stmt_gotoStmt() {
		md1.body.add(new GotoStatement());
		assertAOs();
	}

	@Test
	public void stmt_labeledStmt() {
		IVariableReference ref = varRef("this");
		IExpressionStatement stmt = exprStmt(refExpr(ref));
		md1.body.add(stmt);
		resetAOs();
		addAO(sst, ref);
		addAO(md1);
		assertAOs();
	}

	@Test
	public void stmt_returnStmt() {
		VariableReference this1 = varRef("this");

		ReturnStatement stmt = new ReturnStatement();
		stmt.setIsVoid(false);
		stmt.setExpression(refExpr(this1));
		md1.body.add(stmt);

		resetAOs();
		addAO(sst, this1);
		addAO(md1);
		assertAOs();
	}

	@Test
	public void stmt_throwStmt() {
		VariableReference this1 = varRef("this");

		ThrowStatement stmt = new ThrowStatement();
		stmt.setReference(this1);
		md1.body.add(stmt);

		resetAOs();
		addAO(sst, this1);
		addAO(md1);
		assertAOs();
	}

	@Test
	public void stmt_unknownStmt() {
		md1.body.add(new UnknownStatement());
		assertAOs();
	}

	@Test
	public void stmt_variableDecl() {
		VariableDeclaration d = varDecl("o", ACTION2);
		md1.body.add(d);
		addAO(d.getReference());
		assertAOs();
	}

	// ################################################################################

	// simple

	@Test
	public void expr_constantExpr() {
		md1.body.add(exprStmt(new ConstantValueExpression()));
		assertAOs();
	}

	@Test
	public void expr_nullExpr() {
		md1.body.add(exprStmt(new NullExpression()));
		assertAOs();
	}

	@Test
	public void expr_referenceExpr() {
		VariableReference r = varRef("this");
		md1.body.add(exprStmt(refExpr(r)));
		resetAOs();
		addAO(sst, r);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_unknownExpr() {
		md1.body.add(exprStmt(new UnknownExpression()));
		assertAOs();
	}

	// loop header

	@Test
	public void expr_loopHeaderBlockExpr() {
		VariableReference this1 = varRef("this");

		LoopHeaderBlockExpression header = new LoopHeaderBlockExpression();
		header.getBody().add(exprStmt(refExpr(this1)));
		WhileLoop b = new WhileLoop();
		b.setCondition(header);
		md1.body.add(b);

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	// assignable

	@Test
	public void expr_binary() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		BinaryExpression expr = new BinaryExpression();
		expr.setLeftOperand(refExpr(this1));
		expr.setRightOperand(refExpr(this2));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_cast() {
		VariableReference this1 = varRef("this");

		CastExpression expr = new CastExpression();
		expr.setReference(this1);
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_completion() {
		VariableReference this1 = varRef("this");

		CompletionExpression expr = new CompletionExpression();
		expr.setVariableReference(this1);
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_composed() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		ComposedExpression expr = new ComposedExpression();
		expr.getReferences().add(this1);
		expr.getReferences().add(this2);
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_ifElse() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");
		VariableReference this3 = varRef("this");

		IfElseExpression expr = new IfElseExpression();
		expr.setCondition(refExpr(this1));
		expr.setThenExpression(refExpr(this2));
		expr.setElseExpression(refExpr(this3));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1, this2, this3);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_indexAccess() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(this1);
		expr.getIndices().add(refExpr(this2));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_invocation() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		InvocationExpression expr = new InvocationExpression();
		expr.setReference(this1);
		expr.getParameters().add(refExpr(this2));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_lambda() {
		VariableReference this1 = varRef("this");

		ILambdaName ln = Names.newLambda("[p:int] ([p:object] o)");

		LambdaExpression expr = new LambdaExpression();
		expr.setName(ln);
		expr.getBody().add(exprStmt(refExpr(this1)));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1, ln.getParameters().get(0));
		assertAOs();
	}

	@Test
	public void expr_typeCheck() {
		VariableReference this1 = varRef("this");

		TypeCheckExpression expr = new TypeCheckExpression();
		expr.setReference(this1);
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	@Test
	public void expr_unary() {
		VariableReference this1 = varRef("this");

		UnaryExpression expr = new UnaryExpression();
		expr.setOperand(refExpr(this1));
		md1.body.add(exprStmt(expr));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}

	// ################################################################################

	@Test
	public void ref_event() {
		VariableReference this1 = varRef("this");
		IEventName en = Names.newEvent("[%s] [%s].E", ACTION1.getIdentifier(), t(1).getIdentifier());
		IEventReference ref = eventRef(this1, en);
		md1.body.add(exprStmt(refExpr(ref)));

		resetAOs();
		addAO(sst, this1, ref.getReference());
		addUniqueAOs(md1, ref);
		assertAOs();
	}

	@Test
	public void ref_field() {
		VariableReference this1 = varRef("this");
		IFieldName n = Names.newField("[p:char] [%s]._f", t(1).getIdentifier());
		IFieldReference ref = fieldRef(this1, n);
		md1.body.add(exprStmt(refExpr(ref)));

		resetAOs();
		addAO(sst, this1, ref.getReference());
		addUniqueAOs(md1, ref);
		assertAOs();
	}

	@Test
	public void ref_indexAccess() {
		VariableReference this1 = varRef("this");
		VariableReference this2 = varRef("this");

		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(this1);
		expr.getIndices().add(refExpr(this2));

		IndexAccessReference ref = new IndexAccessReference();
		ref.setExpression(expr);
		md1.body.add(exprStmt(refExpr(ref)));

		resetAOs();
		addAO(sst, this1, this2);
		addUniqueAOs(md1, ref);
		assertAOs();
	}

	@Test
	public void ref_method() {
		VariableReference this1 = varRef("this");
		IMethodName n = Names.newMethod("[p:char] [%s].P([p:object] p1)", t(1).getIdentifier());
		IMethodReference ref = methodRef(this1, n);
		md1.body.add(exprStmt(refExpr(ref)));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1, ref); // does not make sense to include parameters here!
		assertAOs();
	}

	@Test
	public void ref_property() {
		VariableReference this1 = varRef("this");
		IPropertyName n = newProperty("get set [p:char] [%s].P([p:object] p1)", t(1).getIdentifier());
		IPropertyReference ref = propertyRef(this1, n);
		md1.body.add(exprStmt(refExpr(ref)));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1, ref); // does not make sense to include parameters here!
		assertAOs();
	}

	@Test
	public void ref_unknown() {
		md1.body.add(exprStmt(refExpr(new UnknownReference())));
		assertAOs();
	}

	@Test
	public void ref_var() {
		VariableReference this1 = varRef("this");
		md1.body.add(exprStmt(refExpr(this1)));

		resetAOs();
		addAO(sst, this1);
		addUniqueAOs(md1);
		assertAOs();
	}
}