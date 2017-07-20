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
package cc.kave.commons.model.ssts.transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class NormalizationVisitorBaseTest<TContext, TReturn> {
	protected ISSTNodeVisitor<TContext, TReturn> sut;
	protected TContext context;
	protected SST sst, expectedSST;

	protected NormalizationVisitorBaseTest() {
		this.context = null;
	}

	protected NormalizationVisitorBaseTest(TContext context) {
		this.context = context;
	}

	@Before
	public void setup() {
		sst = new SST();
		expectedSST = new SST();
	}

	// ---------------------------- asserts -----------------------------------

	protected void assertTransformation(List<IStatement> toNormalize, List<IStatement> expectedResult) {
		setNormalizing(toNormalize.toArray(new IStatement[toNormalize.size()]));
		setExpected(expectedResult.toArray(new IStatement[expectedResult.size()]));
		assertTransformedSST();
	}

	protected void assertSSTs(ISST expectedSST, ISST sst) {
		Set<IMethodDeclaration> methods = sst.getMethods();
		Set<IMethodDeclaration> expectedMethods = expectedSST.getMethods();
		assertThat(methods.size(), equalTo(expectedMethods.size()));

		Iterator<IMethodDeclaration> methodsIter = methods.iterator();
		Iterator<IMethodDeclaration> expectedMethodsIter = expectedMethods.iterator();

		while (methodsIter.hasNext() && expectedMethodsIter.hasNext()) {
			IMethodDeclaration method = methodsIter.next();
			IMethodDeclaration expectedMethod = expectedMethodsIter.next();

			List<IStatement> body = method.getBody();
			List<IStatement> expectedBody = expectedMethod.getBody();
			assertThat(body.size(), equalTo(expectedBody.size()));

			for (int i = 0; i < body.size(); i++) {
				assertThat(body.get(i), equalTo(expectedBody.get(i)));
			}

			assertThat(method, equalTo(expectedMethod));
		}

		assertThat(expectedSST, equalTo(sst));
	}

	protected void assertTransformedSST() {
		sst.accept(sut, context);
		assertSSTs(expectedSST, sst);
	}

	// ---------------------------- helpers -----------------------------------

	protected void setNormalizing(IStatement... statements) {
		sst.setMethods(Sets.newHashSet(declareMethod(statements)));
	}

	protected void setExpected(IStatement... statements) {
		expectedSST.setMethods(Sets.newHashSet(declareMethod(statements)));
	}

	protected IVariableReference dummyVar(int i) {
		return SSTUtil.variableReference("var" + i);
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> list(T... elements) {
		return Lists.newArrayList(elements);
	}
}