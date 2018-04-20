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
package cc.kave.rsse.calls.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.kave.caret.analyses.PathInsensitivePointsToInfo;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class AbstractObjectToUsageMapperTest {

	private static final ITypeName cCtx = mock(ITypeName.class);
	private static final IMethodName mCtx = mock(IMethodName.class);

	private PathInsensitivePointsToInfo p2info;
	private AbstractObjectToUsageMapper sut;

	@Before
	public void setup() {
		p2info = new PathInsensitivePointsToInfo();
		sut = new AbstractObjectToUsageMapper(p2info, cCtx, mCtx);
	}

	private void addAO(Object... ks) {
		Object ao = new Object();
		for (Object k : ks) {
			assertFalse(p2info.hasKey(k));
			p2info.set(k, ao);
		}
	}

	@Test
	public void defaultValues() {
		Map<Object, Usage> actual = sut.map;
		assertTrue(actual instanceof IdentityHashMap);
		assertEquals(new HashMap<>(), actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void keyIsUnknown() {
		sut.get(new SST());
	}

	@Test
	public void getSST() {
		SST k = new SST();
		addAO(k);
		sut.get(k);
		assertAO(k);
	}

	@Test
	public void getMemberDecl() {
		MethodDeclaration k = new MethodDeclaration();
		addAO(k);
		sut.get(k);
		assertAO(k);
	}

	@Test
	public void getLambdaExpr() {
		LambdaExpression k = new LambdaExpression();
		addAO(k);
		sut.get(k);
		assertAO(k);
	}

	@Test
	public void getParameterName() {
		ParameterName k = new ParameterName();
		addAO(k);
		sut.get(k);
		assertAO(k);
	}

	@Test
	public void getReference() {
		VariableReference k = new VariableReference();
		addAO(k);
		sut.get(k);
		assertAO(k);
	}

	@Test
	public void multipleCallsResolveToTheSameUsage() {
		SST k = new SST();
		addAO(k);
		Usage a = sut.get(k);
		Usage b = sut.get(k);
		assertSame(a, b);
	}

	private void assertAO(Object k) {
		Object ao = ao(k);
		assertTrue(sut.map.containsKey(ao));
		Usage actual = sut.map.get(ao);
		assertEquals(cCtx, actual.getClassContext());
		assertEquals(mCtx, actual.getMethodContext());
	}

	private Object ao(Object k) {
		Object ao = p2info.getAbstractObject(k);
		return ao;
	}
}