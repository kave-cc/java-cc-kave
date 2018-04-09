/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.ssts.impl.visitor;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
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
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.naming.TypeErasure;

public class TypeErasureVisitor extends AbstractThrowingNodeVisitor<Object, Object> {

	@Override
	public Object visit(ISST in, Object ctx) {
		SST out = new SST();
		out.setEnclosingType(TypeErasure.of(in.getEnclosingType()));

		for (IDelegateDeclaration ddIn : in.getDelegates()) {
			out.getDelegates().add(visit(ddIn, null));
		}
		for (IEventDeclaration edIn : in.getEvents()) {
			out.getEvents().add(visit(edIn, null));
		}
		for (IFieldDeclaration fdIn : in.getFields()) {
			out.getFields().add(visit(fdIn, null));
		}
		for (IMethodDeclaration mdIn : in.getMethods()) {
			out.getMethods().add(visit(mdIn, null));
		}
		for (IPropertyDeclaration pdIn : in.getProperties()) {
			out.getProperties().add(visit(pdIn, null));
		}
		return out;
	}

	// --- declarations ---

	@Override
	public IDelegateDeclaration visit(IDelegateDeclaration in, Object context) {
		DelegateDeclaration out = new DelegateDeclaration();
		out.setName(TypeErasure.of(in.getName()).asDelegateTypeName());
		return out;
	}

	@Override
	public IEventDeclaration visit(IEventDeclaration in, Object context) {
		EventDeclaration out = new EventDeclaration();
		out.setName(TypeErasure.of(in.getName()));
		return out;
	}

	@Override
	public IFieldDeclaration visit(IFieldDeclaration in, Object context) {
		FieldDeclaration out = new FieldDeclaration();
		out.setName(TypeErasure.of(in.getName()));
		return out;
	}

	@Override
	public IMethodDeclaration visit(IMethodDeclaration in, Object context) {
		MethodDeclaration out = new MethodDeclaration();
		out.setName(TypeErasure.of(in.getName()));
		out.setEntryPoint(in.isEntryPoint());
		out.body.addAll(visit(in.getBody()));
		return out;
	}

	@Override
	public IPropertyDeclaration visit(IPropertyDeclaration in, Object context) {
		PropertyDeclaration out = new PropertyDeclaration();
		out.setName(TypeErasure.of(in.getName()));
		out.setGet(visit(in.getGet()));
		out.setSet(visit(in.getSet()));
		return out;
	}

	// --- blocks ---

	@Override
	public Object visit(IDoLoop in, Object context) {
		DoLoop out = new DoLoop();
		out.setCondition((ILoopHeaderExpression) in.getCondition().accept(this, null));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IForEachLoop in, Object context) {
		ForEachLoop out = new ForEachLoop();
		out.setDeclaration((IVariableDeclaration) in.getDeclaration().accept(this, null));
		out.setLoopedReference((IVariableReference) in.getLoopedReference().accept(this, null));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IForLoop in, Object context) {
		ForLoop out = new ForLoop();
		out.setInit(visit(in.getInit()));
		out.setCondition((ILoopHeaderExpression) in.getCondition().accept(this, null));
		out.setStep(visit(in.getStep()));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IIfElseBlock in, Object context) {
		IfElseBlock out = new IfElseBlock();
		out.setCondition((ISimpleExpression) in.getCondition().accept(this, null));
		out.setThen(visit(in.getThen()));
		out.setElse(visit(in.getElse()));
		return out;
	}

	@Override
	public Object visit(ILockBlock in, Object context) {
		LockBlock out = new LockBlock();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(ISwitchBlock in, Object context) {
		SwitchBlock out = new SwitchBlock();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		for (ICaseBlock icb : in.getSections()) {
			CaseBlock ocb = new CaseBlock();
			ocb.setLabel((ISimpleExpression) icb.getLabel().accept(this, null));
			ocb.setBody(visit(icb.getBody()));
			out.getSections().add(ocb);
		}
		out.setDefaultSection(visit(in.getDefaultSection()));
		return out;
	}

	@Override
	public Object visit(ITryBlock in, Object context) {
		TryBlock out = new TryBlock();
		out.body.addAll(visit(in.getBody()));
		for (ICatchBlock icb : in.getCatchBlocks()) {
			CatchBlock ocb = new CatchBlock();
			ocb.setKind(icb.getKind());
			ocb.setParameter(TypeErasure.of(icb.getParameter()));
			ocb.setBody(visit(icb.getBody()));
			out.getCatchBlocks().add(ocb);
		}
		out._finally.addAll(visit(in.getFinally()));
		return out;
	}

	@Override
	public Object visit(IUncheckedBlock in, Object context) {
		UncheckedBlock out = new UncheckedBlock();
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IUnsafeBlock in, Object context) {
		return new UnsafeBlock();
	}

	@Override
	public Object visit(IUsingBlock in, Object context) {
		UsingBlock out = new UsingBlock();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IWhileLoop in, Object context) {
		WhileLoop out = new WhileLoop();
		out.setCondition((ILoopHeaderExpression) in.getCondition().accept(this, null));
		out.setBody(visit(in.getBody()));
		return out;
	}

	// --- statements ---

	@Override
	public Object visit(IAssignment in, Object context) {
		Assignment out = new Assignment();
		out.setReference((IAssignableReference) in.getReference().accept(this, null));
		out.setExpression((IAssignableExpression) in.getExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IBreakStatement in, Object context) {
		return new BreakStatement();
	}

	@Override
	public Object visit(IContinueStatement in, Object context) {
		return new ContinueStatement();
	}

	@Override
	public Object visit(IEventSubscriptionStatement in, Object context) {
		EventSubscriptionStatement out = new EventSubscriptionStatement();
		out.setReference((IAssignableReference) in.getReference().accept(this, null));
		out.setOperation(in.getOperation());
		out.setExpression((IAssignableExpression) in.getExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IExpressionStatement in, Object context) {
		ExpressionStatement out = new ExpressionStatement();
		out.setExpression((IAssignableExpression) in.getExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IGotoStatement in, Object context) {
		GotoStatement out = new GotoStatement();
		out.setLabel(in.getLabel());
		return out;
	}

	@Override
	public Object visit(ILabelledStatement in, Object context) {
		LabelledStatement out = new LabelledStatement();
		out.setLabel(in.getLabel());
		out.setStatement((IStatement) in.getStatement().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IReturnStatement in, Object context) {
		ReturnStatement out = new ReturnStatement();
		out.setIsVoid(in.isVoid());
		out.setExpression((ISimpleExpression) in.getExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IThrowStatement in, Object context) {
		ThrowStatement out = new ThrowStatement();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IUnknownStatement in, Object context) {
		return new UnknownStatement();
	}

	@Override
	public Object visit(IVariableDeclaration in, Object context) {
		VariableDeclaration out = new VariableDeclaration();
		out.setReference(in.getReference());
		out.setType(TypeErasure.of(in.getType()));
		return out;
	}

	// --- expressions ---

	@Override
	public Object visit(IConstantValueExpression in, Object context) {
		ConstantValueExpression out = new ConstantValueExpression();
		out.setValue(in.getValue());
		return out;
	}

	@Override
	public Object visit(INullExpression in, Object context) {
		return new NullExpression();
	}

	@Override
	public Object visit(IReferenceExpression in, Object context) {
		ReferenceExpression out = new ReferenceExpression();
		out.setReference((IReference) in.getReference().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IUnknownExpression in, Object context) {
		return new UnknownExpression();
	}

	@Override
	public Object visit(ILoopHeaderBlockExpression in, Object context) {
		LoopHeaderBlockExpression out = new LoopHeaderBlockExpression();
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(IBinaryExpression in, Object context) {
		BinaryExpression out = new BinaryExpression();
		out.setLeftOperand((ISimpleExpression) in.getLeftOperand().accept(this, null));
		out.setOperator(in.getOperator());
		out.setRightOperand((ISimpleExpression) in.getRightOperand().accept(this, null));
		return out;
	}

	@Override
	public Object visit(ICastExpression in, Object context) {
		CastExpression out = new CastExpression();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setOperator(in.getOperator());
		out.setTargetType(TypeErasure.of(in.getTargetType()));
		return out;
	}

	@Override
	public Object visit(ICompletionExpression in, Object context) {
		CompletionExpression out = new CompletionExpression();
		if (in.getVariableReference() != null) {
			out.setObjectReference((IVariableReference) in.getVariableReference().accept(this, null));
		}
		if (in.getToken() != null) {
			out.setToken(in.getToken());
		}
		if (in.getTypeReference() != null) {
			out.setTypeReference(TypeErasure.of(in.getTypeReference()));
		}
		return out;
	}

	@Override
	public Object visit(IComposedExpression in, Object context) {
		ComposedExpression out = new ComposedExpression();
		out.setReferences(visit(in.getReferences()));
		return out;
	}

	@Override
	public Object visit(IIfElseExpression in, Object context) {
		IfElseExpression out = new IfElseExpression();
		out.setCondition((ISimpleExpression) in.getCondition().accept(this, null));
		out.setThenExpression((ISimpleExpression) in.getThenExpression().accept(this, null));
		out.setElseExpression((ISimpleExpression) in.getElseExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IIndexAccessExpression in, Object context) {
		IndexAccessExpression out = new IndexAccessExpression();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setIndices(visit(in.getIndices()));
		return out;
	}

	@Override
	public Object visit(IInvocationExpression in, Object context) {
		InvocationExpression out = new InvocationExpression();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setMethodName(TypeErasure.of(in.getMethodName()));
		out.parameters.addAll(visit(in.getParameters()));
		return out;
	}

	@Override
	public Object visit(ILambdaExpression in, Object context) {
		LambdaExpression out = new LambdaExpression();
		out.setName(TypeErasure.of(in.getName()));
		out.setBody(visit(in.getBody()));
		return out;
	}

	@Override
	public Object visit(ITypeCheckExpression in, Object context) {
		TypeCheckExpression out = new TypeCheckExpression();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setType(TypeErasure.of(in.getType()));
		return out;
	}

	@Override
	public Object visit(IUnaryExpression in, Object context) {
		UnaryExpression out = new UnaryExpression();
		out.setOperator(in.getOperator());
		out.setOperand((ISimpleExpression) in.getOperand().accept(this, null));
		return out;
	}

	// --- references ---

	@Override
	public Object visit(IEventReference in, Object context) {
		EventReference out = new EventReference();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setEventName(TypeErasure.of(in.getEventName()));
		return out;
	}

	@Override
	public Object visit(IFieldReference in, Object context) {
		FieldReference out = new FieldReference();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setFieldName(TypeErasure.of(in.getFieldName()));
		return out;
	}

	@Override
	public Object visit(IIndexAccessReference in, Object context) {
		IndexAccessReference out = new IndexAccessReference();
		out.setExpression((IIndexAccessExpression) in.getExpression().accept(this, null));
		return out;
	}

	@Override
	public Object visit(IMethodReference in, Object context) {
		MethodReference out = new MethodReference();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setMethodName(TypeErasure.of(in.getMethodName()));
		return out;
	}

	@Override
	public Object visit(IPropertyReference in, Object context) {
		PropertyReference out = new PropertyReference();
		out.setReference((IVariableReference) in.getReference().accept(this, null));
		out.setPropertyName(TypeErasure.of(in.getPropertyName()));
		return out;
	}

	@Override
	public Object visit(IUnknownReference in, Object context) {
		return new UnknownReference();
	}

	@Override
	public Object visit(IVariableReference in, Object context) {
		VariableReference out = new VariableReference();
		out.setIdentifier(in.getIdentifier());
		return out;
	}

	// --- utils ---

	private <T extends ISSTNode> List<T> visit(List<T> body) {
		List<T> out = Lists.newLinkedList();
		for (T stmt : body) {
			out.add((T) stmt.accept(this, null));
		}
		return out;
	}
}