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
package cc.kave.commons.model.ssts.impl.visitor;

import cc.kave.commons.model.ssts.ISST;
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
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class ToStringVisitor extends AbstractThrowingNodeVisitor<StringBuilder, Void> {

	@Override
	public Void visit(ISST sst, StringBuilder sb) {
		sb.append("class ");
		sb.append(sst.getEnclosingType().getName());
		sb.append(" {\n");
		for (IEventDeclaration event : sst.getEvents())
			event.accept(this, sb);

		for (IFieldDeclaration field : sst.getFields())
			field.accept(this, sb);

		sb.append(" }\n");
		return null;
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, StringBuilder sb) {
		sb.append(stmt.getName().getDeclaringType().getName());
		sb.append(" ");
		sb.append(stmt.getName().getName());
		sb.append(";\n");
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, StringBuilder sb) {
		sb.append("\tevent ");
		sb.append(stmt.getName().getHandlerType().getName());
		sb.append(" ");
		sb.append(stmt.getName().getName());
		sb.append(";\n");
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, StringBuilder sb) {
		sb.append("\t");
		sb.append(stmt.getName().getValueType().getName());
		sb.append(" ");
		sb.append(stmt.getName().getName());
		sb.append(";\n");
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IDoLoop block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForLoop block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ITryBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(INullExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, StringBuilder sb) {
		sb.append("TODO: IEventReference");
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, StringBuilder sb) {
		sb.append("TODO: IFieldReference");
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, StringBuilder sb) {
		sb.append("TODO: IMethodReference");
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, StringBuilder sb) {
		sb.append("TODO: IPropertyReference");
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, StringBuilder sb) {
		sb.append("TODO: IVariableReference");
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, StringBuilder sb) {
		// TODO Auto-generated method stub
		return null;
	}

}
