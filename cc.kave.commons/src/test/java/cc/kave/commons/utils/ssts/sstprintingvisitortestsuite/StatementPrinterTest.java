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
package cc.kave.commons.utils.ssts.sstprintingvisitortestsuite;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class StatementPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void testBreakStatement() {
		BreakStatement sst = new BreakStatement();
		assertPrint(sst, "break;");
	}

	@Test

	public void testContinueStatement() {
		ContinueStatement sst = new ContinueStatement();
		assertPrint(sst, "continue;");
	}

	@Test

	public void testAssignment() {
		IAssignment sst = SSTUtil.assignmentToLocal("var", constant("true"));
		assertPrint(sst, "var = true;");
	}

	@Test

	public void testGotoStatement() {
		GotoStatement sst = new GotoStatement();
		sst.setLabel("L");

		assertPrint(sst, "goto L;");
	}

	@Test

	public void testLabelledStatement() {
		LabelledStatement sst = new LabelledStatement();
		sst.setLabel("L");
		sst.setStatement(new ContinueStatement());

		assertPrint(sst, "L:", "continue;");
	}

	@Test

	public void testThrowStatement() {
		ThrowStatement sst = new ThrowStatement();
		VariableReference ref = new VariableReference();
		ref.setIdentifier("T");
		sst.setReference(ref);

		// note: we can ignore exception constructors and throwing existing
		// objects
		assertPrint(sst, "throw new T();");
	}

	@Test

	public void testReturnStatement() {
		ReturnStatement sst = new ReturnStatement();
		sst.setExpression(constant("val"));

		assertPrint(sst, "return \"val\";");
	}

	@Test

	public void testReturnStatement_Void() {
		cc.kave.commons.model.ssts.impl.statements.ReturnStatement sst = new ReturnStatement();
		sst.setIsVoid(true);
		assertPrint(sst, "return;");
	}

	@Test

	public void testExpressionStatement() {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(SSTUtil.variableReference("this"));
		invocation.setMethodName(Names.newMethod("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		invocation.getParameters().add(constant("1"));

		ExpressionStatement sst = new ExpressionStatement();
		sst.setExpression(invocation);
		assertPrint(sst, "this.M(1);");
	}

	@Test

	public void testUnknownStatement() {
		UnknownStatement sst = new UnknownStatement();
		assertPrint(sst, "???;");
	}

	@Test
	public void testAddEventSubscriptionStatement() {
		EventSubscriptionStatement sst = new EventSubscriptionStatement();
		sst.setOperation(EventSubscriptionOperation.Add);
		sst.setExpression(constant("2"));
		sst.setReference(varRef("a"));
		assertPrint(sst, "a += 2");
	}

	@Test
	public void testRemoveEventSubscriptionStatement() {
		EventSubscriptionStatement sst = new EventSubscriptionStatement();
		sst.setOperation(EventSubscriptionOperation.Remove);
		sst.setExpression(constant("2"));
		sst.setReference(varRef("a"));
		assertPrint(sst, "a -= 2");
	}
}