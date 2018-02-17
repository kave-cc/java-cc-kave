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
package cc.kave.rsse.calls.usages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.usages.ProjectFoldingIndex;

public class ProjectFoldingIndexTest {

	private static final ITypeName T1 = mock(ITypeName.class);
	private static final ITypeName T2 = mock(ITypeName.class);
	private static final String P1 = "p1";
	private static final String P2 = "p2";

	private ProjectFoldingIndex sut;

	@Before
	public void setup() {
		sut = new ProjectFoldingIndex();
	}

	@Test
	public void noTypesByDefault() {
		assertEquals(Sets.newHashSet(), sut.getTypes());
	}

	@Test
	public void countOfUnknownType() {
		int actual = sut.getTotalCount(T1);
		int expected = 0;
		assertEquals(expected, actual);
	}

	@Test
	public void requestingUnknownType() {
		Map<String, Integer> actual = sut.getCounts(T1);
		Map<String, Integer> expected = Maps.newHashMap();
		assertEquals(expected, actual);
	}

	@Test
	public void canBeCounted() {
		sut.setCount(T1, P1, 12);
		Map<String, Integer> actual = sut.getCounts(T1);
		Map<String, Integer> expected = Maps.newHashMap();
		expected.put(P1, 12);
		assertEquals(expected, actual);
	}

	@Test
	public void canBeCountedStepwise() {
		sut.count(T1, P1);
		sut.count(T1, P1);
		Map<String, Integer> actual = sut.getCounts(T1);
		Map<String, Integer> expected = Maps.newHashMap();
		expected.put(P1, 2);
		assertEquals(expected, actual);
	}

	@Test
	public void canBeCountedStepwise_newProjects() {
		sut.count(T1, P1);
		sut.count(T1, P2);
		Map<String, Integer> actual = sut.getCounts(T1);
		Map<String, Integer> expected = Maps.newHashMap();
		expected.put(P1, 1);
		expected.put(P2, 1);
		assertEquals(expected, actual);
	}

	@Test
	public void calculateTotal() {
		sut.setCount(T1, P1, 1);
		sut.setCount(T1, P2, 2);
		int actual = sut.getTotalCount(T1);
		int expected = 3;
		assertEquals(expected, actual);
	}

	@Test
	public void complexCount() {
		sut.setCount(T1, P1, 1);
		sut.setCount(T1, P2, 22);
		sut.setCount(T2, P1, 333);
		sut.setCount(T2, P2, 4444);
		Map<String, Integer> actual = sut.getCounts(T1);
		Map<String, Integer> expected = Maps.newHashMap();
		expected.put(P1, 1);
		expected.put(P2, 22);
		assertEquals(expected, actual);
	}

	@Test
	public void trivialHashCodeAndEquals() {
		ProjectFoldingIndex a = new ProjectFoldingIndex();
		ProjectFoldingIndex b = new ProjectFoldingIndex();
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}
}