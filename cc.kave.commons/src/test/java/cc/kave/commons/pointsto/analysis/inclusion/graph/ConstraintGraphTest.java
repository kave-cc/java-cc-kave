/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.inclusion.graph;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareFields;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.fieldReference;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.parameter;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Multimap;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.inclusion.ConstructedTerm;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.StmtAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.EmptyContextFactory;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintEdge;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraph;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraphBuilder;
import cc.kave.commons.pointsto.analysis.references.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.references.DistinctVariableReference;
import cc.kave.commons.pointsto.analysis.utils.ScopedMap;

public class ConstraintGraphTest {

	private final static String TEST_TYPE_IDENTIFIER = "Test.ConstraintGraphTest, Test";

	@Test
	public void testFieldExample() {
		// p = new ?()
		// q = new ?()
		// p.f = q
		// r = p.f
		// r.m()

		final IFieldName field = Names.newField("[?] [" + TEST_TYPE_IDENTIFIER + "].f");
		final IMethodName method = Names.newMethod("[p:void] [" + TEST_TYPE_IDENTIFIER + "].m()");

		DeclarationMapper declMapper = mock(DeclarationMapper.class);
		when(declMapper.get((IMemberName) field)).thenReturn(declareFields(field.getIdentifier()).iterator().next());

		ScopedMap<String, DistinctReference> scopes = new ScopedMap<>();
		scopes.enter();
		DistinctReferenceCreationVisitor distRefCreationVisitor = new DistinctReferenceCreationVisitor();
		ConstraintGraphBuilder builder = new ConstraintGraphBuilder(ref -> ref.accept(distRefCreationVisitor, scopes),
				declMapper, new EmptyContextFactory());

		IVariableDeclaration pDecl = declareVar("p");
		DistinctReference pDistRef = new DistinctVariableReference(pDecl);
		scopes.create("p", pDistRef);
		IVariableDeclaration qDecl = declareVar("q");
		DistinctReference qDistRef = new DistinctVariableReference(qDecl);
		scopes.create("q", qDistRef);
		IVariableDeclaration rDecl = declareVar("r");
		DistinctReference rDistRef = new DistinctVariableReference(rDecl);
		scopes.create("r", rDistRef);

		builder.allocate(varRef("p"), new StmtAllocationSite(pDecl));
		builder.allocate(varRef("q"), new StmtAllocationSite(qDecl));

		builder.writeMember(fieldReference(varRef("p"), field), varRef("q"), field);
		builder.readMember(varRef("r"), fieldReference(varRef("p"), field), field);

		builder.invoke(ConstructedTerm.BOTTOM, varRef("r"), Collections.emptyList(), method);

		ConstraintGraph graph = builder.createConstraintGraph();
		graph.computeClosure();
		Multimap<DistinctReference, ConstraintEdge> ls = graph.computeLeastSolution();

		assertThat(ls.get(new DistinctVariableReference(rDecl)),
				Matchers.is(ls.get(new DistinctVariableReference(qDecl))));
		assertThat(ls.get(new DistinctFieldReference(fieldReference(pDecl.getReference(), field), pDistRef)),
				Matchers.is(ls.get(rDistRef)));
		assertThat(ls.get(new DistinctVariableReference(pDecl)),
				Matchers.not(ls.get(new DistinctVariableReference(qDecl))));
		assertThat(ls.get(
				new DistinctMethodParameterReference(parameter("this", Names.newType(TEST_TYPE_IDENTIFIER)), method)),
				Matchers.is(ls.get(new DistinctVariableReference(rDecl))));
	}

	@Test
	public void testDynamicDispatch() {
		// A a = new A()
		// B b = new B()
		// A c = b
		// X x = b.n()
		// X y = c.n()
		// a = b
		// X z = a.n()

		ITypeName aType = Names.newType("A, Test");
		ITypeName bType = Names.newType("B, Test");
		ITypeName xType = Names.newType("X, Test");
		IMethodName aNMethod = Names.newMethod("[" + xType.getIdentifier() + "] [" + aType.getIdentifier() + "].n()");
		IMethodName bNMethod = Names.newMethod("[" + xType.getIdentifier() + "] [" + bType.getIdentifier() + "].n()");

		DeclarationMapper declMapper = mock(DeclarationMapper.class);
		when(declMapper.get(aNMethod)).thenReturn(declareMethod());
		when(declMapper.get(bNMethod)).thenReturn(declareMethod());

		ScopedMap<String, DistinctReference> scopes = new ScopedMap<>();
		DistinctReferenceCreationVisitor distRefCreationVisitor = new DistinctReferenceCreationVisitor();
		ConstraintGraphBuilder builder = new ConstraintGraphBuilder(ref -> ref.accept(distRefCreationVisitor, scopes),
				declMapper, new EmptyContextFactory());

		List<IVariableDeclaration> methodVarDecls = new ArrayList<>();
		for (IMethodName method : Arrays.asList(aNMethod, bNMethod)) {
			ITypeName type = method.getDeclaringType();
			scopes.enter();
			String retValName = "retVal" + type.getName();
			IVariableDeclaration retValDecl = declareVar(retValName);
			methodVarDecls.add(retValDecl);
			scopes.create(retValName, new DistinctVariableReference(retValDecl));
			builder.allocate(retValDecl.getReference(), new StmtAllocationSite(retValDecl));
			builder.alias(builder.getReturnVariable(method), builder.getVariable(retValDecl.getReference()));
			scopes.leave();
		}
		IVariableDeclaration retVarA = methodVarDecls.get(0);
		IVariableDeclaration retVarB = methodVarDecls.get(1);

		scopes.enter();
		IVariableDeclaration aDecl = declareVar("a", aType);
		scopes.create("a", new DistinctVariableReference(aDecl));
		builder.allocate(aDecl.getReference(), new StmtAllocationSite(aDecl));
		IVariableDeclaration bDecl = declareVar("b", bType);
		scopes.create("b", new DistinctVariableReference(bDecl));
		builder.allocate(bDecl.getReference(), new StmtAllocationSite(bDecl));
		IVariableDeclaration cDecl = declareVar("c", aType);
		scopes.create("c", new DistinctVariableReference(cDecl));
		builder.alias(cDecl.getReference(), bDecl.getReference());

		IVariableDeclaration xDecl = declareVar("x", xType);
		scopes.create("x", new DistinctVariableReference(xDecl));
		builder.invoke(builder.getVariable(xDecl.getReference()), bDecl.getReference(), Collections.emptyList(),
				bNMethod);
		IVariableDeclaration yDecl = declareVar("y", xType);
		scopes.create("y", new DistinctVariableReference(yDecl));
		builder.invoke(builder.getVariable(yDecl.getReference()), cDecl.getReference(), Collections.emptyList(),
				aNMethod);

		builder.alias(aDecl.getReference(), bDecl.getReference());
		IVariableDeclaration zDecl = declareVar("z", xType);
		scopes.create("z", new DistinctVariableReference(zDecl));
		builder.invoke(builder.getVariable(zDecl.getReference()), aDecl.getReference(), Collections.emptyList(),
				aNMethod);

		ConstraintGraph graph = builder.createConstraintGraph();
		graph.computeClosure();
		Multimap<DistinctReference, ConstraintEdge> ls = graph.computeLeastSolution();

		DistinctReference distRetVarA = new DistinctVariableReference(retVarA);
		DistinctReference distRetVarB = new DistinctVariableReference(retVarB);
		// pts(x) == pts(retVarB)
		assertThat(ls.get(new DistinctVariableReference(xDecl)), Matchers.is(ls.get(distRetVarB)));
		// pts(y) == pts(retVarB)
		assertThat(ls.get(new DistinctVariableReference(yDecl)), Matchers.is(ls.get(distRetVarB)));
		// pts(z) == pts(retVarA)∪ pts(retVarB)
		Set<ConstraintEdge> retVarABEdges = new HashSet<>(ls.get(distRetVarA));
		retVarABEdges.addAll(ls.get(distRetVarB));
		assertThat(ls.get(new DistinctVariableReference(zDecl)), Matchers.is(retVarABEdges));
		// pts(retVarA) != pts(retVarB)
		assertNotEquals(ls.get(distRetVarA), ls.get(distRetVarB));
	}
}