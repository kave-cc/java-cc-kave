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

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class MethodHierarchyTest {

	@Test
	public void testDefaultValues() {
		MethodHierarchy sut = new MethodHierarchy();
		assertThat(Names.getUnknownMethod(), equalTo(sut.getElement()));
		assertNull(sut.getSuper());
		assertNull(sut.getFirst());
		assertFalse(sut.isDeclaredInParentHierarchy());
	}

	@Test
	public void testDefaultValues_CustomConstructor() {
		MethodHierarchy sut = new MethodHierarchy(m("x"));
		assertThat(m("x"), equalTo(sut.getElement()));
		assertNull(sut.getSuper());
		assertNull(sut.getFirst());
		assertFalse(sut.isDeclaredInParentHierarchy());

	}

	@Test
	public void testSettingValues() {
		MethodHierarchy sut = new MethodHierarchy();
		sut.setElement(m("a"));
		sut.setSuper(m("b"));
		sut.setFirst(m("c"));
		assertThat(m("a"), equalTo(sut.getElement()));
		assertThat(m("b"), equalTo(sut.getSuper()));
		assertThat(m("c"), equalTo(sut.getFirst()));
	}

	@Test
	public void testShouldBeOverrideOrImplementationWhenFirstIsSet() {
		MethodHierarchy sut = new MethodHierarchy();
		sut.setFirst(m("a"));
		assertTrue(sut.isDeclaredInParentHierarchy());
	}

	@Test
	public void testEquality_Default() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_ReallyTheSame() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setElement(m("a"));
		a.setSuper(m("b"));
		a.setFirst(m("c"));
		b.setElement(m("a"));
		b.setSuper(m("b"));
		b.setFirst(m("c"));
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_DifferentElement() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setElement(m("a"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentSuper() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setSuper(m("b"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentFirst() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setFirst(m("c"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	private static IMethodName m(String s) {
		return Names.newMethod("[T1,P1] [T2,P2]." + s + "()");
	}
}