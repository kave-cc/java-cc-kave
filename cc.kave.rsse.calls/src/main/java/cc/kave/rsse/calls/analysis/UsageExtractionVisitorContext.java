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
package cc.kave.rsse.calls.analysis;

import static cc.kave.rsse.calls.analysis.LambdaContextUtils.addLambda;
import static cc.kave.rsse.calls.analysis.LambdaContextUtils.isLambdaName;
import static cc.kave.rsse.calls.analysis.LambdaContextUtils.removeLambda;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;

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
import cc.kave.commons.model.ssts.references.IUnknownReference;
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
import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsageSite;
import cc.kave.rsse.calls.model.usages.impl.Definition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

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

	private Map<AbstractLocation, Usage> locationUsages = new HashMap<>();

	private Map<AbstractLocation, Definition> implicitDefinitions = new HashMap<>();

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

	public List<Usage> getUsages() {
		return new ArrayList<>(locationUsages.values());
	}

	public Usage getUsage(AbstractLocation location) {
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
		return isLambdaName(method) ? removeLambda(method) : method;
	}

	private Set<AbstractLocation> queryPointerAnalysis(IReference reference, ITypeName type) {
		PointsToQuery query = new PointsToQuery(reference, type, currentStatement, getMemberForPointsToQuery());
		return pointsToAnalysis.query(query);
	}

	private void createImplicitDefinitions(Context context) {
		// this
		Definition thisDefinition = Definitions.definedByThis();
		IReference thisReference = SSTBuilder.variableReference("this");
		for (AbstractLocation location : queryPointerAnalysis(thisReference, enclosingClass)) {
			implicitDefinitions.put(location, thisDefinition);
		}

		// super
		Definition superDefinition = Definitions.definedByThis();
		IReference superReference = SSTBuilder.variableReference("base");
		ITypeName superType = languageOptions.getSuperType(context.getTypeShape().getTypeHierarchy());
		for (AbstractLocation location : queryPointerAnalysis(superReference, superType)) {
			if (!implicitDefinitions.containsKey(location)) {
				implicitDefinitions.put(location, superDefinition);
			}
		}

		for (IFieldDeclaration fieldDecl : context.getSST().getFields()) {
			IFieldName field = fieldDecl.getName();
			Definition fieldDefinition = definedByMemberAccess(field);
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
			Definition propertyDefinition = definedByMemberAccess(property);
			IReference propertyRefernce = SSTBuilder.propertyReference(property);
			for (AbstractLocation location : queryPointerAnalysis(propertyRefernce, property.getValueType())) {
				// do not overwrite an existing definition by a real field
				if (!implicitDefinitions.containsKey(location)) {
					implicitDefinitions.put(location, propertyDefinition);
				}
			}
		}
	}

	private Usage initializeUsage(ITypeName type, AbstractLocation location) {
		Usage usage = new Usage();

		usage.type = type;
		usage.classCtx = classContextStack.getFirst();
		usage.methodCtx = getCurrentEnclosingMethod();

		if (location == null || !implicitDefinitions.containsKey(location)) {
			usage.definition = Definitions.definedByUnknown();
		} else {
			usage.definition = implicitDefinitions.get(location);
		}

		return usage;
	}

	private Usage getOrCreateUsage(AbstractLocation location, ITypeName type) {
		if (type == null) {
			type = Names.getUnknownType();
		}

		Usage usage = locationUsages.get(location);
		if (usage == null) {
			usage = initializeUsage(type, location);
			locationUsages.put(location, usage);
		} else if (usage.getType().isUnknown() && !type.isUnknown()) {
			// unknown types cause a lot of usages to be (wrongly) initialized
			// with insufficient type information ->
			// update these usages once we have a concrete type
			usage.type = type;
		}

		return usage;
	}

	private void updateDefinitions(PointsToQuery query, Definition newDefinition) {
		Set<AbstractLocation> locations = pointsToAnalysis.query(query);

		for (AbstractLocation location : locations) {
			Usage usage = getOrCreateUsage(location, query.getType());

			IDefinition currentDefinition = usage.definition;

			boolean currentDefinitionIsReturnOfNonEntryPoint = false;
			if (currentDefinition.getType() == DefinitionType.RETURN_VALUE) {
				IMethodName sstMethod = currentDefinition.getMember(IMethodName.class);
				IMethodDeclaration methodDecl = declarationMapper.get(sstMethod);
				currentDefinitionIsReturnOfNonEntryPoint = methodDecl != null && !methodDecl.isEntryPoint();
			}

			boolean newDefinitionHasHigherPriority = definitionSiteComparator.compare(currentDefinition,
					newDefinition) < 0;

			if (currentDefinitionIsReturnOfNonEntryPoint || newDefinitionHasHigherPriority) {
				usage.definition = newDefinition;
			}
		}
	}

	private void updateCallsites(PointsToQuery query, IUsageSite callsite) {
		Set<AbstractLocation> locations = pointsToAnalysis.query(query);

		for (AbstractLocation location : locations) {
			Usage usage = getOrCreateUsage(location, query.getType());

			usage.usageSites.add(callsite);
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
		Definition newDefinition = Definitions.definedByMethodParameter(method, argIndex);

		updateDefinitions(query, newDefinition);
	}

	private void registerMethodDefinition(Definition definitionSite, ITypeName methodType) {
		if (currentStatement instanceof IExpressionStatement) {
			// method called without saving returned value -> cannot have any
			// calls
			return;
		} else if (!(currentStatement instanceof IAssignment)) {
			LOGGER.error("Cannot register {} definition site: target is no assignment", definitionSite.getType());
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
			LOGGER.warn("Discarding {} definition site due to generic type parameter", definitionSite.getType());
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
		if (assignmentDest == null || assignmentDest instanceof IUnknownReference) {
			// constant not used in an assignment
			return;
		}

		ITypeName type = typeCollector.getType(assignmentDest);
		PointsToQuery query = new PointsToQuery(assignmentDest, type, currentStatement, getMemberForPointsToQuery());
		Definition newDefinition = Definitions.definedByConstant();

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
		Definition newDefinition = definedByMemberAccess(fieldRef.getFieldName());
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
		Definition newDefinition = definedByMemberAccess(propertyRef.getPropertyName());
		updateDefinitions(query, newDefinition);
	}

	public void registerConstructor(IMethodName method) {
		Definition newDefinition = Definitions.definedByConstructor(method);
		registerMethodDefinition(newDefinition, method.getDeclaringType());
	}

	public void registerPotentialReturnDefinitionSite(IMethodName method) {
		Definition newDefinition = Definitions.definedByReturnValue(method);
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
		IUsageSite callsite = UsageSites.callParameter(method, argIndex);

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
		IUsageSite callsite = UsageSites.call(method);

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