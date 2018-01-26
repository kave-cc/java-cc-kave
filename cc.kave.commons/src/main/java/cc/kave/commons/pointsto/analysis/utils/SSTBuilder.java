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
package cc.kave.commons.pointsto.analysis.utils;

import java.util.Arrays;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class SSTBuilder {

	public static IVariableDeclaration variableDeclaration(ITypeName type, IVariableReference reference) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setType(type);
		varDecl.setReference(reference);
		return varDecl;
	}

	public static IConstantValueExpression constantValue() {
		ConstantValueExpression constantValueExpr = new ConstantValueExpression();
		constantValueExpr.setValue("");
		return constantValueExpr;
	}

	public static IVariableReference variableReference(String name) {
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier(name);
		return varRef;
	}

	public static IFieldReference fieldReference(IFieldName field) {
		IVariableReference thisReference = field.isStatic() ? new VariableReference()
				: variableReference(LanguageOptions.getInstance().getThisName());
		return fieldReference(thisReference, field);
	}

	public static IFieldReference fieldReference(IVariableReference reference, IFieldName field) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setReference(reference);
		fieldRef.setFieldName(field);
		return fieldRef;
	}

	public static IPropertyReference propertyReference(IPropertyName property) {
		IVariableReference thisReference = property.isStatic() ? new VariableReference()
				: variableReference(LanguageOptions.getInstance().getThisName());
		return propertyReference(thisReference, property);
	}

	public static IPropertyReference propertyReference(IVariableReference reference, IPropertyName property) {
		PropertyReference propertyRef = new PropertyReference();
		propertyRef.setReference(reference);
		propertyRef.setPropertyName(property);
		return propertyRef;
	}

	public static IEventReference eventReference(IEventName event) {
		EventReference eventRef = new EventReference();
		IVariableReference thisReference = variableReference(LanguageOptions.getInstance().getThisName());
		eventRef.setReference(thisReference);
		eventRef.setEventName(event);
		return eventRef;
	}

	public static IIndexAccessReference indexAccessReference(IIndexAccessExpression expr) {
		IndexAccessReference reference = new IndexAccessReference();
		reference.setExpression(expr);
		return reference;
	}

	public static IIndexAccessReference indexAccessReference(IVariableReference varRef) {
		IndexAccessExpression indexAccessExpr = new IndexAccessExpression();
		indexAccessExpr.setReference(varRef);
		indexAccessExpr.setIndices(Arrays.asList(constantValue()));
		return indexAccessReference(indexAccessExpr);
	}

	public static IParameterName parameter(String name, ITypeName type) {
		return Names.newParameter("[" + type.getIdentifier() + "] " + name);
	}

	public static IReferenceExpression referenceExpr(IReference reference) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(reference);
		return refExpr;
	}

	public static IAssignment assignment(IAssignableReference dest, IReference src) {
		return assignment(dest, referenceExpr(src));
	}

	public static IAssignment assignment(IAssignableReference dest, IAssignableExpression srcExpr) {
		Assignment assignmentStmt = new Assignment();
		assignmentStmt.setReference(dest);
		assignmentStmt.setExpression(srcExpr);
		return assignmentStmt;
	}

	public static IInvocationExpression invocationExpr(IMethodName method, IVariableReference receiver,
			ISimpleExpression... arguments) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(method);
		invocation.setReference(receiver);
		invocation.setParameters(Arrays.asList(arguments));
		return invocation;
	}
}