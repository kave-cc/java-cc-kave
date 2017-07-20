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
package cc.kave.commons.utils.ssts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
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
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.ssts.SSTCloneUtil;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;

public class SSTCloneUtilTest {

	@Test
	public void variableReference() {
		IVariableReference original = someVarRef();
		assertClone(original);
	}

	@Test
	public void eventReference() {
		EventReference original = new EventReference();
		original.setEventName(Names.newEvent("event"));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void fieldReference() {
		FieldReference original = new FieldReference();
		original.setFieldName(Names.newField("field"));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void indexAccessReference() {
		IndexAccessReference original = new IndexAccessReference();
		assertClone(original);
	}

	@Test
	public void propertyReference() {
		PropertyReference original = new PropertyReference();
		original.setPropertyName(Names.newProperty("get [?] [?].P()"));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void unknownReference() {
		UnknownReference original = new UnknownReference();
		assertClone(original);
	}

	@Test
	public void methodReference() {
		MethodReference original = new MethodReference();
		original.setMethodName(Names.newMethod("method"));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void binaryExpression() {
		BinaryExpression original = new BinaryExpression();
		original.setLeftOperand(constant());
		original.setRightOperand(constant());
		original.setOperator(BinaryOperator.And);
		assertClone(original);
	}

	@Test
	public void castExpression() {
		CastExpression original = new CastExpression();
		original.setOperator(CastOperator.Cast);
		original.setReference(someVarRef());
		original.setTargetType(someType());
		assertClone(original);
	}

	@Test
	public void completionExpression() {
		CompletionExpression original = new CompletionExpression();
		original.setObjectReference(someVarRef());
		original.setToken("a");
		original.setTypeReference(someType());
		assertClone(original);
	}

	@Test
	public void composedExpression() {
		ComposedExpression original = new ComposedExpression();
		original.setReferences(Lists.newArrayList(someVarRef()));
		assertClone(original);
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression original = new IfElseExpression();
		original.setCondition(constant());
		original.setElseExpression(constant());
		original.setThenExpression(constant());
		assertClone(original);
	}

	@Test
	public void indexAccessExpression() {
		IndexAccessExpression original = new IndexAccessExpression();
		original.setIndices(Lists.newArrayList(constant()));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void invocationExpression() {
		InvocationExpression original = new InvocationExpression();
		original.setMethodName(Names.newMethod("m1"));
		original.setParameters(Lists.newArrayList(constant()));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression original = new LambdaExpression();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setName(Names.newLambda("l"));
		assertClone(original);
	}

	@Test
	public void typeCheckExpression() {
		TypeCheckExpression original = new TypeCheckExpression();
		original.setReference(someVarRef());
		original.setType(someType());
		assertClone(original);
	}

	@Test
	public void unaryExpression() {
		UnaryExpression original = new UnaryExpression();
		original.setOperand(constant());
		original.setOperator(UnaryOperator.Complement);
		assertClone(original);
	}

	@Test
	public void constantValueExpression() {
		ConstantValueExpression original = constant();
		assertClone(original);
	}

	@Test
	public void nullExpression() {
		NullExpression original = new NullExpression();
		assertClone(original);
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression original = new ReferenceExpression();
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void unknownExpression() {
		UnknownExpression original = new UnknownExpression();
		assertClone(original);
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression original = new LoopHeaderBlockExpression();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		assertClone(original);
	}

	@Test
	public void assignment() {
		Assignment original = new Assignment();
		original.setExpression(someExpr());
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void breakStatement() {
		BreakStatement original = new BreakStatement();
		assertClone(original);
	}

	@Test
	public void continueStatement() {
		ContinueStatement original = new ContinueStatement();
		assertClone(original);
	}

	@Test
	public void doLoop() {
		DoLoop original = new DoLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setCondition(constant());
		assertClone(original);
	}

	@Test
	public void eventSubscriptionStatement() {
		EventSubscriptionStatement original = new EventSubscriptionStatement();
		original.setExpression(someExpr());
		original.setOperation(EventSubscriptionOperation.Add);
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void expressionStatement() {
		ExpressionStatement original = new ExpressionStatement();
		original.setExpression(someExpr());
		assertClone(original);
	}

	@Test
	public void forEachLoop() {
		ForEachLoop original = new ForEachLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setDeclaration(someVarDec());
		original.setLoopedReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void gotoStatement() {
		GotoStatement original = new GotoStatement();
		original.setLabel("1");
		assertClone(original);
	}

	@Test
	public void ifElseBlock() {
		IfElseBlock original = new IfElseBlock();
		original.setCondition(constant());
		original.setElse(Lists.newArrayList(new ContinueStatement()));
		original.setThen(Lists.newArrayList(new ContinueStatement()));
		assertClone(original);
	}

	@Test
	public void labelledStatement() {
		LabelledStatement original = new LabelledStatement();
		original.setLabel("l");
		original.setStatement(new ContinueStatement());
		assertClone(original);
	}

	@Test
	public void lockBlock() {
		LockBlock original = new LockBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void returnStatement() {
		ReturnStatement original = new ReturnStatement();
		original.setExpression(constant());
		original.setIsVoid(true);
		assertClone(original);
	}

	@Test
	public void switchBlock() {
		SwitchBlock original = new SwitchBlock();
		original.setDefaultSection(Lists.newArrayList(new ContinueStatement()));
		original.setReference(someVarRef());
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.getBody().add(new ContinueStatement());
		caseBlock.setLabel(constant());
		original.setSections(Lists.newArrayList(caseBlock));
		assertClone(original);
	}

	@Test
	public void throwStatement() {
		ThrowStatement original = new ThrowStatement();
		original.setReference(someVarRef());
		assertClone(original);
	}

	@Test
	public void tryBlock() {
		TryBlock original = new TryBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setFinally(Lists.newArrayList(new ContinueStatement()));
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		catchBlock.setKind(CatchBlockKind.General);
		catchBlock.setParameter(Names.newParameter("[?] p"));
		original.setCatchBlocks(Lists.newArrayList(catchBlock));
		assertClone(original);
	}

	@Test
	public void uncheckedBlock() {
		UncheckedBlock original = new UncheckedBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		assertClone(original);
	}

	@Test
	public void unknownStatement() {
		UnknownStatement original = new UnknownStatement();
		assertClone(original);
	}

	@Test
	public void unsafeBlock() {
		UnsafeBlock original = new UnsafeBlock();
		assertClone(original);
	}

	@Test
	public void usingBlock() {
		UsingBlock original = new UsingBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		assertClone(original);
	}

	@Test
	public void variableDeclaration() {
		VariableDeclaration original = new VariableDeclaration();
		original.setReference(someVarRef());
		original.setType(Names.newType("t"));
		assertClone(original);
	}

	@Test
	public void whileLoop() {
		WhileLoop original = new WhileLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setCondition(constant());
		assertClone(original);
	}

	@Test
	public void simpleSST() {
		ISST original = new SST();
		assertClone(original);
	}

	@Test
	public void simpleSSTwithHeader() {
		ISST original = defaultSST();
		assertClone(original);
	}

	@Test
	public void simpleSSTwithDeclarations() {
		ISST original = defaultSST();
		original.getDelegates().add(new DelegateDeclaration());
		original.getMethods().add(new MethodDeclaration());
		original.getEvents().add(new EventDeclaration());
		original.getFields().add(new FieldDeclaration());
		original.getProperties().add(new PropertyDeclaration());
		assertClone(original);
	}

	@Test
	public void methodDeclaration() {
		MethodDeclaration original = new MethodDeclaration();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setEntryPoint(true);
		original.setName(Names.newMethod("m"));
		assertClone(original);
	}

	@Test
	public void delegateDeclaration() {
		DelegateDeclaration original = new DelegateDeclaration();
		original.setName(Names.newType("d:[?] [?].()").asDelegateTypeName());
		assertClone(original);
	}

	@Test
	public void eventDeclaration() {
		EventDeclaration original = new EventDeclaration();
		original.setName(Names.newEvent("E"));
		assertClone(original);
	}

	@Test
	public void fieldDeclaration() {
		FieldDeclaration original = new FieldDeclaration();
		original.setName(Names.newField("F"));
		assertClone(original);
	}

	@Test
	public void propertyDeclaration() {
		PropertyDeclaration original = new PropertyDeclaration();
		original.setGet(Lists.newArrayList(new ContinueStatement()));
		original.setSet(Lists.newArrayList(new ContinueStatement()));
		original.setName(Names.newProperty("get [?] [?].P()"));
		assertClone(original);
	}

	private static void assertClone(ISSTNode original) {
		assertClone(original, SSTCloneUtil.clone(original, ISSTNode.class));
	}

	private static void assertClone(Iterable<ISSTNode> o, Iterable<ISSTNode> c) {
		List<ISSTNode> original = Lists.newArrayList(o);
		List<ISSTNode> clone = Lists.newArrayList(c);
		assertThat(clone, equalTo(original));
		assertThat(clone, not(sameInstance(original)));
		for (int i = 0; i < original.size(); i++) {
			assertClone(original.get(i), clone.get(i));
		}
	}

	private static void assertClone(ISSTNode original, ISSTNode clone) {
		assertThat(clone, equalTo(original));
		assertThat(clone, not(sameInstance(original)));
		SSTNodeHierarchy originalUtil = new SSTNodeHierarchy(original);
		SSTNodeHierarchy cloneUtil = new SSTNodeHierarchy(clone);
		assertClone(originalUtil.getChildren(original), cloneUtil.getChildren(clone));
	}

	private static ISST defaultSST() {
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("SST"));
		sst.setPartialClassIdentifier("SST");
		return sst;
	}

	private static IVariableReference someVarRef() {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("a");
		return ref;
	}

	private static ConstantValueExpression constant() {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue("a");
		return expr;
	}

	private static ITypeName someType() {
		return Names.newType("t");
	}

	private static IAssignableExpression someExpr() {
		UnaryExpression expr = new UnaryExpression();
		expr.setOperand(constant());
		return expr;
	}

	private static IVariableDeclaration someVarDec() {
		VariableDeclaration var = new VariableDeclaration();
		var.setReference(someVarRef());
		var.setType(someType());
		return var;
	}
}