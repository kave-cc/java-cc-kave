/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls.pbn;

import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.CALL1;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.CALL2;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.CALL_WITH_DIFFERENT_TYPE;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.DEF1;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.DEF2;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.METHOD1;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.METHOD2;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.PARAM1;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.PARAM1_ARGNUM;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.PARAM2;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.PARAM2_ARGNUM;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.SUPERCLASS1;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.SUPERCLASS2;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.TYPE;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.assertBooleanNode;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.assertNodeExists;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.assertNodesExist;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.assertProbabilities;
import static cc.recommenders.mining.calls.pbn.PBNModelBuilderFixture.assertStates;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.CALL_PREFIX;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.CLASS_CONTEXT_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DEFINITION_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_DEFINITION;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_METHOD;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DUMMY_TYPE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.METHOD_CONTEXT_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.PARAMETER_PREFIX;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.PATTERN_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.UNKNOWN_DEFINITION;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.UNKNOWN_METHOD;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.UNKNOWN_TYPE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newDefinition;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newMethodContext;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.commons.bayesnet.Node;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.mining.calls.pbn.PBNModelBuilder;
import cc.recommenders.mining.calls.pbn.PBNModelConstants;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.UsageFeature;

public class PBNModelBuilderTest {

	private PBNModelBuilderFixture fix;
	private PBNModelBuilder sut;

	private BayesianNetwork network;
	private Dictionary<UsageFeature> dictionary;
	private List<Pattern<UsageFeature>> patterns;

	@Before
	public void setup() {
		fix = new PBNModelBuilderFixture();
		sut = new PBNModelBuilder();

		dictionary = fix.getDictionary();
		patterns = fix.getPatterns();
	}

	@Test
	public void dummyStatesAreAddedToDictionary() {
		network = sut.build(patterns, dictionary);

		assertTrue(dictionary.contains(new ClassFeature(PBNModelConstants.DUMMY_TYPE)));
		assertTrue(dictionary.contains(new FirstMethodFeature(PBNModelConstants.DUMMY_METHOD)));
		assertTrue(dictionary.contains(new DefinitionFeature(UNKNOWN_DEFINITION)));
		assertTrue(dictionary.contains(new DefinitionFeature(PBNModelConstants.DUMMY_DEFINITION)));
	}

	@Test(expected = RuntimeException.class)
	public void crashesIfNoPatternIsProvided() {
		patterns = newArrayList();
		network = sut.build(patterns, dictionary);
	}

	@Test
	public void secondPatternIsCreatedIfOnlyOneIsProvided() {
		dictionary = fix.getDictionaryForSinglePattern();
		patterns = fix.getSinglePattern();
		network = sut.build(patterns, dictionary);

		Pattern<UsageFeature> lastPattern = patterns.get(patterns.size() - 1);
		assertTrue(lastPattern.getName().equals("other"));
	}

	@Test
	public void secondPatternIsOnlyCreatedIfOnlyOneIsProvided() {
		network = sut.build(patterns, dictionary);

		Pattern<UsageFeature> lastPattern = patterns.get(patterns.size() - 1);
		assertFalse(lastPattern.getName().equals("other"));
	}

	@Test
	public void aBayesianNetworkIsReturned() {
		network = sut.build(patterns, dictionary);

		assertNotNull(network);
	}

	@Test
	public void patternNodeExists() {
		network = sut.build(patterns, dictionary);

		assertNodeExists(network, PATTERN_TITLE);
	}

	@Test
	public void patternNodeHasParent() {
		network = sut.build(patterns, dictionary);

		Node pattern = network.getNode(PATTERN_TITLE);
		Node[] actual = pattern.getParents();
		Node[] expected = new Node[] {};

		assertArrayEquals(expected, actual);
	}

	@Test
	public void patternNodeHasCorrectStates() {
		network = sut.build(patterns, dictionary);

		Node pattern = network.getNode(PATTERN_TITLE);
		String[] actual = pattern.getStates();
		String[] expected = new String[] { "p1", "p2" };
		assertArrayEquals(expected, actual);
	}

	@Test
	public void patternNodeHasCorrectProbabilities() {
		network = sut.build(patterns, dictionary);
		assertProbabilities(network, PATTERN_TITLE, 0.5, 0.5);
	}

	@Test
	public void classContextNodeExists() {
		network = sut.build(patterns, dictionary);

		assertNodeExists(network, PBNModelConstants.CLASS_CONTEXT_TITLE);
	}

	@Test
	public void classContextNodeHasParent() {
		network = sut.build(patterns, dictionary);

		Node classContext = network.getNode(CLASS_CONTEXT_TITLE);
		Node[] actual = classContext.getParents();
		Node[] expected = new Node[] { network.getNode(PBNModelConstants.PATTERN_TITLE) };

		assertArrayEquals(expected, actual);
	}

	@Test
	public void classContextNodeHasCorrectStates() {
		network = sut.build(patterns, dictionary);

		Node classContext = network.getNode(CLASS_CONTEXT_TITLE);
		String[] actual = classContext.getStates();
		String[] expected = new String[] { SUPERCLASS1.toString(), SUPERCLASS2.toString(), DUMMY_TYPE.toString(), UNKNOWN_TYPE.toString() };

		assertArrayEquals(expected, actual);
	}

	@Test
	public void classContextNodeHasCorrectProbabilities() {
		network = sut.build(patterns, dictionary);
		assertProbabilities(network, CLASS_CONTEXT_TITLE, 0.99999, 1E-5, 1E-5, 1E-5, 1E-5, 0.99999, 1E-5, 1E-5);
	}

	@Test
	public void methodContextNodeExists() {
		network = sut.build(patterns, dictionary);

		assertNodeExists(network, PBNModelConstants.METHOD_CONTEXT_TITLE);
	}

	@Test
	public void methodContextNodeHasCorrectStates() {
		network = sut.build(patterns, dictionary);

		assertStates(network, PBNModelConstants.METHOD_CONTEXT_TITLE, newMethodContext(METHOD1),
				newMethodContext(METHOD2), newMethodContext(DUMMY_METHOD), newMethodContext(UNKNOWN_METHOD));
	}

	@Test
	public void methodContextNodeHasCorrectProbabilities() {
		network = sut.build(patterns, dictionary);

		assertProbabilities(network, METHOD_CONTEXT_TITLE, 0.99999, 1E-5, 1E-5, 1E-5, 1E-5, 0.99999, 1E-5, 1E-5);
	}

	@Test
	public void definitionNodeExists() {
		network = sut.build(patterns, dictionary);

		assertNodeExists(network, PBNModelConstants.DEFINITION_TITLE);
	}

	@Test
	public void definitionNodeHasCorrectStates() {
		network = sut.build(patterns, dictionary);

		assertStates(network, DEFINITION_TITLE, newDefinition(DEF1), newDefinition(DEF2),
				newDefinition(DUMMY_DEFINITION), newDefinition(UNKNOWN_DEFINITION));
	}

	@Test
	public void definitionNodeHasCorrectProbabilities() {
		network = sut.build(patterns, dictionary);

		assertProbabilities(network, DEFINITION_TITLE, 0.99999, 1E-5, 1E-5, 1E-5, 1E-5, 0.99999, 1E-5, 1E-5);
	}

	@Test
	public void callNodesExist() {
		network = sut.build(patterns, dictionary);

		String call1 = CALL_PREFIX + CALL1;
		String call2 = CALL_PREFIX + CALL2;
		assertNodesExist(network, call1, call2);
	}

	@Test
	public void callNodesHaveCorrectStates() {
		network = sut.build(patterns, dictionary);

		String call1 = CALL_PREFIX + CALL1;
		assertBooleanNode(network, call1);
		String call2 = CALL_PREFIX + CALL2;
		assertBooleanNode(network, call2);
	}

	@Test
	public void callNodesHaveCorrectProbability() {
		network = sut.build(patterns, dictionary);

		String call1 = CALL_PREFIX + CALL1;
		assertProbabilities(network, call1, 0.99999, 1e-5, 1e-5, 0.99999);

		String call2 = CALL_PREFIX + CALL2;
		assertProbabilities(network, call2, 1e-5, 0.99999, 0.99999, 1e-5);
	}

	@Test
	@Ignore
	public void allMethodsAreRebased() {
		fail("to fix that test re-enable rebasing in model builder and recommender");
		dictionary = fix.getDictionaryForSinglePattern();
		patterns = fix.getSinglePattern();

		CallFeature feature = new CallFeature(CALL_WITH_DIFFERENT_TYPE);
		dictionary.add(feature);
		patterns.get(0).setProbability(feature, 1.0);

		network = sut.build(patterns, dictionary);
		for (Node node : network.getNodes()) {
			if (node.getIdentifier().startsWith(CALL_PREFIX)) {
				assertTrue(node.getIdentifier().startsWith(CALL_PREFIX + TYPE));
			}
		}
	}

	@Test
	public void parameterNodesExist() {
		network = sut.build(patterns, dictionary);

		String param1 = PARAMETER_PREFIX + PARAM1 + "#" + PARAM1_ARGNUM;
		String param2 = PARAMETER_PREFIX + PARAM2 + "#" + PARAM2_ARGNUM;
		assertNodesExist(network, param1, param2);
	}

	@Test
	public void parameterNodesHaveCorrectStates() {
		network = sut.build(patterns, dictionary);

		String param1 = PARAMETER_PREFIX + PARAM1 + "#" + PARAM1_ARGNUM;
		assertBooleanNode(network, param1);
		String param2 = PARAMETER_PREFIX + PARAM2 + "#" + PARAM2_ARGNUM;
		assertBooleanNode(network, param2);
	}

	@Test
	public void parameterNodesHaveCorrectProbabilities() {
		network = sut.build(patterns, dictionary);

		String param1 = PARAMETER_PREFIX + PARAM1 + "#" + PARAM1_ARGNUM;
		assertProbabilities(network, param1, 0.99999, 1e-5, 1e-5, 0.99999);

		String param2 = PARAMETER_PREFIX + PARAM2 + "#" + PARAM2_ARGNUM;
		assertProbabilities(network, param2, 1e-5, 0.99999, 0.99999, 1e-5);
	}

	@Test
	public void correctNumberOfNodesIsCreated() {
		network = sut.build(patterns, dictionary);

		int actual = network.getNodes().size();
		int expected = 8; // patterns, classContext, methodContext, definition,
							// 2*call, 2* param

		assertEquals(expected, actual);
	}
}