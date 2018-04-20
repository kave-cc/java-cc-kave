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
import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newLambda;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION;
import static cc.kave.commons.utils.ssts.SSTUtils.BOOL;
import static cc.kave.commons.utils.ssts.SSTUtils.CHAR;
import static cc.kave.commons.utils.ssts.SSTUtils.FUNC2;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.STRING;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLambdaDecl;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLambdaParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByLoopHeader;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMethodParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.IUsage;

public class UsageExtractionDeclarationsTest extends UsageExtractionTestBase {

	private SST sst;
	private MethodDeclaration md1;

	@Before
	public void setup() {
		md1 = new MethodDeclaration(newMethod("[p:bool] [%s].m([p:int] p)", t(1).getIdentifier()));
		sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);
		addUniqueAOs(sst, md1, md1.getName().getParameters().get(0));
	}

	@Test
	public void decl_sst() { // new Class { ... }
		assertInit(ctx(sst), sst, t(1), definedByThis());
	}

	@Test
	public void decl_member_event() { // public event Func<RT, PT> E;
		IEventName en = newEvent("[%s] [%s].E", FUNC2.getIdentifier(), t(1).getIdentifier());
		EventDeclaration ed = new EventDeclaration(en);

		sst.events.add(ed);
		addUniqueAOs(ed);

		assertInit(ctx(sst), ed, FUNC2, definedByMemberAccess(en));
	}

	@Test
	public void decl_member_field() { // public int _f;
		IFieldName fn = newField("[p:string] [%s]._f", t(1).getIdentifier());
		FieldDeclaration fd = new FieldDeclaration(fn);

		sst.fields.add(fd);
		addUniqueAOs(fd);

		assertInit(ctx(sst), fd, STRING, definedByMemberAccess(fn));
	}

	@Test
	public void decl_member_method() { // public bool m(int p) {}
		assertInit(ctx(sst), md1, FUNC2, definedByMemberAccess(md1.getName()));
	}

	@Test
	public void decl_member_property() { // public int P { get; set; }
		IPropertyName pn = newProperty("set get [p:string] [%s].P()", t(1).getIdentifier());
		PropertyDeclaration pd = new PropertyDeclaration(pn);
		IParameterName value = pd.getName().getExplicitSetterName().getParameters().get(0);

		sst.methods.remove(sst.methods.iterator().next());
		sst.properties.add(pd);

		addUniqueAOs(pd, value);
		assertInit(ctx(sst), pd, STRING, definedByMemberAccess(pn));
	}

	@Test
	public void decl_lambda() {

		LambdaExpression lambdaExpr = new LambdaExpression();
		ILambdaName ln = newLambda("[p:void] ()");
		lambdaExpr.setName(ln);
		md1.getBody().add(exprStmt(lambdaExpr));

		addUniqueAOs(lambdaExpr);
		assertInit(ctx(sst), lambdaExpr, ACTION, definedByLambdaDecl());
	}

	@Test
	public void decl_param_method() { // public bool m(int p) {}
		IParameterName p = md1.getName().getParameters().get(0);
		assertInit(ctx(sst), p, INT, definedByMethodParameter(md1.getName(), 0));
	}

	@Test
	public void decl_param_lambda() {

		IVariableReference pRef = SSTUtil.varRef("p");
		LambdaExpression expr = new LambdaExpression();
		expr.setName(Names.newLambda("[p:int] ([p:bool] p)"));
		expr.getBody().add(exprStmt(refExpr(pRef)));

		md1.body.add(exprStmt(expr));

		IParameterName pn = expr.getName().getParameters().get(0);
		addAO(pn, pRef);
		addUniqueAOs(expr);

		assertInit(ctx(sst), pn, BOOL, definedByLambdaParameter());
	}

	@Test
	public void decl_param_lambdaInLambda() {

		ILambdaName lnOuter = Names.newLambda("[p:int] ([p:bool] p1)");
		ILambdaName lnInner = Names.newLambda("[p:int] ([p:char] p2)");

		IVariableReference p1Ref = SSTUtil.varRef("p1");
		IVariableReference p2Ref = SSTUtil.varRef("p2");

		LambdaExpression outer = new LambdaExpression();
		outer.setName(lnOuter);
		md1.body.add(exprStmt(outer));

		LambdaExpression inner = new LambdaExpression();
		inner.setName(lnInner);
		inner.getBody().add(exprStmt(refExpr(p1Ref)));
		outer.getBody().add(exprStmt(inner));

		IParameterName p1 = outer.getName().getParameters().get(0);
		IParameterName p2 = inner.getName().getParameters().get(0);
		addUniqueAOs(outer, inner);
		addAO(p1, p1Ref);
		addAO(p2, p2Ref);

		assertInit(ctx(sst), p1Ref, BOOL, definedByLambdaParameter());
		assertInit(ctx(sst), p2Ref, CHAR, definedByLambdaParameter());
	}

	@Test
	public void decl_param_propertySet() {
		PropertyDeclaration pd = new PropertyDeclaration(
				newProperty("set [p:string] [%s].P([p:bool] p, [p:char] q)", t(1).getIdentifier()));
		sst.properties.add(pd);

		IMethodName setter = pd.getName().getExplicitSetterName();
		IParameterName p = setter.getParameters().get(0);
		IParameterName q = setter.getParameters().get(1);
		IParameterName value = setter.getParameters().get(2);

		addUniqueAOs(pd, p, q, value);
		assertInit(ctx(sst), p, BOOL, definedByMethodParameter(setter, 0));
		assertInit(ctx(sst), q, CHAR, definedByMethodParameter(setter, 1));
		assertInit(ctx(sst), value, STRING, definedByMethodParameter(setter, 2));
	}

	@Test
	public void decl_param_propertyGet() {
		PropertyDeclaration pd = new PropertyDeclaration(
				newProperty("get [p:string] [%s].P([p:bool] p, [p:char] q)", t(1).getIdentifier()));
		sst.properties.add(pd);

		IMethodName getter = pd.getName().getExplicitGetterName();
		IParameterName p = getter.getParameters().get(0);
		IParameterName q = getter.getParameters().get(1);

		addUniqueAOs(pd, p, q);
		assertInit(ctx(sst), p, BOOL, definedByMethodParameter(getter, 0));
		assertInit(ctx(sst), q, CHAR, definedByMethodParameter(getter, 1));
	}

	@Test
	public void decl_param_catch() {

		IParameterName pn = Names.newParameter("[%s] p", INT.getIdentifier());

		CatchBlock cb = new CatchBlock();
		cb.setKind(CatchBlockKind.General);
		cb.setParameter(pn);

		TryBlock tb = new TryBlock();
		tb.catchBlocks.add(cb);

		md1.body.add(tb);

		addUniqueAOs(pn);
		assertInit(ctx(sst), pn, INT, definedByCatchParameter());
	}

	@Test
	public void decl_body_var() {
		// Several definition types exist for assignments, these are tested separately.
		// We focus here on asserting that the corresponding visitor is used at all.
		IVariableDeclaration d1 = varDecl("o", t(2));
		IAssignment assign = assign(varRef("o"), new ConstantValueExpression());

		md1.body.add(d1);
		md1.body.add(assign);

		addAO(d1.getReference(), assign.getReference());
		assertInit(ctx(sst), d1.getReference(), t(2), definedByConstant());
	}

	@Test
	@Ignore // for now, we are excluding the loopHeader defs from the analysis
	public void decl_body_varInDo() {
		IVariableDeclaration d1 = varDecl("o", t(2));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d1);

		DoLoop l = new DoLoop();
		l.setCondition(lhbe);

		md1.body.add(l);

		addUniqueAOs(d1.getReference());
		assertInit(ctx(sst), d1.getReference(), t(2), definedByLoopHeader());
	}

	@Test
	@Ignore // for now, we are excluding the loopHeader defs from the analysis
	public void decl_body_varInFor() {
		IVariableDeclaration d1 = varDecl("o1", t(21));
		IVariableDeclaration d2 = varDecl("o2", t(22));
		IVariableDeclaration d3 = varDecl("o3", t(23));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d2);

		ForLoop l = new ForLoop();
		l.getInit().add(d1);
		l.setCondition(lhbe);
		l.getStep().add(d3);

		md1.body.add(l);

		addUniqueAOs(d1.getReference(), d2.getReference(), d3.getReference());
		assertInit(ctx(sst), d1.getReference(), t(21), definedByLoopHeader());
		assertInit(ctx(sst), d2.getReference(), t(22), definedByLoopHeader());
		assertInit(ctx(sst), d3.getReference(), t(23), definedByLoopHeader());
	}

	@Test
	public void decl_body_varInForeach() {
		IVariableDeclaration d1 = varDecl("o", t(2));

		ForEachLoop l = new ForEachLoop();
		l.setDeclaration(d1);

		md1.body.add(l);

		addUniqueAOs(d1.getReference());
		assertInit(ctx(sst), d1.getReference(), t(2), definedByUnknown());
	}

	@Test
	@Ignore // for now, we are excluding the loopHeader defs from the analysis
	public void decl_body_varInWhile() {
		IVariableDeclaration d = varDecl("o2", t(2));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d);

		WhileLoop l = new WhileLoop();
		l.setCondition(lhbe);

		md1.body.add(l);

		addUniqueAOs(d.getReference());
		assertInit(ctx(sst), d.getReference(), t(2), definedByLoopHeader());
	}

	private void assertInit(Context ctx, Object key, ITypeName expectedType, IDefinition expectedDefSite) {
		Object ao = p2info.getAbstractObject(key);
		Map<Object, List<IUsage>> map = sut.extractMap(ctx);
		if (!map.containsKey(ao)) {
			throw new AssertionError("Extracted map does not contain an IUsage for key: " + ao);
		}
		List<IUsage> usages = map.get(ao);
		assertFalse(usages.isEmpty());
		IUsage actual = usages.get(0);
		assertEquals(expectedType, actual.getType());
		assertEquals(expectedDefSite, actual.getDefinition());
	}
}