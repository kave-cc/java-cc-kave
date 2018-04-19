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
package cc.kave.commons.model.typeshapes;

import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;

public class TypeHierarchyTest {

	private static final ITypeName T1 = Names.newType("T, P");
	private static final ITypeName T2 = mock(ITypeName.class);
	private static final ITypeHierarchy TH1 = mock(ITypeHierarchy.class);
	private static final ITypeHierarchy TH2 = mock(ITypeHierarchy.class);

	@Test
	public void defaultValues() {
		TypeHierarchy sut = new TypeHierarchy(T1);
		assertEquals(T1, sut.getElement());
		assertNull(sut.getExtends());
		assertEquals(new HashSet<>(), sut.getImplements());
	}

	@Test
	public void customCtor() {
		TypeHierarchy sut = new TypeHierarchy(T1.getIdentifier());
		assertEquals(T1, sut.getElement());
		assertNull(sut.getExtends());
		assertEquals(new HashSet<>(), sut.getImplements());
	}

	@Test
	public void builderSetter() {
		TypeHierarchy sut1 = new TypeHierarchy(T1);
		TypeHierarchy sut2 = sut1.setExtends(TH1);
		TypeHierarchy sut3 = sut2.addImplements(TH2);

		assertSame(sut1, sut2);
		assertSame(sut2, sut3);

		assertEquals(T1, sut1.getElement());
		assertEquals(TH1, sut1.getExtends());
		assertEquals(new HashSet<>(asList(TH2)), sut1.getImplements());
	}

	@Test
	public void hasSupertype() {
		assertFalse(new TypeHierarchy(T1).hasSupertypes());
		assertTrue(new TypeHierarchy(T1).setExtends(TH1).hasSupertypes());
		assertTrue(new TypeHierarchy(T1).addImplements(TH1).hasSupertypes());
	}

	@Test
	public void hasSuperclass() {
		assertFalse(new TypeHierarchy(T1).hasSuperclass());
		assertTrue(new TypeHierarchy(T1).setExtends(TH1).hasSuperclass());
	}

	@Test
	public void isImplementingInteraces() {
		assertFalse(new TypeHierarchy(T1).isImplementingInterfaces());
		assertTrue(new TypeHierarchy(T1).addImplements(TH1).isImplementingInterfaces());
	}

	@Test
	public void equality_Default() {
		TypeHierarchy a = new TypeHierarchy(T1);
		TypeHierarchy b = new TypeHierarchy(T1);
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_ReallyTheSame() {
		TypeHierarchy a = new TypeHierarchy(T1).setExtends(TH1).addImplements(TH2);
		TypeHierarchy b = new TypeHierarchy(T1).setExtends(TH1).addImplements(TH2);
		assertEqualDataStructures(a, b);

	}

	@Test
	public void equality_DifferentElement() {
		TypeHierarchy a = new TypeHierarchy(T1);
		TypeHierarchy b = new TypeHierarchy(T2);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_DifferentExtends() {
		TypeHierarchy a = new TypeHierarchy(T1).setExtends(TH1);
		TypeHierarchy b = new TypeHierarchy(T1);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_DifferentImplements() {
		TypeHierarchy a = new TypeHierarchy(T1).addImplements(TH2);
		TypeHierarchy b = new TypeHierarchy(T1);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new TypeHierarchy(T1));
	}
}