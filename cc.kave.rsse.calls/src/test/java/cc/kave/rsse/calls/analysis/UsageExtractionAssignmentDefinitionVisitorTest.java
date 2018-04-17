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
package cc.kave.rsse.calls.analysis;

import static cc.kave.commons.model.ssts.impl.SSTUtil.eventRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.fieldRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpression;
import static cc.kave.commons.model.ssts.impl.SSTUtil.methodRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class UsageExtractionAssignmentDefinitionVisitorTest {

	private TypeShape typeShape;

	@Before
	public void setup() {
		typeShape = new TypeShape();
	}

	private void assertDef(ISSTNode n, IDefinition expected) {
		UsageExtractionAssignmentDefinitionVisitor v = new UsageExtractionAssignmentDefinitionVisitor();
		IDefinition actual = n.accept(v, typeShape);
		assertEquals(expected, actual);
	}

	// ### ISimpleExpr #########

	@Test
	public void assign_constant() {
		assertDef(new ConstantValueExpression(), Definitions.definedByConstant());
	}

	@Test
	public void assign_null() {
		assertDef(new NullExpression(), Definitions.definedByConstant());
	}

	@Test
	public void assign_refExpr_event() {
		IEventName n = mock(IEventName.class);
		assertDef(refExpr(eventRef(varRef("o"), n)), definedByMemberAccess(n));
	}

	@Test
	public void assign_refExpr_eventThisOverridesSuper() {
		IEventName n = mock(IEventName.class, "n");
		IEventName s = mock(IEventName.class, "s");

		EventHierarchy eh = new EventHierarchy(n);
		eh.setSuper(s);
		typeShape.eventHierarchies.add(eh);

		assertDef(refExpr(eventRef(varRef("this"), n)), definedByMemberAccess(s));
	}

	@Test
	public void assign_refExpr_eventThisOverridesFirst() {
		IEventName n = mock(IEventName.class, "n");
		IEventName f = mock(IEventName.class, "f");
		IEventName s = mock(IEventName.class, "s");

		EventHierarchy eh = new EventHierarchy(n);
		eh.setSuper(s);
		eh.setFirst(f);
		typeShape.eventHierarchies.add(eh);

		assertDef(refExpr(eventRef(varRef("this"), n)), definedByMemberAccess(f));
	}

	@Test
	public void assign_refExpr_field() {
		IFieldName n = mock(IFieldName.class);
		assertDef(refExpr(fieldRef(varRef("o"), n)), Definitions.definedByMemberAccess(n));
	}

	@Test
	public void assign_refExpr_indexAccess() {
		assertDef(refExpr(new IndexAccessReference()), Definitions.definedByUnknown());
	}

	@Test
	public void assign_refExpr_method() {
		IMethodName n = mock(IMethodName.class);
		assertDef(refExpr(methodRef(varRef("o"), n)), definedByMemberAccess(n));
	}

	@Test
	public void assign_refExpr_methodThisOverridesSuper() {
		IMethodName n = mock(IMethodName.class, "n");
		IMethodName s = mock(IMethodName.class, "s");

		MethodHierarchy eh = new MethodHierarchy(n);
		eh.setSuper(s);
		typeShape.methodHierarchies.add(eh);

		assertDef(refExpr(methodRef(varRef("this"), n)), definedByMemberAccess(s));
	}

	@Test
	public void assign_refExpr_methodThisOverridesFirst() {
		IMethodName n = mock(IMethodName.class, "n");
		IMethodName f = mock(IMethodName.class, "f");
		IMethodName s = mock(IMethodName.class, "s");

		MethodHierarchy eh = new MethodHierarchy(n);
		eh.setSuper(s);
		eh.setFirst(f);
		typeShape.methodHierarchies.add(eh);

		assertDef(refExpr(methodRef(varRef("this"), n)), definedByMemberAccess(f));
	}

	@Test
	public void assign_refExpr_property() {
		IPropertyName n = mock(IPropertyName.class);
		assertDef(refExpr(propertyRef(varRef("o"), n)), definedByMemberAccess(n));
	}

	@Test
	public void assign_refExpr_propertyThisOverridesSuper() {
		IPropertyName n = mock(IPropertyName.class, "n");
		IPropertyName s = mock(IPropertyName.class, "s");

		PropertyHierarchy eh = new PropertyHierarchy(n);
		eh.setSuper(s);
		typeShape.propertyHierarchies.add(eh);

		assertDef(refExpr(propertyRef(varRef("this"), n)), definedByMemberAccess(s));
	}

	@Test
	public void assign_refExpr_propertyThisOverridesFirst() {
		IPropertyName n = mock(IPropertyName.class, "n");
		IPropertyName f = mock(IPropertyName.class, "f");
		IPropertyName s = mock(IPropertyName.class, "s");

		PropertyHierarchy eh = new PropertyHierarchy(n);
		eh.setSuper(s);
		eh.setFirst(f);
		typeShape.propertyHierarchies.add(eh);

		assertDef(refExpr(propertyRef(varRef("this"), n)), definedByMemberAccess(f));
	}

	@Test
	public void assign_refExpr_var_local() {
		assertDef(refExpr(varRef("o")), Definitions.definedByUnknown());
	}

	@Test
	public void assign_refExpr_var_this() {
		assertDef(refExpr(varRef("this")), Definitions.definedByThis());
	}

	@Test
	public void assign_refExpr_var_base() {
		// not sure if this is actually possible :D
		assertDef(refExpr(varRef("base")), Definitions.definedByThis());
	}

	@Test
	public void assign_refExpr_unknown() {
		assertDef(refExpr(new UnknownReference()), Definitions.definedByUnknown());
	}

	@Test
	public void assign_unknown() {
		assertDef(new UnknownExpression(), Definitions.definedByUnknown());
	}

	// ### IAssignableExpr #########

	@Test
	public void assign_binary() {
		assertDef(new BinaryExpression(), Definitions.definedByUnknown());
	}

	@Test
	public void assign_cast() {
		assertDef(new CastExpression(), Definitions.definedByCast());
	}

	@Test
	public void assign_completion() {
		assertDef(new CompletionExpression(), Definitions.definedByUnknown());
	}

	@Test
	public void assign_composed() {
		assertDef(new ComposedExpression(), Definitions.definedByUnknown());
	}

	@Test
	public void assign_ifElseExpr() {
		assertDef(new IfElseExpression(), Definitions.definedByUnknown());
	}

	@Test
	public void assign_indexAccess() {
		assertDef(new IndexAccessExpression(), Definitions.definedByUnknown());
	}

	@Test
	@Ignore
	public void assign_invocation_lambda() {
		// figure out how this is stored in an SST
		fail();
	}

	@Test
	public void assign_invocation_method() {
		IMethodName m = Names.newMethod("[p:int] [p:int].m()");
		assertDef(SSTUtil.invocationExpression("o", m), Definitions.definedByReturnValue(m));
	}

	@Test
	public void assign_invocation_methodThisOverridesSuper() {
		IMethodName n = Names.newMethod("[p:int] [N, P].m()");
		IMethodName s = Names.newMethod("[p:int] [S, P].m()");

		MethodHierarchy eh = new MethodHierarchy(n);
		eh.setSuper(s);
		typeShape.methodHierarchies.add(eh);

		assertDef(SSTUtil.invocationExpression("this", n), Definitions.definedByReturnValue(s));
	}

	@Test
	public void assign_invocation_methodThisOverridesFirst() {
		IMethodName n = Names.newMethod("[p:int] [N, P].m()");
		IMethodName s = Names.newMethod("[p:int] [S, P].m()");
		IMethodName f = Names.newMethod("[p:int] [F, P].m()");

		MethodHierarchy eh = new MethodHierarchy(n);
		eh.setSuper(s);
		eh.setFirst(f);
		typeShape.methodHierarchies.add(eh);

		assertDef(invocationExpression("this", n), Definitions.definedByReturnValue(f));
	}

	@Test
	public void assign_invocation_ctor() {
		IMethodName n = mock(IMethodName.class, "n");
		when(n.isConstructor()).thenReturn(true);
		assertDef(SSTUtil.invocationExpression("this", n), Definitions.definedByConstructor(n));
	}

	@Test
	@Ignore
	public void assign_invocation_delegate() {
		// figure out how this is stored in an SST
		fail();
	}

	@Test
	public void assign_lambdaDecl() {
		assertDef(new LambdaExpression(), Definitions.definedByLambdaDecl());
	}

	@Test
	public void assign_typecheck() {
		assertDef(new TypeCheckExpression(), Definitions.definedByConstant());
	}

	@Test
	public void assign_unary() {
		assertDef(new UnaryExpression(), Definitions.definedByUnknown());
	}
}