/**
 * Copyright (c) 2011 Sebastian Proksch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.model;

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.testing.ToStringAsserts;

public class DictionaryTest {

	private static final A A1 = new A(1);
	private static final A A2 = new A(2);
	private static final B B1 = new B(1);
	private static final B B2 = new B(2);

	@Test
	public void defaultValues() {
		Dictionary<A> sut = new Dictionary<A>();
		assertEquals(0, sut.size());
		assertEquals(new HashSet<>(), sut.getAllEntries());
		assertEquals(new HashSet<>(), sut.getAllEntries(A.class));
		assertNull(sut.getFirstEntry(A.class));
	}

	@Test
	public void addingValues() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.add(A1);
		sut.add(B1);
		sut.add(A2);
		sut.add(B2);
		assertEquals(4, sut.size());
		assertEquals(new LinkedHashSet<A>(asList(A1, B1, A2, B2)), sut.getAllEntries());
		assertEquals(new HashSet<>(asList(B1, B2)), sut.getAllEntries(B.class));
		assertSame(B1, sut.getFirstEntry(B.class));
	}

	@Test
	public void addingMultipleValues() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.addAll(asList(A1, A2));
		assertEquals(2, sut.size());
	}

	@Test
	public void addingCounts() {
		Dictionary<A> sut = new Dictionary<A>();
		assertEquals(0, sut.add(A1));
		assertEquals(1, sut.add(A2));
	}

	@Test
	public void addingTwiceIsIgnored() {
		Dictionary<A> sut = new Dictionary<A>();
		assertEquals(0, sut.add(A1));
		assertEquals(0, sut.add(A1));
		assertEquals(1, sut.size());
	}

	@Test
	public void getId() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.add(A1);
		sut.add(A2);
		assertEquals(0, sut.getId(A1));
		assertEquals(1, sut.getId(A2));
		assertEquals(-1, sut.getId(B1));
	}

	@Test
	public void getEntry() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.add(A1);
		sut.add(A2);
		assertEquals(A1, sut.getEntry(0));
		assertEquals(A2, sut.getEntry(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getEntry_nonExistant() {
		new Dictionary<A>().getEntry(0);
	}

	@Test
	public void removeEntries() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.add(A1);
		sut.remove(A1);
		assertFalse(sut.contains(A1));
		assertEquals(-1, sut.getId(A1));
		assertEquals(0, sut.add(A2));
	}

	@Test
	public void clear() {
		Dictionary<A> sut = new Dictionary<A>();
		sut.add(A1);
		sut.add(A2);
		sut.clear();
		assertEquals(0, sut.size());
		assertFalse(sut.contains(A1));
		assertEquals(-1, sut.getId(A1));
		assertEquals(0, sut.add(A2));
	}

	@Test
	public void diff() {
		Dictionary<String> a = new Dictionary<String>();
		a.add("x");
		a.add("y");
		Dictionary<String> b = new Dictionary<String>();
		b.add("y");
		b.add("z");

		assertEquals(Sets.newHashSet("+x+", "-z-"), a.diff(b));
		assertEquals(Sets.newHashSet("+z+", "-x-"), b.diff(a));
	}

	@Test
	public void checkingContents() {
		Dictionary<A> sut = new Dictionary<A>();
		assertFalse(sut.contains(A1));
		sut.add(A1);
		assertTrue(sut.contains(A1));
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAsserts.assertToStringUtils(new Dictionary<A>());
	}

	@Test
	public void equality_default() {
		assertEqualDataStructures(new Dictionary<A>(), new Dictionary<A>());
	}

	@Test
	public void equality_withValues() {
		Dictionary<A> a = new Dictionary<A>();
		a.add(A1);
		a.add(B1);
		a.add(A2);
		a.add(B2);
		Dictionary<A> b = new Dictionary<A>();
		b.add(A1);
		b.add(B1);
		b.add(A2);
		b.add(B2);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_withManyValues() {
		Dictionary<A> a = new Dictionary<A>();
		Dictionary<A> b = new Dictionary<A>();
		for (int i = 0; i < 100; i++) {
			if (i % 2 == 0) {
				a.add(new A(i));
				b.add(new A(i));
			} else {
				a.add(new B(i));
				b.add(new B(i));
			}
		}
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffValues() {
		Dictionary<A> a = new Dictionary<A>();
		a.add(A1);
		Dictionary<A> b = new Dictionary<A>();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffOrder() {
		Dictionary<A> a = new Dictionary<A>();
		a.add(A1);
		a.add(B1);
		Dictionary<A> b = new Dictionary<A>();
		b.add(B1);
		b.add(A1);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void sanityCheck() {
		assertEquals(new A(1), new A(1));
		assertEquals(new B(1), new B(1));
		assertNotEquals(new A(1), new B(1));
		assertNotEquals(new B(1), new A(1));
	}

	public static class A {
		private int num;

		public A(int num) {
			this.num = num;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + num;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			A other = (A) obj;
			if (num != other.num)
				return false;
			return true;
		}
	}

	public static class B extends A {
		public B(int num) {
			super(num);
		}
	}
}