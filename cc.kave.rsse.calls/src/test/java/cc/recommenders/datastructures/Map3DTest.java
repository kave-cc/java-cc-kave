/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.datastructures;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.rsse.calls.datastructures.Map3D;

public class Map3DTest {

	private Map3D<Integer, Integer, Integer, int[]> sut;

	@Before
	public void setup() {
		sut = Map3D.create();
	}

	@Test
	public void nonExistingKeysReturnProvidedDefault() {
		int[] expected = new int[1];
		int[] actual = sut.getOrAdd(1, 1, 1, expected);
		assertSame(expected, actual);
	}

	@Test
	public void onceUsedKeysExist() {
		sut.getOrAdd(1, 1, 1, new int[1])[0] = 1;

		int[] actuals = sut.getOrAdd(1, 1, 1, new int[1]);
		int[] expecteds = new int[] { 1 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void useAsMap() {
		sut.getOrAdd(1, 1, 1, new int[1])[0] = 1;

		int actual = sut.get(1).get(1).get(1)[0];
		int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	public void keySet() {
		sut.getOrAdd(1, 1, 1, new int[1])[0] = 1;
		sut.getOrAdd(1, 2, 1, new int[1])[0] = 1;
		sut.getOrAdd(3, 1, 1, new int[1])[0] = 1;
		Set<Integer> actuals = sut.keySet();
		Set<Integer> expecteds = Sets.newHashSet(1, 3);
		assertEquals(expecteds, actuals);
	}

	@Test
	public void hashCodeAndEquals_equals() {
		Map3D<Integer, Integer, Integer, Integer> a = Map3D.create();
		Map3D<Integer, Integer, Integer, Integer> b = Map3D.create();
		a.getOrAdd(1, 1, 1, 1);
		b.getOrAdd(1, 1, 1, 1);
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void hashCodeAndEquals_diff() {
		Map3D<Integer, Integer, Integer, Integer> a = Map3D.create();
		Map3D<Integer, Integer, Integer, Integer> b = Map3D.create();
		a.getOrAdd(1, 1, 1, 1);
		b.getOrAdd(2, 2, 2, 2);
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}
}