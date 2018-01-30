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

import static cc.kace.rsse.calls.LambdaContextUtils.addLambda;
import static cc.kace.rsse.calls.LambdaContextUtils.isLambdaName;
import static cc.kace.rsse.calls.LambdaContextUtils.removeLambda;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class UsageExtractionVisitorContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsageExtractionVisitorContext.class);

	private LanguageOptions languageOptions = LanguageOptions.getInstance();
	private DefinitionSitePriorityComparator definitionSiteComparator = new DefinitionSitePriorityComparator();

	private PointsToAnalysis pointsToAnalysis;
	private DescentStrategy descentStrategy;
	private TypeCollector typeCollector;

	private DeclarationMapper declarationMapper;

	private ITypeName enclosingClass;
	private Deque<ITypeName> classContextStack = new ArrayDeque<>();

	private Map<AbstractLocation, Query> locationUsages = new HashMap<>();

	private Map<AbstractLocation, DefinitionSite> implicitDefinitions = new HashMap<>();

	private IStatement currentStatement;
	private Callpath currentCallpath;

	public UsageExtractionVisitorContext(PointsToContext context, DescentStrategy descentStrategy) {
		this.pointsToAnalysis = context.getPointerAnalysis();
		this.descentStrategy = descentStrategy;

		this.enclosingClass = context.getTypeShape().getTypeHierarchy().getElement();
		this.classContextStack.addFirst(enclosingClass);

		this.typeCollector = new TypeCollector(context);
		this.declarationMapper = new DeclarationMapper(context);

		this.currentStatement = null;
		this.currentCallpath = null;
		createImplicitDefinitions(context);
	}

	public List<Query> getUsages() {
		return new ArrayList<>(locationUsages.values());
	}

	public Query getUsage(AbstractLocation location) {
		return locationUsages.get(location);
	}

	public void setEntryPoint(IMethodName method) {
		currentCallpath = new Callpath(method);
		currentStatement = null;

		// reset usages
		locationUsages.clear();
	}

	public void setCurrentStatement(IStatement stmt) {
		this.currentStatement = stmt;
	}

	private IReference getAssignmentSource() {
		if (currentStatement instanceof IAssignment) {
			IAssignment assignment = (IAssignment) currentStatement;
			if (assignment.getExpression() instanceof IReferenceExpression) {
				return ((IReferenceExpression) assignment.getExpression()).getReference();
			}
		}
		return null;
	}

	private IAssignableReference getAssignmentDestination() {
		if (currentStatement instanceof IAssignment) {
			return ((IAssignment) currentStatement).getReference();
		}
		return null;
	}

	public void enterNonEntryPoint(IMethodName method) {
		currentCallpath.enterMethod(method);
	}

	public void leaveNonEntryPoint() {
		currentCallpath.leaveMethod();
	}

	public void enterLambda() {
		IMethodName currentMethod = currentCallpath.getLast();
		IMethodName lambdaMethod = addLambda(currentMethod);
		currentCallpath.enterMethod(lambdaMethod);

		ITypeName lambdaClassContext = classContextStack.getFirst();
		classContextStack.addFirst(lambdaClassContext);
	}

	public void leaveLambda() {
		currentCallpath.leaveMethod();
		classContextStack.removeFirst();
	}

	private IMethodName getCurrentEnclosingMethod() {
		Iterator<IMethodName> iter = currentCallpath.reverseIterator();
		while (iter.hasNext()) {
			IMethodName method = iter.next();
			IMethodDeclaration methodDecl = declarationMapper.get(method);

			if (isLambdaName(method) || (methodDecl != null && methodDecl.isEntryPoint())) {
				return method;
			}
		}

		return currentCallpath.getFirst();
	}

	private IMemberName getMemberForPointsToQuery() {
		if (currentCallpath == null) {
			return null;
		}
		IMethodName method = currentCallpath.getLast();
		return removeLambda(method);
	}

	private Set<AbstractLocation> queryPointerAnalysis(IReference reference, ITypeName type) {
		PointsToQuery query = new PointsToQuery(reference, type, currentStatement, getMemberForPointsToQuery());
		return pointsToAnalysis.query(query);
	}

	private void createImplicitDefinitions(Context context) {
		// this
		DefinitionSite thisDefinition = DefinitionSites.createDefinitionByThis();
		IReference thisReference = SSTBuilder.variableReference(languageOptions.getThisName());
		for (AbstractLocation location : queryPointerAnalysis(thisReference, enclosingClass)) {
			implicitDefinitions.put(location, thisDefinition);
		}

		// super
		DefinitionSite superDefinition = DefinitionSites.createDefinitionByThis();
		IReference superReference = SSTBuilder.variableReference(languageOptions.getSuperName());
		ITypeName superType = languageOptions.getSuperType(context.getTypeShape().getTypeHierarchy());
		for (AbstractLocation location : queryPointerAnalysis(superReference, superType)) {
			if (!implicitDefinitions.containsKey(location)) {
				implicitDefinitions.put(location, superDefinition);
			}
		}

		for (IFieldDeclaration fieldDecl : context.getSST().getFields()) {
			IFieldName field = fieldDecl.getName();
			DefinitionSite fieldDefinition = DefinitionSites.createDefinitionByField(field);
			IReference fieldReference = SSTBuilder.fieldReference(field);
			for (AbstractLocation location : queryPointerAnalysis(fieldReference, field.getValueType())) {
				// TODO we might overwrite definitions here if two fields share
				// one location
				implicitDefinitions.put(location, fieldDefinition);
			}
		}

		// treat properties as fields if they have no custom get code
		for (IPropertyDeclaration propertyDecl : context.getSST().getProperties()) {
			if (!languageOptions.isAutoImplementedProperty(propertyDecl)) {
				continue;
			}

			IPropertyName property = propertyDecl.getName();
			DefinitionSite propertyDefinition = DefinitionSites.createDefinitionByProperty(property);
			IReference propertyRefernce = SSTBuilder.propertyReference(property);
			for (AbstractLocation location : queryPointerAnalysis(propertyRefernce, property.getValueType())) {
				// do not overwrite an existing definition by a real field
				if (!implicitDefinitions.containsKey(location)) {
					implicitDefinitions.put(location, propertyDefinition);
				}
			}
		}
	}

	private Query initializeUsage(ITypeName type, AbstractLocation location) {
		Query usage = new Query();

		usage.setType(type);
		usage.setClassContext(classContextStack.getFirst());
		usage.setMethodContext(getCurrentEnclosingMethod());

		if (location == null || !implicitDefinitions.containsKey(location)) {
			usage.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		} else {
			usage.setDefinition(implicitDefinitions.get(location));
		}

		return usage;
	}

	private Query getOrCreateUsage(AbstractLocation location, ITypeName type) {
		if (type == null) {
			type = Names.getUnknownType();
		}

		Query usage = locationUsages.get(location);
		if (usage == null) {
			usage = initializeUsage(type, location);
			locationUsages.put(location, usage);
		} else if (usage.getType().isUnknown() && !type.isUnknown()) {
			// unknown types cause a lot of usages to be (wrongly) initialized
			// with insufficient type information ->
			// update these usages once we have a concrete type
			usage.setType(type);
		}

		return usage;
	}

	private void updateDefinitions(PointsToQuery query, DefinitionSite newDefinition) {
		Set<AbstractLocation> locations = pointsToAnalysis.query(query);

		for (AbstractLocation location : locations) {
			Query usage = getOrCreateUsage(location, query.getType());

			DefinitionSite currentDefinition = usage.getDefinitionSite();

			boolean currentDefinitionIsReturnOfNonEntryPoint = false;
			if (currentDefinition.getKind() == DefinitionSiteKind.RETURN) {
				IMethodName sstMethod = currentDefinition.getMethod();
				IMethodDeclaration methodDecl = declarationMapper.get(sstMethod);
				currentDefinitionIsReturnOfNonEntryPoint = methodDecl != null && !methodDecl.isEntryPoint();
			}

			boolean newDefinitionHasHigherPriority = definitionSiteComparator.compare(currentDefinition,
					newDefinition) < 0;

			if (currentDefinitionIsReturnOfNonEntryPoint || newDefinitionHasHigherPriority) {
				usage.setDefinition(newDefinition);
			}
		}
	}

	private void updateCallsites(PointsToQuery query, CallSite callsite) {
		Set<AbstractLocation> locations = pointsToAnalysis.query(query);

		for (AbstractLocation location : locations) {
			Query usage = getOrCreateUsage(location, query.getType());

			usage.addCallSite(callsite);
		}
	}

	public void registerParameter(IMethodName method, IParameterName parameter, int argIndex) {
		// skip parameter definition if we are descending into other methods: do
		// not overwrite the definition of the top
		// most entry point method
		if (currentCallpath.size() > 1) {
			return;
		}

		PointsToQuery query = new PointsToQuery(SSTBuilder.variableReference(parameter.getName()),
				parameter.getValueType(), null, getMemberForPointsToQuery());
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByParam(method, argIndex);

		updateDefinitions(query, newDefinition);
	}

	private void registerMethodDefinition(DefinitionSite definitionSite, ITypeName methodType) {
		if (currentStatement instanceof IExpressionStatement) {
			// method called without saving returned value -> cannot have any
			// calls
			return;
		} else if (!(currentStatement instanceof IAssignment)) {
			LOGGER.error("Cannot register {} definition site: target is no assignment", definitionSite.getKind());
			return;
		}

		IAssignment assignStmt = (IAssignment) currentStatement;
		ITypeName boundType = typeCollector.getType(assignStmt.getReference());
		// fall back to the type generated by the method if no bound type can be
		// found or it is unknown
		ITypeName type;
		if (boundType != null && !boundType.isUnknown()) {
			type = boundType;
		} else if (methodType.isTypeParameter()) {
			// return type of a method can be a generic type parameter which
			// does not give us any information -> skip
			// definition
			// TODO alternatively, the type could be set to unknown. however,
			// updating the definitions for all types
			// will cause imprecision, especially for exceptions which are
			// currently mapped to an unknown definition
			LOGGER.warn("Discarding {} definition site due to generic type parameter", definitionSite.getKind());
			return;
		} else {
			type = methodType;
		}
		PointsToQuery query = new PointsToQuery(assignStmt.getReference(), type, currentStatement,
				getMemberForPointsToQuery());

		updateDefinitions(query, definitionSite);
	}

	public void registerConstant(IConstantValueExpression constExpr) {
		IReference assignmentDest = getAssignmentDestination();
		if (assignmentDest == null) {
			// constant not used in an assignment
			return;
		}

		ITypeName type = typeCollector.getType(assignmentDest);
		PointsToQuery query = new PointsToQuery(assignmentDest, type, currentStatement, getMemberForPointsToQuery());
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByConstant();

		updateDefinitions(query, newDefinition);
	}

	public void registerFieldAccess(IFieldReference fieldRef) {
		IReference assignmentSrc = getAssignmentSource();
		if (assignmentSrc == null || assignmentSrc != fieldRef) {
			// field not accessed in assignment or not read
			return;
		}

		IReference assignmentDest = getAssignmentDestination();
		ITypeName type = typeCollector.getType(assignmentDest);
		PointsToQuery query = new PointsToQuery(assignmentDest, type, currentStatement, getMemberForPointsToQuery());
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByField(fieldRef.getFieldName());
		updateDefinitions(query, newDefinition);
	}

	public void registerPropertyAccess(IPropertyReference propertyRef) {
		IReference assignmentSrc = getAssignmentSource();
		if (assignmentSrc == null || assignmentSrc != propertyRef) {
			// property not accessed in assignment or not read
			return;
		}

		IReference assignmentDest = getAssignmentDestination();
		ITypeName type = typeCollector.getType(assignmentDest);
		PointsToQuery query = new PointsToQuery(assignmentDest, type, currentStatement, getMemberForPointsToQuery());
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByProperty(propertyRef.getPropertyName());
		updateDefinitions(query, newDefinition);
	}

	public void registerConstructor(IMethodName method) {
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByConstructor(method);
		registerMethodDefinition(newDefinition, method.getDeclaringType());
	}

	public void registerPotentialReturnDefinitionSite(IMethodName method) {
		DefinitionSite newDefinition = DefinitionSites.createDefinitionByReturn(method);
		registerMethodDefinition(newDefinition, method.getReturnType());
	}

	public boolean isNonEntryPointMethod(IMethodName method) {
		IMethodDeclaration methodDecl = declarationMapper.get(method);
		return methodDecl != null && !methodDecl.isEntryPoint();
	}

	public void registerParameterCallsite(IMethodName method, IReference parameterExpr, int argIndex) {
		ITypeName type = typeCollector.getType(parameterExpr);
		// fall back to the type of the formal parameter if the type of the
		// actual one is not available
		if (type == null) {
			// special case: last parameter is declared as parameter array
			// (params)
			List<IParameterName> formalParameters = method.getParameters();
			// there are faulty SSTs that have actual parameters even though the
			// function does not have any formal
			// parameters
			if (!formalParameters.isEmpty()) {
				if (argIndex >= formalParameters.size() - 1) {
					IParameterName lastParameter = formalParameters.get(formalParameters.size() - 1);
					if (lastParameter.isParameterArray()) {
						type = lastParameter.getValueType().asArrayTypeName().getArrayBaseType();
					} else {
						type = lastParameter.getValueType();
					}
				} else {
					type = formalParameters.get(argIndex).getValueType();
				}
			}
		}

		PointsToQuery query = new PointsToQuery(parameterExpr, type, currentStatement, getMemberForPointsToQuery());
		CallSite callsite = CallSites.createParameterCallSite(method, argIndex);

		updateCallsites(query, callsite);
	}

	public void registerReceiverCallsite(IMethodName method, IReference receiver) {
		ITypeName type = typeCollector.getType(receiver);
		// fall back to the type of the declaring class if the receiver
		// reference is not bound to any type
		if (type == null) {
			type = method.getDeclaringType();
		}
		PointsToQuery query = new PointsToQuery(receiver, type, currentStatement, getMemberForPointsToQuery());
		CallSite callsite = CallSites.createReceiverCallSite(method);

		updateCallsites(query, callsite);
	}

	public void processDescent(IMethodName method, UsageExtractionVisitor visitor) {
		IMethodDeclaration methodDecl = declarationMapper.get(method);
		if (methodDecl != null && descentStrategy.descent(methodDecl, currentCallpath)) {
			LOGGER.debug("Descending into {}", method.getName());
			methodDecl.accept(visitor, this);
		}
	}

	public void processDescent(IPropertyName property) {
		IPropertyDeclaration propertyDecl = declarationMapper.get(property);
		if (propertyDecl != null) {
			if (!propertyDecl.getGet().isEmpty() || !propertyDecl.getSet().isEmpty()) {
				if (descentStrategy.descent(propertyDecl, currentCallpath)) {
					// TODO get or set statements?
				}
			}
		}
	}
}