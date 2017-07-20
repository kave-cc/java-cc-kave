/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public abstract class SSTBaseTest {

	protected static void assertChildren(ISSTNode sut, ISSTNode... expecteds) {
		Iterator<ISSTNode> actualIterator = sut.getChildren().iterator();
		for (ISSTNode expected : expecteds) {
			assertTrue(actualIterator.hasNext());
			assertThat(expected, equalTo(actualIterator.next()));
		}
		assertFalse(actualIterator.hasNext());
	}

	protected ISimpleExpression label(String label) {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue(label);
		return expr;
	}

	protected IVariableDeclaration someDeclaration(String type) {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(Names.newType(type + ",P1"));
		return decl;
	}

	protected IVariableDeclaration someDeclaration() {
		return someDeclaration("T1");
	}

	protected IVariableReference someVarRef(String id) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(id);
		return ref;
	}

	protected IVariableReference someVarRef() {
		return someVarRef("v");
	}

	protected List<IVariableReference> refs(String[] strRefs) {
		List<IVariableReference> varRefs = new ArrayList<IVariableReference>();
		for (int i = 0; i < strRefs.length; i++)
			varRefs.add(someVarRef(strRefs[i]));
		return varRefs;
	}

	protected IMethodName getMethod(String simpleName) {
		String methodName = "[T1, P1] [T2, P2]" + simpleName + "()";
		return Names.newMethod(methodName);
	}

	protected List<ISimpleExpression> refExprs(String[] ids) {
		List<ISimpleExpression> exprs = new ArrayList<ISimpleExpression>();
		for (String id : ids) {
			ReferenceExpression refExpr = new ReferenceExpression();
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(id);
			refExpr.setReference(varRef);
			exprs.add(refExpr);
		}
		return exprs;
	}

	protected IParameterName someParameter() {
		return Names.newParameter("[T,P] n");
	}

	protected ILambdaName someLambdaName() {
		return Names.newLambda("[T,P] ([T2,P2] p)");
	}
}