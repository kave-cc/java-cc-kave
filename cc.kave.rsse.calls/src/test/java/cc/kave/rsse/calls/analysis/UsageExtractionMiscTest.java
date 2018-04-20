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
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByOutParameter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.rsse.calls.model.usages.IUsage;

public class UsageExtractionMiscTest extends UsageExtractionTestBase {

	private MethodDeclaration md1;
	private SST sst;

	@Before
	public void asd() {
		md1 = new MethodDeclaration(newMethod("[p:bool] [%s].m()", t(1).getIdentifier()));
		sst = new SST();
		sst.enclosingType = t(1);
		sst.methods.add(md1);
		addUniqueAOs(sst, md1);
	}

	@Test
	public void rnd_isQueryFlagIsSet() {

		CompletionExpression ce = new CompletionExpression();
		ce.setObjectReference(varRef("o"));
		md1.getBody().add(exprStmt(ce));

		addUniqueAOs(ce.getVariableReference());

		IUsage actual = assertOneUsage(ctx(sst), ce.getVariableReference());
		assertTrue(actual.isQuery());
	}

	@Test
	public void rnd_isQueryDoesNotThrowForTypeReferences() {

		CompletionExpression ce = new CompletionExpression();
		ce.setTypeReference(t(2));
		md1.getBody().add(exprStmt(ce));

		sut.extractMap(ctx(sst));
	}

	@Test
	@Ignore
	public void rnd_defSiteOrderIsPreserved() {
		Assert.fail();
	}

	@Test
	@Ignore
	public void rnd_nonMapExtractionWorksToo() {
		Assert.fail();
		sut.extract(ctx(null));
	}

	@Test
	public void decl_body_outParameter() {

		VariableDeclaration decl = new VariableDeclaration();
		decl.setReference(varRef("o"));
		decl.setType(t(2));

		IMethodName m = Names.newMethod("[p:void] [p:object].m(out [p:object] o)");
		InvocationExpression inv = new InvocationExpression();
		inv.setMethodName(m);
		inv.parameters.add(SSTUtil.refExpr(varRef("o")));

		md1.body.add(decl);
		md1.body.add(exprStmt(inv));

		addUniqueAOs(inv.getReference());
		addAO(decl.getReference(), ((IReferenceExpression) inv.parameters.get(0)).getReference());

		IUsage actual = assertOneUsage(ctx(sst), decl.getReference());
		assertEquals(definedByOutParameter(m), actual.getDefinition());
		assertEquals(new ArrayList<>(), actual.getUsageSites());
	}
}
