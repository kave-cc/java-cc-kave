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

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;

public class PathInsensitivePointsToInfoTest {

	@Test
	public void storeReference() {
		IReference r = new VariableReference();
		Object o = new Object();

		PathInsensitivePointsToInfo sut = new PathInsensitivePointsToInfo();

		Assert.assertFalse(sut.hasKey(r));
		sut.set(r, o);
		Assert.assertTrue(sut.hasKey(r));
		Assert.assertSame(o, sut.getAbstractObject(r));
	}

	@Test
	public void identityEquals() {
		IReference r1 = new VariableReference();
		IReference r2 = new VariableReference();
		Assert.assertEquals(r1, r2); // this is the fundamental requirement for this test!
		Object o1 = new Object();
		Object o2 = new Object();

		PathInsensitivePointsToInfo sut = new PathInsensitivePointsToInfo();

		sut.set(r1, o1);
		Assert.assertTrue(sut.hasKey(r1));
		Assert.assertFalse(sut.hasKey(r2));
		sut.set(r2, o2);

		Assert.assertSame(o1, sut.getAbstractObject(r1));
		Assert.assertSame(o2, sut.getAbstractObject(r2));
	}

	@Test
	public void equality_default() {
		IPathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		IPathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_withValues() {
		Object key1 = new VariableReference();
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(key1, obj1);

		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_different() {
		Object key1 = new VariableReference();
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_diffKey() {
		Object obj1 = new Object();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(new VariableReference(), obj1);
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(new VariableReference(), obj1);

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_diffObj() {
		Object key1 = new VariableReference();

		PathInsensitivePointsToInfo a = new PathInsensitivePointsToInfo();
		a.set(key1, new Object());
		PathInsensitivePointsToInfo b = new PathInsensitivePointsToInfo();
		b.set(key1, new Object());

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}
}