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

import static cc.kave.commons.model.naming.Names.newLambda;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.PropertyHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.utils.naming.TypeErasure;
import cc.kave.rsse.calls.model.usages.IUsage;

public class UsageExtractionContextsTest extends UsageExtractionTestBase {

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
	public void cCtx() {
		IVariableReference o = varRef("o");

		VariableDeclaration d = new VariableDeclaration();
		d.setReference(o);
		md1.body.add(d);

		addUniqueAOs(o);
		IUsage actual = assertOneUsage(ctx(sst), o);
		assertEquals(t(1), actual.getClassContext());
	}

	@Test
	public void mCtx_Method() {
		IVariableReference o = varRef("o");

		VariableDeclaration d = new VariableDeclaration();
		d.setReference(o);
		md1.body.add(d);

		addUniqueAOs(o);
		IUsage actual = assertOneUsage(ctx(sst), o);
		assertEquals(m(1, 1), actual.getMethodContext());
	}

	@Test
	public void mCtx_PropertyGet() {

		VariableDeclaration d = varDecl("o", INT);

		IPropertyName pn = newProperty("get [p:int] [%s].P()", t(1).getIdentifier());
		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(pn);
		pd.getGet().add(d);

		sst.properties.add(pd);

		addUniqueAOs(pd, d.getReference());
		IUsage actual = assertOneUsage(ctx(sst), d.getReference());
		assertEquals(pn.getExplicitGetterName(), actual.getMethodContext());
	}

	@Test
	public void mCtx_PropertySet() {

		VariableDeclaration d = varDecl("o", INT);

		IPropertyName pn = newProperty("set [p:int] [%s].P()", t(1).getIdentifier());
		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(pn);
		pd.getSet().add(d);

		sst.properties.add(pd);

		addUniqueAOs(pd, d.getReference(), pn.getExplicitSetterName().getParameters().get(0));
		IUsage actual = assertOneUsage(ctx(sst), d.getReference());
		assertEquals(pn.getExplicitSetterName(), actual.getMethodContext());
	}

	@Test
	public void mCtx_Lambda() {

		LambdaExpression lambdaExpr = new LambdaExpression();
		ILambdaName ln = newLambda("[p:int] ()");
		lambdaExpr.setName(ln);
		md1.getBody().add(exprStmt(lambdaExpr));

		IVariableReference o = varRef("o");
		lambdaExpr.getBody().add(varDecl(o, INT));

		addUniqueAOs(o, lambdaExpr);
		IUsage actual = assertOneUsage(ctx(sst), o);
		assertEquals(TypeErasure.of(ln.getExplicitMethodName()), actual.getMethodContext());
	}

	@Test
	public void sameAoInDiffContextsIsDiffUsage() {

		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(p(1, 2));
		sst.properties.add(pd);

		LambdaExpression outerLambdaExpr = new LambdaExpression();
		ILambdaName outerLambdaName = newLambda("[p:void] ()");
		outerLambdaExpr.setName(outerLambdaName);
		md1.body.add(SSTUtil.exprStmt(outerLambdaExpr));

		LambdaExpression innerLambdaExpr = new LambdaExpression();
		ILambdaName innerLambdaName = newLambda("[p:int] ()");
		innerLambdaExpr.setName(innerLambdaName);
		outerLambdaExpr.getBody().add(exprStmt(innerLambdaExpr));

		// ----

		IVariableReference o1 = varRef("o1");
		md1.body.add(varDecl(o1, INT));

		IVariableReference o2 = varRef("o2");
		pd.getGet().add(varDecl(o2, INT));

		IVariableReference o3 = varRef("o3");
		pd.getSet().add(varDecl(o3, INT));

		IVariableReference o4 = varRef("o4");
		outerLambdaExpr.getBody().add(varDecl(o4, INT));

		IVariableReference o5 = varRef("o5");
		innerLambdaExpr.getBody().add(varDecl(o5, INT));

		// ----

		addAO(o1, o2, o3, o4, o5);
		addUniqueAOs(pd, pd.getName().getSetterValueParam(), outerLambdaExpr, innerLambdaExpr);
		Object ao = p2info.getAbstractObject(o1);

		Map<Object, List<IUsage>> map = sut.extractMap(ctx(sst));
		List<IUsage> usages = map.get(ao);
		List<IMethodName> actuals = usages.stream().map(u -> u.getMethodContext()).collect(Collectors.toList());
		List<IMethodName> expecteds = new LinkedList<>(asList( //
				newMethod("[p:int] [%s].P2__get__()", t(1).getIdentifier()), //
				newMethod("[p:void] [%s].P2__set__([p:int] value)", t(1).getIdentifier()),
				newMethod("[p:void] [%s].m1()", t(1).getIdentifier()), //
				TypeErasure.of(outerLambdaName.getExplicitMethodName()), //
				TypeErasure.of(innerLambdaName.getExplicitMethodName())));
		assertEquals(expecteds, actuals);

	}

	@Test
	public void rebase_mCtx_method() {
		IVariableReference o = varRef("o");

		VariableDeclaration d = new VariableDeclaration();
		d.setReference(o);
		md1.body.add(d);

		Context ctx = new Context();
		ctx.getTypeShape().setTypeHierarchy(new TypeHierarchy(t(1)));
		ctx.setSST(sst);
		ctx.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(m(1, 1)).setSuper(m(2, 1)).setFirst(m(3, 1)));

		addUniqueAOs(o);
		IUsage actual = assertOneUsage(ctx, o);
		assertEquals(m(3, 1), actual.getMethodContext());
	}

	@Test
	public void rebase_mCtx_propertyGet() {
		IPropertyName pn1 = p(1, 1);
		IPropertyName pn2 = p(2, 1);
		IPropertyName pn3 = p(3, 1);

		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(pn1);
		sst.properties.add(pd);

		IVariableReference o = varRef("o");

		VariableDeclaration d = new VariableDeclaration();
		d.setReference(o);
		pd.getGet().add(d);

		Context ctx = new Context();
		ctx.getTypeShape().setTypeHierarchy(new TypeHierarchy(t(1)));
		ctx.setSST(sst);
		ctx.getTypeShape().getPropertyHierarchies().add(new PropertyHierarchy(pn1).setSuper(pn2).setFirst(pn3));

		addAO(pn1.getSetterValueParam(), pn2.getSetterValueParam(), pn3.getSetterValueParam());
		addUniqueAOs(o, pd);
		IUsage actual = assertOneUsage(ctx, o);
		assertEquals(pn3.getExplicitGetterName(), actual.getMethodContext());
	}

	private IPropertyName p(int i, int j) {
		return Names.newProperty("get set [p:int] [%s].P%d()", t(1).getIdentifier(), j);
	}

	@Test
	public void rebase_mCtx_propertySet() {
		IPropertyName pn1 = p(1, 1);
		IPropertyName pn2 = p(2, 1);
		IPropertyName pn3 = p(3, 1);

		PropertyDeclaration pd = new PropertyDeclaration();
		pd.setName(pn1);
		sst.properties.add(pd);

		IVariableReference o = varRef("o");

		VariableDeclaration d = new VariableDeclaration();
		d.setReference(o);
		pd.getSet().add(d);

		Context ctx = new Context();
		ctx.getTypeShape().setTypeHierarchy(new TypeHierarchy(t(1)));
		ctx.setSST(sst);
		ctx.getTypeShape().getPropertyHierarchies().add(new PropertyHierarchy(pn1).setSuper(pn2).setFirst(pn3));

		addAO(pn1.getSetterValueParam(), pn2.getSetterValueParam(), pn3.getSetterValueParam());
		addUniqueAOs(o, pd);
		IUsage actual = assertOneUsage(ctx, o);
		assertEquals(pn3.getExplicitSetterName(), actual.getMethodContext());
	}
}