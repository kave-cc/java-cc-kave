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
package cc.kave.commons.pointsto.tests;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public abstract class TestBuilder {

	public IVariableDeclaration declare(String id, ITypeName type) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setReference(variableReference(id));
		varDecl.setType(type);
		return varDecl;
	}

	public IFieldDeclaration declare(IFieldName field) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(field);
		return fieldDecl;
	}

	public IPropertyDeclaration declare(IPropertyName property) {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(property);
		propertyDecl.setGet(Collections.emptyList());
		propertyDecl.setSet(Collections.emptyList());
		return propertyDecl;
	}

	public ITypeName type(String id) {
		return Names.newType(id + ",Test");
	}

	public ITypeName voidType() {
		return Names.newType("p:void");
	}

	public ITypeName intType() {
		return Names.newType("p:int");
	}

	public IMethodName method(ITypeName declType, String name, ITypeName... parameters) {
		return method(voidType(), declType, name, parameters);
	}

	private String getParameterIdentifiers(List<ITypeName> parameterTypes) {
		return IntStream.range(0, parameterTypes.size())
				.mapToObj(i -> "[" + parameterTypes.get(i).getIdentifier() + "] p" + i)
				.collect(Collectors.joining(", "));
	}

	public IMethodName method(ITypeName retType, ITypeName declType, String name, ITypeName... parameters) {
		String parameterIdentifiers = getParameterIdentifiers(Arrays.asList(parameters));
		return Names.newMethod("[" + retType.getIdentifier() + "] [" + declType.getIdentifier() + "]." + name + "("
				+ parameterIdentifiers + ")");
	}

	public IMethodName constructor(ITypeName declType, ITypeName... parameters) {
		String parameterIdentifiers = getParameterIdentifiers(Arrays.asList(parameters));
		return Names.newMethod("[p:void] [" + declType.getIdentifier() + "]..ctor(" + parameterIdentifiers + ")");
	}

	public IFieldName field(ITypeName type, ITypeName declType, int id) {
		return Names.newField("[" + type.getIdentifier() + "] [" + declType.getIdentifier() + "].f" + id);
	}

	public IPropertyName property(ITypeName type, ITypeName declType, int id) {
		return Names
				.newProperty("get set [" + type.getIdentifier() + "] [" + declType.getIdentifier() + "].p" + id + "()");
	}

	public IConstantValueExpression constantExpr() {
		return new ConstantValueExpression();
	}

	public IAssignment assign(String dest, IAssignableExpression srcExpr) {
		Assignment assignment = new Assignment();
		assignment.setReference(variableReference(dest));
		assignment.setExpression(srcExpr);
		return assignment;
	}

	public IAssignment assign(IAssignableReference dest, IAssignableExpression src) {
		Assignment assignment = new Assignment();
		assignment.setReference(dest);
		assignment.setExpression(src);
		return assignment;
	}

	public IAssignment assign(String dest, String src) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(variableReference(src));
		Assignment assignment = new Assignment();
		assignment.setReference(variableReference(dest));
		assignment.setExpression(refExpr);
		return assignment;
	}

	public IInvocationExpression invoke(String recv, IMethodName method) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(variableReference(recv));
		invocation.setMethodName(method);
		invocation.setParameters(Collections.emptyList());
		return invocation;
	}

	public IInvocationExpression invoke(IMethodName method, ISimpleExpression... arguments) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(method);
		invocation.setParameters(Arrays.asList(arguments));
		return invocation;
	}

	public ILambdaExpression lambda(ITypeName returnType, List<ITypeName> parameterTypes, IStatement... stmts) {
		LambdaExpression lambdaExpr = new LambdaExpression();
		lambdaExpr.setBody(Arrays.asList(stmts));
		ILambdaName lambdaName = Names
				.newLambda("[" + returnType.getIdentifier() + "] (" + getParameterIdentifiers(parameterTypes) + ")");
		lambdaExpr.setName(lambdaName);
		return lambdaExpr;
	}

	public IReferenceExpression refExpr(IReference ref) {
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(ref);
		return referenceExpression;
	}

	public IExpressionStatement exprStmt(IAssignableExpression expr) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	public IForEachLoop forEachLoop(IVariableDeclaration loopVar, String source, IStatement... stmts) {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(loopVar);
		loop.setLoopedReference(variableReference(source));
		loop.setBody(Arrays.asList(stmts));
		return loop;
	}

	public IReturnStatement ret(String id) {
		ReturnStatement retStmt = new ReturnStatement();
		retStmt.setExpression(refExpr(variableReference(id)));
		retStmt.setIsVoid(false);
		return retStmt;
	}

	public Context context(ITypeName type, Set<IMethodDeclaration> methods, Set<IFieldDeclaration> fields,
			Set<IPropertyDeclaration> properties) {
		SST sst = new SST();
		sst.setEnclosingType(type);
		sst.setDelegates(Collections.emptySet());
		sst.setEvents(Collections.emptySet());
		sst.setFields(fields);
		sst.setMethods(methods);
		sst.setProperties(properties);

		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(type);
		typeShape.setTypeHierarchy(typeHierarchy);
		Set<IMemberHierarchy<IMethodName>> methodHierarchies = methods.stream().filter(IMethodDeclaration::isEntryPoint)
				.map(md -> {
					MethodHierarchy methodHierarchy = new MethodHierarchy();
					methodHierarchy.setElement(md.getName());
					return methodHierarchy;
				}).collect(Collectors.toCollection(Sets::newLinkedHashSet));
		typeShape.setMethodHierarchies(methodHierarchies);

		Context context = new Context();
		context.setSST(sst);
		context.setTypeShape(typeShape);
		return context;
	}
}