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
package cc.kave.commons.pointsto.extraction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class UsageExtractionVisitor extends TraversingVisitor<UsageExtractionVisitorContext, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsageExtractionVisitorContext.class);

	@Override
	public Void visit(IMethodDeclaration stmt, UsageExtractionVisitorContext context) {
		if (stmt.isEntryPoint()) {
			visitEntryPoint(stmt, context);
		} else {
			visitNonEntryPoint(stmt, context);
		}

		return null;
	}

	private void visitMethod(IMethodDeclaration methodDecl, UsageExtractionVisitorContext context) {
		IMethodName method = methodDecl.getName();
		List<IParameterName> parameters = method.getParameters();
		for (int i = 0; i < parameters.size(); ++i) {
			context.registerParameter(method, parameters.get(i), i);
		}

		visitStatements(methodDecl.getBody(), context);
	}

	public void visitEntryPoint(IMethodDeclaration methodDecl, UsageExtractionVisitorContext context) {
		context.setEntryPoint(methodDecl.getName());

		visitMethod(methodDecl, context);
	}

	public void visitNonEntryPoint(IMethodDeclaration methodDecl, UsageExtractionVisitorContext context) {
		context.enterNonEntryPoint(methodDecl.getName());
		visitMethod(methodDecl, context);
		context.leaveNonEntryPoint();
	}

	@Override
	public Void visit(ILambdaExpression expr, UsageExtractionVisitorContext context) {
		context.enterLambda();
		super.visit(expr, context);
		context.leaveLambda();

		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, UsageExtractionVisitorContext context) {
		IMethodName method = entity.getMethodName();

		if (method.isUnknown()) {
			LOGGER.debug("Skipping unknown method call");
			return null;
		}

		// do not register call sites for non entry point (private) methods of
		// the current class
		if (!context.isNonEntryPointMethod(method)) {

			// static methods and constructors do not have any receiver objects
			if (!method.isStatic() && !method.isConstructor()) {
				context.registerReceiverCallsite(method, entity.getReference());
			}

			for (int i = 0; i < entity.getParameters().size(); ++i) {
				ISimpleExpression parameterExpr = entity.getParameters().get(i);

				// TODO ignore constant, null and unknown parameters?
				if (parameterExpr instanceof IReferenceExpression) {
					IReferenceExpression refExpr = (IReferenceExpression) parameterExpr;
					context.registerParameterCallsite(method, refExpr.getReference(), i);
				}
			}
		}

		if (method.isConstructor()) {
			context.registerConstructor(method);
		} else {
			context.registerPotentialReturnDefinitionSite(method);
		}
		// TODO what about definition by ref/out parameters?

		// descend into method if applicable
		context.processDescent(method, this);

		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, UsageExtractionVisitorContext context) {
		context.registerConstant(expr);
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, UsageExtractionVisitorContext context) {
		context.registerFieldAccess(fieldRef);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, UsageExtractionVisitorContext context) {
		context.registerPropertyAccess(propertyRef);

		// descend into property if applicable
		context.processDescent(propertyRef.getPropertyName());

		return super.visit(propertyRef, context);
	}

	@Override
	public Void visit(IAssignment stmt, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IBreakStatement stmt, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IContinueStatement stmt, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IReturnStatement stmt, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IExpressionStatement stmt, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IDoLoop block, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IForEachLoop block, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IForLoop block, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IWhileLoop block, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(ISwitchBlock block, UsageExtractionVisitorContext context) {
		context.setCurrentStatement(block);
		return super.visit(block, context);
	}
}