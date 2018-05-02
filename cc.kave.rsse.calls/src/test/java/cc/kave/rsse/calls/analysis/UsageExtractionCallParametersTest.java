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

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.utils.ssts.SSTUtils.exprStmt;
import static cc.kave.commons.utils.ssts.SSTUtils.invExpr;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;

public class UsageExtractionCallParametersTest extends UsageExtractionTestBase {

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
	public void access_methodCallParam() {

		IMethodName m = newMethod("[p:void] [p:object].m([p:int] arg)");
		IInvocationExpression inv1 = invExpr("o", m, "p");

		md1.body.add(exprStmt(inv1));

		IVariableReference o = inv1.getReference();
		IReference arg = ((IReferenceExpression) inv1.getParameters().get(0)).getReference();
		addUniqueAOs(o, arg);

		IUsage actual = assertOneUsage(ctx(sst), arg);
		assertEquals(newHashSet(new CallParameter(m, 0)), actual.getCallParameters());
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
		assertEquals(newHashSet(new CallParameter(s, 0)), actual.getCallParameters());
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
		assertEquals(newHashSet(new CallParameter(f, 0)), actual.getCallParameters());
	}
}