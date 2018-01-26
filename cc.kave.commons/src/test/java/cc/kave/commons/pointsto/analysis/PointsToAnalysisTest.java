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
package cc.kave.commons.pointsto.analysis;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.fieldReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.indexAccessReference;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableSet;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.tests.AnalysesProvider;
import cc.kave.commons.pointsto.tests.TestBuilder;

@RunWith(Parameterized.class)
public class PointsToAnalysisTest extends TestBuilder {

	@Parameters
	public static Collection<Object[]> data() {
		return AnalysesProvider.ANALYSES_AS_PARAMETERS;
	}

	private static final ITypeName TEST_DELEGATE_TYPE = Names
			.newType("d:[TResult] [System.Func`2[[T -> p:string],[TResult -> p:string]], mscorlib, 4.0.0.0].([T] arg)");

	private static final IMethodName TEST_TO_STRING_METHOD = Names.newMethod("[p:string] [p:object].ToString()");

	private final PointsToAnalysisFactory analysisFactory;

	public PointsToAnalysisTest(PointsToAnalysisFactory analysisFactory) {
		this.analysisFactory = analysisFactory;
	}

	@Test
	public void enumerableForEachReferenceShouldObtainLocation() {
		// foreach (B entry : p0)
		// entry.M1()

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A")), true,
				forEachLoop(declare("entry", type("B")), "p0", exprStmt(invoke("entry", method(type("B"), "M1")))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IForEachLoop loop = (IForEachLoop) enclosingMethod.getBody().get(0);
		PointsToQuery query = new PointsToQuery(variableReference("entry"), type("B"), loop.getBody().get(0),
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void arrayForEachReferenceShouldObtainLocation() {
		// foreach (A entry : p0)
		// entry.M1()

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A[]")), true,
				forEachLoop(declare("entry", type("A")), "p0", exprStmt(invoke("entry", method(type("A"), "M1")))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IForEachLoop loop = (IForEachLoop) enclosingMethod.getBody().get(0);
		PointsToQuery query = new PointsToQuery(variableReference("entry"), type("A"), loop.getBody().get(0),
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void arrayAccessHasLocation() {
		// A[] a = new A[...]
		// a[0] = p0

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A")), true,
				declare("a", type("A[]")), assign("a", invoke(constructor(type("A[]"), intType()), constantExpr())),
				assign(indexAccessReference(variableReference("a")), refExpr(variableReference("p0"))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IAssignment assignment = (IAssignment) enclosingMethod.getBody().get(2);
		PointsToQuery query = new PointsToQuery(assignment.getReference(), type("A"), assignment,
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void fieldHasLocation() {
		ITypeName enclosingType = type("ET");
		IFieldDeclaration fieldDecl = declare(field(type("A"), enclosingType, 0));
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry"), true,
				declare("a", type("A")), assign("a", refExpr(fieldReference(fieldDecl.getName()))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), ImmutableSet.of(fieldDecl), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IAssignment assignment = (IAssignment) enclosingMethod.getBody().get(1);
		PointsToQuery query = new PointsToQuery(fieldReference(fieldDecl.getName()), type("A"), assignment,
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void lambdaDelegateHasLocation() {
		// fun = p0 =>
		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry"), true,
				declare("fun", TEST_DELEGATE_TYPE),
				assign("fun", lambda(type("A"), Arrays.asList(type("A")), ret("p0"))),
				exprStmt(invoke("fun", TEST_TO_STRING_METHOD)));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IExpressionStatement stmt = (IExpressionStatement) enclosingMethod.getBody().get(2);
		PointsToQuery query = new PointsToQuery(variableReference("fun"), TEST_DELEGATE_TYPE, stmt,
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void parameterDelegateHasLocation() {
		// p0.ToString()

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", TEST_DELEGATE_TYPE), true,
				exprStmt(invoke("p0", TEST_TO_STRING_METHOD)));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IExpressionStatement stmt = (IExpressionStatement) enclosingMethod.getBody().get(0);
		PointsToQuery query = new PointsToQuery(variableReference("p0"), TEST_DELEGATE_TYPE, stmt,
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}
}