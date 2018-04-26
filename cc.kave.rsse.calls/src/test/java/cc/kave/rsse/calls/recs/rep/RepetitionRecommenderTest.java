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
package cc.kave.rsse.calls.recs.rep;

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.model.ssts.impl.SSTUtil.completionExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.exprStmt;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class RepetitionRecommenderTest {

	private static final IFieldName MEMBER1 = Names.newField("[p:int] [p:object]._f1");
	private static final IFieldName MEMBER2 = Names.newField("[p:int] [p:object]._f2");
	private static final IFieldName MEMBER3 = Names.newField("[p:int] [p:object]._f3");

	private RepetitionModel model;
	private RepetitionRecommender sut;

	@Before
	public void setup() {
		model = new RepetitionModel();

		RepetitionModelStore modelStore = mock(RepetitionModelStore.class);
		when(modelStore.hasModel(any(ITypeName.class))).thenReturn(true);
		when(modelStore.getModel(any(ITypeName.class))).thenReturn(model);

		sut = new RepetitionRecommender(modelStore);
	}

	@Test
	public void lastModelSizeIsSimplyZero() {
		assertEquals(0, sut.getLastModelSize());
		queryGivesProposals();
		assertEquals(0, sut.getLastModelSize());
	}

	@Test
	public void noQueryNoProposals() {
		add(MEMBER1, 0.4);
		Set<Pair<IMemberName, Double>> actual = query(true);
		assertEquals(new HashSet<>(), actual);
	}

	@Test
	public void noModelNoProposals() {
		RepetitionModelStore modelStore = mock(RepetitionModelStore.class);
		when(modelStore.hasModel(any(ITypeName.class))).thenReturn(false);
		sut = new RepetitionRecommender(modelStore);

		Set<Pair<IMemberName, Double>> actual = query(true);
		assertEquals(new HashSet<>(), actual);
		assertTrue(actual instanceof HashSet);
	}

	@Test
	public void queryGivesProposals() {
		add(MEMBER1, 0.4);
		add(MEMBER2, 0.3);
		Set<Pair<IMemberName, Double>> actual = query(true, MEMBER1, MEMBER3);
		Set<Pair<IMemberName, Double>> expected = ProposalHelper.createSortedSet();
		expected.add(Pair.of(MEMBER1, 0.4));
		assertEquals(expected, actual);
	}

	@Test
	public void queryGivesProposals2() {
		add(MEMBER1, 0.4);
		add(MEMBER2, 0.3);
		Set<Pair<IMemberName, Double>> actual = query(true, MEMBER1, MEMBER2);
		Set<Pair<IMemberName, Double>> expected = ProposalHelper.createSortedSet();
		expected.add(Pair.of(MEMBER1, 0.4));
		expected.add(Pair.of(MEMBER2, 0.3));
		assertEquals(expected, actual);
	}

	@Test
	public void noProposalsWithoutCompletionExpr() {
		add(MEMBER1, 0.4);
		add(MEMBER2, 0.3);
		Set<Pair<IMemberName, Double>> actual = query(false, MEMBER1, MEMBER2);
		assertEquals(new HashSet<>(), actual);
	}

	private void add(IMemberName m, double p) {
		model.setRepetitionProbability(m, p);
	}

	private Set<Pair<IMemberName, Double>> query(boolean addQuery, IFieldName... ms) {

		MethodDeclaration md = new MethodDeclaration();
		md.body.add(varDecl(varRef("o"), newType("T, P")));
		if (addQuery) {
			md.body.add(exprStmt(completionExpr("o")));
		}
		for (IFieldName m : ms) {
			md.body.add(exprStmt(refExpr(SSTUtil.fieldRef(varRef("o"), m))));
		}

		SST sst = new SST();
		sst.enclosingType = Names.newType("T, P");
		sst.methods.add(md);

		Context ctx = new Context();
		ctx.setSST(sst);

		return sut.query(ctx, new LinkedList<>());
	}
}