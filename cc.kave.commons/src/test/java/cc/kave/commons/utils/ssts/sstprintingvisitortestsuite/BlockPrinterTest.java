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
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;

public class BlockPrinterTest extends SSTPrintingVisitorBaseTest {

	@Test
	public void testForEachLoop() {
		ForEachLoop sst = new ForEachLoop();
		sst.setDeclaration(SSTUtil.declare("e", Names.newType("T,P")));
		sst.setLoopedReference(SSTUtil.variableReference("elements"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "foreach (T e in elements)", "{", "    continue;", "}");
	}

	@Test
	public void testSwitchBlock() {
		SwitchBlock sst = new SwitchBlock();
		CaseBlock case1 = new CaseBlock();
		case1.setLabel(constant("1"));
		case1.getBody().add(new BreakStatement());
		case1.getBody().add(new BreakStatement());
		CaseBlock case2 = new CaseBlock();
		case2.setLabel(constant("2"));
		case2.getBody().add(new BreakStatement());
		sst.setReference(SSTUtil.variableReference("a"));
		sst.getDefaultSection().add(new BreakStatement());
		sst.getSections().add(case1);
		sst.getSections().add(case2);

		assertPrint(sst, "switch (a)", "{", "    case 1:", "        break;", "        break;", "    case 2:",
				"        break;", "    default:", "        break;", "}");
	}

	@Test
	public void testSwitchBlock_NoDefaultBlock() {
		SwitchBlock sst = new SwitchBlock();
		CaseBlock case1 = new CaseBlock();
		case1.setLabel(constant("1"));
		case1.getBody().add(new BreakStatement());
		case1.getBody().add(new BreakStatement());
		CaseBlock case2 = new CaseBlock();
		case2.setLabel(constant("2"));
		case2.getBody().add(new BreakStatement());
		sst.setReference(SSTUtil.variableReference("a"));
		sst.getSections().add(case1);
		sst.getSections().add(case2);

		assertPrint(sst, "switch (a)", "{", "    case 1:", "        break;", "        break;", "    case 2:",
				"        break;", "}");
	}

	@Test
	public void testTryBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(Names.newParameter("[ExceptionType,P] e"));
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getFinally().add(new ContinueStatement());
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType e)", "{",
				"    break;", "}", "finally", "{", "    continue;", "}");
	}

	@Test
	public void testTryBlock_NoFinallyBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(Names.newParameter("[ExceptionType,P] e"));
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType e)", "{",
				"    break;", "}");
	}

	@Test
	public void testTryBlock_GeneralCatchBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setKind(CatchBlockKind.General);
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch", "{", "    break;", "}");
	}

	@Test
	public void testTryBlock_UnnamedCatchBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(Names.newParameter("[ExceptionType,P] e"));
		catch1.setKind(CatchBlockKind.Unnamed);
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType)", "{", "    break;",
				"}");
	}

	@Test
	public void testUncheckedBlock() {
		UncheckedBlock sst = new UncheckedBlock();
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "unchecked", "{", "    break;", "}");
	}

	@Test
	public void testUnsafeBlock() {
		UnsafeBlock sst = new UnsafeBlock();
		assertPrint(sst, "unsafe { /* content ignored */ }");
	}

	@Test
	public void testUsingBlock() {
		UsingBlock sst = new UsingBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "using (variable)", "{", "    break;", "}");
	}

	@Test
	public void testIfElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());
		sst.getElse().add(new BreakStatement());

		assertPrint(sst, "if (true)", "{", "    continue;", "}", "else", "{", "    break;", "}");
	}

	@Test
	public void testIfElseBlock_NoElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());

		assertPrint(sst, "if (true)", "{", "    continue;", "}");
	}

	@Test
	public void testLockBlock() {
		LockBlock sst = new LockBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "lock (variable)", "{", "    continue;", "}");
	}

	@Test
	public void testWhileLoop() {
		WhileLoop sst = new WhileLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(constant("true"));
		loopHeader.getBody().add(returnStatement);
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "while (", "    {", "        return true;", "    }", ")", "{", "    continue;", "    break;",
				"}");
	}

	@Test
	public void testDoLoop() {
		DoLoop sst = new DoLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(constant("true"));
		loopHeader.getBody().add(returnStatement);
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());
		assertPrint(sst, "do", "{", "    continue;", "    break;", "}", "while (", "    {", "        return true;",
				"    }", ")");
	}

	@Test
	public void testForLoop() {
		ForLoop sst = new ForLoop();
		sst.getInit().add(SSTUtil.declare("i", Names.newType("T,P")));
		sst.getInit().add(SSTUtil.assignmentToLocal("i", constant("0")));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(constant("true"));
		loopHeader.getBody().add(returnStatement);
		sst.setCondition(loopHeader);

		assertPrint(sst, "for (", "    {", "        T i;", "        i = 0;", "    };", "    {", "        return true;",
				"    }; { }", ")", "{", "    continue;", "    break;", "}");
	}
}