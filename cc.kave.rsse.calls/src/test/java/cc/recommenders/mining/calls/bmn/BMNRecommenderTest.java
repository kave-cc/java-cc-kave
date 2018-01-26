/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls.bmn;

import static cc.recommenders.mining.calls.bmn.BMNRecommender.calculateDistance;
import static cc.recommenders.mining.calls.bmn.QueryState.CREATE_PROPOSAL;
import static cc.recommenders.mining.calls.bmn.QueryState.FALSE;
import static cc.recommenders.mining.calls.bmn.QueryState.IGNORE_IN_DISTANCE_CALCULATION;
import static cc.recommenders.mining.calls.bmn.QueryState.TRUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.datastructures.Tuple;
import cc.kave.commons.exceptions.AssertionException;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.UsageFeature;

@SuppressWarnings("unchecked")
public class BMNRecommenderTest {

	@Mock
	private ClassFeature class1;
	@Mock
	private ClassFeature class2;
	@Mock
	private FirstMethodFeature method1;
	@Mock
	private FirstMethodFeature method2;
	@Mock
	private FirstMethodFeature methodUnknown;
	@Mock
	private DefinitionFeature def1;
	@Mock
	private DefinitionFeature def2;
	@Mock
	private ParameterFeature param1;
	@Mock
	private ParameterFeature param2;

	private CallFeature call1;
	private CallFeature call2;
	private CallFeature call3;
	private CallFeature callUnknown;

	@Mock
	private FeatureExtractor<Usage, UsageFeature> featureExtractor;
	private Dictionary<UsageFeature> dict;
	private Set<Tuple<ICoReMethodName, Double>> expecteds;
	private Set<Tuple<ICoReMethodName, Double>> actuals;
	private Table table;
	private BMNRecommender sut;
	private QueryOptions qOpts;
	private BMNModel bmnModel;

	@Before
	public void setup() {
		initMocks(this);
		qOpts = new QueryOptions();

		call1 = mockMethod(1);
		call2 = mockMethod(2);
		call3 = mockMethod(3);
		callUnknown = mockMethod(0);

		bmnModel = new BMNModel();
		init(method1, method2, call1, call2, call3);
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
	}

	private void init(UsageFeature... fs) {
		dict = new Dictionary<UsageFeature>();
		for (UsageFeature f : fs) {
			dict.add(f);
		}

		table = new Table(fs.length);
		bmnModel.table = table;
		bmnModel.dictionary = dict;
		sut = new BMNRecommender(featureExtractor, bmnModel, qOpts);
	}

	private void setqOpts(String opts) {
		qOpts.setFrom(QueryOptions.newQueryOptions(opts));
	}

	private CallFeature mockMethod(int i) {
		CallFeature f = mock(CallFeature.class, "call" + i);
		when(f.getMethodName()).thenReturn(CoReMethodName.get("LSomeType.m" + i + "()V"));
		return f;
	}

	@Test(expected = AssertionException.class)
	public void distanceDimensionsMustMatch() {
		calculateDistance(_(FALSE, TRUE), _(1));
	}

	@Test
	public void distanceCalculation() {
		assertDistance(_(FALSE), _(1), 1);
		assertDistance(_(TRUE), _(1), 0);
		assertDistance(_(CREATE_PROPOSAL), _(1), 0);
		assertDistance(_(IGNORE_IN_DISTANCE_CALCULATION), _(1), 0);

		assertDistance(_(FALSE), _(0), 0);
		assertDistance(_(TRUE), _(0), 1);
		assertDistance(_(CREATE_PROPOSAL), _(0), 0);
		assertDistance(_(IGNORE_IN_DISTANCE_CALCULATION), _(0), 0);

		assertDistance(_(FALSE, FALSE), _(1, 0), 1);
		assertDistance(_(TRUE, TRUE), _(1, 0), 1);
		assertDistance(_(CREATE_PROPOSAL, CREATE_PROPOSAL), _(1, 0), 0);
		assertDistance(_(IGNORE_IN_DISTANCE_CALCULATION, IGNORE_IN_DISTANCE_CALCULATION), _(1, 0), 0);

		assertDistance(_(FALSE, TRUE), _(1, 0), 2);
		assertDistance(_(TRUE, FALSE), _(1, 0), 0);
		assertDistance(_(CREATE_PROPOSAL, TRUE), _(1, 0), 1);
		assertDistance(_(CREATE_PROPOSAL, FALSE), _(1, 0), 0);
	}

	@Test
	public void recommendationDoesNotProposeZeroValues() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(0, 1, 0, 1, 0));

		// --> 1, 0, 1, ?, ?
		Query q = q(call1);

		actuals = sut.query(q);
		expecteds = __();
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendation() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 1, 0));
		table.add(_(1, 0, 1, 1, 1));
		table.add(_(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Query q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(3, 2.0 / 3.0), $(4, 1.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void respectQueryOptionsAndMinProbability() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 1, 0));
		table.add(_(1, 0, 1, 1, 1));
		table.add(_(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Query q = q(method1, call1);

		qOpts.minProbability = 0.5;
		actuals = sut.query(q);
		expecteds = __($(3, 2.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationInORder() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 0, 1));
		table.add(_(1, 0, 1, 1, 1));
		table.add(_(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Query q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(4, 2.0 / 3.0), $(3, 1.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationFrequenciesAreConsidered() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 0, 1));
		table.add(_(1, 0, 1, 1, 1));
		table.add(_(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Query q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(4, 2.0 / 4.0), $(3, 1.0 / 4.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationForUnknownInformation() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 0, 0));
		table.add(_(1, 0, 1, 0, 1));
		table.add(_(1, 0, 1, 1, 1));
		table.add(_(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Query q = q(methodUnknown, callUnknown);

		actuals = sut.query(q);
		expecteds = __($(2, 4.0 / 5.0), $(4, 3.0 / 5.0), $(3, 2.0 / 5.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void sizeIsCalculated() {
		bmnModel.table = mock(Table.class);
		sut = new BMNRecommender(featureExtractor, bmnModel, qOpts);

		when(bmnModel.table.getSize()).thenReturn(152637);
		int actual = sut.getSize();
		int expected = 152637;
		assertEquals(expected, actual);
		verify(bmnModel.table).getSize();
	}

	@Test
	public void featuresWithoutCLASS() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(class1, class2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(class1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithCLASS() {
		setqOpts("+CLASS-METHOD-DEF-PARAMS");
		init(class1, class2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(class1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutMETHOD() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(method1, method2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(method1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithMETHOD() {
		setqOpts("-CLASS+METHOD-DEF-PARAMS");
		init(method1, method2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(method1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutDEF() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(def1, def2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(def1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithDEF() {
		setqOpts("-CLASS-METHOD+DEF-PARAMS");
		init(def1, def2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(def1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutPARAMS() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(param1, param2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(param1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithPARAMS() {
		setqOpts("-CLASS-METHOD-DEF+PARAMS");
		init(param1, param2, call1);
		table.add(_(1, 0, 1));
		table.add(_(0, 1, 0));
		Query q = q(param1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	private Query q(UsageFeature... fs) {
		Query q = mock(Query.class);
		List<UsageFeature> fss = Lists.newLinkedList();
		for (UsageFeature f : fs) {
			fss.add(f);
		}
		when(featureExtractor.extract(eq(q))).thenReturn(fss);
		return q;
	}

	private Set<Tuple<ICoReMethodName, Double>> __(Tuple<ICoReMethodName, Double>... tuples) {
		Set<Tuple<ICoReMethodName, Double>> res = Sets.newLinkedHashSet();
		for (Tuple<ICoReMethodName, Double> t : tuples) {
			res.add(t);
		}
		return res;
	}

	private Tuple<ICoReMethodName, Double> $(int indexOfFeature, double probability) {
		UsageFeature f = dict.getEntry(indexOfFeature);
		if (!(f instanceof CallFeature)) {
			throw new RuntimeException("CallFeature expected");
		}
		CallFeature cf = (CallFeature) f;
		return Tuple.newTuple(cf.getMethodName(), probability);
	}

	private void assertDistance(QueryState[] query, boolean[] row, int expected) {
		int actual = calculateDistance(query, row);
		assertEquals(expected, actual);
	}

	private static QueryState[] _(QueryState... values) {
		return values;
	}

	private static boolean[] _(int... values) {
		boolean[] res = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i] == 1;
		}
		return res;
	}

	private static void assertProposals(Set<Tuple<ICoReMethodName, Double>> expecteds,
			Set<Tuple<ICoReMethodName, Double>> actuals) {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Tuple<ICoReMethodName, Double>> itE = expecteds.iterator();
		Iterator<Tuple<ICoReMethodName, Double>> itA = actuals.iterator();
		while (itE.hasNext()) {
			Tuple<ICoReMethodName, Double> expected = itE.next();
			Tuple<ICoReMethodName, Double> actual = itA.next();
			assertEquals(expected, actual);
		}
	}
}