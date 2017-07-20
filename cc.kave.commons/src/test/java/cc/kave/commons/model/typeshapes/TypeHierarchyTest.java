/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.typeshapes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.testcommons.ToStringAssert;

public class TypeHierarchyTest {

	@Test
	public void testDefaultValues() {
		TypeHierarchy sut = new TypeHierarchy();
		assertThat(Names.getUnknownType(), equalTo(sut.getElement()));
		assertNull(sut.getExtends());
		assertThat(new HashSet<ITypeHierarchy>(), equalTo(sut.getImplements()));
		assertFalse(sut.hasSuperclass());
		assertFalse(sut.hasSupertypes());
		assertFalse(sut.isImplementingInterfaces());
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testDefaultValues_CustomConstructor() {
		TypeHierarchy sut = new TypeHierarchy("T,P");
		assertThat(Names.newType("T,P"), equalTo(sut.getElement()));
	}

	@Test
	public void testSettingValues() {
		TypeHierarchy sut = new TypeHierarchy();
		sut.setElement(Names.newType("T1,P1"));
		sut.setExtends(someHierarchy("x"));
		sut.setImplements(Sets.newHashSet(someHierarchy("y")));
		assertThat(Names.newType("T1,P1"), equalTo(sut.getElement()));
		assertThat(someHierarchy("x"), equalTo(sut.getExtends()));
		assertThat(Sets.newHashSet(someHierarchy("y")), equalTo(sut.getImplements()));
		assertTrue(sut.hasSuperclass());
		assertTrue(sut.hasSupertypes());
		assertTrue(sut.isImplementingInterfaces());
	}

	@Test
	public void testEquality_Default() {
		TypeHierarchy a = new TypeHierarchy();
		TypeHierarchy b = new TypeHierarchy();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_ReallyTheSame() {
		TypeHierarchy a = new TypeHierarchy();
		a.setElement(Names.newType("T1,P1"));
		a.setExtends(someHierarchy("x"));
		a.setImplements(Sets.newHashSet(someHierarchy("y")));
		TypeHierarchy b = new TypeHierarchy();
		b.setElement(Names.newType("T1,P1"));
		b.setExtends(someHierarchy("x"));
		b.setImplements(Sets.newHashSet(someHierarchy("y")));
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_DifferentElement() {
		TypeHierarchy a = new TypeHierarchy();
		a.setElement(Names.newType("T1,P1"));
		TypeHierarchy b = new TypeHierarchy();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentExtends() {
		TypeHierarchy a = new TypeHierarchy();
		a.setExtends(someHierarchy("x"));
		TypeHierarchy b = new TypeHierarchy();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentImplements() {
		TypeHierarchy a = new TypeHierarchy();
		a.setImplements(Sets.newHashSet(someHierarchy("y")));
		TypeHierarchy b = new TypeHierarchy();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void toStringIsImplemented() {
		ToStringAssert.assertToStringUtils(new TypeHierarchy());
	}

	private static TypeHierarchy someHierarchy(String string) {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(Names.newType(string + ",P"));
		return typeHierarchy;
	}
}