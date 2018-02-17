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
package cc.kave.rsse.calls.datastructures;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.rsse.calls.datastructures.Map2D;

public class Map2DTest {

	private Map2D<Integer, Integer, int[]> sut;

	@Before
	public void setup() {
		sut = Map2D.create();
	}

	@Test
	public void nonExistingKeysReturnProvidedDefault() {
		int[] expected = new int[1];
		int[] actual = sut.getOrAdd(1, 1, expected);
		assertSame(expected, actual);
	}

	@Test
	public void onceUsedKeysExist() {
		sut.getOrAdd(1, 1, new int[1])[0] = 1;

		int[] actuals = sut.getOrAdd(1, 1, new int[1]);
		int[] expecteds = new int[] { 1 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void puttingEntries() {
		@SuppressWarnings("unchecked")
		Map<Integer, int[]> expected = mock(Map.class);
		sut.put(1, expected);
		Map<Integer, int[]> actual = sut.get(1);
		assertSame(expected, actual);
	}

	@Test
	public void useAsMap() {
		sut.getOrAdd(1, 1, new int[1])[0] = 1;

		int actual = sut.get(1).get(1)[0];
		int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	public void containsKey() {
		assertFalse(sut.containsKey(1));
		sut.getOrAdd(1, 1, new int[] { 1 });
		assertTrue(sut.containsKey(1));
	}

	@Test
	public void keySet() {
		Set<Integer> actual = sut.keySet();
		Set<Integer> expected = Sets.newHashSet();
		assertEquals(expected, actual);

		sut.getOrAdd(1, 1, new int[1]);

		actual = sut.keySet();
		expected = Sets.newHashSet(1);
		assertEquals(expected, actual);
	}

	@Test
	public void multiKeyGet() {
		int[] expected = new int[1];
		sut.getOrAdd(1, 1, expected);
		int[] actual = sut.get(1, 1);
		assertSame(expected, actual);
	}

	@Test
	public void hashCodeAndEquals_equals() {
		Map2D<Integer, Integer, Integer> a = Map2D.create();
		Map2D<Integer, Integer, Integer> b = Map2D.create();
		a.getOrAdd(1, 1, 1);
		b.getOrAdd(1, 1, 1);
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void hashCodeAndEquals_diff() {
		Map2D<Integer, Integer, Integer> a = Map2D.create();
		Map2D<Integer, Integer, Integer> b = Map2D.create();
		a.getOrAdd(1, 1, 1);
		b.getOrAdd(2, 2, 2);
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}
}