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
package cc.kave.commons.pointsto.analysis.inclusion.graph;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.inclusion.Allocator;
import cc.kave.commons.pointsto.analysis.inclusion.ConstraintResolver;
import cc.kave.commons.pointsto.analysis.inclusion.ConstructedTerm;
import cc.kave.commons.pointsto.analysis.inclusion.DeclarationLambdaStore;
import cc.kave.commons.pointsto.analysis.inclusion.LambdaTerm;
import cc.kave.commons.pointsto.analysis.inclusion.Projection;
import cc.kave.commons.pointsto.analysis.inclusion.RefTerm;
import cc.kave.commons.pointsto.analysis.inclusion.SetExpression;
import cc.kave.commons.pointsto.analysis.inclusion.SetVariable;
import cc.kave.commons.pointsto.analysis.inclusion.SetVariableFactory;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.AllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ArrayEntryAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UndefinedMemberAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UniqueAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.IndexAccessAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InvocationAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.StorageAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.ContextFactory;
import cc.kave.commons.pointsto.analysis.references.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.references.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.utils.io.Logger;

public class ConstraintGraphBuilder {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final SetVariableFactory variableFactory = new SetVariableFactory();
	private final Map<DistinctReference, SetVariable> referenceVariables = new HashMap<>();
	private final Map<AllocationSite, SetVariable> objectVariables = new HashMap<>();

	private final Map<SetExpression, ConstraintNode> constraintNodes = new HashMap<>();

	private final Function<IReference, DistinctReference> referenceResolver;
	private final ContextFactory contextFactory;

	private final ConstraintResolver constraintResolver = new ConstraintResolver(this::getNode);
	private final DeclarationMapper declMapper;
	private final Allocator allocator;
	private final DeclarationLambdaStore declLambdaStore;

	private final RefTerm staticObject;
	private final Map<IMemberName, SetVariable> staticMembers = new HashMap<>();
	private final Map<IMemberName, Set<SetVariable>> undefinedStorageMembers = new HashMap<>();
	private final Map<Pair<DistinctReference, Class<? extends ISSTNode>>, SetVariable> volatileEntities = new HashMap<>();

	public ConstraintGraphBuilder(Function<IReference, DistinctReference> referenceResolver,
			DeclarationMapper declMapper, ContextFactory contextFactory) {
		this.referenceResolver = referenceResolver;
		this.declMapper = declMapper;
		this.allocator = new Allocator(constraintResolver, variableFactory);
		this.declLambdaStore = new DeclarationLambdaStore(this::getVariable, variableFactory, allocator, declMapper);
		this.contextFactory = contextFactory;

		staticObject = new RefTerm(new UniqueAllocationSite(Names.getUnknownType()), ConstructedTerm.BOTTOM);
	}

	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	public Allocator getAllocator() {
		return allocator;
	}

	public ConstraintGraph createConstraintGraph() {
		initializeStaticMembers();
		initializeNonStaticMembers();
		return new ConstraintGraph(referenceVariables, declLambdaStore, constraintNodes,
				new HashSet<>(volatileEntities.values()), contextFactory);
	}

	private SetVariable getVariable(DistinctReference distRef) {
		Objects.requireNonNull(distRef);

		SetVariable variable = referenceVariables.get(distRef);
		if (variable == null) {
			variable = variableFactory.createReferenceVariable();
			referenceVariables.put(distRef, variable);
		}

		return variable;
	}

	public SetVariable getVariable(IVariableReference varRef) {
		// redirect super-refs to this-refs
		if ("base".equals(varRef.getIdentifier())) {
			varRef = variableReference("this");
		}
		return getVariable(referenceResolver.apply(varRef));
	}

	private SetVariable getVariable(AllocationSite allocationSite) {
		Objects.requireNonNull(allocationSite);

		SetVariable variable = objectVariables.get(allocationSite);
		if (variable == null) {
			variable = variableFactory.createObjectVariable();
			objectVariables.put(allocationSite, variable);
		}

		return variable;
	}

	private ConstraintNode getNode(SetExpression setExpr) {
		Objects.requireNonNull(setExpr);

		ConstraintNode node = constraintNodes.get(setExpr);
		if (node == null) {
			node = new ConstraintNode(setExpr);
			constraintNodes.put(setExpr, node);
		}

		return node;
	}

	private SetVariable getStaticMemberVariable(IMemberName member) {
		SetVariable var = staticMembers.get(member);
		if (var == null) {
			var = createTemporaryVariable();
			staticMembers.put(member, var);
		}
		return var;
	}

	/**
	 * Ensures that a member reference appears in the least solution of the
	 * {@link ConstraintGraph} by reading them into a variable associated to the
	 * {@link DistinctReference}.
	 */
	private void ensureStorageMemberHasVariable(IMemberReference memberRef, IMemberName member) {
		DistinctReference distRef = referenceResolver.apply(memberRef);
		if (!referenceVariables.containsKey(distRef)) {
			SetVariable temp = createTemporaryVariable();
			referenceVariables.put(distRef, temp);
			readMember(temp, memberRef, member);
		}

		// collect non-static members without a definition
		if (declMapper.get(member) == null && !member.isStatic()) {
			Set<SetVariable> recvVars = undefinedStorageMembers.get(member);
			if (recvVars == null) {
				recvVars = new HashSet<>();
				undefinedStorageMembers.put(member, recvVars);
			}
			recvVars.add(getVariable(memberRef.getReference()));
		}
	}

	private void ensureInvokablePropertyHasVariable(IPropertyReference propertyRef) {
		DistinctReference distRef = referenceResolver.apply(propertyRef);
		if (!referenceVariables.containsKey(distRef)) {
			SetVariable temp = createTemporaryVariable();
			referenceVariables.put(distRef, temp);
			invokeGetProperty(temp, propertyRef);
		}
	}

	private void ensureArrayAccessHasVariable(IIndexAccessReference arrayRef) {
		DistinctReference distRef = referenceResolver.apply(arrayRef);
		if (!referenceVariables.containsKey(distRef)) {
			SetVariable temp = createTemporaryVariable();
			referenceVariables.put(distRef, temp);
			readArray(temp, arrayRef);
		}
	}

	private void initializeStaticMembers() {
		for (Map.Entry<IMemberName, SetVariable> memberEntry : staticMembers.entrySet()) {
			if (declMapper.get(memberEntry.getKey()) != null) {
				// only initialize members that do not have a definition
				continue;
			}

			ConstraintNode memberNode = getNode(memberEntry.getValue());
			if (memberNode.getPredecessors().isEmpty()) {
				ITypeName type = memberEntry.getKey().getValueType();
				if (!allocator.allocateDelegate(type, memberEntry.getValue())) {
					AllocationSite allocationSite = new UndefinedMemberAllocationSite(memberEntry.getKey(), type);
					RefTerm obj = new RefTerm(allocationSite, getVariable(allocationSite));
					memberNode.addPredecessor(
							new ConstraintEdge(getNode(obj), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

					if (type.isArray()) {
						// provide one entry for arrays
						RefTerm arrayEntry = new RefTerm(new ArrayEntryAllocationSite(allocationSite),
								variableFactory.createObjectVariable());
						writeArrayRaw(obj, arrayEntry);
					}
				}
			}
		}
	}

	private void initializeNonStaticMembers() {
		for (Map.Entry<IMemberName, Set<SetVariable>> memberEntry : undefinedStorageMembers.entrySet()) {
			IMemberName member = memberEntry.getKey();
			for (SetVariable recv : memberEntry.getValue()) {
				ITypeName type = member.getValueType();
				if (!allocator.allocateDelegate(type, recv)) {
					AllocationSite allocationSite = new UndefinedMemberAllocationSite(member, type);
					RefTerm obj = new RefTerm(allocationSite, variableFactory.createObjectVariable());
					writeMemberRaw(recv, obj, member);

					if (allocationSite.getType().isArray()) {
						// provide one entry for arrays
						RefTerm arrayEntry = new RefTerm(new ArrayEntryAllocationSite(allocationSite),
								variableFactory.createObjectVariable());
						writeArrayRaw(obj, arrayEntry);
					}
				}
			}
		}
	}

	private void registerVolatileEntity(IIndexAccessReference indexAccessRef) {
		DistinctIndexAccessReference distRef = (DistinctIndexAccessReference) referenceResolver.apply(indexAccessRef);
		DistinctReference baseDistRef = distRef.getBaseReference();
		SetVariable setVar = getVariable(baseDistRef);
		Pair<DistinctReference, Class<? extends ISSTNode>> key = ImmutablePair.of(baseDistRef,
				IIndexAccessReference.class);
		if (!volatileEntities.containsKey(key)) {
			SetVariable content = createTemporaryVariable();
			volatileEntities.put(key, content);
			writeArrayRaw(setVar, content);
			readArrayRaw(content, setVar);
		}
	}

	private void registerVolatileEntities(ILambdaExpression lambdaExpr) {
		for (IParameterName parameter : lambdaExpr.getName().getParameters()) {
			if (!parameter.isOutput()) {
				DistinctReference distRef = new DistinctLambdaParameterReference(parameter, lambdaExpr);
				Pair<DistinctReference, Class<? extends ISSTNode>> key = ImmutablePair.of(distRef,
						ILambdaExpression.class);
				if (!volatileEntities.containsKey(key)) {
					volatileEntities.put(key, getVariable(distRef));
				}
			}
		}
	}

	public SetVariable createTemporaryVariable() {
		return variableFactory.createReferenceVariable();
	}

	public void allocate(IVariableReference dest, AllocationSite allocationSite) {
		SetVariable destSetVar = getVariable(referenceResolver.apply(dest));
		allocate(destSetVar, allocationSite);
	}

	public void allocate(SetVariable destSetVar, AllocationSite allocationSite) {
		RefTerm ref = new RefTerm(allocationSite, getVariable(allocationSite));

		ConstraintNode destNode = getNode(destSetVar);
		destNode.addPredecessor(new ConstraintEdge(getNode(ref), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void alias(IVariableReference dest, IVariableReference src) {
		SetVariable destSetVar = getVariable(referenceResolver.apply(dest));
		SetVariable srcSetVar = getVariable(referenceResolver.apply(src));

		// src ⊆ dest
		constraintResolver.addConstraint(srcSetVar, destSetVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void alias(SetVariable dest, SetVariable src) {
		// src ⊆ dest
		constraintResolver.addConstraint(src, dest, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void readMember(IVariableReference dest, IMemberReference src, IMemberName member) {
		SetVariable destSetVar = getVariable(dest);
		readMember(destSetVar, src, member);
	}

	public void readMember(SetVariable destSetVar, IMemberReference src, IMemberName member) {
		ensureStorageMemberHasVariable(src, member);

		if (member.isStatic()) {
			readStaticMember(destSetVar, member);
		} else {
			SetVariable recvSetVar = getVariable(src.getReference());
			SetVariable temp = variableFactory.createProjectionVariable();

			Projection projection = new Projection(RefTerm.class, RefTerm.READ_INDEX, temp);
			// recv ⊆ proj
			ConstraintNode recvNode = getNode(recvSetVar);
			recvNode.addSuccessor(
					new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

			// temp ⊆_m dest
			ConstraintNode tempNode = getNode(temp);
			tempNode.addSuccessor(
					new ConstraintEdge(getNode(destSetVar), new StorageAnnotation(member), ContextAnnotation.EMPTY));
		}
	}

	private void readStaticMember(SetVariable destVar, IMemberName member) {
		SetVariable memberVar = getStaticMemberVariable(member);
		// memberVar ⊆ dest
		constraintResolver.addConstraint(memberVar, destVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void readArray(IVariableReference dest, IIndexAccessReference src) {
		readArray(getVariable(dest), src);
	}

	public void readArray(SetVariable destSetVar, IIndexAccessReference src) {
		ensureArrayAccessHasVariable(src);
		registerVolatileEntity(src);

		SetVariable arraySetVar = getVariable(src.getExpression().getReference());
		SetVariable temp = variableFactory.createProjectionVariable();

		Projection projection = new Projection(RefTerm.class, RefTerm.READ_INDEX, temp);
		// array ⊆ proj
		ConstraintNode arrayNode = getNode(arraySetVar);
		arrayNode.addSuccessor(
				new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		// temp ⊆ dest
		ConstraintNode tempNode = getNode(temp);
		tempNode.addSuccessor(
				new ConstraintEdge(getNode(destSetVar), IndexAccessAnnotation.INSTANCE, ContextAnnotation.EMPTY));
	}

	private void readArrayRaw(SetVariable dest, SetExpression arrayExpr) {
		SetVariable temp = variableFactory.createProjectionVariable();

		Projection projection = new Projection(RefTerm.class, RefTerm.READ_INDEX, temp);
		// array ⊆ proj
		constraintResolver.addConstraint(arrayExpr, projection, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);

		// temp ⊆ dest
		constraintResolver.addConstraint(temp, dest, IndexAccessAnnotation.INSTANCE, ContextAnnotation.EMPTY);
	}

	public void writeMember(IMemberReference dest, IVariableReference src, IMemberName member) {
		SetVariable srcSetVar = getVariable(src);
		writeMember(dest, srcSetVar, member);
	}

	public void writeMember(IMemberReference dest, SetVariable srcSetVar, IMemberName member) {
		ensureStorageMemberHasVariable(dest, member);

		if (member.isStatic()) {
			writeStaticMember(srcSetVar, member);
		} else {
			SetVariable recvSetVar = getVariable(dest.getReference());
			SetVariable temp = variableFactory.createProjectionVariable();

			Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

			// recv ⊆ proj
			ConstraintNode recvNode = getNode(recvSetVar);
			recvNode.addSuccessor(
					new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

			// src ⊆_m temp
			ConstraintNode tempNode = getNode(temp);
			tempNode.addPredecessor(
					new ConstraintEdge(getNode(srcSetVar), new StorageAnnotation(member), ContextAnnotation.EMPTY));
		}
	}

	public void writeMemberRaw(SetVariable recv, SetExpression src, IMemberName member) {
		SetVariable temp = variableFactory.createProjectionVariable();
		Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

		// recv ⊆ proj
		ConstraintNode recvNode = getNode(recv);
		recvNode.addSuccessor(
				new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		// src ⊆_m temp
		constraintResolver.addConstraint(src, temp, new StorageAnnotation(member), ContextAnnotation.EMPTY);
	}

	private void writeStaticMember(SetVariable srcVar, IMemberName member) {
		SetVariable memberVar = getStaticMemberVariable(member);
		// src ⊆ memberVar
		constraintResolver.addConstraint(srcVar, memberVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void writeArray(IIndexAccessReference dest, IVariableReference src) {
		writeArray(dest, getVariable(src));
	}

	public void writeArray(IIndexAccessReference dest, SetVariable srcSetVar) {
		ensureArrayAccessHasVariable(dest);

		SetVariable arraySetVar = getVariable(dest.getExpression().getReference());
		writeArray(arraySetVar, srcSetVar);
	}

	public void writeArray(SetVariable arraySetVar, SetVariable srcSetVar) {
		SetVariable temp = variableFactory.createProjectionVariable();
		Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

		// array ⊆ proj
		ConstraintNode arrayNode = getNode(arraySetVar);
		arrayNode.addSuccessor(
				new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		// src ⊆ temp
		ConstraintNode tempNode = getNode(temp);
		tempNode.addPredecessor(
				new ConstraintEdge(getNode(srcSetVar), IndexAccessAnnotation.INSTANCE, ContextAnnotation.EMPTY));
	}

	private void writeArrayRaw(SetExpression arrayExpr, SetExpression srcExpr) {
		SetVariable temp = variableFactory.createProjectionVariable();
		Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

		// array ⊆ proj
		constraintResolver.addConstraint(arrayExpr, projection, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);

		// src ⊆ temp
		constraintResolver.addConstraint(srcExpr, temp, IndexAccessAnnotation.INSTANCE, ContextAnnotation.EMPTY);
	}

	public void invoke(SetVariable dest, IVariableReference recv, List<SetVariable> normalizedActualParameters,
			IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		ITypeName returnType = method.getReturnType();

		if (formalParameters.isEmpty() && !normalizedActualParameters.isEmpty()) {
			Logger.err("Attempted to invoke method {}.{} which expects zero parameters with {} actual parameters",
					method.getDeclaringType().getName(), method.getName(), normalizedActualParameters.size());
			normalizedActualParameters = Collections.emptyList();
		}

		LambdaTerm lambda = getInvocationLambda(normalizedActualParameters, dest, formalParameters, returnType);

		ConstraintNode recvNode;
		InvocationAnnotation inclusionAnnotation;
		if (method.isConstructor()) {
			// constructors use their destination as receiver and do not rely on
			// dynamic dispatch
			recvNode = getNode(dest);
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else if (method.isExtensionMethod()) {
			// assume that the first parameter is the this-reference
			if (normalizedActualParameters.isEmpty()) {
				Logger.err("Ignoring an extension method call without any parameters");
				return;
			} else if (normalizedActualParameters.get(0) == null) {
				Logger.err("Ignoring an extension method call without a valid receiver");
				return;
			}
			recvNode = getNode(normalizedActualParameters.get(0));
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else if (method.isStatic() && !method.isExtensionMethod()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
			// static methods are not dispatched virtually
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else {
			// super invocations are not subject to dynamic dispatch
			if ("base".equals(recv.getIdentifier())) {
				inclusionAnnotation = new InvocationAnnotation(method, false);
			} else {
				inclusionAnnotation = new InvocationAnnotation(method);
			}

			SetVariable recvSetVar = getVariable(recv);
			// recv ⊆_method lambda
			recvNode = getNode(recvSetVar);
		}

		recvNode.addSuccessor(new ConstraintEdge(getNode(lambda), inclusionAnnotation, ContextAnnotation.EMPTY));
	}

	public void invokeDelegate(SetVariable dest, IVariableReference delegate,
			List<SetVariable> normalizedActualParameters, IMethodName method) {
		LambdaTerm lambda = getInvocationLambda(normalizedActualParameters, dest, method.getParameters(),
				method.getReturnType());

		// delegate ⊆ lambda
		ConstraintNode delegateNode = getNode(getVariable(delegate));
		delegateNode
				.addSuccessor(new ConstraintEdge(getNode(lambda), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void invokeSetProperty(IPropertyReference propertyRef, SetVariable valueSetVar) {
		ensureInvokablePropertyHasVariable(propertyRef);

		LambdaTerm invocationLambda = LambdaTerm
				.newPropertyLambda(Arrays.asList(ConstructedTerm.BOTTOM, valueSetVar, ConstructedTerm.BOTTOM));
		ConstraintNode recvNode;
		if (propertyRef.getPropertyName().isStatic()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
		} else {
			recvNode = getNode(getVariable(propertyRef.getReference()));
		}

		// super invocations are not subject to dynamic dispatch
		boolean dynamicallyDispatched = !"base".equals(propertyRef.getReference().getIdentifier());

		// recv ⊆_property lambda
		recvNode.addSuccessor(new ConstraintEdge(getNode(invocationLambda),
				new InvocationAnnotation(propertyRef.getPropertyName(), dynamicallyDispatched),
				ContextAnnotation.EMPTY));
	}

	public void invokeGetProperty(SetVariable destSetVar, IPropertyReference propertyRef) {
		ensureInvokablePropertyHasVariable(propertyRef);

		LambdaTerm invocationLambda = LambdaTerm
				.newPropertyLambda(Arrays.asList(ConstructedTerm.BOTTOM, ConstructedTerm.BOTTOM, destSetVar));
		ConstraintNode recvNode;
		if (propertyRef.getPropertyName().isStatic()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
		} else {
			recvNode = getNode(getVariable(propertyRef.getReference()));
		}

		// super invocations are not subject to dynamic dispatch
		boolean dynamicallyDispatched = !"base".equals(propertyRef.getReference().getIdentifier());

		// recv ⊆_property lambda
		recvNode.addSuccessor(new ConstraintEdge(getNode(invocationLambda),
				new InvocationAnnotation(propertyRef.getPropertyName(), dynamicallyDispatched),
				ContextAnnotation.EMPTY));
	}

	public void storeFunction(IVariableReference dest, IMethodReference src) {
		storeFunction(getVariable(dest), src);
	}

	public void storeFunction(SetVariable destSetVar, IMethodReference src) {
		IMethodName method = src.getMethodName();
		SetVariable thisSetVar;
		if (method.isStatic() && !method.isExtensionMethod()) {
			thisSetVar = ConstructedTerm.BOTTOM;
		} else {
			thisSetVar = getVariable(src.getReference());
		}
		storeFunction(destSetVar, thisSetVar, method.getParameters(), method.getReturnType(),
				p -> new DistinctMethodParameterReference(p, method));
	}

	public SetVariable storeFunction(SetVariable dest, ILambdaExpression lambdaExpr) {
		ILambdaName lambdaName = lambdaExpr.getName();
		LambdaTerm term = storeFunction(dest, ConstructedTerm.BOTTOM, lambdaName.getParameters(),
				lambdaName.getReturnType(), p -> new DistinctLambdaParameterReference(p, lambdaExpr));

		registerVolatileEntities(lambdaExpr);

		if (!lambdaName.getReturnType().isVoidType()) {
			return term.getArgument(term.getNumberOfArguments() - 1);
		} else {
			return ConstructedTerm.BOTTOM;
		}
	}

	private LambdaTerm storeFunction(SetVariable dest, SetVariable thisSetVar, List<IParameterName> formalParameters,
			ITypeName returnType, Function<IParameterName, DistinctReference> distRefBuilder) {
		// construct lambda term
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);
		variables.add(thisSetVar);
		for (IParameterName parameter : formalParameters) {
			DistinctReference parameterDistRef = distRefBuilder.apply(parameter);
			variables.add(getVariable(parameterDistRef));
		}
		if (!returnType.isVoidType()) {
			variables.add(createTemporaryVariable());
		}
		LambdaTerm lambda = LambdaTerm.newMethodLambda(variables, formalParameters, returnType);

		// lambda ⊆ dest
		ConstraintNode destNode = getNode(dest);
		destNode.addPredecessor(
				new ConstraintEdge(getNode(lambda), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		return lambda;
	}

	public SetVariable getReturnVariable(IMemberName member) {
		LambdaTerm lambda = declLambdaStore.getDeclarationLambda(member);
		return lambda.getArgument(lambda.getNumberOfArguments() - 1);
	}

	private LambdaTerm getInvocationLambda(List<SetVariable> actualParameters, SetVariable dest,
			List<IParameterName> formalParameters, ITypeName returnType) {
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);

		variables.add(ConstructedTerm.BOTTOM);
		SetVariable parameterArrayTemporary = null;

		for (int i = 0; i < actualParameters.size(); ++i) {
			SetVariable actualParameter = actualParameters.get(i);
			int formalParameterIndex = Math.min(i, formalParameters.size() - 1);
			IParameterName formalParameter = formalParameters.get(formalParameterIndex);

			if (formalParameter.isExtensionMethodParameter()) {
				// this-parameter handled prior to loop
				continue;
			}

			if (actualParameter == null) {
				actualParameter = ConstructedTerm.BOTTOM;
			}

			if (formalParameter.isParameterArray()) {
				if (parameterArrayTemporary == null) {
					parameterArrayTemporary = initializeParameterArray(formalParameter);
					variables.add(parameterArrayTemporary);
				}

				writeArray(parameterArrayTemporary, actualParameter);
			} else if (i > formalParameters.size() - 1) {
				Logger.err("Pruning {} extra method arguments", actualParameters.size() - formalParameters.size());
				break;
			} else {
				variables.add(actualParameter);
			}
		}

		// handle unset optional parameters
		for (int i = actualParameters.size(); i < formalParameters.size(); ++i) {
			IParameterName parameter = formalParameters.get(i);
			if (parameter.isParameterArray()) {
				parameterArrayTemporary = initializeParameterArray(parameter);
				variables.add(parameterArrayTemporary);
			} else {
				variables.add(ConstructedTerm.BOTTOM);
			}
		}

		if (!returnType.isVoidType()) {
			if (dest != null) {
				variables.add(dest);
			} else {
				variables.add(ConstructedTerm.BOTTOM);
			}
		}

		return LambdaTerm.newMethodLambda(variables, formalParameters, returnType);
	}

	private SetVariable initializeParameterArray(IParameterName parameter) {
		SetVariable array = createTemporaryVariable();
		allocate(array, new UniqueAllocationSite(parameter.getValueType()));
		return array;
	}
}