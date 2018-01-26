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
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
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
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
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
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnsVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.InvocationMethodNameVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
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
import cc.kave.commons.utils.ssts.SSTCloneUtil;

public class InliningVisitor extends AbstractThrowingNodeVisitor<InliningContext, Void> {

	@Override
	public Void visit(ISST sst, InliningContext context) {
		ISST clone = SSTCloneUtil.clone(sst, ISST.class);
		visitMethods(context, clone);
		removeInlinedMethods(context);
		return null;
	}

	private void removeInlinedMethods(InliningContext context) {
		Set<IMethodDeclaration> notInlinedMethods = new HashSet<>();
		Set<IMethodName> calls = new HashSet<>();
		for (IMethodDeclaration method : context.getNonEntryPoints()) {
			if (!context.getInlinedMethods().contains(method)) {
				notInlinedMethods.add(method);
				context.addMethod(method);
				Set<IMethodName> invokes = new HashSet<>();
				method.accept(new InvocationMethodNameVisitor(), invokes);
				calls.addAll(invokes);
			}
		}

		for (IMethodDeclaration method : context.getInlinedMethods()) {
			if (calls.contains(method.getName())) {
				context.addMethod(method);
			}
		}
		context.setMethods();
	}

	private void visitMethods(InliningContext context, ISST clone) {
		context.setNonEntryPoints(clone.getNonEntryPoints());
		context.createSST(clone);
		if (clone.getEntryPoints().size() > 0) {
			for (IMethodDeclaration method : clone.getEntryPoints()) {
				method.accept(this, context);
			}
		} else {
			for (IMethodDeclaration method : clone.getMethods()) {
				context.addMethod(method);
			}
		}
	}

	public Void visit(IMethodDeclaration stmt, InliningContext context) {
		MethodDeclaration method = new MethodDeclaration();
		context.visitScope(stmt.getBody());
		method.getBody().addAll(context.getBody());
		method.setEntryPoint(true);
		method.setName(stmt.getName());
		context.addMethod(method);

		// Clear body for next Method
		context.resetScope();
		return null;
	}

	public Void visit(IExpressionStatement stmt, InliningContext context) {
		stmt.getExpression().accept(this, context);
		if (stmt.getExpression() instanceof IInvocationExpression) {
			if (context.getReturnExpression() != null) {
				ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
				expressionStatement.setExpression(context.getReturnExpression());
				context.setReturnExpression(null);
				context.addStatement(stmt);
			}
			return null;
		}
		context.addStatement(stmt);
		return null;
	}

	public Void visit(IVariableDeclaration stmt, InliningContext context) {
		stmt.getReference().accept(this, context);
		context.addStatement(stmt);
		return null;
	}

	public Void visit(IAssignment stmt, InliningContext context) {
		stmt.getReference().accept(this, context);
		stmt.getExpression().accept(this, context);
		if (stmt.getExpression() instanceof IInvocationExpression) {
			if (context.getReturnExpression() != null) {
				Assignment assignment = (Assignment) stmt;
				assignment.setExpression(context.getReturnExpression());
				context.setReturnExpression(null);
				context.addStatement(stmt);
			}
			return null;
		}
		context.addStatement(stmt);
		return null;
	}

	public Void visit(IReturnStatement stmt, InliningContext context) {
		if (context.isInline() && !context.isInCondition()) {
			inlineReturnStatement(stmt, context);
		} else {
			stmt.getExpression().accept(this, context);
			context.addStatement(stmt);
		}
		return null;
	}

	private void inlineReturnStatement(IReturnStatement stmt, InliningContext context) {
		if (!context.isVoid()) {
			Assignment assignment = new Assignment();
			assignment.setReference(SSTUtil.variableReference(context.getResultName()));
			assignment.setExpression(stmt.getExpression());
			context.addStatement(assignment);
		}
		Assignment resultAssignment = new Assignment();
		resultAssignment.setReference(SSTUtil.variableReference(context.getGotResultName()));
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue("false");
		resultAssignment.setExpression(constant);
		context.addStatement(resultAssignment);
		context.setGuardNeeded(true);
		context.setGlobalGuardNeeded(true);
	}

	@Override
	public Void visit(IForLoop block, InliningContext context) {
		checkForReturn(block, context);
		if (context.hasReturnInLoop() && block.getCondition() instanceof ISimpleExpression) {
			LoopHeaderBlockExpression loopheader = new LoopHeaderBlockExpression();
			loopheader.getBody().add(SSTUtil.returnStatement((ISimpleExpression) block.getCondition()));
			ForLoop newBlock = (ForLoop) block;
			newBlock.setCondition(loopheader);
		}
		block.getCondition().accept(this, context);
		context.visitBlock(block.getInit());
		context.visitBlock(block.getStep());
		context.visitBlock(block.getBody());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, InliningContext context) {
		checkForReturn(block, context);
		if (context.hasReturnInLoop() && block.getCondition() instanceof ISimpleExpression) {
			LoopHeaderBlockExpression loopheader = new LoopHeaderBlockExpression();
			loopheader.getBody().add(SSTUtil.returnStatement((ISimpleExpression) block.getCondition()));
			WhileLoop newBlock = (WhileLoop) block;
			newBlock.setCondition(loopheader);
		}
		block.getCondition().accept(this, context);
		context.visitBlock(block.getBody());
		context.addStatement(block);
		return null;
	}

	private void checkForReturn(IStatement statement, InliningContext context) {
		if (context.checkForReturn(statement))
			context.sethasReturnInLoop(true);
	}

	@Override
	public Void visit(IIfElseBlock block, InliningContext context) {
		block.getCondition().accept(this, context);
		context.visitBlock(block.getElse());
		context.visitBlock(block.getThen());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, InliningContext context) {
		checkForReturn(block, context);
		if (context.hasReturnInLoop() && block.getCondition() instanceof ISimpleExpression) {
			LoopHeaderBlockExpression loopheader = new LoopHeaderBlockExpression();
			loopheader.getBody().add(SSTUtil.returnStatement((ISimpleExpression) block.getCondition()));
			DoLoop newBlock = (DoLoop) block;
			newBlock.setCondition(loopheader);
		}
		block.getCondition().accept(this, context);
		context.visitBlock(block.getBody());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, InliningContext context) {
		ForEachLoop forEachLoop = (ForEachLoop) block;
		forEachLoop.setDeclaration((IVariableDeclaration) context.visit(block.getDeclaration(), context));
		block.getLoopedReference().accept(this, context);
		context.enterBlock();
		checkForReturn(block, context);
		context.visitBlock(block.getBody());
		context.sethasReturnInLoop(false);
		context.leaveBlock(Lists.newArrayList());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, InliningContext context) {
		block.getReference().accept(this, context);
		context.visitBlock(block.getDefaultSection());
		for (ICaseBlock caseBlock : block.getSections()) {
			context.visitBlock(caseBlock.getBody());
		}
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, InliningContext context) {
		stmt.getReference().accept(this, context);
		context.visitBlock(stmt.getBody());
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, InliningContext context) {
		block.getReference().accept(this, context);
		context.visitBlock(block.getBody());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, InliningContext context) {
		context.visitBlock(block.getBody());
		context.visitBlock(block.getFinally());
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			context.visitBlock(catchBlock.getBody());
		}
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, InliningContext context) {
		context.visitBlock(block.getBody());
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, InliningContext context) {
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, InliningContext context) {
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, InliningContext context) {
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, InliningContext context) {
		context.addStatement(unknownStmt);
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, InliningContext context) {
		LabelledStatement labelledStatement = (LabelledStatement) stmt;
		labelledStatement.setStatement(context.visit(stmt.getStatement(), context));
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, InliningContext context) {
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, InliningContext context) {
		stmt.getReference();
		context.addStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, InliningContext context) {
		stmt.getReference().accept(this, context);
		if (stmt.getExpression() instanceof IInvocationExpression) {
			stmt.getExpression().accept(this, context);
			IAssignableExpression assExpr = context.getReturnExpression();
			context.setReturnExpression(null);
			if (assExpr != null) {
				((EventSubscriptionStatement) stmt).setExpression(assExpr);
				context.addStatement(stmt);
			}
		} else {
			stmt.getExpression().accept(this, context);
			context.addStatement(stmt);
		}
		return null;
	}

	public Void visit(IInvocationExpression expr, InliningContext context) {
		IMethodDeclaration possibleMethod = context.getNonEntryPoint(expr.getMethodName());
		if (possibleMethod != null) {
			IMethodDeclaration method = SSTCloneUtil.clone(possibleMethod, IMethodDeclaration.class);
			context.addInlinedMethod(possibleMethod);
			List<IStatement> body = new ArrayList<>();
			Map<IVariableReference, IVariableReference> preChangedNames = new HashMap<>();

			boolean guardsNeeded = setupMethodInlining(expr, context, method, body, preChangedNames);
			// Add all Statements from the method body
			body.addAll(method.getBody());

			context.enterScope(body, preChangedNames);
			context.setInline(true);
			if (!guardsNeeded) {
				for (IStatement statement : body) {
					if (statement instanceof IReturnStatement) {
						IReturnStatement stmt = (IReturnStatement) statement;
						context.leaveScope();
						context.setInline(false);
						context.setReturnExpression(stmt.getExpression());
						return null;
					}
					statement.accept(this, context);
				}
				context.leaveScope();
				context.setInline(false);
			} else {
				context.setGuardVariableNames(method.getName());
				context.visitBlock(body);
				context.leaveScope();
				context.setInline(false);
				for (IStatement statement : body) {
					context.addStatement(statement);
				}
				if (!context.isVoid()) {
					context.setReturnExpression(SSTUtil.referenceExprToVariable(context.getResultName()));
				}
				context.setPreparedGuards(false);
			}
		} else {
			expr.getReference().accept(this, context);
			context.setReturnExpression(expr);
		}
		return null;
	}

	private boolean setupMethodInlining(IInvocationExpression expr, InliningContext context, IMethodDeclaration method,
			List<IStatement> body, Map<IVariableReference, IVariableReference> preChangedNames) {
		if (method.getName().hasParameters()) {
			createParameterVariables(body, method.getName().getParameters(), expr.getParameters(), preChangedNames);
		}
		// Checking if guards statements are needed and setting them up if
		// so
		CountReturnContext countReturnContext = new CountReturnContext();
		method.accept(new CountReturnsVisitor(), countReturnContext);
		int returnStatementCount = countReturnContext.returnCount;
		context.setVoid(countReturnContext.isVoid);
		boolean guardsNeeded = returnStatementCount > 1 ? true : false;
		if (returnStatementCount == 1 && method.getBody().size() > 0) {
			guardsNeeded = !(method.getBody().get(method.getBody().size() - 1) instanceof IReturnStatement);
		}
		if (guardsNeeded) {
			setupGuardVariables(method.getName(), body, context);
		}
		return guardsNeeded;
	}

	private void setupGuardVariables(IMethodName methodName, List<IStatement> body, InliningContext context) {
		context.setGuardVariableNames(methodName);
		context.setPreparedGuards(true);
		if (!context.isVoid()) {
			IVariableDeclaration variable = SSTUtil.declare(context.getResultName(), methodName.getReturnType());
			body.add(variable);
		}
		body.add(SSTUtil.declare(context.getGotResultName(), InliningContext.GOT_RESULT_TYPE));
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue("true");
		body.add(SSTUtil.assignmentToLocal(context.getGotResultName(), constant));

	}

	private void createParameterVariables(List<IStatement> body, List<IParameterName> parameters,
			List<ISimpleExpression> expressions, Map<IVariableReference, IVariableReference> preChangedNames) {
		for (int i = 0; i < parameters.size(); i++) {
			IParameterName parameter = parameters.get(i);
			// TODO what to do when parameter has out/ref keyWord but is
			// no ReferenceExpression ?
			if (!parameter.isOptional() || expressions.size() == parameters.size()) {

				/*
				 * if (parameter.isPassedByReference() &&
				 * !parameter.isParameterArray() && i < expressions.size() &&
				 * expressions.get(i) instanceof ReferenceExpression) {
				 * ReferenceExpression refExpr = (ReferenceExpression)
				 * expressions.get(i); if (refExpr.getReference() instanceof
				 * VariableReference) {
				 * preChangedNames.put(SSTUtil.variableReference(parameter.
				 * getName()), (IVariableReference) refExpr.getReference()); }
				 * else if (refExpr.getReference() instanceof IMemberReference)
				 * { preChangedNames.put(SSTUtil.variableReference(parameter.
				 * getName()), ((IMemberReference)
				 * refExpr.getReference()).getReference()); } continue; } else
				 * if (parameter.isParameterArray()) {
				 * body.add(SSTUtil.declare(parameter.getName(),
				 * parameter.getValueType()));
				 * body.add(SSTUtil.assigmentToLocal(parameter.getName(), new
				 * UnknownExpression())); break; } else {
				 * body.add(SSTUtil.declare(parameter.getName(),
				 * parameter.getValueType()));
				 * body.add(SSTUtil.assigmentToLocal(parameter.getName(),
				 * expressions.get(i))); }
				 */
				if (parameter.isParameterArray()) {
					body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
					body.add(SSTUtil.assignmentToLocal(parameter.getName(), createArray()));
					break;
				} else if (i < expressions.size() && expressions.get(i) instanceof ReferenceExpression) {
					ReferenceExpression refExpr = SSTCloneUtil.clone(expressions.get(i), ReferenceExpression.class);
					if (refExpr.getReference() instanceof VariableReference) {
						preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
								(IVariableReference) refExpr.getReference());
					} else if (refExpr.getReference() instanceof IMemberReference) {
						preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
								((IMemberReference) refExpr.getReference()).getReference());
					}
				} else if (i < expressions.size()) {
					body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
					body.add(SSTUtil.assignmentToLocal(parameter.getName(), expressions.get(i)));
				}
			}
		}

	}

	private IAssignableExpression createArray() {
		// TODO: implement
		return new UnknownExpression();
	}

	public Void visit(IReferenceExpression expr, InliningContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, InliningContext context) {
		for (IVariableReference ref : expr.getReferences()) {
			ref.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, InliningContext context) {
		context.enterCondition();
		context.visitBlock(expr.getBody());
		context.leaveCondition();
		if (context.hasReturnInLoop()) {
			IfElseBlock stmt = new IfElseBlock();
			stmt.setCondition(SSTUtil.refExpr(context.getGotResultName()));
			stmt.setThen(Lists.newArrayList(expr.getBody()));
			stmt.setElse(Lists.newArrayList(SSTUtil.returnStatement(SSTUtil.constant("false"))));
			expr.getBody().clear();
			expr.getBody().add(stmt);
			context.sethasReturnInLoop(false);
		}
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, InliningContext context) {
		expr.getCondition().accept(this, context);
		expr.getElseExpression().accept(this, context);
		expr.getThenExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(INullExpression expr, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, InliningContext context) {
		context.visitBlock(expr.getBody());
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, InliningContext context) {
		if (entity.getVariableReference() != null) {
			entity.getVariableReference().accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, InliningContext context) {
		expr.getOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, InliningContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ITypeCheckExpression expr, InliningContext context) {
		expr.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, InliningContext context) {
		expr.getLeftOperand().accept(this, context);
		expr.getRightOperand().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, InliningContext context) {
		for (ISimpleExpression e : expr.getIndices())
			e.accept(this, context);
		expr.getReference().accept(this, context);
		return null;
	}

	public Void visit(IVariableReference ref, InliningContext context) {
		context.resolve(ref);
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, InliningContext context) {
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, InliningContext context) {
		indexAccessRef.getExpression().accept(this, context);
		return null;
	}

}
