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
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.FUNC2;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.invExpr;
import static cc.kave.commons.utils.ssts.SSTUtils.varDecl;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

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
	public void body_inMethods() {
		fail();
	}

	@Test
	public void body_inProperties() {
		fail();
	}

	@Test
	public void access_calls() {

		IVariableDeclaration varDecl = varDecl("o", t(2));
		IInvocationExpression inv1 = invExpr("o", m(2, 1));

		md1.body.add(varDecl);
		md1.body.add(exprStmt(inv1));

		Context ctx = ctx(sst);

		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)));

		addAO(varDecl.getReference(), inv1.getReference());

		Usage u = new Usage();
		u.methodCtx = m(1, 1);
		u.usageSites.add(UsageSites.call(m(2, 1)));

		assertUsageSites(ctx, inv1.getReference(), u);
	}

	@Test
	public void access_callParameters() {

		IVariableDeclaration varDecl1 = varDecl("p", t(3));
		IVariableDeclaration varDecl2 = varDecl("o", t(2));
		IInvocationExpression inv1 = invExpr("o", m(2, 1), "p");

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(varDecl1);
		md1.body.add(varDecl2);
		md1.body.add(exprStmt(inv1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		Context ctx = ctx(sst);

		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)));

		addAO(varDecl1.getReference(), ((IReferenceExpression) inv1.getParameters().get(0)).getReference());
		addAO(varDecl2.getReference(), inv1.getReference());
		addUniqueAOs(sst, md1);

		Usage expected = new Usage();
		expected.type = t(3);
		expected.definition = definedByUnknown();
		expected.classCtx = t(1);
		expected.methodCtx = m(1, 1);
		expected.usageSites.add(callParameter(m(2, 1), 0));

		assertUsage(ctx, md1.getName(), varDecl1.getReference(), expected);
	}

	@Test
	public void access_rebasedOverriddenCallsOnThis() {
		MethodDeclaration md1 = new MethodDeclaration(m(1, 1)); // overrides 2,1

		IInvocationExpression inv1 = invExpr("this", m(1, 1));

		md1.body.add(exprStmt(inv1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		Context ctx = ctx(sst);

		MethodHierarchy mh1 = new MethodHierarchy(m(1, 1));
		mh1.setSuper(m(2, 1));
		ctx.getTypeShape().getMethodHierarchies().add(mh1);

		addAO(sst, inv1.getReference());
		addUniqueAOs(md1);

		Usage expected = new Usage();
		expected.type = t(1);
		expected.classCtx = t(1);
		expected.methodCtx = md1.getName();
		expected.definition = definedByThis();
		expected.usageSites.add(call(m(2, 1)));

		assertUsage(ctx, md1.getName(), sst, expected);
	}

	@Test
	public void access_rebasesOverriddenParameterCallsOnThis() {

		IVariableDeclaration varDecl1 = varDecl("p", t(3));
		IInvocationExpression inv1 = invExpr("this", m(1, 2), "p"); // overrides 2,2

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(varDecl1);
		md1.body.add(exprStmt(inv1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		Context ctx = ctx(sst);

		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)));
		MethodHierarchy mh2 = new MethodHierarchy(m(1, 2));
		mh2.setSuper(m(2, 2));
		ctx.getTypeShape().getMethodHierarchies().add(mh2);

		addAO(varDecl1.getReference(), ((IReferenceExpression) inv1.getParameters().get(0)).getReference());
		addAO(sst, inv1.getReference());
		addUniqueAOs(md1);

		Usage expected = new Usage();
		expected.type = t(3);
		expected.definition = definedByUnknown();
		expected.classCtx = t(1);
		expected.methodCtx = m(1, 1);
		expected.usageSites.add(callParameter(m(2, 2), 0));

		assertUsage(ctx, md1.getName(), varDecl1.getReference(), expected);
	}

	@Test
	public void access_event() {

		EventReference er = new EventReference();
		er.setReference(varRef("this"));
		er.setEventName(newEvent("[%s] [%s].E", FUNC2, t(1)));

		md1.body.add(exprStmt(SSTUtil.refExpr(er)));

		addUniqueAOs(er.getReference(), er);

		Usage u = new Usage();
		u.methodCtx = m(1, 1);
		// member access to events is not captured right nwo

		assertUsageSites(ctx(sst), er, u);
	}

	@Test
	public void access_rebaseOverriddenThisEvent() {
		Assert.fail();
	}

	@Test
	public void access_field() {

		IFieldName f = Names.newField("[p:int] [%s].E", t(1));

		FieldReference r = new FieldReference();
		r.setReference(varRef("o"));
		r.setFieldName(f);

		md1.body.add(exprStmt(SSTUtil.refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		Usage u = new Usage();
		u.methodCtx = m(1, 1);
		u.usageSites.add(UsageSites.fieldAccess(f));

		assertUsageSites(ctx(sst), r, u);
	}

	@Test
	public void access_method() {

		MethodReference r = new MethodReference();
		r.setReference(varRef("o"));
		r.setMethodName(m(2, 3));

		md1.body.add(exprStmt(SSTUtil.refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		Usage u = new Usage();
		u.methodCtx = m(1, 1);
		// method access is not stored right now

		assertUsageSites(ctx(sst), r, u);
	}

	@Test
	public void access_rebaseOverriddenThisMethod() {
		Assert.fail();
	}

	@Test
	public void access_property() {

		IPropertyName p = Names.newProperty("set get [p:int] [%s].P()", t(1));

		PropertyReference r = new PropertyReference();
		r.setReference(varRef("o"));
		r.setPropertyName(p);

		md1.body.add(exprStmt(SSTUtil.refExpr(r)));

		addUniqueAOs(r.getReference(), r);

		Usage u = new Usage();
		u.methodCtx = m(1, 1);
		u.usageSites.add(UsageSites.propertyAccess(p));

		assertUsageSites(ctx(sst), r, u);
	}

	@Test
	public void access_property_valueAutoParameter() {
		// assign method
		Assert.fail();
	}

	@Test
	public void access_rebaseOverriddenThisProperty() {
		Assert.fail();
	}

	@Test
	public void rnd_thisMemberAccessIsRewrittenWhenOverridden_field() {
		Assert.fail();
	}

	@Test
	public void rnd_thisMemberAccessIsRewrittenWhenOverridden_methodAccess() {
		Assert.fail();
	}

	@Test
	public void rnd_thisMemberAccessIsRewrittenWhenOverridden_methodCall() {
		Assert.fail();
	}

	@Test
	public void rnd_thisMemberAccessIsRewrittenWhenOverridden_property() {
		Assert.fail();
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