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
package cc.recommenders.usages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.ProjectFoldedUsage;
import cc.recommenders.usages.Usage;

public class ProjectFoldedUsageTest {

	private static final String PROJECT_NAME = "SOME_PROJECT_NAME";
	private static final String PROJECT_NAME2 = "SOME_PROJECT_NAME2";

	private Usage usage;
	private ICoReTypeName type;
	private Usage usage2;
	private ICoReTypeName type2;

	private ProjectFoldedUsage sut;

	@Before
	public void setup() {
		type = mock(ICoReTypeName.class);
		usage = mock(Usage.class);
		when(usage.getType()).thenReturn(type);

		type2 = mock(ICoReTypeName.class);
		usage2 = mock(Usage.class);
		when(usage2.getType()).thenReturn(type2);
	}

	@Test
	public void projectNameCanBeAccessed() {
		sut = new ProjectFoldedUsage(usage, PROJECT_NAME);
		assertEquals(PROJECT_NAME, sut.getProjectName());
		verifyNoMoreInteractions(usage);
	}

	@Test
	public void usageTypeCanBeAccessed() {
		sut = new ProjectFoldedUsage(usage, PROJECT_NAME);
		assertSame(type, sut.getType());
		verify(usage).getType();
		verifyNoMoreInteractions(usage);
	}

	@Test
	public void usageCanBeAccessed() {
		sut = new ProjectFoldedUsage(usage, PROJECT_NAME);
		assertSame(usage, sut.getRawUsage());
		verifyNoMoreInteractions(usage);
	}

	@Test
	public void equalsAndHashCode_same() {
		ProjectFoldedUsage a = new ProjectFoldedUsage(usage, PROJECT_NAME);
		ProjectFoldedUsage b = new ProjectFoldedUsage(usage, PROJECT_NAME);
		assertEquals(a, b);
		assertTrue(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_diffName() {
		ProjectFoldedUsage a = new ProjectFoldedUsage(usage, PROJECT_NAME);
		ProjectFoldedUsage b = new ProjectFoldedUsage(usage, PROJECT_NAME2);
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}

	@Test
	public void equalsAndHashCode_diffUsage() {
		ProjectFoldedUsage a = new ProjectFoldedUsage(usage, PROJECT_NAME);
		ProjectFoldedUsage b = new ProjectFoldedUsage(usage2, PROJECT_NAME);
		assertNotEquals(a, b);
		assertFalse(a.hashCode() == b.hashCode());
	}
}