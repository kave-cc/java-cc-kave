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

import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.utils.ssts.SSTUtils.FUNC2;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.STRING;
import static cc.kave.commons.utils.ssts.SSTUtils.varDecl;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCatchParameter;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByMemberAccess;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByThis;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByUnknown;
import static org.junit.Assert.fail;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SST;
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
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class UsageExtractionDeclarationsTest extends UsageExtractionTestBase {

	@Test
	public void decl_sst() {
		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1);

		assertInit(ctx(sst), sst, t(1), definedByThis());
	}

	@Test
	public void decl_event() {
		// DelT E;
		IEventName en = Names.newEvent("[%s] [%s].E", FUNC2.getIdentifier(), t(1).getIdentifier());
		EventDeclaration ed = new EventDeclaration(en);

		MethodDeclaration md = new MethodDeclaration(m(1, 1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.events.add(ed);
		sst.methods.add(md);

		setToDifferentAOs(sst, ed, md);

		assertInit(ctx(sst), ed, FUNC2, definedByUnknown());
	}

	@Test
	public void decl_field() {
		// int _f
		IFieldName fn = newField("[p:string] [%s]._f", t(1).getIdentifier());
		FieldDeclaration fd = new FieldDeclaration(fn);

		MethodDeclaration md = new MethodDeclaration(m(1, 1));

		SST sst = new SST();
		sst.setEnclosingType(t(1));
		sst.fields.add(fd);
		sst.methods.add(md);

		setToDifferentAOs(sst, fd, md);

		assertInit(ctx(sst), fd, STRING, definedByMemberAccess(fn));
	}

	@Test
	public void decl_method() {
		// void m() {}
		IMethodName mn = newMethod("[p:int] [T, P].m([p:string] m)");
		MethodDeclaration md = new MethodDeclaration(mn);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md);

		setToDifferentAOs(sst, md, mn.getParameters().get(0));

		assertInit(ctx(sst), md, FUNC2, definedByUnknown());
	}

	@Test
	public void decl_property() {
		// int P { get; set; }
		IPropertyName pn = newProperty("set get [p:string] [%s].P()", t(1).getIdentifier());
		PropertyDeclaration pd = new PropertyDeclaration(pn);

		MethodDeclaration md = new MethodDeclaration(m(1, 1));

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.properties.add(pd);
		sst.methods.add(md);

		setToDifferentAOs(sst, pd, md);

		assertInit(ctx(sst), pd, STRING, definedByMemberAccess(pn));
	}

	@Test
	public void decl_lambda() {
		// o = () => {}
		fail();
	}

	@Test
	public void decl_methodParams() {
		// foreach(var o in foo) {}
		fail();
	}

	@Test
	public void decl_lambdaParam() {
		// foreach(var o in foo) {}
		fail();
	}

	@Test
	public void decl_catchParams() {

		IParameterName pn = Names.newParameter("[%s] p", INT.getIdentifier());

		CatchBlock cb = new CatchBlock();
		cb.setKind(CatchBlockKind.General);
		cb.setParameter(pn);

		TryBlock tb = new TryBlock();
		tb.catchBlocks.add(cb);

		MethodDeclaration md = new MethodDeclaration(m(1, 1));
		md.body.add(tb);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md);

		setToDifferentAOs(sst, md, pn);

		assertInit(ctx(sst), pn, INT, definedByCatchParameter(INT));
	}

	@Test
	public void decl_var() {
		IVariableDeclaration d1 = varDecl("o", t(2));

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(d1);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, d1.getReference());

		assertInit(ctx(sst), d1.getReference(), t(2), definedByUnknown());
	}

	@Test
	public void decl_varInDo() {
		IVariableDeclaration d1 = varDecl("o", t(2));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d1);

		DoLoop l = new DoLoop();
		l.setCondition(lhbe);

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(l);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, d1.getReference());

		assertInit(ctx(sst), d1.getReference(), t(2), definedByUnknown());
	}

	@Test
	public void decl_varInFor() {
		IVariableDeclaration d1 = varDecl("o1", t(21));
		IVariableDeclaration d2 = varDecl("o2", t(22));
		IVariableDeclaration d3 = varDecl("o3", t(23));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d2);

		ForLoop l = new ForLoop();
		l.getInit().add(d1);
		l.setCondition(lhbe);
		l.getStep().add(d3);

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(l);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, d1.getReference(), d2.getReference(), d3.getReference());

		assertInit(ctx(sst), d1.getReference(), t(21), definedByUnknown());
		assertInit(ctx(sst), d2.getReference(), t(22), definedByUnknown());
		assertInit(ctx(sst), d3.getReference(), t(23), definedByUnknown());
	}

	@Test
	public void decl_varInForeach() {
		IVariableDeclaration d1 = varDecl("o", t(2));

		ForEachLoop l = new ForEachLoop();
		l.setDeclaration(d1);

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(l);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, d1.getReference());

		assertInit(ctx(sst), d1.getReference(), t(2), definedByUnknown());
	}

	@Test
	public void decl_varInWhile() {
		IVariableDeclaration d = varDecl("o2", t(2));

		LoopHeaderBlockExpression lhbe = new LoopHeaderBlockExpression();
		lhbe.getBody().add(d);

		WhileLoop l = new WhileLoop();
		l.setCondition(lhbe);

		MethodDeclaration md1 = new MethodDeclaration(m(1, 1));
		md1.body.add(l);

		SST sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);

		setToDifferentAOs(sst, md1, d.getReference());

		assertInit(ctx(sst), d.getReference(), t(2), definedByUnknown());
	}

	@Test
	public void decl_varOutParameter() {
		// TODO extend DefinitionType enum and Definitions util
		fail();
	}
}