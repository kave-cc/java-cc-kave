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
package cc.kave.commons.pointsto.tests.analysis.inclusion;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.fieldReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.indexAccessReference;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.inclusion.InclusionAnalysis;
import cc.kave.commons.pointsto.tests.TestBuilder;
import cc.kave.commons.pointsto.tests.TestSSTBuilder;

public class InclusionAnalysisTest extends TestBuilder {

	private static <T> T getLast(List<T> items) {
		return items.get(items.size() - 1);
	}

	private static <T> T getReverse(List<T> items, int i) {
		return items.get(items.size() - i - 1);
	}

	@Test
	public void testStreamTest() {
		TestSSTBuilder sstBuilder = new TestSSTBuilder();
		Context context = sstBuilder.createStreamTest();
		PointsToAnalysis ptAnalysis = new InclusionAnalysis();
		ptAnalysis.compute(context);
		PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);

		IMethodDeclaration copyToDecl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("CopyTo")).get();
		IMethodDeclaration openSrcDecl = context.getSST().getNonEntryPoints().iterator().next();
		assertEquals("OpenSource", openSrcDecl.getName().getName());
		IReturnStatement returnStmt = (IReturnStatement) getLast(openSrcDecl.getBody());
		IReference returnedRef = ((IReferenceExpression) returnStmt.getExpression()).getReference();
		Set<AbstractLocation> inputLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("input"), copyToDecl.getBody().get(1)));
		assertFalse(inputLocations.isEmpty());
		Set<AbstractLocation> returnLocations = ptAnalysis.query(queryBuilder.newQuery(returnedRef, returnStmt));
		assertFalse(returnLocations.isEmpty());
		assertThat(inputLocations, Matchers.is(returnLocations));
		Set<AbstractLocation> outputLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("output"), copyToDecl.getBody().get(3)));
		assertFalse(outputLocations.isEmpty());
		assertThat(inputLocations, Matchers.not(outputLocations));

		IFieldDeclaration srcDecl = context.getSST().getFields().iterator().next();
		assertEquals("source", srcDecl.getName().getName());
		Set<AbstractLocation> openSrcFilenameLocations = ptAnalysis
				.query(queryBuilder.newQuery(fieldReference(srcDecl.getName()), getReverse(openSrcDecl.getBody(), 1)));
		assertFalse(openSrcFilenameLocations.isEmpty());
		IMethodDeclaration ctorDecl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().isConstructor()).get();
		Set<AbstractLocation> ctorSrcLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("source"), ctorDecl.getBody().get(0)));
		assertFalse(ctorSrcLocations.isEmpty());
		assertThat(openSrcFilenameLocations, Matchers.is(ctorSrcLocations));
	}

	@Test
	public void testDelegateTest() {
		TestSSTBuilder sstBuilder = new TestSSTBuilder();
		Context context = sstBuilder.createDelegateTest();
		PointsToAnalysis ptAnalysis = new InclusionAnalysis();
		ptAnalysis.compute(context);
		PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);

		IMethodDeclaration entry1Decl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("entry1")).get();
		IMethodDeclaration entry2Decl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("entry2")).get();
		IMethodDeclaration fooDecl = context.getSST().getNonEntryPoints().iterator().next();
		assertEquals("foo", fooDecl.getName().getName());

		Set<AbstractLocation> entry1ArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("arg"), getReverse(entry1Decl.getBody(), 1)));
		assertFalse(entry1ArgLocations.isEmpty());
		Set<AbstractLocation> fooArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("x"), fooDecl.getBody().get(1)));
		assertFalse(fooArgLocations.isEmpty());
		assertThat(fooArgLocations, Matchers.is(entry1ArgLocations));
		Set<AbstractLocation> entry2ArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("arg"), getReverse(entry2Decl.getBody(), 1)));
		assertFalse(entry2ArgLocations.isEmpty());
		assertThat(fooArgLocations, Matchers.not(entry2ArgLocations));
		ILambdaExpression lamdaExpr = (ILambdaExpression) ((IAssignment) entry2Decl.getBody().get(1)).getExpression();
		IStatement lambdaFormatStmt = lamdaExpr.getBody().get(1);
		Set<AbstractLocation> entry2LambdaArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("x"), lambdaFormatStmt));
		assertFalse(entry2LambdaArgLocations.isEmpty());
		assertThat(entry2LambdaArgLocations, Matchers.is(entry2ArgLocations));
	}

	@Test
	public void testParameterArrayTest() {
		TestSSTBuilder sstBuilder = new TestSSTBuilder();
		Context context = sstBuilder.createParameterArrayTest();
		PointsToAnalysis ptAnalysis = new InclusionAnalysis();
		ptAnalysis.compute(context);
		PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);

		IMethodDeclaration runDecl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("Run")).get();
		IMethodDeclaration consumeDecl = Iterables
				.tryFind(context.getSST().getNonEntryPoints(), ne -> ne.getName().getName().equals("Consume")).get();

		Set<AbstractLocation> name1Locations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("name1"), getLast(runDecl.getBody())));
		assertEquals(1, name1Locations.size());
		Set<AbstractLocation> name2Locations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("name2"), getLast(runDecl.getBody())));
		assertEquals(1, name2Locations.size());
		assertThat(name1Locations, Matchers.not(name2Locations));

		IForEachLoop consumeLoop = (IForEachLoop) consumeDecl.getBody().get(0);
		Set<AbstractLocation> namesLocations = ptAnalysis
				.query(queryBuilder.newQuery(variableReference("names"), consumeLoop));
		assertEquals(1, namesLocations.size());
		assertThat(namesLocations, Matchers.not(name1Locations));
		assertThat(namesLocations, Matchers.not(name2Locations));

		Set<AbstractLocation> nameLocations = ptAnalysis.query(new PointsToQuery(variableReference("name"),
				consumeLoop.getDeclaration().getType(), consumeLoop.getBody().get(0), consumeDecl.getName()));
		assertEquals(2, nameLocations.size());
		assertThat(nameLocations,
				Matchers.containsInAnyOrder(name1Locations.iterator().next(), name2Locations.iterator().next()));
	}

	@Test
	@Ignore("fails in build")
	public void testRecursionTest() {
		TestSSTBuilder sstBuilder = new TestSSTBuilder();
		Context context = sstBuilder.createRecursionTest();
		PointsToAnalysis ptAnalysis = new InclusionAnalysis();
		ptAnalysis.compute(context);
		PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);

		IMethodDeclaration entry1Decl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("Entry1")).get();
		IIfElseBlock entry1IfElse = (IIfElseBlock) entry1Decl.getBody().get(2);
		IReturnStatement anchorReturn = (IReturnStatement) entry1IfElse.getThen().get(0);
		Set<AbstractLocation> entry1ArgLocations = ptAnalysis.query(queryBuilder
				.newQuery(((IReferenceExpression) anchorReturn.getExpression()).getReference(), anchorReturn));
		// initial parameter (entry point) + locations generated by recursive
		// call:
		// String.Substring(int), String.Substring(int,int)
		assertEquals(3, entry1ArgLocations.size());

		IMethodDeclaration entry2Decl = Iterables
				.tryFind(context.getSST().getEntryPoints(), e -> e.getName().getName().equals("Entry2")).get();

		IIfElseBlock entry2IfElse = (IIfElseBlock) getLast(entry2Decl.getBody());
		IAssignment entry2Assignment = (IAssignment) getReverse(entry2IfElse.getThen(), 1);
		IInvocationExpression entry2SubstringInvocation = (IInvocationExpression) entry2Assignment.getExpression();
		Set<AbstractLocation> entry2ArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(entry2SubstringInvocation.getReference(), entry2Assignment));
		// initial parameter (entry point) + location generated by indirect
		// recursive call:
		// String.Substring(int,int)
		assertEquals(2, entry2ArgLocations.size());

		IMethodDeclaration showDecl = Iterables
				.tryFind(context.getSST().getNonEntryPoints(), ne -> ne.getName().getName().equals("Show")).get();
		IInvocationExpression showWLInvocation = (IInvocationExpression) ((IExpressionStatement) showDecl.getBody()
				.get(0)).getExpression();
		IReference showArgRef = ((IReferenceExpression) showWLInvocation.getParameters().get(0)).getReference();
		Set<AbstractLocation> showArgLocations = ptAnalysis
				.query(queryBuilder.newQuery(showArgRef, showDecl.getBody().get(0)));
		assertEquals(1, showArgLocations.size());
		assertThat(entry2ArgLocations, Matchers.hasItem(showArgLocations.iterator().next()));
	}

	@Test
	public void fieldLocationsHaveNoEffectOnIndexAccessLocations() {
		// b = p0.F0
		// c = p0[x]

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A")), true,
				declare("b", type("B")),
				assign("b", refExpr(fieldReference(variableReference("p0"), field(type("B"), type("A"), 0)))),
				declare("c", type("C")), assign("c", refExpr(indexAccessReference(variableReference("p0")))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = new InclusionAnalysis();
		analysis.compute(ctxt);

		IAssignment bAssignment = (IAssignment) enclosingMethod.getBody().get(1);
		IAssignment cAssignment = (IAssignment) enclosingMethod.getBody().get(3);
		Set<AbstractLocation> bLocations = analysis
				.query(new PointsToQuery(variableReference("b"), type("B"), bAssignment, enclosingMethod.getName()));
		assertFalse(bLocations.isEmpty());
		Set<AbstractLocation> cLocations = analysis
				.query(new PointsToQuery(variableReference("c"), type("C"), cAssignment, enclosingMethod.getName()));
		assertFalse(cLocations.isEmpty());
		assertThat(bLocations, Matchers.not(cLocations));
	}
}