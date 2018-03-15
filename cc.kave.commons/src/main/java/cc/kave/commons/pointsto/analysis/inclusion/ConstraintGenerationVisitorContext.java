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
package cc.kave.commons.pointsto.analysis.inclusion;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.eventReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.fieldReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.propertyReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.AllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ArrayEntryAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.CatchBlockAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ContextAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.EntryPointAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.EntryPointMemberAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ExprAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.StmtAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UndefinedMemberAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.ContextFactory;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraph;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraphBuilder;
import cc.kave.commons.pointsto.analysis.references.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;
import cc.kave.commons.utils.io.Logger;

public class ConstraintGenerationVisitorContext extends DistinctReferenceVisitorContext {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final ITypeName thisType;

	private final DistinctReferenceCreationVisitor distinctReferenceCreationVisitor = new DistinctReferenceCreationVisitor();
	private final SimpleExpressionReader simpleExpressionReader;
	private final AssignableReferenceWriter destinationReferenceWriter;

	private final DeclarationMapper declMapper;
	private final PropertyAsFieldPredicate treatPropertyAsField;

	private final ConstraintGraphBuilder builder;

	private IMemberName currentMember;
	private IAssignment lastAssignment;

	private final Deque<Pair<ILambdaExpression, SetVariable>> lambdaStack = new ArrayDeque<>();

	private SetVariable contextThisVariable;

	public ConstraintGenerationVisitorContext(Context context, ContextFactory contextFactory) {
		super(context, ThisReferenceOption.PER_MEMBER);
		thisType = context.getTypeShape().getTypeHierarchy().getElement();
		declMapper = new DeclarationMapper(context);
		builder = new ConstraintGraphBuilder(this::getDistinctReference, declMapper, contextFactory);

		treatPropertyAsField = new PropertyAsFieldPredicate(declMapper);
		simpleExpressionReader = new SimpleExpressionReader(builder, treatPropertyAsField);
		destinationReferenceWriter = new AssignableReferenceWriter(builder, treatPropertyAsField);

		initializeContext(context);
	}

	private void initializeContext(Context context) {
		DistinctKeywordReference thisDistRef = new DistinctKeywordReference("this", thisType);
		namesToReferences.create("this", thisDistRef);
		AllocationSite thisAllocationSite = new ContextAllocationSite(context);
		builder.allocate(thisDistRef.getReference(), thisAllocationSite);
		contextThisVariable = builder.getVariable(thisDistRef.getReference());

		// generate a super-entry for querying, should not be used by the
		// analysis itself
		DistinctKeywordReference superDistRef = new DistinctKeywordReference("base",
				languageOptions.getSuperType(context.getTypeShape().getTypeHierarchy()));
		namesToReferences.enter();
		namesToReferences.create("base", superDistRef);
		builder.alias(superDistRef.getReference(), thisDistRef.getReference());
		namesToReferences.leave();

		ConstructorMemberInitializationVisitor memberInitializationVisitor = new ConstructorMemberInitializationVisitor(
				treatPropertyAsField);
		Set<IMemberName> constructorInitializedMembers = new HashSet<>();

		// call entry points
		for (IMethodDeclaration entryPointDecl : context.getSST().getEntryPoints()) {
			IMethodName method = entryPointDecl.getName();
			List<IParameterName> formalParameters = method.getParameters();
			List<SetVariable> actualParameters = new ArrayList<>(formalParameters.size());
			for (IParameterName parameter : formalParameters) {
				actualParameters.add(initializeMethodArgument(method, parameter));
			}

			SetVariable dest = null;
			if (method.isConstructor()) {
				// constructor invocations require an allocated destination
				dest = builder.createTemporaryVariable();
				builder.allocate(dest, thisAllocationSite);

				// collect initialized members
				entryPointDecl.accept(memberInitializationVisitor, constructorInitializedMembers);
			}

			builder.invoke(dest, thisDistRef.getReference(), actualParameters, method);
		}

		initializeMissingMembers(constructorInitializedMembers);
	}

	private SetVariable initializeMethodArgument(IMethodName method, IParameterName parameter) {
		SetVariable parameterVar = builder.createTemporaryVariable();
		ITypeName type = parameter.getValueType();

		if (!builder.getAllocator().allocateDelegate(type, parameterVar)) {
			EntryPointAllocationSite parameterAllocationSite = new EntryPointAllocationSite(method, parameter);
			builder.allocate(parameterVar, parameterAllocationSite);
			if (type.isArray()) {
				builder.getAllocator().allocateArrayEntry(parameterAllocationSite, type.asArrayTypeName(),
						parameterVar);
			} else if (type.equals(thisType)) {
				for (IMemberName member : declMapper.getAssignableMembers()) {
					SetVariable memberVar = builder.createTemporaryVariable();
					ITypeName memberType = member.getValueType();

					if (memberType.isDelegateType()) {
						builder.getAllocator().allocateDelegate((IDelegateTypeName) memberType, memberVar);
					} else {
						AllocationSite memberAllocationSite = new EntryPointMemberAllocationSite(
								parameterAllocationSite, member);
						builder.allocate(memberVar, memberAllocationSite);

						if (memberType.isArray()) {
							builder.getAllocator().allocateArrayEntry(memberAllocationSite,
									memberType.asArrayTypeName(), memberVar);
						}
					}

					builder.writeMemberRaw(parameterVar, memberVar, member);
				}
			}
		}

		return parameterVar;
	}

	private void initializeMissingMembers(Set<IMemberName> constructorInitializedMembers) {
		// approximate the members which are directly initialized and initialize
		// them
		Set<IMemberName> uninitializedMembers = new HashSet<>(declMapper.getAssignableMembers());
		uninitializedMembers.removeAll(constructorInitializedMembers);
		for (IMemberName member : uninitializedMembers) {
			IMemberReference memberRef = null;
			if (member instanceof IFieldName) {
				memberRef = fieldReference((IFieldName) member);
			} else if (member instanceof IPropertyName) {
				IPropertyName property = (IPropertyName) member;
				if (treatPropertyAsField.test(property)) {
					memberRef = propertyReference(property);
				}
			} else if (member instanceof IEventName) {
				memberRef = eventReference((IEventName) member);
			} else {
				throw new UnexpectedNameException(member);
			}

			if (memberRef != null) {
				SetVariable temp = builder.createTemporaryVariable();
				AllocationSite allocationSite = new UndefinedMemberAllocationSite(member, member.getValueType());
				builder.allocate(temp, allocationSite);
				builder.writeMember(memberRef, temp, member);

				if (allocationSite.getType().isArray()) {
					// provide one array entry
					SetVariable arrayEntry = builder.createTemporaryVariable();
					builder.allocate(arrayEntry, new ArrayEntryAllocationSite(allocationSite));
					builder.writeArray(temp, arrayEntry);
				}
			}
		}
	}

	public ConstraintGraph createConstraintGraph() {
		return builder.createConstraintGraph();
	}

	private DistinctReference getDistinctReference(IReference ref) {
		return ref.accept(distinctReferenceCreationVisitor, namesToReferences);
	}

	public void setLastAssignment(IAssignment assignment) {
		this.lastAssignment = assignment;
	}

	public IAssignableReference getDestinationForExpr(IExpression expr) {
		if (lastAssignment == null || lastAssignment.getReference() instanceof IUnknownReference
				|| lastAssignment.getExpression() != expr) {
			return null;
		} else {
			return lastAssignment.getReference();
		}
	}

	@Override
	public void enterMember(IMemberName member) {
		super.enterMember(member);
		// connect context-this to method-this
		builder.alias(builder.getVariable(variableReference("this")), contextThisVariable);
		// enter member, operations above happen in the global context
		this.currentMember = member;
	}

	@Override
	public void leaveMember() {
		super.leaveMember();
		this.currentMember = null;
	}

	public void enterLambda(ILambdaExpression lambdaExpr) {
		IAssignableReference destRef = getDestinationForExpr(lambdaExpr);

		SetVariable tempDest = (destRef != null) ? builder.createTemporaryVariable() : ConstructedTerm.BOTTOM;
		SetVariable returnVar = builder.storeFunction(tempDest, lambdaExpr);
		lambdaStack.addFirst(ImmutablePair.of(lambdaExpr, returnVar));

		if (destRef != null) {
			assign(destRef, tempDest);
		}
	}

	public void leaveLambda() {
		lambdaStack.removeFirst();
	}

	public ConstraintGraphBuilder getBuilder() {
		return builder;
	}

	public void assignForEachVariable(IVariableReference destRef, IVariableReference srcRef,
			ConstraintGenerationVisitor visitor) {
		List<IStatement> emulationStmts = languageOptions.emulateForEachVariableAssignment(destRef, srcRef,
				getDistinctReference(srcRef).getType());
		enterScope();
		for (IStatement stmt : emulationStmts) {
			stmt.accept(visitor, this);
		}
		leaveScope();
	}

	public void assign(IAssignableReference dest, IReference src) {
		SetVariable srcSetVar = simpleExpressionReader.read(src);
		if (srcSetVar != null) {
			destinationReferenceWriter.assign(dest, srcSetVar);
		}
	}

	public void assign(IAssignableReference dest, ISimpleExpression src) {
		SetVariable srcSetVar = simpleExpressionReader.read(src);
		if (srcSetVar != null) {
			destinationReferenceWriter.assign(dest, srcSetVar);
		}
	}

	public void assign(IAssignableReference dest, SetVariable srcSetVar) {
		destinationReferenceWriter.assign(dest, srcSetVar);
	}

	public void expressionAllocation(IExpression expr) {
		IAssignableReference destRef = getDestinationForExpr(expr);
		if (destRef != null) {
			SetVariable temp = builder.createTemporaryVariable();
			builder.allocate(temp, new ExprAllocationSite(expr));
			assign(destRef, temp);
		}
	}

	public void invoke(IInvocationExpression invocation) {
		IAssignableReference destRef = getDestinationForExpr(invocation);
		SetVariable tempDest = (destRef != null) ? builder.createTemporaryVariable() : null;
		IMethodName method = invocation.getMethodName();

		if (method.isConstructor()) {
			if (tempDest == null) {
				// constructor invocation needs an allocated destination
				tempDest = builder.createTemporaryVariable();
			}
			AllocationSite allocationSite = new StmtAllocationSite(lastAssignment);
			builder.allocate(tempDest, allocationSite);
			if (allocationSite.getType().isArray()) {
				ITypeName baseType = allocationSite.getType().asArrayTypeName().getArrayBaseType();
				if (baseType.isStructType()) {
					// elements in a struct array are allocated at allocation
					// time of the array
					SetVariable arrayEntry = builder.createTemporaryVariable();
					builder.allocate(arrayEntry, new ArrayEntryAllocationSite(allocationSite));
					builder.writeArray(tempDest, arrayEntry);
				}
			}
		}

		// sometimes the context analysis screws up and a method is called with
		// less arguments than it has formal
		// parameters
		// unfortunately, we cannot assume that the positions of available
		// arguments match the intended formal
		// parameters as arguments might be missing at any position
		List<IParameterName> formalParameters = method.getParameters();
		int parameterDiff = invocation.getParameters().size() - formalParameters.size()
				+ languageOptions.countOptionalParameters(formalParameters);
		if (parameterDiff < 0) {
			Logger.err("Skipping a method invocation which has less arguments than formal parameters");
		} else {
			List<SetVariable> actualParameters = simpleExpressionReader.read(invocation.getParameters());
			if (languageOptions.isDelegateInvocation(method)) {
				builder.invokeDelegate(tempDest, invocation.getReference(), actualParameters, method);
			} else {
				builder.invoke(tempDest, invocation.getReference(), actualParameters, method);
			}
		}

		if (destRef != null) {
			assign(destRef, tempDest);
		}
	}

	public void registerReturnedExpression(ISimpleExpression expr) {
		SetVariable returnedValue = simpleExpressionReader.read(expr);
		if (returnedValue != null) {
			if (lambdaStack.isEmpty()) {
				builder.alias(builder.getReturnVariable(currentMember), returnedValue);
			} else {
				builder.alias(lambdaStack.getFirst().getValue(), returnedValue);
			}
		}
	}

	@Override
	public void declareParameter(IParameterName parameter, ICatchBlock catchBlock) {
		super.declareParameter(parameter, catchBlock);
		builder.allocate(variableReference(parameter.getName()), new CatchBlockAllocationSite(catchBlock));
	}
}