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
package cc.kave.commons.pointsto.analysis.types;

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.fieldRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.utils.ssts.SSTUtils.BYTE_ARR1D;
import static cc.kave.commons.utils.ssts.SSTUtils.FILESTREAM;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.STRING;
import static cc.kave.commons.utils.ssts.SSTUtils.VOID;
import static cc.kave.commons.utils.ssts.SSTUtils.sst;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.tests.TestSSTBuilder;

public class TypeCollectorTest {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	@Test
	public void testAllTypesOfStreamTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context streamTestContext = builder.createStreamTest();

		TypeCollector collector = new TypeCollector(streamTestContext);
		Set<ITypeName> allTypes = collector.getTypes();

		assertFalse(allTypes.isEmpty());

		ITypeName streamTestType = streamTestContext.getTypeShape().getTypeHierarchy().getElement();
		Set<ITypeName> expectedTypes = Sets.newHashSet(streamTestType, languageOptions.getTopClass(), STRING,
				FILESTREAM, BYTE_ARR1D, INT);
		expectedTypes.add(Names.getUnknownType()); // assume that enums are
													// treated as unknown types
		assertEquals(expectedTypes, allTypes);
	}

	@Test
	public void testReferenceTypesOfStreamTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context streamTestContext = builder.createStreamTest();
		TypeCollector collector = new TypeCollector(streamTestContext);

		List<Boolean> testedMethods = new ArrayList<>(Arrays.asList(false, false, false));

		for (IMethodDeclaration methodDecl : streamTestContext.getSST().getMethods()) {
			IMethodName method = methodDecl.getName();
			List<IStatement> stmts = methodDecl.getBody();
			if (method.isConstructor()) {
				assertEquals(1, stmts.size());
				IAssignment assignment = (IAssignment) stmts.get(0);
				assertEquals(STRING, collector.getType(assignment.getReference()));
				testedMethods.set(0, true);
			} else if (method.getName().equals("OpenSource")) {
				IReturnStatement retStmt = (IReturnStatement) stmts.get(stmts.size() - 1);
				IReferenceExpression refExpr = (IReferenceExpression) retStmt.getExpression();
				assertEquals(FILESTREAM, collector.getType(refExpr.getReference()));
				testedMethods.set(1, true);
			}
		}

		assertEquals(Arrays.asList(true, true, false), testedMethods);
	}

	@Test
	public void testReturnTypes() {
		TestSSTBuilder builder = new TestSSTBuilder();

		// void return type should not be collected
		assertFalse(new TypeCollector(builder.createStreamTest()).getTypes().contains(VOID));

		SST sst = sst(Names.newType("Test.ReturnTest, Test"));
		IMethodDeclaration testVoidDecl = SSTUtil.declareMethod(
				Names.newMethod("[p:void] [" + sst.getEnclosingType().getIdentifier() + "].test1()"), true);
		IMethodDeclaration testStringDecl = SSTUtil.declareMethod(
				Names.newMethod("[p:string] [" + sst.getEnclosingType().getIdentifier() + "].test2()"), true);
		sst.setMethods(Sets.newHashSet(testVoidDecl, testStringDecl));
		Context context = builder.createContext(sst);

		TypeCollector collector = new TypeCollector(context);
		Set<ITypeName> expected = Sets.newHashSet(sst.getEnclosingType(), languageOptions.getTopClass(), STRING);
		assertEquals(expected, collector.getTypes());
	}

	public void testInvocationReference() {
		TestSSTBuilder builder = new TestSSTBuilder();

		SST sst = sst(Names.newType("Test.InvocationReferenceTest, Test"));
		IInvocationExpression equalsInvocation = SSTUtil.invocationExpression(languageOptions.getSuperName(),
				Names.newMethod("[p:bool] [" + languageOptions.getTopClass().getIdentifier() + "].Equals(["
						+ languageOptions.getTopClass() + "] obj"),
				Iterators.forArray(SSTUtil.refExpr("tmp")));
		IMethodDeclaration testDecl = SSTUtil.declareMethod(
				Names.newMethod("[p:void] [" + sst.getEnclosingType().getIdentifier() + "].test()"), true,
				SSTUtil.declareVar("tmp", languageOptions.getTopClass()),
				SSTUtil.assignmentToLocal("tmp", SSTUtil.nullExpr()), SSTUtil.exprStmt(equalsInvocation));
		sst.setMethods(Sets.newHashSet(testDecl));

		TypeCollector collector = new TypeCollector(builder.createContext(sst));
		assertEquals(languageOptions.getTopClass(), collector.getType(equalsInvocation.getReference()));
	}

	@Test
	public void testSubReferenceTypes() {
		TestSSTBuilder builder = new TestSSTBuilder();

		SST sst = sst(Names.newType("Test.SubReferenceTest, Test"));
		ITypeName testClass = Names.newType("Test.SomeClassWithStringField, Test");
		IFieldName testField = Names.newField("[p:string] [" + testClass.getIdentifier() + "].desc");
		IPropertyName strLengthProperty = Names.newProperty("get set [p:int] [p:string].Length()");

		IFieldReference fieldReference = fieldRef("dummy", testField);
		IPropertyReference propertyReference = propertyRef("str", strLengthProperty);

		IMethodDeclaration testDecl = declareMethod(
				newMethod("[p:void] [" + sst.getEnclosingType().getIdentifier() + "].test()"), true,
				declareVar("dummy", testClass), //
				assignmentToLocal("dummy", SSTUtil.nullExpr()),
				declareVar("str", STRING), //
				SSTUtil.assignmentToLocal("str", SSTUtil.refExpr(fieldReference)),
				exprStmt(refExpr(propertyReference)));
		sst.setMethods(Sets.newHashSet(testDecl));

		TypeCollector collector = new TypeCollector(builder.createContext(sst));
		assertEquals(testClass, collector.getType(fieldReference.getReference()));
		assertEquals(STRING, collector.getType(propertyReference.getReference()));
	}
}
