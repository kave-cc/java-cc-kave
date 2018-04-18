/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.ssts;

import static java.util.Arrays.asList;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class SSTUtils {

	public static final ITypeName OBJECT = Names.newType("p:object");
	public static final ITypeName STRING = Names.newType("p:string");
	public static final ITypeName INT = Names.newType("p:int");
	public static final ITypeName CHAR = Names.newType("p:char");
	public static final ITypeName VOID = Names.newType("p:void");
	public static final ITypeName BYTE_ARR1D = Names.newType("p:byte[]");
	public static final ITypeName BOOL = Names.newType("p:bool");
	public static final ITypeName FILESTREAM = Names.newType("System.IO.FileStream, mscorlib");
	public static final ITypeName ACTION = Names.newType("d:[p:void] [System.Action, mscorlib, 4.0.0.0].()");
	public static final ITypeName ACTION1 = Names
			.newType("d:[p:void] [System.Action`1[[T]], mscorlib, 4.0.0.0].([T] obj)");
	public static final ITypeName ACTION2 = Names
			.newType("d:[p:void] [System.Action`1[[T1],[T2]], mscorlib, 4.0.0.0].([T1] arg1, [T1] arg1)");
	public static final ITypeName FUNC1 = Names.newType("d:[TResult] [System.Func`1[[TResult]], mscorlib, 4.0.0.0].()");
	public static final ITypeName FUNC2 = Names
			.newType("d:[TResult] [System.Func`2[[T],[TResult]], mscorlib, 4.0.0.0].([T] arg)");
	public static final ITypeName FUNC3 = Names
			.newType("d:[TResult] [System.Func`3[[T1],[T2],[TResult]], mscorlib, 4.0.0.0].([T1] arg1, [T2] arg2)");

	public static SST sst(ITypeName enclosingType) {
		SST sst = new SST();
		sst.setEnclosingType(enclosingType);
		return sst;
	}

	public static IVariableReference varRef(String id) {
		VariableReference r = new VariableReference();
		r.setIdentifier(id);
		return r;
	}

	public static IAssignment assign(String id, IAssignableExpression expr) {
		Assignment a = new Assignment();
		a.setReference(varRef(id));
		a.setExpression(expr);
		return a;
	}

	public static IAssignment assign(IAssignableReference ref, IAssignableExpression expr) {
		Assignment a = new Assignment();
		a.setReference(ref);
		a.setExpression(expr);
		return a;
	}

	public static IVariableDeclaration varDecl(String id, ITypeName t) {
		VariableDeclaration stmt = new VariableDeclaration();
		stmt.setReference(varRef(id));
		stmt.setType(t);
		return stmt;
	}

	public static IExpressionStatement exprStmt(IAssignableExpression expr) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	public static IExpressionStatement completionStmt(ITypeName t, String token) {
		return exprStmt(completionExpr(t, token));
	}

	public static IExpressionStatement completionStmt(String id, String token) {
		return exprStmt(completionExpr(id, token));
	}

	public static ICompletionExpression completionExpr(ITypeName t, String token) {
		CompletionExpression expr = new CompletionExpression();
		expr.setTypeReference(t);
		expr.setToken(token);
		return expr;
	}

	public static ICompletionExpression completionExpr(String id, String token) {
		CompletionExpression expr = new CompletionExpression();
		expr.setObjectReference(varRef(id));
		expr.setToken(token);
		return expr;
	}

	public static IReferenceExpression refExpr(String id) {
		ReferenceExpression expr = new ReferenceExpression();
		expr.setReference(varRef(id));
		return expr;
	}

	public static IReferenceExpression refExpr(IReference ref) {
		ReferenceExpression expr = new ReferenceExpression();
		expr.setReference(ref);
		return expr;
	}

	public static IInvocationExpression invExpr(String id, IMethodName m, String... paramIds) {
		InvocationExpression expr = new InvocationExpression();
		expr.setReference(varRef(id));
		expr.setMethodName(m);
		for (String paramId : paramIds) {
			expr.getParameters().add(refExpr(paramId));
		}
		return expr;
	}

	public static IExpressionStatement invStmt(String id, IMethodName m, String... paramIds) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(invExpr(id, m, paramIds));
		return stmt;
	}

	public static IFieldReference fRef(String id, IFieldName fn) {
		FieldReference fr = new FieldReference();
		fr.setReference(varRef(id));
		fr.setFieldName(fn);
		return fr;
	}

	public static InvocationExpression invExpr(IVariableReference varRef, IMethodName m,
			IReferenceExpression... params) {
		InvocationExpression e = new InvocationExpression();
		e.setReference(varRef);
		e.setMethodName(m);
		e.getParameters().addAll(asList(params));
		return e;
	}
}