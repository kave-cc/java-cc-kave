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

import static cc.kave.commons.model.naming.Names.newEvent;
import static cc.kave.commons.model.naming.Names.newLambda;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.ssts.impl.SSTUtil.eventRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.methodRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.FUNC2;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.invExpr;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.memberAccess;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.EventHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;

public class UsageExtractionUsageSitesTest extends UsageExtractionTestBase {

	private MethodDeclaration md1;
	private SST sst;

	@Before
	public void setup() {
		md1 = new MethodDeclaration(m(1, 1));
		sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);
		addUniqueAOs(sst, md1);
	}

	@Test
	public void extraction_methods() {
		IInvocationExpression inv1 = invExpr("o", m(2, 1));
		md1.body.add(exprStmt(inv1));

		addUniqueAOs(inv1.getReference());

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void extraction_propertyGetters() {
		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(newProperty("set get [p:int] [%s].P()", t(1).getIdentifier()));
		sst.getProperties().add(pd);

		IInvocationExpression inv1 = invExpr("o", m(2, 1));
		pd.getGet().add(exprStmt(inv1));

		IParameterName value = pd.getName().getExplicitSetterName().getParameters().get(0);
		addUniqueAOs(pd, value, inv1.getReference());

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void extraction_propertySetters() {
		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(newProperty("set get [p:int] [%s].P()", t(1).getIdentifier()));
		sst.getProperties().add(pd);

		IInvocationExpression inv1 = invExpr("o", m(2, 1));
		pd.getSet().add(exprStmt(inv1));

		IParameterName value = pd.getName().getExplicitSetterName().getParameters().get(0);
		addUniqueAOs(pd, value, inv1.getReference());

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void extraction_lambda() {

		IInvocationExpression inv1 = invExpr("p1", m(2, 1));

		ILambdaName ln = newLambda("[p:void] ([p:int] p1)");
		LambdaExpression le = new LambdaExpression();
		le.setName(ln);
		le.getBody().add(exprStmt(inv1));

		md1.getBody().add(exprStmt(le));

		IParameterName p = ln.getParameters().get(0);
		addUniqueAOs(le, p, inv1.getReference());

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void extraction_lambdaInLambda() {

		IInvocationExpression inv1 = invExpr("p2", m(2, 1));

		ILambdaName ln2 = newLambda("[p:void] ([p:int] p2)");
		LambdaExpression le2 = new LambdaExpression();
		le2.setName(ln2);
		le2.getBody().add(exprStmt(inv1));

		ILambdaName ln1 = newLambda("[p:void] ([p:char] p1)");
		LambdaExpression le1 = new LambdaExpression();
		le1.setName(ln1);
		le1.getBody().add(exprStmt(le2));

		md1.getBody().add(exprStmt(le1));

		IParameterName p1 = ln1.getParameters().get(0);
		IParameterName p2 = ln2.getParameters().get(0);
		addAO(inv1.getReference(), p2);
		addUniqueAOs(le1, p1, le2);

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void access_event() {

		IEventName e = newEvent("[%s] [%s].E", FUNC2, t(1));

		EventReference er = new EventReference();
		er.setReference(varRef("this"));
		er.setEventName(e);

		md1.body.add(exprStmt(refExpr(er)));

		addUniqueAOs(er.getReference(), er);

		IUsage actual = assertOneUsage(ctx(sst), er.getReference());
		assertEquals(newArrayList(memberAccess(e)), actual.getUsageSites());
	}

	@Test
	public void access_field() {

		IFieldName f = Names.newField("[p:int] [%s].E", t(1));

		FieldReference r = new FieldReference();
		r.setReference(varRef("o"));
		r.setFieldName(f);

		md1.body.add(exprStmt(refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		IUsage actual = assertOneUsage(ctx(sst), r.getReference());
		assertEquals(newArrayList(memberAccess(f)), actual.getUsageSites());
	}

	@Test
	public void access_methodAccess() {

		MethodReference r = new MethodReference();
		r.setReference(varRef("o"));
		r.setMethodName(m(2, 3));

		md1.body.add(exprStmt(refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		IUsage actual = assertOneUsage(ctx(sst), r.getReference());
		assertEquals(newArrayList(memberAccess(m(2, 3))), actual.getUsageSites());
	}

	@Test
	public void access_methodCall() {

		IInvocationExpression inv1 = invExpr("o", m(2, 1));

		md1.body.add(exprStmt(inv1));

		addAO(inv1.getReference());

		IUsage actual = assertOneUsage(ctx(sst), inv1.getReference());
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void access_methodCallParam() {

		IMethodName m = newMethod("[p:void] [p:object].m([p:int] arg)");
		IInvocationExpression inv1 = invExpr("o", m, "p");

		md1.body.add(exprStmt(inv1));

		IVariableReference o = inv1.getReference();
		IReference arg = ((IReferenceExpression) inv1.getParameters().get(0)).getReference();
		addUniqueAOs(o, arg);

		IUsage actual = assertOneUsage(ctx(sst), arg);
		assertEquals(newArrayList(callParameter(m, 0)), actual.getUsageSites());
	}

	@Test
	public void access_property() {

		IPropertyName p = Names.newProperty("set get [p:int] [%s].P()", t(1).getIdentifier());

		PropertyReference r = new PropertyReference();
		r.setReference(varRef("o"));
		r.setPropertyName(p);

		md1.body.add(exprStmt(refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		IUsage actual = assertOneUsage(ctx(sst), r.getReference());
		assertEquals(newArrayList(memberAccess(p)), actual.getUsageSites());
	}

	@Test
	public void rebase_this_event() {
		IEventName e = newEvent("[?] [%s].E", t(1).getIdentifier());
		IEventName s = newEvent("[?] [%s].E", t(1).getIdentifier());
		IEventReference r = eventRef(varRef("this"), e);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getEventHierarchies().add(new EventHierarchy(e).setSuper(s));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(s)), actual.getUsageSites());
	}

	@Test
	public void rebase_base_event() {
		IEventName e = newEvent("[?] [%s].E", t(1).getIdentifier());
		IEventName s = newEvent("[?] [%s].E", t(1).getIdentifier());
		IEventName f = newEvent("[?] [%s].E", t(1).getIdentifier());
		IEventReference r = eventRef(varRef("base"), s);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getEventHierarchies().add(new EventHierarchy(e).setSuper(s).setFirst(f));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(f)), actual.getUsageSites());
	}

	@Test
	public void rebase_this_methodAccess() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 1);
		IMethodReference r = methodRef(varRef("this"), e);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(e).setSuper(s));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(s)), actual.getUsageSites());
	}

	@Test
	public void rebase_base_methodAccess() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 1);
		IMethodName f = m(3, 1);
		IMethodReference r = methodRef(varRef("base"), s);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(e).setSuper(s).setFirst(f));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(f)), actual.getUsageSites());
	}

	@Test
	public void rebase_this_methodCall() {
		IInvocationExpression inv1 = invExpr("this", m(1, 1));

		md1.body.add(exprStmt(inv1));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)).setSuper(m(2, 1)));

		resetAOs();
		addAO(sst, inv1.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, sst);
		assertEquals(newArrayList(call(m(2, 1))), actual.getUsageSites());
	}

	@Test
	public void rebase_base_methodCall() {
		IMethodName e = m(1, 1);
		IMethodName s = m(2, 1);
		IMethodName f = m(3, 1);
		IInvocationExpression inv1 = invExpr("base", s);

		md1.body.add(exprStmt(inv1));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(e).setSuper(s).setFirst(f));

		resetAOs();
		addAO(sst, inv1.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, sst);
		assertEquals(newArrayList(call(f)), actual.getUsageSites());
	}

	@Test
	public void rebase_this_methodCallParam() {

		IMethodName e = Names.newMethod("[p:void] [%s].m([p:char] arg)", t(1).getIdentifier());
		IMethodName s = Names.newMethod("[p:void] [%s].m([p:char] arg)", t(2).getIdentifier());

		IInvocationExpression inv1 = invExpr("this", e, "p"); // overrides 2,2
		md1.body.add(exprStmt(inv1));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(e).setSuper(s));

		resetAOs();
		IReference arg = ((IReferenceExpression) inv1.getParameters().get(0)).getReference();
		addAO(sst, inv1.getReference());
		addUniqueAOs(md1, arg);

		IUsage actual = assertOneUsage(ctx, arg);
		assertEquals(newArrayList(callParameter(s, 0)), actual.getUsageSites());
	}

	@Test
	public void rebase_base_methodCallParam() {

		IMethodName e = Names.newMethod("[p:void] [%s].m([p:char] arg)", t(1).getIdentifier());
		IMethodName s = Names.newMethod("[p:void] [%s].m([p:char] arg)", t(2).getIdentifier());
		IMethodName f = Names.newMethod("[p:void] [%s].m([p:char] arg)", t(3).getIdentifier());

		IInvocationExpression inv1 = invExpr("base", s, "p"); // overrides 2,2
		md1.body.add(exprStmt(inv1));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(e).setSuper(s).setFirst(f));

		resetAOs();
		IReference arg = ((IReferenceExpression) inv1.getParameters().get(0)).getReference();
		addAO(sst, inv1.getReference());
		addUniqueAOs(md1, arg);

		IUsage actual = assertOneUsage(ctx, arg);
		assertEquals(newArrayList(callParameter(f, 0)), actual.getUsageSites());
	}

	@Test
	public void rebase_this_property() {
		IPropertyName e = newProperty("set get [p:int] [%s].P()", t(1).getIdentifier());
		IPropertyName s = newProperty("set get [p:int] [%s].P()", t(2).getIdentifier());

		IPropertyReference r = propertyRef(varRef("this"), e);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getPropertyHierarchies().add(new PropertyHierarchy(e).setSuper(s));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(s)), actual.getUsageSites());
	}

	@Test
	public void rebase_base_property() {
		IPropertyName e = newProperty("set get [p:int] [%s].P()", t(1).getIdentifier());
		IPropertyName s = newProperty("set get [p:int] [%s].P()", t(2).getIdentifier());
		IPropertyName f = newProperty("set get [p:int] [%s].P()", t(3).getIdentifier());

		IPropertyReference r = propertyRef(varRef("base"), s);
		md1.body.add(exprStmt(refExpr(r)));

		Context ctx = ctx(sst);
		ctx.getTypeShape().getPropertyHierarchies().add(new PropertyHierarchy(e).setSuper(s).setFirst(f));

		resetAOs();
		addAO(sst, r.getReference());
		addUniqueAOs(md1);

		IUsage actual = assertOneUsage(ctx, r.getReference());
		assertEquals(newArrayList(memberAccess(f)), actual.getUsageSites());
	}

	protected void assertUsageSites(Context ctx, Object k, Usage expected) {
		Object ao = p2info.getAbstractObject(k);
		Map<Object, List<IUsage>> actuals = sut.extractMap(ctx);
		for (IUsage actual : actuals.get(ao)) {
			boolean isRightMCtx = actual.getMethodContext().equals(expected.getMethodContext());
			if (isRightMCtx) {
				if (expected.getUsageSites().equals(actual.getUsageSites())) {
					return;
				} else {
					fail(String.format("UsageSites did not match. Expected:\n%s\n---- but was: ----\n%s", expected,
							actual));
				}
			}
		}
		fail("Usage could not be found.");
	}
}