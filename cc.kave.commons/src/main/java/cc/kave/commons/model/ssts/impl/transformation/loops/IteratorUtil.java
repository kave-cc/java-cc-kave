/**
 * Copyright 2016 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class IteratorUtil {
	public static final String BUNDLE = "jre, 1.6";
	public static final String ITER_TYPE = "java.util.Iterator";
	public static final String ITER_INV = "iterator";
	public static final String ITER_HAS_NEXT = "hasNext";
	public static final String ITER_GET_NEXT = "next";

	public static final Map<String, String> java2CSharp;

	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put(BUNDLE, "mscorlib, 4.0.0");
		map.put(ITER_TYPE, "System.Collections.Generic.IEnumerator");
		map.put(ITER_INV, "GetEnumerator");
		map.put(ITER_HAS_NEXT, "MoveNext");
		map.put(ITER_GET_NEXT, "get [?] [?].Current()"); // C# property
		java2CSharp = Collections.unmodifiableMap(map);
	}

	// ------------------------- iterator type --------------------------------

	public static ITypeName iteratorType(ITypeName paramType) {
		return iteratorType(paramType, true);
	}

	public static ITypeName iteratorType(ITypeName paramType, boolean java) {
		String name = java ? ITER_TYPE : java2CSharp.get(ITER_TYPE);
		String bundle = java ? BUNDLE : java2CSharp.get(BUNDLE);
		return Names.newType(name + "`1[[T -> " + paramType.getFullName() + "]], " + bundle);
	}

	// ----------------------- iterator invocation ----------------------------

	public static IInvocationExpression iteratorInvocation(IVariableReference ref) {
		return invocation(ref, ITER_INV, true);
	}

	public static IInvocationExpression iteratorInvocation(IVariableReference ref, boolean java) {
		return invocation(ref, ITER_INV, java);
	}

	// ------------------------ iterator has next -----------------------------

	public static IInvocationExpression hasNext(IVariableReference ref) {
		return invocation(ref, ITER_HAS_NEXT, true);
	}

	public static IInvocationExpression hasNext(IVariableReference ref, boolean java) {
		return invocation(ref, ITER_HAS_NEXT, java);
	}

	// ------------------------ iterator get next -----------------------------

	public static IAssignableExpression getNext(IVariableReference ref) {
		return getNext(ref, true);
	}

	public static IAssignableExpression getNext(IVariableReference ref, boolean java) {
		IInvocationExpression javaGetNext = invocation(ref, ITER_GET_NEXT);
		IReferenceExpression cSharpNext = refExpr(propertyReference(ref, java2CSharp.get(ITER_GET_NEXT)));
		return java ? javaGetNext : cSharpNext;
	}

	// ---------------------------- helpers -----------------------------------

	private static IInvocationExpression invocation(IVariableReference ref, String javaMethodName) {
		return invocation(ref, javaMethodName, true);
	}

	private static IInvocationExpression invocation(IVariableReference ref, String javaMethodName, boolean java) {
		String methodName = java ? javaMethodName : java2CSharp.get(javaMethodName);
		InvocationExpression iteratorInvocation = new InvocationExpression();
		iteratorInvocation.setMethodName(Names.newMethod(methodName));
		iteratorInvocation.setReference(ref);
		return iteratorInvocation;
	}
}