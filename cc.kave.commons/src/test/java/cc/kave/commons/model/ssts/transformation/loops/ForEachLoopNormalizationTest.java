/**
 * Copyright 2016 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.getNext;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.hasNext;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.iteratorInvocation;
import static cc.kave.commons.model.ssts.impl.transformation.loops.IteratorUtil.iteratorType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.transformation.loops.ForEachLoopNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.transformation.StatementNormalizationVisitorBaseTest;

public class ForEachLoopNormalizationTest extends StatementNormalizationVisitorBaseTest<Void> {

	private ForEachLoop forEachLoop;
	private IVariableReference loopedRef0, loopedRef1;
	private IVariableReference it0, it1;
	IVariableDeclaration dec0, dec1;

	@Before
	public void setup() {
		super.setup();
		sut = new ForEachLoopNormalizationVisitor();
		forEachLoop = new ForEachLoop();
		setNormalizing(forEachLoop);

		loopedRef0 = dummyVar(0);
		loopedRef1 = dummyVar(1);
		it0 = variableReference("$it_0");
		it1 = variableReference("$it_1");
		dec0 = declare("e0", Names.newType("t0"));
		dec1 = declare("e1", Names.newType("t1"));
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(E e : elems) {} |..=>..| Iterator<E> it = elems.iterator(); |
	// |_____________________|......| while(it.hasNext()) { ............ |
	// .............................| ..E e = it.next(); ............... |
	// .............................| } ................................ |
	// .............................|____________________________________|
	@Test
	public void testSimpleForEachToWhile() {
		setDeclaration(dec0);
		setLoopedReference(loopedRef0);

		// declare & initialize iterator
		ITypeName iteratorTypeName = iteratorType(dec0.getType());
		IVariableDeclaration iteratorDec = declareVar(it0.getIdentifier(), iteratorTypeName);
		IAssignment iteratorInit = assign(it0, iteratorInvocation(loopedRef0));

		// assign next element
		IAssignment assignNext = assign(dec0.getReference(), getNext(it0));

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(getWhileCondition(it0));
		whileLoop.setBody(list(dec0, assignNext));

		setExpected(iteratorDec, iteratorInit, whileLoop);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(E e : elems) { |......| Iterator<E> it = elems.iterator(); |
	// | ..body; .......... |..=>..| while(it.hasNext()) { ............ |
	// | } ................ |......| ..E e = it.next(); ............... |
	// |____________________|......| ..body; .......................... |
	// ............................| } ................................ |
	// ............................|____________________________________|
	@Test
	public void testLoopBody() {
		setDeclaration(dec0);
		setLoopedReference(loopedRef0);
		setBody(stmt0);

		// declare & initialize iterator
		ITypeName iteratorTypeName = iteratorType(dec0.getType());
		IVariableDeclaration iteratorDec = declareVar(it0.getIdentifier(), iteratorTypeName);
		IAssignment iteratorInit = assign(it0, iteratorInvocation(loopedRef0));

		// assign next element
		IAssignment assignNext = assign(dec0.getReference(), getNext(it0));

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(getWhileCondition(it0));
		whileLoop.setBody(list(dec0, assignNext, stmt0));

		setExpected(iteratorDec, iteratorInit, whileLoop);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | for(A a : as) . |......| Iterator<A> itA = as.iterator(); . |
	// | ..for(B b : bs) |..=>..| while(itA.hasNext()) { ........... |
	// | ....body; ..... |......| ..A a = itA.next(); .............. |
	// |_________________|......| ..Iterator<B> itB = bs.iterator(); |
	// .........................| ..while(itB.hasNext()) { ......... |
	// .........................| ....B b = itB.next(); ............ |
	// .........................| ....body; ........................ |
	// .........................| ..} .............................. |
	// .........................| } ................................ |
	// .........................|____________________________________|
	@Test
	public void testCascadedForEachLoops() {
		// inner for-each loop
		ForEachLoop innerForEach = new ForEachLoop();
		innerForEach.setDeclaration(dec0);
		innerForEach.setLoopedReference(loopedRef0);
		innerForEach.setBody(list(stmt0));

		// outer for-each loop
		setDeclaration(dec1);
		setLoopedReference(loopedRef1);
		setBody(innerForEach);

		// declare & initialize iterators
		ITypeName iteratorType0 = iteratorType(dec0.getType());
		ITypeName iteratorType1 = iteratorType(dec1.getType());
		IVariableDeclaration iteratorDec0 = declareVar(it0.getIdentifier(), iteratorType0);
		IVariableDeclaration iteratorDec1 = declareVar(it1.getIdentifier(), iteratorType1);
		IAssignment iteratorInit0 = assign(it0, iteratorInvocation(loopedRef0));
		IAssignment iteratorInit1 = assign(it1, iteratorInvocation(loopedRef1));

		// assign next element
		IAssignment assignNext0 = assign(dec0.getReference(), getNext(it0));
		IAssignment assignNext1 = assign(dec1.getReference(), getNext(it1));

		WhileLoop innerWhile = new WhileLoop();
		innerWhile.setCondition(getWhileCondition(it0));
		innerWhile.setBody(list(dec0, assignNext0, stmt0));

		WhileLoop outerWhile = new WhileLoop();
		outerWhile.setCondition(getWhileCondition(it1));
		outerWhile.setBody(list(dec1, assignNext1, iteratorDec0, iteratorInit0, innerWhile));
		setExpected(iteratorDec1, iteratorInit1, outerWhile);
		assertTransformedSST();
	}

	// ---------------------------- helpers -----------------------------------

	private void setBody(IStatement... statements) {
		forEachLoop.setBody(Lists.newArrayList(statements));
	}

	private void setDeclaration(IVariableDeclaration declaration) {
		forEachLoop.setDeclaration(declaration);
	}

	private void setLoopedReference(IVariableReference loopedReference) {
		forEachLoop.setLoopedReference(loopedReference);
	}

	private ILoopHeaderExpression getWhileCondition(IVariableReference iterator) {
		List<IStatement> hasNext = define("hasNext", hasNext(iterator));
		IStatement returnStatement = returnStatement(mainCondition(hasNext));
		List<IStatement> loopHeaderBody = new ArrayList<IStatement>();
		loopHeaderBody.addAll(hasNext);
		loopHeaderBody.add(returnStatement);
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		loopHeader.setBody(loopHeaderBody);
		return loopHeader;
	}
}