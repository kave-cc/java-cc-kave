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
package cc.kave.commons.utils.ssts;

import static cc.kave.commons.utils.ssts.SSTUtils.ACTION;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachy;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.findFirstOccurrenceInHierachyFromBase;
import static cc.kave.commons.utils.ssts.TypeShapeUtils.isDeclaredInSameType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class TypeShapeUtilsTest {

	private TypeShape ts;

	@Before
	public void setup() {
		ts = new TypeShape();
		ts.setTypeHierarchy(new TypeHierarchy(t(1).getIdentifier()));
	}

	@Test
	public void isSameType_true() {
		assertTrue(isDeclaredInSameType(m(1, 2), ts));
	}

	@Test
	public void isSameType_false() {
		assertFalse(isDeclaredInSameType(m(2, 3), ts));
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_memberNull() {
		findFirstOccurrenceInHierachy(null, ts);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fail_baseMemberNull() {
		findFirstOccurrenceInHierachyFromBase(null, ts);
	}

	@Test(expected = IllegalArgumentException.class)
	public void findFirst_diffType() {
		findFirstOccurrenceInHierachy(m(2, 3), ts);
	}

	@Test
	public void event_onlyElement() {
		IEventName e = e(1, 1);
		ts.eventHierarchies.add(new EventHierarchy(e));
		assertEquals(e, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void event_super() {
		IEventName e = e(1, 1);
		IEventName s = e(2, 2);
		ts.eventHierarchies.add(set(new EventHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void event_superAndFirst() {
		IEventName e = e(1, 1);
		IEventName s = e(2, 2);
		IEventName f = e(3, 3);
		ts.eventHierarchies.add(set(new EventHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void event_notFound() {
		assertEquals(e(1, 1), findFirstOccurrenceInHierachy(e(1, 1), ts));
	}

	@Test(expected = IllegalArgumentException.class)
	public void field_alwaysFails() {
		findFirstOccurrenceInHierachy(mock(IFieldName.class), ts);
	}

	@Test
	public void method_onlyElement() {
		IMethodName e = m(1, 1);
		ts.methodHierarchies.add(new MethodHierarchy(e));
		assertEquals(e, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void method_super() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 2);
		ts.methodHierarchies.add(set(new MethodHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void method_superAndFirst() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 2);
		IMethodName f = m(3, 3);
		ts.methodHierarchies.add(set(new MethodHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void method_notFound() {
		assertEquals(m(1, 1), findFirstOccurrenceInHierachy(m(1, 1), ts));
	}

	@Test
	public void property_onlyElement() {
		IPropertyName e = p(1, 1);
		ts.propertyHierarchies.add(new PropertyHierarchy(e));
		assertEquals(e, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void property_super() {
		IPropertyName e = p(1, 1);
		IPropertyName s = p(2, 2);
		ts.propertyHierarchies.add(set(new PropertyHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void property_superAndFirst() {
		IPropertyName e = p(1, 1);
		IPropertyName s = p(2, 2);
		IPropertyName f = p(3, 3);
		ts.propertyHierarchies.add(set(new PropertyHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachy(e, ts));
	}

	@Test
	public void property_notFound() {
		assertEquals(p(1, 1), findFirstOccurrenceInHierachy(p(1, 1), ts));
	}

	@Test
	public void base_method_super() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 2);
		ts.methodHierarchies.add(set(new MethodHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_event_super() {
		IEventName e = e(1, 1);
		IEventName s = e(2, 2);
		ts.eventHierarchies.add(set(new EventHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_event_superAndFirst() {
		IEventName e = e(1, 1);
		IEventName s = e(2, 2);
		IEventName f = e(3, 3);
		ts.eventHierarchies.add(set(new EventHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_event_notFound() {
		IEventName e = e(1, 1);
		assertEquals(e, findFirstOccurrenceInHierachyFromBase(e, ts));
	}

	@Test(expected = IllegalArgumentException.class)
	public void base_field_alwaysFails() {
		findFirstOccurrenceInHierachyFromBase(mock(IFieldName.class), ts);
	}

	@Test
	public void base_method_superAndFirst() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 2);
		IMethodName f = m(3, 3);
		ts.methodHierarchies.add(set(new MethodHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_method_notFound() {
		IMethodName e = m(1, 1);
		assertEquals(e, findFirstOccurrenceInHierachyFromBase(e, ts));
	}

	@Test
	public void base_property_super() {
		IPropertyName e = p(1, 1);
		IPropertyName s = p(2, 2);
		ts.propertyHierarchies.add(set(new PropertyHierarchy(e), s, null));
		assertEquals(s, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_property_superAndFirst() {
		IPropertyName e = p(1, 1);
		IPropertyName s = p(2, 2);
		IPropertyName f = p(3, 3);
		ts.propertyHierarchies.add(set(new PropertyHierarchy(e), s, f));
		assertEquals(f, findFirstOccurrenceInHierachyFromBase(s, ts));
	}

	@Test
	public void base_property_notFound() {
		IPropertyName e = p(1, 1);
		assertEquals(e, findFirstOccurrenceInHierachyFromBase(e, ts));
	}

	private <T extends IMemberName> IMemberHierarchy<T> set(IMemberHierarchy<T> mh, T s, T f) {
		if (s != null) {
			mh.setSuper(s);
		}
		if (f != null) {
			mh.setFirst(f);
		}
		return mh;
	}

	private IEventName e(int i, int j) {
		return Names.newEvent("[%s] [%s].E%d", ACTION.getIdentifier(), t(i).getIdentifier(), j);
	}

	private IMethodName m(int i, int j) {
		return Names.newMethod("[p:void] [%s].m%d()", t(i).getIdentifier(), j);
	}

	private IPropertyName p(int i, int j) {
		return Names.newProperty("set get [p:int] [%s].P%d()", t(i).getIdentifier(), j);
	}

	private ITypeName t(int i) {
		return Names.newType("T%d, P", i);
	}
}