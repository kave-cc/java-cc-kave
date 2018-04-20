package cc.kave.commons.utils.ssts;

import static cc.kave.commons.utils.ssts.SSTUtils.assign;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.invExpr;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
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
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
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
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class SSTNodeHierarchyTest {

	IStatement stmt = new ContinueStatement();
	IStatement stmt2 = new BreakStatement();
	IVariableReference ref = new VariableReference();
	ISimpleExpression expr = new ConstantValueExpression();
	ISimpleExpression expr2 = new ConstantValueExpression();

	@Test
	public void sst() {
		SST uut = new SST();
		MethodDeclaration method = new MethodDeclaration();
		PropertyDeclaration property = new PropertyDeclaration();
		EventDeclaration event = new EventDeclaration();
		FieldDeclaration field = new FieldDeclaration();
		DelegateDeclaration delegate = new DelegateDeclaration();
		uut.getDelegates().add(delegate);
		uut.getEvents().add(event);
		uut.getFields().add(field);
		uut.getMethods().add(method);
		uut.getProperties().add(property);
		assertRelations(uut, Lists.newArrayList(delegate, event, field, method, property));
	}

	@Test
	public void delegateDeclaration() {
		DelegateDeclaration uut = new DelegateDeclaration();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void eventDeclaration() {
		EventDeclaration uut = new EventDeclaration();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void fieldDeclaration() {
		FieldDeclaration uut = new FieldDeclaration();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void methodDeclaration() {
		MethodDeclaration uut = new MethodDeclaration();
		uut.getBody().add(stmt);
		assertRelations(uut, Lists.newArrayList(stmt));
	}

	@Test
	public void propertyDeclaration() {
		PropertyDeclaration uut = new PropertyDeclaration();
		uut.getGet().add(stmt);
		uut.getSet().add(stmt2);
		assertRelations(uut, Lists.newArrayList(stmt2, stmt));
	}

	@Test
	public void variableReference() {
		VariableReference uut = new VariableReference();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void eventReference() {
		EventReference uut = new EventReference();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void fieldReference() {
		FieldReference uut = new FieldReference();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void indexAccessReference() {
		IndexAccessReference uut = new IndexAccessReference();
		IndexAccessExpression expr = new IndexAccessExpression();
		uut.setExpression(expr);
		assertRelations(uut, Lists.newArrayList(expr));
	}

	@Test
	public void propertyReference() {
		PropertyReference uut = new PropertyReference();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void unknownReference() {
		UnknownReference uut = new UnknownReference();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void methodReference() {
		MethodReference uut = new MethodReference();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void binaryExpression() {
		BinaryExpression uut = new BinaryExpression();
		uut.setLeftOperand(expr);
		uut.setRightOperand(expr2);
		assertRelations(uut, Lists.newArrayList(expr, expr2));
	}

	@Test
	public void castExpression() {
		CastExpression uut = new CastExpression();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void completionExpression() {
		CompletionExpression uut = new CompletionExpression();
		uut.setObjectReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void composedExpression() {
		ComposedExpression uut = new ComposedExpression();
		uut.setReferences(Lists.newArrayList(ref));
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression uut = new IfElseExpression();
		uut.setCondition(expr);
		uut.setThenExpression(expr2);
		uut.setElseExpression(expr);
		assertRelations(uut, Lists.newArrayList(expr, expr2, expr));
	}

	@Test
	public void indexAccessExpression() {
		IndexAccessExpression uut = new IndexAccessExpression();
		uut.setIndices(Lists.newArrayList(expr));
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(expr, ref));
	}

	@Test
	public void invocationExpression() {
		InvocationExpression uut = new InvocationExpression();
		uut.parameters.add(expr);
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(expr, ref));
	}

	@Test
	public void complexNesting() {
		IMethodName m = Names.newMethod("[p:void] [T, P].m()");

		IInvocationExpression invExpr = invExpr("flattener", m, "expression");
		IReferenceExpression refExpr = (IReferenceExpression) invExpr.getParameters().get(0);
		IAssignment a = assign("$0", invExpr);
		assertRelations(a, invExpr, a.getReference());
		assertRelations(invExpr, refExpr, invExpr.getReference());
		assertRelations(refExpr, refExpr.getReference());
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression uut = new LambdaExpression();
		uut.setBody(Lists.newArrayList(stmt));
		assertRelations(uut, Lists.newArrayList(stmt));
	}

	@Test
	public void typeCheckExpression() {
		TypeCheckExpression uut = new TypeCheckExpression();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void unaryExpression() {
		UnaryExpression uut = new UnaryExpression();
		uut.setOperand(expr);
		assertRelations(uut, Lists.newArrayList(expr));
	}

	@Test
	public void constantValueExpression() {
		ConstantValueExpression uut = new ConstantValueExpression();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void nullExpression() {
		NullExpression uut = new NullExpression();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression uut = new ReferenceExpression();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void unknownExpression() {
		UnknownExpression uut = new UnknownExpression();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression uut = new LoopHeaderBlockExpression();
		uut.setBody(Lists.newArrayList(stmt));
		assertRelations(uut, Lists.newArrayList(stmt));
	}

	@Test
	public void assignment() {
		Assignment uut = new Assignment();
		uut.setExpression(expr);
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(expr, ref));
	}

	@Test
	public void breakStatement() {
		BreakStatement uut = new BreakStatement();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void continueStatement() {
		ContinueStatement uut = new ContinueStatement();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void doLoop() {
		DoLoop uut = new DoLoop();
		uut.setBody(Lists.newArrayList(stmt));
		uut.setCondition(expr);
		assertRelations(uut, Lists.newArrayList(stmt, expr));
	}

	@Test
	public void eventSubscriptionStatement() {
		EventSubscriptionStatement uut = new EventSubscriptionStatement();
		uut.setExpression(expr);
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(expr, ref));
	}

	@Test
	public void expressionStatement() {
		ExpressionStatement uut = new ExpressionStatement();
		uut.setExpression(expr);
		assertRelations(uut, Lists.newArrayList(expr));
	}

	@Test
	public void forEachLoop() {
		ForEachLoop uut = new ForEachLoop();
		uut.setBody(Lists.newArrayList(stmt));
		VariableDeclaration var = new VariableDeclaration();
		uut.setDeclaration(var);
		uut.setLoopedReference(ref);
		assertRelations(uut, Lists.newArrayList(stmt, var, ref));
	}

	@Test
	public void forLoop() {
		ForLoop uut = new ForLoop();
		uut.setBody(Lists.newArrayList(stmt));
		uut.setCondition(expr);
		uut.setInit(Lists.newArrayList(stmt2));
		uut.setStep(Lists.newArrayList(stmt));
		assertRelations(uut, Lists.newArrayList(stmt, expr, stmt2, stmt));
	}

	@Test
	public void gotoStatement() {
		GotoStatement uut = new GotoStatement();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void ifElseBlock() {
		IfElseBlock uut = new IfElseBlock();
		uut.setCondition(expr);
		uut.setElse(Lists.newArrayList(stmt));
		uut.setThen(Lists.newArrayList(stmt2));
		assertRelations(uut, Lists.newArrayList(expr, stmt, stmt2));
	}

	@Test
	public void labelledStatement() {
		LabelledStatement uut = new LabelledStatement();
		uut.setStatement(stmt);
		assertRelations(uut, Lists.newArrayList(stmt));
	}

	@Test
	public void lockBlock() {
		LockBlock uut = new LockBlock();
		uut.setBody(Lists.newArrayList(stmt));
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(stmt, ref));
	}

	@Test
	public void returnStatement() {
		ReturnStatement uut = new ReturnStatement();
		uut.setExpression(expr);
		assertRelations(uut, Lists.newArrayList(expr));
	}

	@Test
	public void switchBlock() {
		SwitchBlock uut = new SwitchBlock();
		uut.setDefaultSection(Lists.newArrayList(stmt));
		uut.setReference(ref);
		CaseBlock caseblock = new CaseBlock();
		caseblock.getBody().add(stmt2);
		uut.setSections(Lists.newArrayList(caseblock));
		assertRelations(uut, Lists.newArrayList(stmt, ref, stmt2));
	}

	@Test
	public void throwStatement() {
		ThrowStatement uut = new ThrowStatement();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void tryBlock() {
		TryBlock uut = new TryBlock();
		uut.body.add(stmt);
		CatchBlock catchblock = new CatchBlock();
		catchblock.getBody().add(stmt2);
		uut.getCatchBlocks().add(catchblock);
		uut.getFinally().add(stmt);
		assertRelations(uut, Lists.newArrayList(stmt, stmt2, stmt));
	}

	@Test
	public void uncheckedBlock() {
		UncheckedBlock uut = new UncheckedBlock();
		uut.getBody().add(stmt);
		assertRelations(uut, Lists.newArrayList(stmt));
	}

	@Test
	public void unknownStatement() {
		UnknownStatement uut = new UnknownStatement();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void unsafeBlock() {
		UnsafeBlock uut = new UnsafeBlock();
		assertRelations(uut, Lists.newArrayList());
	}

	@Test
	public void usingBlock() {
		UsingBlock uut = new UsingBlock();
		uut.getBody().add(stmt);
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(stmt, ref));
	}

	@Test
	public void variableDeclaration() {
		VariableDeclaration uut = new VariableDeclaration();
		uut.setReference(ref);
		assertRelations(uut, Lists.newArrayList(ref));
	}

	@Test
	public void whileLoop() {
		WhileLoop uut = new WhileLoop();
		uut.getBody().add(stmt);
		uut.setCondition(expr);
		assertRelations(uut, Lists.newArrayList(stmt, expr));
	}

	@Test
	public void findParent() {

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		LambdaExpression outerLambda = new LambdaExpression();
		LambdaExpression innerLambda = new LambdaExpression();
		ContinueStatement stmt = new ContinueStatement();

		innerLambda.getBody().add(stmt);
		outerLambda.getBody().add(exprStmt(innerLambda));
		md.getBody().add(exprStmt(outerLambda));
		sst.getMethods().add(md);

		SSTNodeHierarchy sut = new SSTNodeHierarchy(sst);
		assertSame(md, sut.findParent(stmt, IMethodDeclaration.class));
		assertSame(innerLambda, sut.findParent(stmt, ILambdaExpression.class));
		assertNull(sut.findParent(stmt, BreakStatement.class));
		assertNull(sut.findParent(stmt, ContinueStatement.class));

	}

	private static void assertRelations(ISSTNode uut, ISSTNode... expecteds) {
		assertRelations(uut, Lists.newArrayList(expecteds));
	}

	private static void assertRelations(ISSTNode uut, List<ISSTNode> expected) {
		SSTNodeHierarchy sut = new SSTNodeHierarchy(uut);
		List<ISSTNode> children = Lists.newArrayList(sut.getChildren(uut));
		assertThat(children, equalTo(expected));
		for (int i = 0; i < children.size(); i++) {
			ISSTNode parent = sut.getParent(expected.get(i));
			assertThat(uut, equalTo(parent));
			assertThat(uut, sameInstance(parent));
			assertThat(children.get(i), equalTo(expected.get(i)));
			assertThat(children.get(i), sameInstance(expected.get(i)));
		}
	}
}
