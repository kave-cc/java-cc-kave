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
package cc.kave.rsse.calls.recs.pbn;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.rsse.calls.mining.DictionaryBuilder;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.FeatureWeighter;
import cc.kave.rsse.calls.mining.MiningOptions;
import cc.kave.rsse.calls.mining.OptionAwareFeatureFilter;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.mining.VectorBuilder;
import cc.kave.rsse.calls.mining.MiningOptions.DistanceMeasure;
import cc.kave.rsse.calls.mining.clustering.DistanceMeasureFactory;
import cc.kave.rsse.calls.mining.clustering.PatternFinderFactory;
import cc.kave.rsse.calls.model.usages.IUsage;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.model.usages.impl.UsageSites;
import cc.kave.rsse.calls.recs.pbn.BayesianNetwork;
import cc.kave.rsse.calls.recs.pbn.Node;
import cc.kave.rsse.calls.recs.pbn.PBNMiner;
import cc.kave.rsse.calls.recs.pbn.PBNModelBuilder;
import cc.kave.rsse.calls.recs.pbn.PBNModelConstants;

public class PBNMinerIntegrationTest {

	private static final double PRECISION = 0.00001;
	
	private FeatureExtractor featureExtractor;
	private DictionaryBuilder dictionaryBuilder;
	private PatternFinderFactory patternFinderFactory;
	private PBNModelBuilder modelBuilder;
	private MiningOptions mOpts;
	private QueryOptions qOpts;
	private QueryOptions queryOptions;

	private PBNMiner sut;
	private List<IUsage> usages;
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
		featureExtractor = new FeatureExtractor(mOpts);
		modelBuilder = new PBNModelBuilder();

		dictionaryBuilder = new DictionaryBuilder(mOpts, qOpts);
		FeatureWeighter weighter = new FeatureWeighter(mOpts);
		patternFinderFactory = new PatternFinderFactory(weighter, new VectorBuilder(weighter), mOpts,
				new DistanceMeasureFactory(mOpts));

		sut = new PBNMiner(featureExtractor, dictionaryBuilder, patternFinderFactory, modelBuilder, queryOptions, mOpts,
				new OptionAwareFeatureFilter(qOpts));

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
		addUsage(60, createUsageInClass("ClassA"));
		addUsage(30, createUsageInClass("ClassB"));
		addUsage(1, createUsageInClass("Class1"));
		addUsage(1, createUsageInClass("Class2"));
		addUsage(1, createUsageInClass("Class3"));
		addUsage(1, createUsageInClass("Class4"));
		addUsage(1, createUsageInClass("Class5"));
		addUsage(1, createUsageInClass("Class6"));
		addUsage(1, createUsageInClass("Class7"));
		addUsage(1, createUsageInClass("Class8"));
		addUsage(1, createUsageInClass("Class9"));
		addUsage(1, createUsageInClass("Class0"));

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
		expectedStates = new String[] { "ClassA", "ClassB", "__DUMMY__, ???", "?" };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	@Test
	public void correctResultWithInMethodFeatureDropping() {
		addUsage(55, createUsageInMethod("[p:void] [MyClass, P].mA()"));
		addUsage(35, createUsageInMethod("[p:void] [MyClass, P].mB()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m1()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m2()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m3()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m4()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m5()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m6()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m7()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m8()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m9()"));
		addUsage(1, createUsageInMethod("[p:void] [MyClass, P].m0()"));

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
		expectedStates = new String[] { "[p:void] [MyClass, P].mA()", "[p:void] [MyClass, P].mB()",
				"[?] [__DUMMY__, ???].???()", "[?] [?].???()" };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	@Test
	public void correctResultWithDefinitionFeatureDropping() {
		addUsage(50, createUsageWithDefinition("[p:void] [MyClass, P].mA()"));
		addUsage(40, createUsageWithDefinition("[p:void] [MyClass, P].mB()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m1()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m2()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m3()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m4()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m5()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m6()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m7()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m8()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m9()"));
		addUsage(1, createUsageWithDefinition("[p:void] [MyClass, P].m0()"));

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
		expectedStates = new String[] { "RETURN:[p:void] [MyClass, P].mA()", "RETURN:[p:void] [MyClass, P].mB()",
				"RETURN:[?] [__DUMMY__, ???].???()", Definitions.definedByUnknown().toString() };
		assertArrayEquals(expectedStates, actualStates);

		actualProbs = actual.getProbabilities();
		expectedProbs = new double[] { 1.0, 0.0, 0.0, 0.0, /* */0.0, 1.0, 0.0, 0.0, /* */0.0, 0.0, 0.0, 1.0 };
		assertArrayEquals(actualProbs, expectedProbs, PRECISION);
	}

	private IUsage createUsageWithDefinition(String def) {
		Usage query = createQuery("a", "b");
		query.definition = Definitions.definedByReturnValue(Names.newMethod(def));
		return query;
	}

	private IUsage createUsageInMethod(String name) {
		Usage query = createQuery("a", "b");
		query.methodCtx = Names.newMethod(name);
		return query;
	}

	private void addUsage(int num, IUsage u) {
		for (int i = 0; i < num; i++) {
			usages.add(u);
		}
	}

	private void addUsagesWithMethods(int num, String... methods) {
		Usage q = createQuery(methods);
		addUsage(num, q);
	}

	private static IUsage createUsageInClass(String inClass) {
		Usage query = createQuery("a", "b");
		query.classCtx = Names.newType(inClass);
		return query;
	}

	private static Usage createQuery(String... methods) {
		Usage q = new Usage();
		q.classCtx = Names.newType("S, P");
		q.definition = Definitions.definedByUnknown();
		q.type = Names.newType("T, P");
		q.methodCtx = Names.newMethod("[p:void] [T, P].ctx()");

		for (String methodName : methods) {
			q.usageSites.add(UsageSites.call("[p:void] [T, P]." + methodName + "()"));
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
		inClass.setStates(new String[] { "S, P", "__DUMMY__, ???", "?" });
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
		inMethod.setStates(new String[] { "[p:void] [T, P].ctx()", "[?] [__DUMMY__, ???].???()", "[?] [?].???()" });
		inMethod.setProbabilities(allProbs);
		expectedNetwork.addNode(inMethod);

		Node def = new Node(PBNModelConstants.DEFINITION_TITLE);
		def.setParents(new Node[] { patternNode });
		def.setStates(new String[] { "UNKNOWN", "RETURN:[?] [__DUMMY__, ???].???()" });
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
		String id = String.format("C:[p:void] [T, P].%s()", name);
		Node n = new Node(id);
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