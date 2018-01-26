/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.types;

import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class TypeCollectorVisitor extends TraversingVisitor<TypeCollectorVisitorContext, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, TypeCollectorVisitorContext context) {
		context.enterMethod(stmt);
		super.visit(stmt, context);
		context.leaveMethod();

		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, TypeCollectorVisitorContext context) {
		ILambdaName lambda = expr.getName();
		context.collectType(lambda.getReturnType());

		context.enterScope();
		for (IParameterName parameter : lambda.getParameters()) {
			context.declareParameter(parameter);
		}
		visitStatements(expr.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, TypeCollectorVisitorContext context) {
		if (!stmt.getGet().isEmpty()) {
			context.enterScope();
			visitStatements(stmt.getGet(), context);
			context.leaveScope();
		}

		if (!stmt.getSet().isEmpty()) {
			context.enterScope();
			IVariableDeclaration parameter = SSTUtil.declareVar(
					LanguageOptions.getInstance().getPropertyParameterName(), stmt.getName().getValueType());
			context.declareVariable(parameter);
			visitStatements(stmt.getSet(), context);
			context.leaveScope();
		}

		return null;
	}

	@Override
	public Void visit(ITryBlock block, TypeCollectorVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			context.enterScope();

			// only default catch blocks have a usable parameter, the other
			// kinds do not bind the caught exception to a
			// usable name
			if (catchBlock.getKind() == CatchBlockKind.Default) {
				context.declareParameter(catchBlock.getParameter());
			}

			visitStatements(catchBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getFinally(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, TypeCollectorVisitorContext context) {
		block.getReference().accept(this, context);

		for (ICaseBlock caseBlock : block.getSections()) {
			context.enterScope();
			caseBlock.getLabel().accept(this, context);
			visitStatements(caseBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getDefaultSection(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, TypeCollectorVisitorContext context) {
		block.getCondition().accept(this, context);

		context.enterScope();
		visitStatements(block.getThen(), context);
		context.leaveScope();

		context.enterScope();
		visitStatements(block.getElse(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IForLoop block, TypeCollectorVisitorContext context) {
		context.enterScope();
		visitStatements(block.getInit(), context);
		block.getCondition().accept(this, context);
		visitStatements(block.getBody(), context);
		visitStatements(block.getStep(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IForEachLoop block, TypeCollectorVisitorContext context) {
		context.enterScope();
		block.getLoopedReference().accept(this, context);
		block.getDeclaration().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IUsingBlock block, TypeCollectorVisitorContext context) {
		context.enterScope();
		block.getReference().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IDoLoop block, TypeCollectorVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();
		block.getCondition().accept(this, context);

		return null;
	}

	@Override
	public Void visit(IWhileLoop block, TypeCollectorVisitorContext context) {
		block.getCondition().accept(this, context);
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, TypeCollectorVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, TypeCollectorVisitorContext context) {
		context.declareVariable(stmt);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, TypeCollectorVisitorContext context) {
		IMethodName method = entity.getMethodName();
		context.collectType(method.getDeclaringType());
		context.collectType(method.getReturnType());

		for (IParameterName parameter : method.getParameters()) {
			context.collectType(parameter.getValueType());
		}

		return super.visit(entity, context);
	}

	@Override
	public Void visit(IFieldReference fieldRef, TypeCollectorVisitorContext context) {
		fieldRef.getReference().accept(this, context);
		context.useFieldReference(fieldRef);
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, TypeCollectorVisitorContext context) {
		context.useVariableReference(varRef);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, TypeCollectorVisitorContext context) {
		propertyRef.getReference().accept(this, context);
		context.usePropertyReference(propertyRef);
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, TypeCollectorVisitorContext context) {
		indexAccessRef.getExpression().getReference().accept(this, context);
		context.useIndexAccessReference(indexAccessRef);
		return null;
	}
}