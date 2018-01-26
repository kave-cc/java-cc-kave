/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.ssts.impl.visitor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.typeshapes.IMemberHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class TypeErasureVisitorTest {

	@Test
	public void defaultWorks() {

		SST in = new SST();
		SST expected = new SST();

		ISST actual = TypeErasureVisitor.erase(in);
		assertEquals(expected, actual);
	}

	@Test
	public void enclosingTypeIsErased() {

		SST in = new SST();
		in.setEnclosingType(Names.newType("T`1[[G->T,P]], P"));
		SST expected = new SST();
		expected.setEnclosingType(Names.newType("T`1[[G]], P"));

		ISST actual = TypeErasureVisitor.erase(in);
		assertEquals(expected, actual);
	}

	@Test
	public void allDeclsAreCopied() {

		IDelegateDeclaration dd = mock(IDelegateDeclaration.class);
		IEventDeclaration ed = mock(IEventDeclaration.class);
		IFieldDeclaration fd = mock(IFieldDeclaration.class);
		IPropertyDeclaration pd = mock(IPropertyDeclaration.class);

		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[T,P] [T,P].M()"));

		SST in = new SST();
		in.getDelegates().add(dd);
		in.getEvents().add(ed);
		in.getFields().add(fd);
		in.getMethods().add(md);
		in.getProperties().add(pd);

		SST expected = new SST();
		expected.getDelegates().add(dd);
		expected.getEvents().add(ed);
		expected.getFields().add(fd);
		expected.getMethods().add(md);
		expected.getProperties().add(pd);

		ISST actual = TypeErasureVisitor.erase(in);
		assertEquals(expected, actual);
	}

	@Test
	public void methodNamesAreErased() {
		MethodDeclaration mdIn = new MethodDeclaration();
		mdIn.setName(Names.newMethod("[T,P] [T,P].M`1[[G1->T,P]]()"));
		mdIn.setEntryPoint(true);
		SST in = new SST();
		in.getMethods().add(mdIn);

		MethodDeclaration mdOut = new MethodDeclaration();
		mdOut.setName(Names.newMethod("[T,P] [T,P].M`1[[G1]]()"));
		mdOut.setEntryPoint(true);
		SST expected = new SST();
		expected.getMethods().add(mdOut);

		ISST actual = TypeErasureVisitor.erase(in);
		assertEquals(expected, actual);
	}

	@Test
	public void bodiesAreJustCopied() {
		IStatement stmt = new ContinueStatement();

		MethodDeclaration mdIn = new MethodDeclaration();
		mdIn.getBody().add(stmt);
		SST in = new SST();
		in.getMethods().add(mdIn);

		MethodDeclaration mdOut = new MethodDeclaration();
		mdOut.getBody().add(stmt);
		SST expected = new SST();
		expected.getMethods().add(mdOut);

		ISST actual = TypeErasureVisitor.erase(in);
		assertEquals(expected, actual);
	}

	@Test
	public void typeHierarchyErasure() {

		TypeHierarchy ext = new TypeHierarchy();
		ext.setElement(Names.newType("E`1[[G1->T,P]],P"));

		TypeHierarchy impl = new TypeHierarchy();
		impl.setElement(Names.newType("I`1[[G1->T,P]],P"));

		TypeHierarchy in = new TypeHierarchy();
		in.setElement(Names.newType("T`1[[G1->T,P]],P"));
		in.setExtends(ext);
		in.getImplements().add(impl);

		TypeHierarchy in2 = new TypeHierarchy();
		in2.setExtends(in);

		/* */

		TypeHierarchy outExt = new TypeHierarchy();
		outExt.setElement(Names.newType("E`1[[G1]],P"));

		TypeHierarchy outImpl = new TypeHierarchy();
		outImpl.setElement(Names.newType("I`1[[G1]],P"));

		TypeHierarchy out = new TypeHierarchy();
		out.setElement(Names.newType("T`1[[G1]],P"));
		out.setExtends(outExt);
		out.getImplements().add(outImpl);

		TypeHierarchy out2 = new TypeHierarchy();
		out2.setExtends(out);

		/* */

		ITypeHierarchy actual = TypeErasureVisitor.erase(in2);
		assertEquals(out2, actual);
	}

	@Test
	public void methodHierarchyErasure() {
		MethodHierarchy in = new MethodHierarchy();
		in.setElement(Names.newMethod("[T,P] [T,P].E`1[[G1->T,P]]()"));
		in.setSuper(Names.newMethod("[T,P] [T,P].S`1[[G1->T,P]]()"));
		in.setFirst(Names.newMethod("[T,P] [T,P].F`1[[G1->T,P]]()"));

		MethodHierarchy out = new MethodHierarchy();
		out.setElement(Names.newMethod("[T,P] [T,P].E`1[[G1]]()"));
		out.setSuper(Names.newMethod("[T,P] [T,P].S`1[[G1]]()"));
		out.setFirst(Names.newMethod("[T,P] [T,P].F`1[[G1]]()"));

		IMemberHierarchy<IMethodName> actual = TypeErasureVisitor.erase(in);
		assertEquals(out, actual);
	}

	@Test
	public void integrationInContext() {
		SST sstIn = new SST();
		sstIn.setEnclosingType(Names.newType("T`1[[G->T,P]],P"));

		TypeHierarchy thIn = new TypeHierarchy();
		thIn.setElement(Names.newType("T`1[[G1->T,P]],P"));
		MethodHierarchy mhIn = new MethodHierarchy();
		mhIn.setElement(Names.newMethod("[T,P] [T,P].M`1[[G1->T,P]]()"));
		TypeShape tsIn = new TypeShape();
		tsIn.setTypeHierarchy(thIn);
		tsIn.getMethodHierarchies().add(mhIn);

		Context ctxIn = new Context();
		ctxIn.setSST(sstIn);
		ctxIn.setTypeShape(tsIn);

		SST sstOut = new SST();
		sstOut.setEnclosingType(Names.newType("T`1[[G]],P"));

		TypeHierarchy thOut = new TypeHierarchy();
		thOut.setElement(Names.newType("T`1[[G1]],P"));
		MethodHierarchy mhOut = new MethodHierarchy();
		mhOut.setElement(Names.newMethod("[T,P] [T,P].M`1[[G1]]()"));
		TypeShape tsOut = new TypeShape();
		tsOut.setTypeHierarchy(thOut);
		tsOut.getMethodHierarchies().add(mhOut);

		Context ctxOut = new Context();
		ctxOut.setSST(sstOut);
		ctxOut.setTypeShape(tsOut);

		Context actual = TypeErasureVisitor.erase(ctxIn);
		assertEquals(ctxOut, actual);
	}
}