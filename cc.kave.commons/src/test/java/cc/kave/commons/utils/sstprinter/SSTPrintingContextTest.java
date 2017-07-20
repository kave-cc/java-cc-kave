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
package cc.kave.commons.utils.sstprinter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class SSTPrintingContextTest {

	private static void AssertTypeFormat(String expected, String typeIdentifier) {
		SSTPrintingContext sut = new SSTPrintingContext();
		Assert.assertEquals(expected, sut.type(Names.newType(typeIdentifier)).toString());
	}

	@Test
	public void testTypeNameFormat() {
		AssertTypeFormat("T", "T,P");
	}

	@Test
	public void testTypeNameFormat_Generics() {
		AssertTypeFormat("EventHandler<EventArgsType>", "EventHandler`1[[T -> EventArgsType,P]],P");
	}

	@Test
	public void testTypeNameFormat_UnknownGenericType() {
		// these TypeNames are equivalent
		AssertTypeFormat("C<T>", "C`1[[T]],P");
		AssertTypeFormat("C<?>", "C`1[[T -> ?]],P");
		AssertTypeFormat("C<T>", "C`1[[T -> T]],P");
	}

	@Test
	public void testTypeNameFormat_UnknownToUnknownGenericType() {
		AssertTypeFormat("Task<T>", "Task`1[[TResult -> T]], mscorlib, 4.0.0.0");
	}

	@Test
	public void testTypeNameFormat_MultipleGenerics() {
		AssertTypeFormat("A<B, C>", "A`2[[T1 -> B,P],[T2 -> C,P]],P");
	}

	@Test
	public void testTypeNameFormat_NestedGenerics() {
		AssertTypeFormat("A<B<C>>", "A`1[[T -> B`1[[T -> C,P]],P]],P");
	}

	@Test
	public void testStatementBlock_NotEmpty_WithBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		stmts.add(new ContinueStatement());
		stmts.add(new BreakStatement());
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		String expected = String.join("\n", "", "{", "    continue;", "    break;", "}");

		sut.statementBlock(stmts, visitor, true);
		Assert.assertEquals(expected, sut.toString());
	}

	@Test
	public void testStatementBlock_Empty_WithBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		sut.statementBlock(stmts, visitor, true);
		Assert.assertEquals(" { }", sut.toString());
	}

	@Test
	public void testStatementBlock_NotEmpty_WithoutBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		stmts.add(new ContinueStatement());
		stmts.add(new BreakStatement());
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		String expected = String.join("\n", "", "    continue;", "    break;");

		sut.statementBlock(stmts, visitor, false);
		Assert.assertEquals(expected, sut.toString());
	}

	@Test
	public void testStatementBlock_Empty_WithoutBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		sut.statementBlock(stmts, visitor, false);
		Assert.assertEquals("", sut.toString());
	}

	@Test
	public void testParameterList_NoParameters() {
		List<IParameterName> parameters = new ArrayList<>();
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("()", sut.toString());
	}

	@Test
	public void testParameterList_OneParameter() {
		List<IParameterName> parameters = new ArrayList<>();
		parameters.add(Names.newParameter("[A,P] p1"));
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("(A p1)", sut.toString());
	}

	@Test
	public void testParameterList_MultipleParameters() {
		List<IParameterName> parameters = new ArrayList<>();
		parameters.add(Names.newParameter("[A,P] p1"));
		parameters.add(Names.newParameter("[B,P] p2"));
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("(A p1, B p2)", sut.toString());
	}
}