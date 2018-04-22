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
package cc.kave.caret.analyses;

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class PathInsensitivePointsToInfoTest {

	private static final Object AO = new Object();
	private PathInsensitivePointsToInfo sut;

	@Before
	public void setup() {
		sut = new PathInsensitivePointsToInfo();
	}

	@Test
	public void getKeysIsAnIdentityHashSet() {
		assertEquals("java.util.IdentityHashMap$KeySet", sut.getKeys().getClass().getName());
	}

	@Test
	public void storeReference() {
		IReference r = new VariableReference();
		Object o = new Object();

		Assert.assertFalse(sut.hasKey(r));
		assertEquals(new HashSet<>(), sut.getKeys());
		sut.set(r, o);
		Assert.assertTrue(sut.hasKey(r));
		assertEquals(new HashSet<>(asList(r)), sut.getKeys());
		Assert.assertSame(o, sut.getAbstractObject(r));
	}

	@Test
	public void identityEquals() {
		IReference r1 = new VariableReference();
		IReference r2 = new VariableReference();
		Assert.assertEquals(r1, r2); // this is the fundamental requirement for this test!
		Object o1 = new Object();
		Object o2 = new Object();

		sut.set(r1, o1);
		Assert.assertTrue(sut.hasKey(r1));
		Assert.assertFalse(sut.hasKey(r2));
		sut.set(r2, o2);

		Assert.assertSame(o1, sut.getAbstractObject(r1));
		Assert.assertSame(o2, sut.getAbstractObject(r2));
	}

	@Test
	public void useWithSST() {
		SST k = new SST();
		assertFalse(sut.hasKey(k));
		sut.set(k, AO);
		assertTrue(sut.hasKey(k));
		assertSame(AO, sut.getAbstractObject((ISST) k));
	}

	@Test
	public void useWithMemberDeclaration() {
		MethodDeclaration k = new MethodDeclaration();
		assertFalse(sut.hasKey(k));
		sut.set(k, AO);
		assertTrue(sut.hasKey(k));
		assertSame(AO, sut.getAbstractObject((IMethodDeclaration) k));
	}

	@Test
	public void useWithLambdaExpr() {
		LambdaExpression k = new LambdaExpression();
		assertFalse(sut.hasKey(k));
		sut.set(k, AO);
		assertTrue(sut.hasKey(k));
		assertSame(AO, sut.getAbstractObject((ILambdaExpression) k));
	}

	@Test
	public void useWithParameterName() {
		ParameterName k = new ParameterName();
		assertFalse(sut.hasKey(k));
		sut.set(k, AO);
		assertTrue(sut.hasKey(k));
		assertSame(AO, sut.getAbstractObject((IParameterName) k));
	}

	@Test
	public void useWithReference() {
		VariableReference k = new VariableReference();
		assertFalse(sut.hasKey(k));
		sut.set(k, AO);
		assertTrue(sut.hasKey(k));
		assertSame(AO, sut.getAbstractObject((IVariableReference) k));
	}

	@Test(expected = IllegalArgumentException.class)
	public void useWithUnknownKey() {
		sut.getAbstractObject(new Object());
	}

	@Test
	public void equality_default() {
		IPathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		IPathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_withValues() {
		Object key1 = new VariableReference();
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(key1, obj1);

		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_different() {
		Object key1 = new VariableReference();
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();

		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffKey() {
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(new VariableReference(), obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(new VariableReference(), obj1);

		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffObj() {
		Object key1 = new VariableReference();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, new Object());
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(key1, new Object());

		assertNotEqualDataStructures(a, b);
	}
}