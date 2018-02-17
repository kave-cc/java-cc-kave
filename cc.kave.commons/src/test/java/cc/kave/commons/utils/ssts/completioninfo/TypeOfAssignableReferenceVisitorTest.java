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
package cc.kave.commons.utils.ssts.completioninfo;

import static cc.kave.commons.utils.ssts.SSTUtils.varRef;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling;

public class TypeOfAssignableReferenceVisitorTest {

	private static final ITypeName INT = Names.newType("p:int");

	private VariableScope<ITypeName> vars;

	// Event
	// field
	// index access
	// prop
	// unknown
	// Variable

	@Before
	public void setup() {
		vars = new VariableScope<ITypeName>(ErrorHandling.THROW);
	}

	private void assertType(ITypeName expected, IReference ref) {
		ITypeName actual = ref.accept(new TypeOfAssignableReferenceVisitor(), vars);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void event() {
		EventReference ref = new EventReference();
		ref.setEventName(Names.newEvent("[p:int] [C,P].E"));
		assertType(INT, ref);
	}

	@Test
	public void field() {
		FieldReference ref = new FieldReference();
		ref.setFieldName(Names.newField("[p:int] [C,P]._f"));
		assertType(INT, ref);
	}

	@Test
	public void indexAccess() {
		ITypeName int3D = Names.newArrayType(3, INT);
		ITypeName int2D = Names.newArrayType(2, INT);

		vars.declare("arr", int3D);

		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(varRef("arr"));
		expr.getIndices().add(new ConstantValueExpression());

		IndexAccessReference ref = new IndexAccessReference();
		ref.setExpression(expr);

		assertType(int2D, ref);
	}

	@Test
	public void indexAccess_undeclared() {
		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(varRef("arr"));
		expr.getIndices().add(new ConstantValueExpression());

		IndexAccessReference ref = new IndexAccessReference();
		ref.setExpression(expr);

		assertType(Names.getUnknownType(), ref);
	}

	@Test
	public void indexAccess_nonArray() {
		vars.declare("arr", INT);

		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(varRef("arr"));
		expr.getIndices().add(new ConstantValueExpression());

		IndexAccessReference ref = new IndexAccessReference();
		ref.setExpression(expr);

		assertType(Names.getUnknownType(), ref);
	}

	@Test
	public void property() {
		PropertyReference ref = new PropertyReference();
		ref.setPropertyName(Names.newProperty("get set [p:int] [C,P].P()"));
		assertType(INT, ref);
	}

	@Test
	public void unknown() {
		UnknownReference ref = new UnknownReference();
		assertType(Names.getUnknownType(), ref);
	}

	@Test
	public void variable() {
		vars.declare("i", INT);

		VariableReference ref = new VariableReference();
		ref.setIdentifier("i");

		assertType(INT, ref);
	}

	@Test
	public void variableUndeclared() {

		VariableReference ref = new VariableReference();
		ref.setIdentifier("i");

		assertType(Names.getUnknownType(), ref);
	}
}