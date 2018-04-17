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
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;

public class UsageExtractionUsageSitesTest extends UsageExtractionTestBase {

	@Test
	public void access_calls() {

		IVariableDeclaration varDecl = varDecl("o", t(2));
		IInvocationExpression inv1 = invExpr("o", m(2, 1));

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(varDecl);
		md1.body.add(exprStmt(inv1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		Context ctx = ctx(sst);

		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)));

		setToSameAO(varDecl.getReference(), inv1.getReference());
		setToDifferentAOs(sst, md1);

		Usage u = new Usage();
		u.type = t(2);
		u.definition = definedByUnknown();
		u.classCtx = t(1);
		u.methodCtx = m(1, 1);
		u.usageSites.add(UsageSites.call(m(2, 1)));

		assertUsages(ctx, u);
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

		setToSameAO(varDecl1.getReference(), ((IReferenceExpression) inv1.getParameters().get(0)).getReference());
		setToSameAO(varDecl2.getReference(), inv1.getReference());
		setToDifferentAOs(sst, md1);

		Usage expected = new Usage();
		expected.type = t(3);
		expected.definition = definedByUnknown();
		expected.classCtx = t(1);
		expected.methodCtx = m(1, 1);
		expected.usageSites.add(callParameter(m(2, 1), 1));

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

		setToSameAO(sst, inv1.getReference());
		setToDifferentAOs(md1);

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

		setToSameAO(varDecl1.getReference(), ((IReferenceExpression) inv1.getParameters().get(0)).getReference());
		setToSameAO(sst, inv1.getReference());
		setToDifferentAOs(md1);

		Usage expected = new Usage();
		expected.type = t(3);
		expected.definition = definedByUnknown();
		expected.classCtx = t(1);
		expected.methodCtx = m(1, 1);
		expected.usageSites.add(callParameter(m(2, 2), 1));

		assertUsage(ctx, md1.getName(), varDecl1.getReference(), expected);
	}

	@Test
	public void access_event() {

		EventReference er = new EventReference();
		er.setReference(varRef("this"));
		er.setEventName(newEvent("[%s] [%s].E", FUNC2, t(1)));

		IReferenceExpression refExpr = SSTUtil.refExpr(er);

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(exprStmt(refExpr));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, er.getReference(), er);

		Usage u = new Usage();
		u.type = t(2);
		u.definition = definedByMemberAccess(er.getEventName());
		u.classCtx = t(1);
		u.methodCtx = m(1, 1);
		u.usageSites.add(call(m(2, 1)));

		assertUsage(ctx(sst), md1.getName(), er, u);
	}

	@Test
	public void access_rebaseOverriddenThisEvent() {
		Assert.fail();
	}

	@Test
	public void access_field() {
		Assert.fail();
	}

	@Test
	public void access_method() {
		// assign method
		Assert.fail();
	}

	@Test
	public void access_rebaseOverriddenThisMethod() {
		Assert.fail();
	}

	@Test
	public void access_property() {
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
}