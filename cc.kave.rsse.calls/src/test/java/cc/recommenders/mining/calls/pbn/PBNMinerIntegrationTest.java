/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls.pbn;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.commons.bayesnet.Node;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.DistanceMeasureFactory;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.PatternFinderFactory;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.calls.MiningOptions.DistanceMeasure;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.mining.calls.pbn.PBNModelBuilder;
import cc.recommenders.mining.calls.pbn.PBNModelConstants;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.OptionAwareFeaturePredicate;
import cc.recommenders.mining.features.RareFeatureDropper;
import cc.recommenders.mining.features.UsageFeatureExtractor;
import cc.recommenders.mining.features.UsageFeatureWeighter;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;

import com.google.common.collect.Lists;

public class PBNMinerIntegrationTest {

	private static final double PRECISION = 0.00001;
	private RareFeatureDropper<UsageFeature> dropper;
	private FeatureExtractor<Usage, UsageFeature> featureExtractor;
	private DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder;
	private PatternFinderFactory<UsageFeature> patternFinderFactory;
	private ModelBuilder<UsageFeature, BayesianNetwork> modelBuilder;
	private MiningOptions mOpts;
	private QueryOptions qOpts;
	private QueryOptions queryOptions;

	private PBNMiner sut;
	private List<Usage> usages;
	private BayesianNetwork expectedNetwork;
	private Node patternNode;

	@Before
	public void setup() {

		qOpts = new QueryOptions();
		mOpts = new MiningOptions();
		mOpts.setT1(0.02);
		mOpts.setT2(0.01);
		mOpts.setDistanceMeasure(DistanceMeasure.MANHATTAN);
		queryOptions = new QueryOptions();
		dropper = new RareFeatureDropper<UsageFeature>();
		featureExtractor = new UsageFeatureExtractor(mOpts);
		modelBuilder = new PBNModelBuilder();

		dictionaryBuilder = new DictionaryBuilder<Usage, UsageFeature>(featureExtractor);
		patternFinderFactory = new PatternFinderFactory<UsageFeature>(new UsageFeatureWeighter(mOpts), mOpts,
				new DistanceMeasureFactory(mOpts));

		sut = new PBNMiner(featureExtractor, dictionaryBuilder, patternFinderFactory, modelBuilder,
				queryOptions, mOpts, dropper, new OptionAwareFeaturePredicate(qOpts));

		usages = Lists.newLinkedList();
	}

	@Test
	public void correctResultWithoutCallSiteFeatureDropping() {
		addUsagesWithMethods(60, "a");
		addUsagesWithMethods(39, "a", "b");
		addUsagesWithMethods(1, "a", "b", "c");

		mOpts.setFeatureDropping(false);

		createBasicNetworkWithPatterns(0.6, 0.39, 0.01);
		addMethodNode("a", 1.0, 1.0, 1.0);
		addMethodNode("b", 0.0, 1.0, 1.0);
		addMethodNode("c", 0.0, 0.0, 1.0);

		assertNetwork();
	}

	@Test
	public void correctResultWithCallSiteFeatureDropping() {
		addUsagesWithMethods(60, "a");
		addUsagesWithMethods(30, "a", "b");
		addUsagesWithMethods(1, "a", "b", "c");
		addUsagesWithMethods(1, "a", "b", "d");
		addUsagesWithMethods(1, "a", "b", "e");
		addUsagesWithMethods(1, "a", "b", "f");
		addUsagesWithMethods(1, "a", "b", "g");
		addUsagesWithMethods(1, "a", "b", "h");
		addUsagesWithMethods(1, "a", "b", "i");
		addUsagesWithMethods(1, "a", "b", "j");
		addUsagesWithMethods(1, "a", "b", "k");
		addUsagesWithMethods(1, "a", "b", "l");

		mOpts.setFeatureDropping(true);

		createBasicNetworkWithPatterns(0.6, 0.4);
		addMethodNode("a", 1.0, 1.0);
		addMethodNode("b", 0.0, 1.0);

		assertNetwork();
	}

	@Test
	public void correctResultWithInClassFeatureDropping() {
		addUsage(60, createUsageInClass("LClassA"));
		addUsage(30, createUsageInClass("LClassB"));
		addUsage(1, createUsageInClass("LClass1"));
		addUsage(1, createUsageInClass("LClass2"));
		addUsage(1, createUsageInClass("LClass3"));
		addUsage(1, createUsageInClass("LClass4"));
		addUsage(1, createUsageInClass("LClass5"));
		addUsage(1, createUsageInClass("LClass6"));
		addUsage(1, createUsageInClass("LClass7"));
		addUsage(1, createUsageInClass("LClass8"));
		addUsage(1, createUsageInClass("LClass9"));
		addUsage(1, createUsageInClass("LClass0"));

		mOpts.setFeatureDropping(true);

		// verify pattern node
		Node pN = sut.learnModel(usages).getNode(PBNModelConstants.PATTERN_TITLE);
		String[] actualStates = pN.getStates();
		String[] expectedStates = new String[] { "p0", "p1", "p2" };
		assertArrayEquals(expectedStates, actualStates);
		double[] expectedProbs = new double[] { 0.6, 0.3, 0.1 };
		double[] actualProbs = pN.getProbabilities();
		assertArrayEquals(expectedProbs, actualProbs, PRECISION);

		// verify inclass node
		Node actual = sut.learnModel(usages).getNode(PBNModelConstants.CLASS_CONTEXT_TITLE);
		Asserts.assertNotNull(actual);
		actualStates = actual.getStates();
		expectedStates = new String[] { "LClassA", "LClassB", "LDummy", "LUnknown" };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	@Test
	public void correctResultWithInMethodFeatureDropping() {
		addUsage(55, createUsageInMethod("LMyClass.mA()V"));
		addUsage(35, createUsageInMethod("LMyClass.mB()V"));
		addUsage(1, createUsageInMethod("LMyClass.m1()V"));
		addUsage(1, createUsageInMethod("LMyClass.m2()V"));
		addUsage(1, createUsageInMethod("LMyClass.m3()V"));
		addUsage(1, createUsageInMethod("LMyClass.m4()V"));
		addUsage(1, createUsageInMethod("LMyClass.m5()V"));
		addUsage(1, createUsageInMethod("LMyClass.m6()V"));
		addUsage(1, createUsageInMethod("LMyClass.m7()V"));
		addUsage(1, createUsageInMethod("LMyClass.m8()V"));
		addUsage(1, createUsageInMethod("LMyClass.m9()V"));
		addUsage(1, createUsageInMethod("LMyClass.m0()V"));

		mOpts.setFeatureDropping(true);

		// verify pattern node
		Node pN = sut.learnModel(usages).getNode(PBNModelConstants.PATTERN_TITLE);
		String[] actualStates = pN.getStates();
		String[] expectedStates = new String[] { "p0", "p1", "p2" };
		assertArrayEquals(expectedStates, actualStates);
		double[] expectedProbs = new double[] { 0.55, 0.35, 0.1 };
		double[] actualProbs = pN.getProbabilities();
		assertArrayEquals(expectedProbs, actualProbs, PRECISION);

		// verify inclass node
		Node actual = sut.learnModel(usages).getNode(PBNModelConstants.METHOD_CONTEXT_TITLE);
		Asserts.assertNotNull(actual);
		actualStates = actual.getStates();
		expectedStates = new String[] { "LMyClass.mA()V", "LMyClass.mB()V", "LDummy.dummy()V", "LUnknown.unknown()V" };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	@Test
	public void correctResultWithDefinitionFeatureDropping() {
		addUsage(50, createUsageWithDefinition("LMyClass.mA()V"));
		addUsage(40, createUsageWithDefinition("LMyClass.mB()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m1()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m2()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m3()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m4()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m5()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m6()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m7()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m8()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m9()V"));
		addUsage(1, createUsageWithDefinition("LMyClass.m0()V"));

		mOpts.setFeatureDropping(true);

		// verify pattern node
		Node pN = sut.learnModel(usages).getNode(PBNModelConstants.PATTERN_TITLE);
		String[] actualStates = pN.getStates();
		String[] expectedStates = new String[] { "p0", "p1", "p2" };
		assertArrayEquals(expectedStates, actualStates);
		double[] expectedProbs = new double[] { 0.5, 0.4, 0.1 };
		double[] actualProbs = pN.getProbabilities();
		assertArrayEquals(expectedProbs, actualProbs, PRECISION);

		// verify inclass node
		Node actual = sut.learnModel(usages).getNode(PBNModelConstants.DEFINITION_TITLE);
		Asserts.assertNotNull(actual);
		actualStates = actual.getStates();
		expectedStates = new String[] { "RETURN:LMyClass.mA()V", "RETURN:LMyClass.mB()V", "RETURN:LDummy.dummy()V",
				DefinitionSites.createUnknownDefinitionSite().toString() };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	private Usage createUsageWithDefinition(String def) {
		Query query = createQuery("a", "b");
		query.setDefinition(DefinitionSites.createDefinitionByReturn(CoReMethodName.get(def)));
		return query;
	}

	private Usage createUsageInMethod(String name) {
		Query query = createQuery("a", "b");
		query.setMethodContext(CoReMethodName.get(name));
		return query;
	}

	private void addUsage(int num, Usage u) {
		for (int i = 0; i < num; i++) {
			usages.add(u);
		}
	}

	private void addUsagesWithMethods(int num, String... methods) {
		Query q = createQuery(methods);
		addUsage(num, q);
	}

	private static Usage createUsageInClass(String inClass) {
		Query query = createQuery("a", "b");
		query.setClassContext(CoReTypeName.get(inClass));
		return query;
	}

	private static Query createQuery(String... methods) {
		Query q = new Query();
		q.setClassContext(CoReTypeName.get("Lmy/Type"));
		q.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		q.setType(CoReTypeName.get("Lfw/Type"));
		q.setMethodContext(CoReMethodName.get("Lmy/Type.doit()V"));

		for (String methodName : methods) {
			q.addCallSite(CallSites.createReceiverCallSite("Lfw/Type." + methodName + "()V"));
		}
		return q;
	}

	private BayesianNetwork createBasicNetworkWithPatterns(double... patternProbabilities) {
		expectedNetwork = new BayesianNetwork();
		patternNode = new Node(PBNModelConstants.PATTERN_TITLE);
		addPatterns(patternProbabilities);

		expectedNetwork.addNode(patternNode);

		Node inClass = new Node(PBNModelConstants.CLASS_CONTEXT_TITLE);
		inClass.setParents(new Node[] { patternNode });
		inClass.setStates(new String[] { "Lmy/Type", "LDummy", "LUnknown" });
		double[] allProbs = new double[patternProbabilities.length * 3];
		for (int i = 0; i < patternProbabilities.length; i++) {
			allProbs[3 * i] = 1.0;
			allProbs[3 * i + 1] = 0.0;
			allProbs[3 * i + 2] = 0.0;
		}
		inClass.setProbabilities(allProbs);
		expectedNetwork.addNode(inClass);

		Node inMethod = new Node(PBNModelConstants.METHOD_CONTEXT_TITLE);
		inMethod.setParents(new Node[] { patternNode });
		inMethod.setStates(new String[] { "Lmy/Type.doit()V", "LDummy.dummy()V", "LUnknown.unknown()V" });
		inMethod.setProbabilities(allProbs);
		expectedNetwork.addNode(inMethod);

		Node def = new Node(PBNModelConstants.DEFINITION_TITLE);
		def.setParents(new Node[] { patternNode });
		def.setStates(new String[] { "UNKNOWN", "RETURN:LDummy.dummy()V" });
		allProbs = new double[patternProbabilities.length * 2];
		for (int i = 0; i < patternProbabilities.length; i++) {
			allProbs[2 * i] = 1.0;
			allProbs[2 * i + 1] = 0.0;
		}
		def.setProbabilities(allProbs);
		expectedNetwork.addNode(def);

		return expectedNetwork;
	}

	private void addPatterns(double... probabilities) {
		String[] states = new String[probabilities.length];
		for (int i = 0; i < probabilities.length; i++) {
			states[i] = "p" + i;
		}
		patternNode.setStates(states);
		patternNode.setProbabilities(probabilities);
	}

	private void addMethodNode(String name, double... probabilities) {
		Node n = new Node("C_Lfw/Type." + name + "()V");
		n.setStates(new String[] { PBNModelConstants.STATE_TRUE, PBNModelConstants.STATE_FALSE });
		double[] allProbs = new double[2 * probabilities.length];
		for (int i = 0; i < probabilities.length; i++) {
			allProbs[2 * i] = probabilities[i];
			allProbs[2 * i + 1] = 1 - probabilities[i];
		}

		n.setProbabilities(allProbs);

		expectedNetwork.addNode(n);

	}

	private void assertNetwork() {
		BayesianNetwork actualNetwork = sut.learnModel(usages);

		int expectedNumOfNodes = expectedNetwork.getNodes().size();
		int actualNumOfNodes = actualNetwork.getNodes().size();
		assertEquals(expectedNumOfNodes, actualNumOfNodes);

		for (Node expectedNode : expectedNetwork.getNodes()) {
			Node actualNode = actualNetwork.getNode(expectedNode.getIdentifier());
			Asserts.assertNotNull(actualNode);

			String[] expectedStates = expectedNode.getStates();
			String[] actualStates = actualNode.getStates();
			String nodeIdentifier = expectedNode.getIdentifier();
			assertArrayEquals(nodeIdentifier, expectedStates, actualStates);

			double[] expectedProbs = expectedNode.getProbabilities();
			double[] actualProbs = actualNode.getProbabilities();
			assertArrayEquals(nodeIdentifier, expectedProbs, actualProbs, PRECISION);
		}
	}
}