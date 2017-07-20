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
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;

public class TypeShapeTest {

	private static TypeHierarchy someTypeHierarchy() {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(Names.newType("T,P"));
		return typeHierarchy;
	}

	@Test
	public void testDefaultValues() {
		TypeShape sut = new TypeShape();
		assertThat(new TypeHierarchy(), equalTo(sut.getTypeHierarchy()));
		assertThat(new HashSet<MethodHierarchy>(), equalTo(sut.getMethodHierarchies()));
	}

	@Test
	public void testSettingValues() {
		TypeShape sut = new TypeShape();
		sut.setTypeHierarchy(someTypeHierarchy());
		sut.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));

		assertThat(someTypeHierarchy(), equalTo(sut.getTypeHierarchy()));
		assertThat(Sets.newHashSet(new MethodHierarchy()), equalTo(sut.getMethodHierarchies()));
	}

	@Test
	public void testEquality_Default() {
		TypeShape a = new TypeShape();
		TypeShape b = new TypeShape();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_ReallyTheSame() {
		TypeShape a = new TypeShape();
		TypeShape b = new TypeShape();
		a.setTypeHierarchy(someTypeHierarchy());
		a.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		b.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		b.setTypeHierarchy(someTypeHierarchy());
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_DifferentType() {
		TypeShape a = new TypeShape();
		a.setTypeHierarchy(someTypeHierarchy());
		TypeShape b = new TypeShape();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b)));
	}

	@Test
	public void testEquality_DifferentMethods() {
		TypeShape a = new TypeShape();
		a.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		TypeShape b = new TypeShape();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b)));
	}
}