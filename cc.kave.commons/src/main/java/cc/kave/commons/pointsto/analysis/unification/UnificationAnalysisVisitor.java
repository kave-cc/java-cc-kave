/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.unification;

import java.util.List;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.analysis.exceptions.MissingVariableException;
import cc.kave.commons.pointsto.analysis.exceptions.UndeclaredVariableException;
import cc.kave.commons.pointsto.analysis.references.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.unification.locations.FunctionLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.ReferenceLocation;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.pointsto.analysis.visitors.ScopingVisitor;
import cc.kave.commons.utils.io.Logger;

public class UnificationAnalysisVisitor extends ScopingVisitor<UnificationAnalysisVisitorContext, Void> {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	private SimpleExpressionVisitor simpleExpressionVisitor = new SimpleExpressionVisitor();
	private MethodParameterVisitor parameterVisitor = new MethodParameterVisitor();

	@Override
	public Void visit(ISST sst, UnificationAnalysisVisitorContext context) {
		super.visit(sst, context);
		context.finalizeAnalysis();
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration decl, UnificationAnalysisVisitorContext context) {
		context.enterMember(decl.getName());
		try {
			super.visit(decl, context);
		} finally {
			context.leaveMember();
		}
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration decl, UnificationAnalysisVisitorContext context) {
		context.enterMember(decl.getName());
		try {
			super.visit(decl, context);
		} finally {
			context.leaveMember();
		}
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, UnificationAnalysisVisitorContext context) {
		context.enterScope();
		block.getLoopedReference().accept(this, context);
		context.declareVariable(block.getDeclaration());

		try {
			IVariableReference srcRef = block.getLoopedReference();
			IVariableReference destRef = block.getDeclaration().getReference();
			List<IStatement> emulationStmts = languageOptions.emulateForEachVariableAssignment(destRef, srcRef,
					context.getDistinctReference(srcRef).getType());
			context.enterScope();
			for (IStatement stmt : emulationStmts) {
				stmt.accept(this, context);
			}
			context.leaveScope();

			visitStatements(block.getBody(), context);
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err("Failed to process a for each loop: {}", ex.getMessage());
		} finally {
			context.leaveScope();
		}

		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, UnificationAnalysisVisitorContext context) {
		try {
			super.visit(stmt, context);
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err("Failed to process an expression statement: {}", ex.getMessage());
		}

		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, UnificationAnalysisVisitorContext context) {
		// a flow-insensitive analysis is only interested in adding observers
		if (stmt.getOperation() == EventSubscriptionOperation.Add) {
			IAssignment assignment = new EventSubscriptionAssignment(stmt);
			return assignment.accept(this, context);
		}

		return null;
	}

	@Override
	public Void visit(IAssignment stmt, UnificationAnalysisVisitorContext context) {
		context.setLastAssignment(stmt);

		IAssignableReference destRef = stmt.getReference();
		IAssignableExpression srcExpr = stmt.getExpression();

		if (destRef instanceof IUnknownReference) {
			Logger.err("Ignoring assignment to an unknown reference");
			return null;
		}

		try {
			if (srcExpr instanceof ISimpleExpression || srcExpr instanceof IIndexAccessExpression) {
				srcExpr.accept(simpleExpressionVisitor, new ContextReferencePair(context, destRef));
			} else {
				// visit expressions (invocation, lambda, ...)
				return super.visit(stmt, context);
			}

		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err("Failed to process an assignment: {}", ex.getMessage());
		}

		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, UnificationAnalysisVisitorContext context) {
		// protect against isConstructor/getSignature bug in MethodName
		try {
			IMethodName method = entity.getMethodName();

			IAssignableReference destRef = context.getDestinationForExpr(entity);

			if (method.isUnknown()) {
				// assume that unknown methods return a newly allocated object
				if (destRef != null) {
					context.allocate(destRef);
				}
				return super.visit(entity, context);
			}

			List<ReferenceLocation> parameterLocations;
			ReferenceLocation returnLocation;

			if (method.isConstructor() && destRef != null) {
				context.allocate(destRef);
			}

			if (languageOptions.isDelegateInvocation(method)) {
				// TODO detect a bug where a delegate stored in a property of
				// the class is invoked by using 'this' as
				// the
				// receiver
				if (entity.getReference().getIdentifier().equals(languageOptions.getThisName())) {
					Logger.err("Skipping malformed delegate invocation");
					return null;
				}
				FunctionLocation functionLocation = context.invokeDelegate(entity);
				parameterLocations = functionLocation.getParameterLocations();
				returnLocation = functionLocation.getReturnLocation();
			} else {
				parameterLocations = context.getMethodParameterLocations(method);
				returnLocation = context.getMethodReturnLocation(method);
			}

			List<ISimpleExpression> parameters = entity.getParameters();
			int numberOfFormalParameters = method.getParameters().size();
			if ((numberOfFormalParameters == 0 || parameterLocations.isEmpty()) && !parameters.isEmpty()) {
				Logger.err("Attempted to invoke method {}.{} which expects zero parameters with {} actual parameters",
						method.getDeclaringType().getName(), method.getName(), parameters.size());
			} else {
				for (int i = 0; i < parameters.size(); ++i) {
					// due to parameter arrays the number of actual parameters
					// can be greater than the number for formal
					// parameters
					int formalParameterIndex = Math.min(i, numberOfFormalParameters - 1);
					ReferenceLocation formalParameterLocation = parameterLocations.get(formalParameterIndex);
					IParameterName formalParameter = method.getParameters().get(formalParameterIndex);

					ISimpleExpression parameterExpr = parameters.get(i);
					if (parameterExpr instanceof IReferenceExpression) {
						IReference parameterRef = ((IReferenceExpression) parameterExpr).getReference();

						if (parameterRef instanceof IUnknownReference) {
							Logger.log("Skipping unknown parameter reference");
							continue;
						}

						// parameter arrays are treated separately: write actual
						// parameters into the array
						if (formalParameter.isParameterArray()) {
							IIndexAccessReference indexAccessRef = SSTBuilder
									.indexAccessReference(SSTBuilder.variableReference(formalParameter.getName()));
							DistinctIndexAccessReference distRef = new DistinctIndexAccessReference(indexAccessRef,
									new DistinctMethodParameterReference(formalParameter, method));
							context.writeArray(distRef, parameterRef);
						} else {

							parameterRef.accept(parameterVisitor,
									new ContextLocationPair(context, formalParameterLocation));
						}
					} else if (parameterExpr instanceof IConstantValueExpression) {
						if (formalParameter.isParameterArray()) {
							ReferenceLocation tempLoc = context.createSimpleReferenceLocation();
							context.allocate(tempLoc);

							IIndexAccessReference indexAccessRef = SSTBuilder
									.indexAccessReference(SSTBuilder.variableReference(formalParameter.getName()));
							DistinctIndexAccessReference distRef = new DistinctIndexAccessReference(indexAccessRef,
									new DistinctMethodParameterReference(formalParameter, method));

							context.writeArray(distRef, tempLoc);
						} else {
							context.allocate(formalParameterLocation);
						}
					}
				}
			}

			if (!method.isStatic() && !entity.getReference().isMissing()) {
				// ensure a location is created for the receiver: we might end
				// up without one if the variable is not
				// used
				// anywhere else
				context.getLocation(entity.getReference());
			}

			if (destRef != null && !method.getReturnType().isVoidType()) {
				context.storeReturn(destRef, returnLocation);
			}

			return super.visit(entity, context);
		} catch (RuntimeException ex) {
			if (ex.getMessage().startsWith("Invalid Signature Syntax")) {
				Logger.err("Ignoring invocation expression due to MethodName.getSignature bug");
				return null;
			} else {
				throw ex;
			}
		}
	}

	@Override
	public Void visit(ILambdaExpression expr, UnificationAnalysisVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);
		context.enterLambda(expr);
		try {
			super.visit(expr, context);
			if (destRef != null) {
				context.storeFunction(destRef, expr);
			}
		} finally {
			context.leaveLambda();
		}
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, UnificationAnalysisVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);

		if (destRef != null) {
			IVariableReference srcVarRef = expr.getReference();
			context.alias(destRef, srcVarRef);
		}

		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, UnificationAnalysisVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);

		if (destRef != null) {
			ContextReferencePair crPair = new ContextReferencePair(context, destRef);
			expr.getThenExpression().accept(simpleExpressionVisitor, crPair);
			expr.getElseExpression().accept(simpleExpressionVisitor, crPair);
		}

		return null;
	}

	@Override
	public Void visit(IBinaryExpression expr, UnificationAnalysisVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);
		if (destRef != null) {
			context.allocate(destRef);
		}
		return null;
	}

	@Override
	public Void visit(IUnaryExpression expr, UnificationAnalysisVisitorContext context) {
		IAssignableReference destRef = context.getDestinationForExpr(expr);
		if (destRef != null) {
			context.allocate(destRef);
		}
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, UnificationAnalysisVisitorContext context) {
		ISimpleExpression expr = stmt.getExpression();
		if (expr instanceof IReferenceExpression) {
			try {
				IReference ref = ((IReferenceExpression) expr).getReference();
				context.registerReturnedReference(ref);
			} catch (MissingVariableException | UndeclaredVariableException ex) {
				Logger.err("Failed to process an assignment: %s", ex.getMessage());
			}
		}

		return null;
	}
}
