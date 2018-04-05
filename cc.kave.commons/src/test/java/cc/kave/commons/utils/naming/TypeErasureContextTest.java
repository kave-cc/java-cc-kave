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
package cc.kave.commons.utils.naming;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class TypeErasureContextTest {

	@Test
	public void context_typeShapeAndSSTCombined() {

		SST inSst = new SST();
		inSst.setEnclosingType(Names.newType("T`1[[U -> p:double]], P"));
		Context in = new Context();
		in.setSST(inSst);
		in.getTypeShape().getFields().add(Names.newField("[p:int] [T`1[[U -> p:double]], P]._f"));

		SST outSst = new SST();
		outSst.setEnclosingType(Names.newType("T`1[[U]], P"));
		Context expected = new Context();
		expected.setSST(outSst);
		expected.getTypeShape().getFields().add(Names.newField("[p:int] [T`1[[U]], P]._f"));

		Context actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void context_sst() {
		// Please note: This merely checks whether SST are erased at all, the actual
		// SST-related test are included in TypeErasureVisitorTest

		SST in = new SST();
		in.setEnclosingType(Names.newType("E`1[[G1->T,P]],P"));

		SST expected = new SST();
		expected.setEnclosingType(Names.newType("E`1[[G1]],P"));

		ISST actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void context_typeShape() {
		TypeHierarchy th1 = new TypeHierarchy();
		th1.setElement(Names.newType("E`1[[G1->T,P]],P"));
		TypeShape in = new TypeShape();
		in.setTypeHierarchy(th1);

		TypeHierarchy th2 = new TypeHierarchy();
		th2.setElement(Names.newType("E`1[[G1]],P"));
		TypeShape expected = new TypeShape();
		expected.setTypeHierarchy(th2);

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void typeShape_typeHierarchy() {

		TypeHierarchy th1Ext = new TypeHierarchy();
		th1Ext.setElement(Names.newType("E`1[[G1->T,P]],P"));

		TypeHierarchy th1Impl = new TypeHierarchy();
		th1Impl.setElement(Names.newType("I`1[[G1->T,P]],P"));

		TypeHierarchy th1 = new TypeHierarchy();
		th1.setElement(Names.newType("T`1[[G1->T,P]],P"));
		th1.setExtends(th1Ext);
		th1.getImplements().add(th1Impl);

		TypeHierarchy th1Sub = new TypeHierarchy();
		th1Sub.setExtends(th1);

		TypeShape in = new TypeShape();
		in.setTypeHierarchy(th1Sub);

		/* */

		TypeHierarchy th2Ext = new TypeHierarchy();
		th2Ext.setElement(Names.newType("E`1[[G1]],P"));

		TypeHierarchy th2Impl = new TypeHierarchy();
		th2Impl.setElement(Names.newType("I`1[[G1]],P"));

		TypeHierarchy th2 = new TypeHierarchy();
		th2.setElement(Names.newType("T`1[[G1]],P"));
		th2.setExtends(th2Ext);
		th2.getImplements().add(th2Impl);

		TypeHierarchy th2Sub = new TypeHierarchy();
		th2Sub.setExtends(th2);

		TypeShape expected = new TypeShape();
		expected.setTypeHierarchy(th2Sub);

		/* */

		ITypeShape actual = TypeErasure.of(in);
		assertEquals(expected, actual);
	}

	@Test
	public void typeShape_eventHierarchy() {
		EventHierarchy h1 = new EventHierarchy(Names.newEvent("[p:void] [T`1[[G1->T,P]],P].E()"));
		h1.setSuper(Names.newEvent("[p:void] [S`1[[G1->T,P]],P].E()"));
		h1.setFirst(Names.newEvent("[p:void] [F`1[[G1->T,P]],P].E()"));
		TypeShape in = new TypeShape();
		in.getEventHierarchies().add(h1);

		EventHierarchy h2 = new EventHierarchy(Names.newEvent("[p:void] [T`1[[G1]],P].E()"));
		h2.setSuper(Names.newEvent("[p:void] [S`1[[G1]],P].E()"));
		h2.setFirst(Names.newEvent("[p:void] [F`1[[G1]],P].E()"));
		TypeShape expected = new TypeShape();
		expected.getEventHierarchies().add(h2);

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void typeShape_fields() {
		TypeShape in = new TypeShape();
		in.getFields().add(Names.newField("[p:int] [T`1[[U -> p:double]], P]._f"));

		TypeShape expected = new TypeShape();
		expected.getFields().add(Names.newField("[p:int] [T`1[[U]], P]._f"));

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void typeShape_methodHierarchy() {
		MethodHierarchy h1 = new MethodHierarchy(Names.newMethod("[T,P] [T,P].E`1[[G1->T,P]]()"));
		h1.setSuper(Names.newMethod("[T,P] [T,P].S`1[[G1->T,P]]()"));
		h1.setFirst(Names.newMethod("[T,P] [T,P].F`1[[G1->T,P]]()"));
		TypeShape in = new TypeShape();
		in.getMethodHierarchies().add(h1);

		MethodHierarchy h2 = new MethodHierarchy(Names.newMethod("[T,P] [T,P].E`1[[G1]]()"));
		h2.setSuper(Names.newMethod("[T,P] [T,P].S`1[[G1]]()"));
		h2.setFirst(Names.newMethod("[T,P] [T,P].F`1[[G1]]()"));
		TypeShape expected = new TypeShape();
		expected.getMethodHierarchies().add(h2);

		ITypeShape actual = TypeErasure.of(in);
		assertEquals(expected, actual);
	}

	@Test
	public void typeShape_propertyHierarchy() {
		PropertyHierarchy h1 = new PropertyHierarchy(Names.newProperty("get set [p:int] [T`1[[G->p:double]],P].P()"));
		h1.setSuper(Names.newProperty("get set [p:int] [S`1[[G->p:double]],P].P()"));
		h1.setFirst(Names.newProperty("get set [p:int] [F`1[[G->p:double]],P].P()"));
		TypeShape in = new TypeShape();
		in.getPropertyHierarchies().add(h1);

		PropertyHierarchy h2 = new PropertyHierarchy(Names.newProperty("get set [p:int] [T`1[[G]],P].P()"));
		h2.setSuper(Names.newProperty("get set [p:int] [S`1[[G]],P].P()"));
		h2.setFirst(Names.newProperty("get set [p:int] [F`1[[G]],P].P()"));
		TypeShape expected = new TypeShape();
		expected.getPropertyHierarchies().add(h2);

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void typeShape_delegates() {
		IDelegateTypeName delIn = Names.newType("d:[p:void] [T`1[[U -> p:double]], P].()").asDelegateTypeName();
		IDelegateTypeName delExpected = Names.newType("d:[p:void] [T`1[[U]], P].()").asDelegateTypeName();

		TypeShape in = new TypeShape();
		in.getDelegates().add(delIn);

		TypeShape expected = new TypeShape();
		expected.getDelegates().add(delExpected);

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void typeShape_nestedTypes() {

		TypeShape in = new TypeShape();
		in.getNestedTypes().add(Names.newType("T`1[[U -> p:double]]+N, P"));

		TypeShape expected = new TypeShape();
		expected.getNestedTypes().add(Names.newType("T`1[[U]]+N, P"));

		ITypeShape actual = TypeErasure.of(in);
		Assert.assertEquals(expected, actual);
	}
}