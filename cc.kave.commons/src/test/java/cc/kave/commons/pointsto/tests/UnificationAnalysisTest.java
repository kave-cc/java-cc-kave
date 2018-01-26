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

import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Iterables;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.impl.v0.types.ArrayTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.references.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctVariableReference;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysisVisitorContext;
import cc.kave.commons.pointsto.analysis.unification.identifiers.SteensgaardLocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;

public class UnificationAnalysisTest {

	@Test
	public void testVisitorContext() {
		TestSSTBuilder builder = new TestSSTBuilder();
		ITypeName testType = Names.newType("Test.UnificationContextTest, Test");
		UnificationAnalysisVisitorContext visitorContext = new UnificationAnalysisVisitorContext(
				builder.createContext(builder.createEmptySST(testType)), new SteensgaardLocationIdentifierFactory());

		IMethodName aCtor = Names.newMethod("[?] [UnificationContextTest.A]..ctor()");
		IMethodName bCtor = Names.newMethod("[?] [UnificationContextTest.B]..ctor()");
		IVariableDeclaration xDecl = declare("x", Names.getUnknownType());
		IVariableDeclaration zDecl = declare("z", Names.getUnknownType());
		IVariableDeclaration aDecl = declare("a", Names.getUnknownType());
		IVariableDeclaration bDecl = declare("b", Names.getUnknownType());
		IVariableDeclaration yDecl = declare("y", Names.getUnknownType());
		IVariableDeclaration cDecl = declare("c", Names.getUnknownType());

		visitorContext.declareVariable(xDecl);
		IInvocationExpression invocation = invocationExpr(aCtor);
		visitorContext.setLastAssignment(assignmentToLocal("x", invocation));
		visitorContext.allocate(visitorContext.getDestinationForExpr(invocation));

		visitorContext.declareVariable(zDecl);
		invocation = invocationExpr(bCtor);
		visitorContext.setLastAssignment(assignmentToLocal("z", invocation));
		visitorContext.allocate(visitorContext.getDestinationForExpr(invocation));

		visitorContext.declareVariable(aDecl);
		visitorContext.alias(variableReference("a"), variableReference("x"));

		visitorContext.declareVariable(bDecl);
		visitorContext.alias(variableReference("b"), variableReference("z"));

		visitorContext.declareVariable(yDecl);
		visitorContext.alias(variableReference("y"), variableReference("x"));
		visitorContext.alias(variableReference("y"), variableReference("z"));

		Map<DistinctReference, AbstractLocation> referenceLocations = visitorContext.getReferenceLocations();
		// all variables point to the same location + this/super location
		assertEquals(2, new HashSet<>(referenceLocations.values()).size());

		visitorContext.declareVariable(cDecl);
		IFieldReference fieldRef = builder.buildFieldReference("y", Names.newField("[?] [?].f"));
		visitorContext.setLastAssignment(assignmentToLocal("c", refExpr(fieldRef)));
		visitorContext.readField(variableReference("c"), fieldRef);

		visitorContext.finalizeAnalysis();
		referenceLocations = visitorContext.getReferenceLocations();
		AbstractLocation thisLocation = referenceLocations
				.get(new DistinctKeywordReference(LanguageOptions.getInstance().getThisName(), testType));
		assertNotNull(thisLocation);
		AbstractLocation xLocation = referenceLocations.get(new DistinctVariableReference(xDecl));
		assertNotNull(xLocation);
		AbstractLocation yLocation = referenceLocations.get(new DistinctVariableReference(yDecl));
		assertNotNull(yLocation);
		AbstractLocation cLocation = referenceLocations.get(new DistinctVariableReference(cDecl));
		assertNotNull(cLocation);

		assertTrue(xLocation.equals(yLocation));
		assertFalse(yLocation.equals(cLocation));
		assertFalse(thisLocation.equals(xLocation));

	}

	@Test
	public void testStreams() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context context = builder.createStreamTest();
		ITypeName enclosingType = context.getSST().getEnclosingType();

		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		pointsToAnalysis.compute(context);

		IFieldName sourceField = Names.newField(
				"[" + builder.getStringType().getIdentifier() + "] [" + enclosingType.getIdentifier() + "].source");
		Set<AbstractLocation> sourceFieldLocations = pointsToAnalysis
				.query(new PointsToQuery(SSTBuilder.fieldReference(sourceField), builder.getStringType(), null, null));
		assertEquals(1, sourceFieldLocations.size());

		IMethodDeclaration openSourceDecl = context.getSST().getNonEntryPoints().iterator().next();
		IAssignment assignment = (IAssignment) openSourceDecl.getBody().get(1);
		IInvocationExpression invocation = (IInvocationExpression) assignment.getExpression();
		IReference firstParameterRef = ((IReferenceExpression) invocation.getParameters().get(0)).getReference();
		Set<AbstractLocation> firstParameterLocations = pointsToAnalysis.query(
				new PointsToQuery(firstParameterRef, builder.getStringType(), assignment, openSourceDecl.getName()));
		assertEquals(1, firstParameterLocations.size());
		assertTrue(sourceFieldLocations.equals(firstParameterLocations));

		Set<AbstractLocation> openSourceStreamLocations = pointsToAnalysis
				.query(new PointsToQuery(assignment.getReference(), builder.getFileStreamType(),
						openSourceDecl.getBody().get(2), openSourceDecl.getName()));
		assertEquals(1, openSourceStreamLocations.size());

		IMethodDeclaration copyToDecl = null;
		for (IMethodDeclaration decl : context.getSST().getEntryPoints()) {
			if (decl.getName().getName().equals("CopyTo")) {
				copyToDecl = decl;
				break;
			}
		}
		assertNotNull(copyToDecl);

		assignment = (IAssignment) copyToDecl.getBody().get(1);
		Set<AbstractLocation> inputStreamLocations = pointsToAnalysis.query(new PointsToQuery(assignment.getReference(),
				builder.getFileStreamType(), assignment, copyToDecl.getName()));
		assertEquals(1, inputStreamLocations.size());
		// input = object allocated in OpenSource
		assertTrue(openSourceStreamLocations.equals(inputStreamLocations));
		assertFalse(sourceFieldLocations.equals(inputStreamLocations));

		assignment = (IAssignment) copyToDecl.getBody().get(3);
		Set<AbstractLocation> outputStreamLocations = pointsToAnalysis.query(new PointsToQuery(
				assignment.getReference(), builder.getFileStreamType(), assignment, copyToDecl.getName()));
		assertEquals(1, outputStreamLocations.size());
		// input != output
		assertFalse(inputStreamLocations.equals(outputStreamLocations));
	}

	@Test
	public void testDelegates() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context context = builder.createDelegateTest();

		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		pointsToAnalysis.compute(context);

		IMethodDeclaration fooDecl = context.getSST().getNonEntryPoints().iterator().next();
		IMethodDeclaration entry1Decl = Iterables.find(context.getSST().getEntryPoints(),
				md -> md.getName().getName().equals("entry1"));
		IMethodDeclaration entry2Decl = Iterables.find(context.getSST().getEntryPoints(),
				md -> md.getName().getName().equals("entry2"));
		assertNotNull(fooDecl);
		assertNotNull(entry1Decl);
		assertNotNull(entry2Decl);

		IMemberName entry1Name = entry1Decl.getName();
		IVariableDeclaration entry1ArgDecl = (IVariableDeclaration) entry1Decl.getBody().get(2);
		ITypeName objectType = entry1ArgDecl.getType();
		IAssignment entry1InvokeFunAssignment = (IAssignment) entry1Decl.getBody().get(5);
		Set<AbstractLocation> entry1ArgInvocationLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("arg"), objectType, entry1InvokeFunAssignment, entry1Name));
		assertEquals(1, entry1ArgInvocationLocations.size());

		Set<AbstractLocation> fooParameterLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("x"), objectType, null, fooDecl.getName()));
		assertThat(entry1ArgInvocationLocations, Matchers.is(fooParameterLocations));

		IMemberName entry2Name = entry2Decl.getName();
		IAssignment entry2InvokeFunAssignment = (IAssignment) entry2Decl.getBody().get(5);
		Set<AbstractLocation> entry2ArgInvocationLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("arg"), objectType, entry2InvokeFunAssignment, entry2Name));
		assertEquals(1, entry2ArgInvocationLocations.size());

		ILambdaExpression lambda = (ILambdaExpression) ((IAssignment) entry2Decl.getBody().get(1)).getExpression();
		Set<AbstractLocation> lambdaParameterLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("x"), objectType, lambda.getBody().get(1), entry2Name));
		assertThat(entry2ArgInvocationLocations, Matchers.is(lambdaParameterLocations));

		// the parameter of 'foo' and the lambda get unified by the 'arg0'
		// parameter of String.Format
		assertThat(fooParameterLocations, Matchers.is(lambdaParameterLocations));

		// check that the 'fun' variables of entry1 and entry2 do not refer to
		// the same object
		ITypeName delegateType = ((IVariableDeclaration) entry1Decl.getBody().get(0)).getType();
		Set<AbstractLocation> entry1FunLocations = pointsToAnalysis.query(
				new PointsToQuery(variableReference("fun"), delegateType, entry1InvokeFunAssignment, entry1Name));
		Set<AbstractLocation> entry2FunLocations = pointsToAnalysis.query(
				new PointsToQuery(variableReference("fun"), delegateType, entry2InvokeFunAssignment, entry2Name));
		assertEquals(1, entry1FunLocations.size());
		assertEquals(1, entry2FunLocations.size());
		assertThat(entry1FunLocations, Matchers.not(entry2FunLocations));

	}

	@Test
	public void testParameterArrays() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context context = builder.createParameterArrayTest();

		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		pointsToAnalysis.compute(context);

		IMethodDeclaration runDecl = context.getSST().getEntryPoints().iterator().next();
		ITypeName stringType = ((IVariableDeclaration) runDecl.getBody().get(0)).getType();
		IMemberName runName = runDecl.getName();
		IExpressionStatement stmt = (IExpressionStatement) runDecl.getBody().get(4);

		Set<AbstractLocation> name1Locations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("name1"), stringType, stmt, runName));
		Set<AbstractLocation> name2Locations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("name2"), stringType, stmt, runName));
		assertEquals(1, name1Locations.size());
		assertEquals(1, name2Locations.size());
		// should be unified as they are stored in the same array
		assertThat(name1Locations, Matchers.is(name2Locations));

		IMethodDeclaration consumeDecl = context.getSST().getNonEntryPoints().iterator().next();
		ITypeName stringArrayType = ArrayTypeName.from(stringType, 1);
		IMemberName consumeName = consumeDecl.getName();
		IForEachLoop consumeLoop = (IForEachLoop) consumeDecl.getBody().get(0);
		IStatement consoleCall = consumeLoop.getBody().get(0);

		Set<AbstractLocation> consumeParameterLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("names"), stringArrayType, null, consumeName));
		assertEquals(1, consumeParameterLocations.size());
		assertThat(name1Locations, Matchers.not(consumeParameterLocations));
		assertThat(name2Locations, Matchers.not(consumeParameterLocations));

		Set<AbstractLocation> consoleCallArgLocations = pointsToAnalysis
				.query(new PointsToQuery(variableReference("name"), stringType, consoleCall, consumeName));
		assertEquals(1, consoleCallArgLocations.size());
		assertThat(consoleCallArgLocations, Matchers.not(consumeParameterLocations));
		assertThat(consoleCallArgLocations, Matchers.is(name1Locations));
		assertThat(consoleCallArgLocations, Matchers.is(name2Locations));
	}
}