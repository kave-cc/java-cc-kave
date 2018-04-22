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
package cc.kave.caret.analyses;

import static cc.kave.commons.model.naming.Names.getUnknownType;
import static cc.kave.commons.model.naming.Names.newEvent;
import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.model.naming.impl.v0.NameUtils.toValueType;
import static cc.kave.commons.model.ssts.impl.SSTUtil.eventRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.indexAccessRef;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION;
import static cc.kave.commons.utils.ssts.SSTUtils.ACTION1;
import static cc.kave.commons.utils.ssts.SSTUtils.BOOL;
import static cc.kave.commons.utils.ssts.SSTUtils.CHAR;
import static cc.kave.commons.utils.ssts.SSTUtils.FUNC1;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.commons.utils.ssts.SSTUtils.OBJECT;
import static cc.kave.commons.utils.ssts.completioninfo.VariableScope.ErrorHandling.THROW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.IdentityHashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class TypeBasedPointsToAnalysisMatchingTest extends PathInsensitivePointToAnalysisTestBase {

	private IdentityHashMap<Object, ITypeName> map = new IdentityHashMap<>();

	@Before
	public void setup() {
		sut = new TypeBasedPointsToAnalysis(THROW);
		resetAOs();
	}

	@Test
	public void decl_sst() {
		VariableReference o = varRef("o");
		md1.body.add(varDecl(o, t(1)));

		addAO(t(1), sst, o);
		addAO(toValueType(md1.getName(), true), md1);
		assertAOs();
	}

	@Test
	public void decl_event() {

		IEventName en = newEvent("[%s] [%s].E", FUNC1.getIdentifier(), t(1).getIdentifier());

		EventDeclaration ed = new EventDeclaration();
		ed.setName(en);
		sst.events.add(ed);

		VariableReference o = varRef("o");
		md1.body.add(varDecl(o, FUNC1));

		VariableReference this1 = varRef("this");
		IEventReference er = eventRef(this1, en);
		md1.body.add(exprStmt(refExpr(er)));

		addAO(t(1), sst, this1);
		addAO(FUNC1, ed, o, er);
		addAO(toValueType(md1.getName(), true), md1);
		assertAOs();
	}

	@Test
	public void decl_field() {

		IFieldName fn = newField("[%s] [%s]._f", t(10).getIdentifier(), t(1).getIdentifier());

		FieldDeclaration fd = new FieldDeclaration();
		fd.setName(fn);
		sst.fields.add(fd);

		VariableReference o = varRef("o");
		md1.body.add(varDecl(o, t(10)));

		VariableReference this1 = varRef("this");
		IFieldReference fr = SSTUtil.fieldRef(this1, fn);
		md1.body.add(exprStmt(refExpr(fr)));

		addAO(t(1), sst, this1);
		addAO(t(10), fd, o, fr);
		addAO(toValueType(md1.getName(), true), md1);
		assertAOs();
	}

	@Test
	public void decl_method() {

		VariableReference o = varRef("o");
		VariableReference o2 = varRef("o2");

		md1.body.add(varDecl(o, toValueType(md1.getName(), true)));

		IMethodName mn = Names.newMethod("[p:int] [%s].m2([p:object] p)", t(1).getIdentifier());
		MethodDeclaration md2 = new MethodDeclaration();
		md2.setName(mn);
		md2.body.add(varDecl(o2, OBJECT));
		sst.methods.add(md2);

		VariableReference this1 = varRef("this");
		IMethodReference mr = SSTUtil.methodRef(this1, md1.getName());
		md1.body.add(exprStmt(refExpr(mr)));

		addAO(t(1), sst, this1);
		addAO(toValueType(md1.getName(), true), md1, o, mr);
		addAO(toValueType(md2.getName(), true), md2);
		addAO(OBJECT, mn.getParameters().get(0), o2);
		assertAOs();
	}

	@Test
	public void decl_property() {

		IVariableReference o1 = varRef("o1");
		IVariableReference o2 = varRef("o2");
		IVariableReference o3 = varRef("o3");
		IParameterName arg1;
		IParameterName value;

		IPropertyName pn1 = Names.newProperty("get set [p:int] [%s].P([p:int] arg1)", t(1).getIdentifier());
		arg1 = pn1.getParameters().get(0);
		value = pn1.getSetterValueParam();

		PropertyDeclaration pd1 = new PropertyDeclaration();
		pd1.setName(pn1);
		pd1.getGet().add(varDecl(o1, INT));
		pd1.getSet().add(varDecl(o2, INT));

		PropertyDeclaration pd2 = new PropertyDeclaration();
		pd2.setName(newProperty("get [p:int] [%s].P()", t(1).getIdentifier()));
		pd2.getGet().add(varDecl(o3, INT));

		VariableReference this1 = varRef("this");
		IPropertyReference pr = SSTUtil.propertyRef(this1, pd1.getName());
		pd1.getGet().add(exprStmt(refExpr(pr)));

		sst = new SST();
		sst.enclosingType = t(1);
		sst.properties.add(pd1);
		sst.properties.add(pd2);

		addAO(t(1), sst, this1);
		addAO(INT, pd1, pd2, o1, o2, o3, arg1, value, pr);
		assertAOs();
	}

	// ########################################################################

	@Test
	public void block_try() {
		IParameterName e1 = Names.newParameter("[E1, P] e");
		IParameterName e2 = Names.newParameter("[E2, P] e");
		IParameterName e3 = Names.newParameter("[E3, P] ?");
		IParameterName e4; // "[?] ???"
		VariableReference o1 = varRef("o");
		VariableReference o2 = varRef("o");
		VariableReference o3 = varRef("o");
		VariableReference o4 = varRef("o");

		TryBlock tb = new TryBlock();
		tb.getBody().add(varDecl(o1, BOOL));

		CatchBlock cb1 = new CatchBlock();
		cb1.setParameter(e1);
		cb1.getBody().add(varDecl(o2, INT));

		CatchBlock cb2 = new CatchBlock();
		cb2.setParameter(e2);
		cb2.getBody().add(varDecl(o3, OBJECT));

		CatchBlock cb3 = new CatchBlock();
		cb3.setKind(CatchBlockKind.Unnamed);
		cb3.setParameter(e3);

		CatchBlock cb4 = new CatchBlock();
		cb3.setKind(CatchBlockKind.General);
		e4 = cb4.getParameter();

		tb._finally.add(varDecl(o4, CHAR));

		tb.catchBlocks.add(cb1);
		tb.catchBlocks.add(cb2);
		tb.catchBlocks.add(cb3);
		tb.catchBlocks.add(cb4);
		md1.body.add(tb);

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(BOOL, o1);
		addAO(INT, o2);
		addAO(OBJECT, o3);
		addAO(CHAR, o4);
		addAO(newType("E1, P"), e1);
		addAO(newType("E2, P"), e2);
		addAO(newType("E3, P"), e3);
		addAO(getUnknownType(), e4);
		assertAOs();
	}

	@Test
	public void stmt_varDecl() {
		VariableReference o1 = varRef("o1");
		VariableReference o2 = varRef("o2");
		VariableReference o3 = varRef("o3");
		md1.body.add(varDecl(o1, INT));
		md1.body.add(varDecl(o2, INT));
		md1.body.add(varDecl(o3, CHAR));

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(INT, o1, o2);
		addAO(CHAR, o3);
		assertAOs();
	}

	@Test
	public void stmt_throw() {
		IParameterName p1 = Names.newParameter("[E1, P] e");
		IParameterName p2 = Names.newParameter("[E2, P] e");
		IParameterName p3 = Names.newParameter("[E3, P] ?");
		IParameterName p4 = new ParameterName();

		IVariableReference e1 = varRef("e");
		IVariableReference e2;
		IVariableReference e3;
		IVariableReference e4;

		ThrowStatement t1 = new ThrowStatement();
		t1.setReference(e1);
		// re-throws
		ThrowStatement t2 = new ThrowStatement();
		e2 = t2.getReference();
		ThrowStatement t3 = new ThrowStatement();
		e3 = t3.getReference();
		ThrowStatement t4 = new ThrowStatement();
		e4 = t4.getReference();

		CatchBlock cb1 = new CatchBlock();
		cb1.setKind(CatchBlockKind.Default);
		cb1.setParameter(p1);
		cb1.getBody().add(t1);

		CatchBlock cb2 = new CatchBlock();
		cb2.setKind(CatchBlockKind.Default);
		cb2.setParameter(p2);
		cb2.getBody().add(t2);

		CatchBlock cb3 = new CatchBlock();
		cb3.setKind(CatchBlockKind.Unnamed);
		cb3.setParameter(p3);
		cb3.getBody().add(t3);

		CatchBlock cb4 = new CatchBlock();
		cb4.setKind(CatchBlockKind.General);
		cb4.setParameter(p4);
		cb4.getBody().add(t4);

		TryBlock tb = new TryBlock();
		tb.catchBlocks.add(cb1);
		tb.catchBlocks.add(cb2);
		tb.catchBlocks.add(cb3);
		tb.catchBlocks.add(cb4);
		md1.body.add(tb);

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(newType("E1, P"), p1, e1);
		addAO(newType("E2, P"), p2, e2); // infer from catch
		addAO(newType("E3, P"), p3, e3); // infer from catch
		addAO(Names.getUnknownType(), p4, e4); // impossible to infer
		assertAOs();
	}

	@Test
	public void expr_lambda() {
		VariableReference o1 = varRef("o1");
		md1.body.add(varDecl(o1, ACTION1));

		ILambdaName ln = Names.newLambda("[p:void] ([p:int] i)");
		IParameterName i = ln.getParameters().get(0);
		LambdaExpression le = new LambdaExpression();
		le.setName(ln);
		md1.body.add(exprStmt(le));

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(newType("d:[p:void] [System.Action`1[[T -> p:int]], mscorlib, 4.0.0.0].([T] obj)"), o1, le);
		addAO(INT, i);
		assertAOs();
	}

	@Test
	public void ref_indexAccess_arr() {

		IVariableReference a1 = varRef("a");
		IVariableReference a2 = varRef("a");

		md1.body.add(varDecl(a1, newType("p:int[,]")));

		IndexAccessReference ref = indexAccessRef(a2, new ConstantValueExpression());
		md1.body.add(exprStmt(refExpr(ref)));

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(newType("p:int[,]"), a1, a2);
		addAO(newType("p:int[]"), ref);
		assertAOs();
	}

	@Test
	public void ref_indexAccess_indexer() {

		IVariableReference a1 = varRef("a");
		IVariableReference a2 = varRef("a");

		md1.body.add(varDecl(a1, newType("IndexerType, P")));

		IndexAccessReference ref = indexAccessRef(a2, new ConstantValueExpression());
		md1.body.add(exprStmt(refExpr(ref)));

		addAO(t(1), sst);
		addAO(ACTION, md1);
		addAO(newType("IndexerType, P"), a1, a2);
		addAO(Names.getUnknownType(), ref);
		assertAOs();
	}

	@Ignore
	@Test
	public void expr_indexAccess() {
		// cannot be expresses right now, because expressions cannot be used as AO
		// queries. Fix as soon as IdxAccExpr/Ref is polished.
		fail();
	}

	// ########################################################################

	public void addAO(ITypeName ao, Object... keys) {
		for (Object key : keys) {
			map.put(key, ao);
		}
		super.addAO(keys);
	}

	@Override
	protected PathInsensitivePointsToInfo assertAOs() {
		PathInsensitivePointsToInfo p2info = super.assertAOs();
		assertEquals(map.keySet(), p2info.getKeys());
		for (Object key : map.keySet()) {
			assertTrue(p2info.hasKey(key));
			Object ao = p2info.getAbstractObject(key);
			assertEquals(ao, p2info.getAbstractObject(key));
		}
		return p2info;
	}
}
