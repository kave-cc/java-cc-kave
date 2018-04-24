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
package cc.kave.rsse.calls.recs.bmn;

import static cc.kave.commons.model.naming.Names.newField;
import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.commons.model.naming.Names.newProperty;
import static cc.kave.commons.model.ssts.impl.SSTUtil.completionExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.varDecl;
import static cc.kave.commons.utils.ssts.SSTUtils.INT;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.fieldAccess;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.propertyAccess;
import static cc.kave.rsse.calls.recs.bmn.BMNRecommender.calculateDistance;
import static cc.kave.rsse.calls.recs.bmn.QueryState.IGNORE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.SET;
import static cc.kave.rsse.calls.recs.bmn.QueryState.TO_PROPOSE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.UNSET;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.utils.OptionsBuilder;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class BMNRecommenderTest {

	private ClassContextFeature CCTX1 = new ClassContextFeature(Names.newType("Cctx, P"));
	private MethodContextFeature MCTX1 = new MethodContextFeature(newMethod("[p:void] [M1,P].m()"));
	private MethodContextFeature MCTX2 = new MethodContextFeature(newMethod("[p:void] [M2,P].m()"));
	private DefinitionFeature DEF1 = new DefinitionFeature(Definitions.definedByCast());
	private UsageSiteFeature CALL1 = new UsageSiteFeature(call(newMethod("[p:void] [C1,P].c()")));
	private UsageSiteFeature CALL2 = new UsageSiteFeature(call(newMethod("[p:void] [C2,P].c()")));
	private UsageSiteFeature CALL3 = new UsageSiteFeature(call(newMethod("[p:void] [C3,P].c()")));
	private UsageSiteFeature PARAM1 = new UsageSiteFeature(callParameter(newMethod("[R,P] [P1,P].p([P,P] o)"), 0));
	private UsageSiteFeature FIELD1 = new UsageSiteFeature(fieldAccess(newField("[p:void] [F1,P]._f")));
	private UsageSiteFeature PROP1 = new UsageSiteFeature(propertyAccess(newProperty("get [R,P] [P1,P].P()")));

	@Mock
	private FeatureExtractor featureExtractor;
	@Mock
	private IModelStore<BMNModel> bmnModelStore;

	private BMNModel bmnModel;
	private Options opts;
	private Set<Pair<IMemberName, Double>> expecteds;

	@Before
	public void setup() {
		setup(c -> {
		});
	}

	private void setup(Consumer<OptionsBuilder> c) {
		initMocks(this);
		OptionsBuilder ob = OptionsBuilder.bmn().cCtx(true).mCtx(true).def(true).calls(true).params(true).members(true);
		c.accept(ob);
		bmnModel = new BMNModel();
		opts = ob.get();
		expecteds = ProposalHelper.createSortedSet();

		when(bmnModelStore.hasModel(Matchers.any(ITypeName.class))).thenReturn(true);
		when(bmnModelStore.getModel(any(ITypeName.class))).thenReturn(bmnModel);
	}

	private void dict(IFeature... fs) {
		bmnModel.dictionary = new Dictionary<IFeature>();
		for (IFeature f : fs) {
			bmnModel.dictionary.add(f);
		}
		bmnModel.table = new Table(fs.length);
	}

	private void addRow(int... row) {
		assertEquals(bmnModel.dictionary.size(), row.length);
		boolean[] brow = new boolean[row.length];
		for (int i = 0; i < row.length; i++) {
			brow[i] = row[i] == 1;
		}
		bmnModel.table.add(brow);
	}

	public IUsage mockUsage(IFeature... fs) {
		IUsage u = mock(IUsage.class);
		when(featureExtractor.extract(u)).thenReturn(asList(fs));
		return u;
	}

	public void assertRecommendations(IUsage u) {
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		Set<Pair<IMemberName, Double>> actuals = sut.query(u);
		Iterator<Pair<IMemberName, Double>> eit = expecteds.iterator();
		Iterator<Pair<IMemberName, Double>> ait = actuals.iterator();
		while (eit.hasNext()) {
			assertTrue(ait.hasNext());
			Pair<IMemberName, Double> e = eit.next();
			Pair<IMemberName, Double> a = ait.next();
			assertEquals(e, a);
		}
		assertFalse(ait.hasNext());
	}

	private void expect(UsageSiteFeature usf, double count, double total) {
		IMemberName member = usf.site.getMember(IMemberName.class);
		double probability = count / (double) total;
		Pair<IMemberName, Double> expect = Pair.of(member, probability);
		assertTrue(expecteds.add(expect));
	}

	@Test
	public void rec_works() {
		dict(CALL1, CALL2);
		addRow(1, 1);
		addRow(1, 0);
		addRow(0, 0);

		expect(CALL1, 2, 3);
		expect(CALL2, 1, 3);

		assertRecommendations(mockUsage());
	}

	@Test
	public void rec_doesNotProposeZeroValues() {
		dict(CALL1);
		addRow(0);
		assertRecommendations(mockUsage());
	}

	@Test
	public void rec_minProbability() {
		setup(b -> b.minProbability(0.5));

		dict(CALL1, CALL2);
		addRow(1, 1);
		addRow(1, 0);
		addRow(0, 0);

		expect(CALL1, 2, 3);

		assertRecommendations(mockUsage());
	}

	@Test
	public void rec_selectsNearestNeighbor() {
		dict(MCTX1, CALL1, MCTX2, CALL2);
		addRow(1, 1, 0, 0);
		addRow(0, 0, 1, 1);

		expect(CALL1, 1, 1);

		assertRecommendations(mockUsage(MCTX1));
	}

	@Test
	public void rec_averagesOverGroup() {
		dict(MCTX1, CALL1, CALL2, MCTX2, CALL3);
		addRow(1, 1, 1, 0, 0);
		addRow(1, 1, 0, 0, 0);
		addRow(0, 0, 0, 1, 1);

		expect(CALL1, 2, 2);
		expect(CALL2, 1, 2);

		assertRecommendations(mockUsage(MCTX1));
	}

	@Test
	public void rec_considersFrequencyVector() {
		dict(MCTX1, CALL1, CALL2);
		addRow(1, 1, 0);
		addRow(1, 1, 0); // only increases freq vector
		addRow(1, 0, 1);

		expect(CALL1, 2, 3);
		expect(CALL2, 1, 3);

		assertRecommendations(mockUsage(MCTX1));
	}

	@Test
	public void rec_onlyProposesMembers() {
		dict(CCTX1, MCTX1, DEF1, CALL1, PARAM1, FIELD1, PROP1);
		addRow(1, 1, 1, 1, 1, 1, 1);

		expect(CALL1, 1, 1);
		expect(FIELD1, 1, 1);
		expect(PROP1, 1, 1);

		assertRecommendations(mockUsage());
	}

	@Test
	public void rec_unknownFeatures() {
		dict(CALL1);
		addRow(1);

		expect(CALL1, 1, 1);

		assertRecommendations(mockUsage(CALL2));
	}

	@Test
	public void rec_noModel() {
		when(bmnModelStore.hasModel(any(ITypeName.class))).thenReturn(false);
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		Set<Pair<IMemberName, Double>> actual = sut.query(new Usage());
		assertTrue(actual instanceof HashSet);
	}

	// ####################################################################################

	@Test
	public void recWithContext_noQueryNoProposal() {
		dict(CALL1);
		addRow(1);
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		Set<Pair<IMemberName, Double>> actuals = sut.query(new Context());
		assertTrue(actuals instanceof HashSet); // not a TreeSet!
	}

	@Test
	public void recWithContext_withQuery() {
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[p:void] [T,P].m()"));
		md.body.add(varDecl("o", INT));
		md.body.add(SSTUtil.exprStmt(completionExpr("o")));

		SST sst = new SST();
		sst.methods.add(md);

		Context ctx = new Context();
		ctx.setSST(sst);

		when(featureExtractor.extract(any(IUsage.class))).thenReturn(asList());

		dict(CALL1);
		addRow(1);
		expect(CALL1, 1, 1);

		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		Set<Pair<IMemberName, Double>> actuals = sut.query(ctx);
		Iterator<Pair<IMemberName, Double>> eit = expecteds.iterator();
		Iterator<Pair<IMemberName, Double>> ait = actuals.iterator();
		while (eit.hasNext()) {
			assertTrue(ait.hasNext());
			Pair<IMemberName, Double> e = eit.next();
			Pair<IMemberName, Double> a = ait.next();
			assertEquals(e, a);
		}
		assertFalse(ait.hasNext());
	}

	// ####################################################################################

	@Test
	public void size_initial() {
		dict();
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		assertEquals(-1, sut.getLastModelSize());
	}

	@Test
	public void size_empty() {
		dict();
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		sut.query(mockUsage());
		assertEquals(0, sut.getLastModelSize());
	}

	@Test
	public void size_simple() {
		dict(CALL1);
		addRow(1);
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		sut.query(mockUsage());
		assertEquals(5, sut.getLastModelSize());
	}

	@Test
	public void size_advanced() {
		dict(MCTX1, DEF1, CALL1, CALL2, CALL3);
		addRow(1, 1, 1, 0, 0);
		addRow(1, 1, 1, 0, 1);
		addRow(1, 1, 1, 1, 0);
		addRow(1, 1, 1, 1, 1);
		BMNRecommender sut = new BMNRecommender(featureExtractor, bmnModelStore, opts);
		sut.query(mockUsage());
		assertEquals(19, sut.getLastModelSize());
	}

	// ####################################################################################

	@Test
	public void markColumns() {
		QueryState[] queryRow = new QueryState[] { SET, UNSET, TO_PROPOSE, IGNORE };
		boolean[] expected = TestBMNRecommender.markColumnsForProposals(queryRow);
		boolean[] actual = new boolean[] { false, false, true, false };
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void qs_type() {
		IFeature f = mock(TypeFeature.class);
		// call to "calls" does not have any effect
		assertQueryState(f, b -> b.calls(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.calls(false), true, QueryState.IGNORE);
	}

	@Test
	public void qs_cCtx() {
		IFeature f = mock(ClassContextFeature.class);
		assertQueryState(f, b -> b.cCtx(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.cCtx(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.cCtx(true), false, QueryState.UNSET);
		assertQueryState(f, b -> b.cCtx(true), true, QueryState.SET);
	}

	@Test
	public void qs_mCtx() {
		IFeature f = mock(MethodContextFeature.class);
		assertQueryState(f, b -> b.mCtx(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.mCtx(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.mCtx(true), false, QueryState.UNSET);
		assertQueryState(f, b -> b.mCtx(true), true, QueryState.SET);
	}

	@Test
	public void qs_def() {
		IFeature f = mock(DefinitionFeature.class);
		assertQueryState(f, b -> b.def(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.def(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.def(true), false, QueryState.UNSET);
		assertQueryState(f, b -> b.def(true), true, QueryState.SET);
	}

	@Test
	public void qs_calls() {
		IFeature f = new UsageSiteFeature(call("[p:void] [T, P].m()"));
		assertQueryState(f, b -> b.calls(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.calls(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.calls(true), false, QueryState.TO_PROPOSE);
		assertQueryState(f, b -> b.calls(true), true, QueryState.SET);
	}

	@Test
	public void qs_params() {
		IFeature f = new UsageSiteFeature(callParameter("[p:void] [T, P].m([p:object] o)", 0));
		assertQueryState(f, b -> b.params(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.params(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.params(true), false, QueryState.UNSET);
		assertQueryState(f, b -> b.params(true), true, QueryState.SET);
	}

	@Test
	public void qs_members_field() {
		IFeature f = new UsageSiteFeature(fieldAccess("[p:int] [T, P]._f"));
		assertQueryState(f, b -> b.members(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.members(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.members(true), false, QueryState.TO_PROPOSE);
		assertQueryState(f, b -> b.members(true), true, QueryState.SET);
	}

	@Test
	public void qs_members_property() {
		IFeature f = new UsageSiteFeature(propertyAccess("get [p:int] [T, P].P()"));
		assertQueryState(f, b -> b.members(false), false, QueryState.IGNORE);
		assertQueryState(f, b -> b.members(false), true, QueryState.IGNORE);
		assertQueryState(f, b -> b.members(true), false, QueryState.TO_PROPOSE);
		assertQueryState(f, b -> b.members(true), true, QueryState.SET);
	}

	private void assertQueryState(IFeature f, Consumer<OptionsBuilder> c, boolean isInQuery, QueryState expected) {
		OptionsBuilder b = OptionsBuilder.bmn().cCtx(false).mCtx(false).def(false).calls(false).params(false)
				.members(false);
		c.accept(b);
		Options opts = b.get();
		QueryState actual = TestBMNRecommender.getQueryState(f, isInQuery, opts);
		assertEquals(expected, actual);
	}

	// ####################################################################################

	@Test(expected = AssertionException.class)
	public void dist_dimensionsMustMatch() {
		calculateDistance(q(UNSET, SET), q(1));
	}

	@Test
	public void dist_calculation() {

		assertDistance(q(0), q(UNSET), 0);
		assertDistance(q(0), q(SET), 1);
		assertDistance(q(0), q(TO_PROPOSE), 0);
		assertDistance(q(0), q(IGNORE), 0);

		assertDistance(q(1), q(UNSET), 1);
		assertDistance(q(1), q(SET), 0);
		assertDistance(q(1), q(TO_PROPOSE), 0);
		assertDistance(q(1), q(IGNORE), 0);

		assertDistance(q(1, 0), q(UNSET, UNSET), 1);
		assertDistance(q(1, 0), q(SET, SET), 1);
		assertDistance(q(1, 0), q(TO_PROPOSE, TO_PROPOSE), 0);
		assertDistance(q(1, 0), q(IGNORE, IGNORE), 0);

		assertDistance(q(1, 0), q(UNSET, SET), 2);
		assertDistance(q(1, 0), q(SET, UNSET), 0);
		assertDistance(q(1, 0), q(TO_PROPOSE, SET), 1);
		assertDistance(q(1, 0), q(TO_PROPOSE, UNSET), 0);
	}

	private static boolean[] q(int... values) {
		boolean[] res = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i] == 1;
		}
		return res;
	}

	private static QueryState[] q(QueryState... values) {
		return values;
	}

	private static void assertDistance(boolean[] row, QueryState[] query, int expected) {
		int actual = calculateDistance(query, row);
		assertEquals(expected, actual);
	}

	// ####################################################################################

	private static class TestBMNRecommender extends BMNRecommender {

		public TestBMNRecommender(FeatureExtractor featureExtractor, BMNModelStore modelStore, Options opts) {
			super(featureExtractor, modelStore, opts);
		}

		public static boolean[] markColumnsForProposals(QueryState[] queryRow) {
			return BMNRecommender.markColumnsForProposals(queryRow);
		}

		protected static QueryState getQueryState(IFeature f, boolean isFeaturePartOfQuery, Options opts) {
			return BMNRecommender.getQueryState(f, isFeaturePartOfQuery, opts);
		}

		protected static int calculateDistance(QueryState[] query, boolean[] row) {
			return BMNRecommender.calculateDistance(query, row);
		}
	}
}