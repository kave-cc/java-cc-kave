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
package cc.kave.rsse.calls.recs.bmn;

import static cc.kave.rsse.calls.recs.bmn.BMNRecommender.calculateDistance;
import static cc.kave.rsse.calls.recs.bmn.QueryState.CREATE_PROPOSAL;
import static cc.kave.rsse.calls.recs.bmn.QueryState.FALSE;
import static cc.kave.rsse.calls.recs.bmn.QueryState.IGNORE_IN_DISTANCE_CALCULATION;
import static cc.kave.rsse.calls.recs.bmn.QueryState.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.utils.OptionsBuilder;

@SuppressWarnings("unchecked")
public class BMNRecommenderTest {

	@Mock
	private ClassContextFeature class1;
	@Mock
	private ClassContextFeature class2;
	@Mock
	private MethodContextFeature method1;
	@Mock
	private MethodContextFeature method2;
	@Mock
	private MethodContextFeature methodUnknown;
	@Mock
	private DefinitionFeature def1;
	@Mock
	private DefinitionFeature def2;

	private UsageSiteFeature call1;
	private UsageSiteFeature call2;
	private UsageSiteFeature call3;
	private UsageSiteFeature callUnknown;

	@Mock
	private FeatureExtractor featureExtractor;
	private Dictionary<IFeature> dict;
	private Set<Pair<IMethodName, Double>> expecteds;
	private Set<Pair<IMethodName, Double>> actuals;
	private Table table;
	private BMNRecommender sut;
	private Options qOpts;
	private BMNModel bmnModel;

	@Before
	public void setup() {
		initMocks(this);
		qOpts = new Options("");

		call1 = mockMethod(1);
		call2 = mockMethod(2);
		call3 = mockMethod(3);
		callUnknown = mockMethod(0);

		bmnModel = new BMNModel();
		init(method1, method2, call1, call2, call3);
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
	}

	private void init(IFeature... fs) {
		dict = new Dictionary<IFeature>();
		for (IFeature f : fs) {
			dict.add(f);
		}

		table = new Table(fs.length);
		bmnModel.table = table;
		bmnModel.dictionary = dict;
		sut = new BMNRecommender(featureExtractor, bmnModel, qOpts);
	}

	private void setqOpts(String opts) {
		qOpts = new Options(opts);
	}

	private UsageSiteFeature mockMethod(int i) {
		UsageSiteFeature f = mock(UsageSiteFeature.class, "call" + i);
		fail();
		// when(f.getMethodName()).thenReturn(Names.newMethod("LSomeType.m" + i +
		// "()V"));
		return f;
	}

	@Test(expected = AssertionException.class)
	public void distanceDimensionsMustMatch() {
		calculateDistance(q(FALSE, TRUE), q(1));
	}

	@Test
	public void distanceCalculation() {
		assertDistance(q(FALSE), q(1), 1);
		assertDistance(q(TRUE), q(1), 0);
		assertDistance(q(CREATE_PROPOSAL), q(1), 0);
		assertDistance(q(IGNORE_IN_DISTANCE_CALCULATION), q(1), 0);

		assertDistance(q(FALSE), q(0), 0);
		assertDistance(q(TRUE), q(0), 1);
		assertDistance(q(CREATE_PROPOSAL), q(0), 0);
		assertDistance(q(IGNORE_IN_DISTANCE_CALCULATION), q(0), 0);

		assertDistance(q(FALSE, FALSE), q(1, 0), 1);
		assertDistance(q(TRUE, TRUE), q(1, 0), 1);
		assertDistance(q(CREATE_PROPOSAL, CREATE_PROPOSAL), q(1, 0), 0);
		assertDistance(q(IGNORE_IN_DISTANCE_CALCULATION, IGNORE_IN_DISTANCE_CALCULATION), q(1, 0), 0);

		assertDistance(q(FALSE, TRUE), q(1, 0), 2);
		assertDistance(q(TRUE, FALSE), q(1, 0), 0);
		assertDistance(q(CREATE_PROPOSAL, TRUE), q(1, 0), 1);
		assertDistance(q(CREATE_PROPOSAL, FALSE), q(1, 0), 0);
	}

	@Test
	public void recommendationDoesNotProposeZeroValues() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(0, 1, 0, 1, 0));

		// --> 1, 0, 1, ?, ?
		Usage q = q(call1);

		actuals = sut.query(q);
		expecteds = __();
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendation() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 1, 0));
		table.add(q(1, 0, 1, 1, 1));
		table.add(q(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Usage q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(3, 2.0 / 3.0), $(4, 1.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void respectQueryOptionsAndMinProbability() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 1, 0));
		table.add(q(1, 0, 1, 1, 1));
		table.add(q(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Usage q = q(method1, call1);

		qOpts = OptionsBuilder.bmn().minProbability(0.5).get();
		actuals = sut.query(q);
		expecteds = __($(3, 2.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationInORder() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 0, 1));
		table.add(q(1, 0, 1, 1, 1));
		table.add(q(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Usage q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(4, 2.0 / 3.0), $(3, 1.0 / 3.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationFrequenciesAreConsidered() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 0, 1));
		table.add(q(1, 0, 1, 1, 1));
		table.add(q(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Usage q = q(method1, call1);

		actuals = sut.query(q);
		expecteds = __($(4, 2.0 / 4.0), $(3, 1.0 / 4.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void recommendationForUnknownInformation() {
		// dict: ctx1, ctx2, call1, call2, call3
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 0, 0));
		table.add(q(1, 0, 1, 0, 1));
		table.add(q(1, 0, 1, 1, 1));
		table.add(q(0, 1, 0, 1, 1));

		// --> 1, 0, 1, ?, ?
		Usage q = q(methodUnknown, callUnknown);

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
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(class1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithCLASS() {
		setqOpts("+CLASS-METHOD-DEF-PARAMS");
		init(class1, class2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(class1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutMETHOD() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(method1, method2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(method1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithMETHOD() {
		setqOpts("-CLASS+METHOD-DEF-PARAMS");
		init(method1, method2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(method1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutDEF() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		init(def1, def2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(def1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithDEF() {
		setqOpts("-CLASS-METHOD+DEF-PARAMS");
		init(def1, def2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		Usage q = q(def1);

		actuals = sut.query(q);
		expecteds = __($(2, 1.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithoutPARAMS() {
		setqOpts("-CLASS-METHOD-DEF-PARAMS");
		// init(param1, param2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		// Usage q = q(param1);

		// actuals = sut.query(q);
		expecteds = __($(2, 1.0 / 2.0));
		assertProposals(expecteds, actuals);
	}

	@Test
	public void featuresWithPARAMS() {
		setqOpts("-CLASS-METHOD-DEF+PARAMS");
		// init(param1, param2, call1);
		table.add(q(1, 0, 1));
		table.add(q(0, 1, 0));
		// Usage q = q(param1);
		//
		// actuals = sut.query(q);
		// expecteds = __($(2, 1.0));
		// assertProposals(expecteds, actuals);
	}

	private Usage q(IFeature... fs) {
		Usage q = mock(Usage.class);
		List<IFeature> fss = Lists.newLinkedList();
		for (IFeature f : fs) {
			fss.add(f);
		}
		when(featureExtractor.extract(eq(q))).thenReturn(fss);
		return q;
	}

	private Set<Pair<IMethodName, Double>> __(Pair<IMethodName, Double>... tuples) {
		Set<Pair<IMethodName, Double>> res = Sets.newLinkedHashSet();
		for (Pair<IMethodName, Double> t : tuples) {
			res.add(t);
		}
		return res;
	}

	private Pair<IMethodName, Double> $(int indexOfFeature, double probability) {
		IFeature f = dict.getEntry(indexOfFeature);
		if (!(f instanceof UsageSiteFeature)) {
			throw new RuntimeException("CallFeature expected");
		}
		UsageSiteFeature cf = (UsageSiteFeature) f;
		fail();
		return null;// Tuple.newTuple(cf.getMethodName(), probability);
	}

	private void assertDistance(QueryState[] query, boolean[] row, int expected) {
		int actual = calculateDistance(query, row);
		assertEquals(expected, actual);
	}

	private static QueryState[] q(QueryState... values) {
		return values;
	}

	private static boolean[] q(int... values) {
		boolean[] res = new boolean[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i] == 1;
		}
		return res;
	}

	private static void assertProposals(Set<Pair<IMethodName, Double>> expecteds,
			Set<Pair<IMethodName, Double>> actuals) {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Pair<IMethodName, Double>> itE = expecteds.iterator();
		Iterator<Pair<IMethodName, Double>> itA = actuals.iterator();
		while (itE.hasNext()) {
			Pair<IMethodName, Double> expected = itE.next();
			Pair<IMethodName, Double> actual = itA.next();
			assertEquals(expected, actual);
		}
	}
}