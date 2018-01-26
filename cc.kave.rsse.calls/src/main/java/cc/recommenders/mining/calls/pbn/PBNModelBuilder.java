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

import static cc.recommenders.mining.calls.NetworkMathUtils.ensureAllProbabilitiesInValidRange;
import static cc.recommenders.mining.calls.NetworkMathUtils.getProbabilityInMinMaxRange;
import static cc.recommenders.mining.calls.NetworkMathUtils.safeDivMaxMin;
import static cc.recommenders.mining.calls.NetworkMathUtils.scaleMaximalValue;
import static cc.recommenders.mining.calls.pbn.DictionaryHelper.UNKNOWN_IN_CLASS;
import static cc.recommenders.mining.calls.pbn.DictionaryHelper.UNKNOWN_IN_DEFINITION;
import static cc.recommenders.mining.calls.pbn.DictionaryHelper.UNKNOWN_IN_METHOD;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.CLASS_CONTEXT_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.DEFINITION_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.METHOD_CONTEXT_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.PATTERN_TITLE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.STATE_FALSE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.STATE_TRUE;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.getTitle;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newCallSite;
import static cc.recommenders.mining.calls.pbn.PBNModelConstants.newParameterSite;
import static java.lang.System.arraycopy;

import java.util.List;
import java.util.Set;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.commons.bayesnet.Node;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.datastructures.Dictionary;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.NetworkMathUtils;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.UsageFeature;

public class PBNModelBuilder implements ModelBuilder<UsageFeature, BayesianNetwork> {

	private DictionaryHelper dictionary;
	private List<Pattern<UsageFeature>> patterns;

	private BayesianNetwork network;
	private Node patternNode;
	private Node classNode;
	private Node methodNode;
	private Node defNode;

	@Override
	public BayesianNetwork build(List<Pattern<UsageFeature>> _patterns, Dictionary<UsageFeature> _dictionary) {
		patterns = _patterns;
		dictionary = new DictionaryHelper(_dictionary);

		ensureValidData();
		createNetwork();

		return network;
	}

	private void ensureValidData() {
		dictionary.addDummyStatesToEnsureAtLeastTwoStatesPerNode();
		ensureAtLeastTwoPatternsExist();
	}

	private void ensureAtLeastTwoPatternsExist() {
		int numPatterns = patterns.size();
		Asserts.assertGreaterOrEqual(numPatterns, 1);

		if (numPatterns < 2) {
			patterns.add(patterns.get(0).clone("other"));
		}
	}

	private void createNetwork() {
		network = new BayesianNetwork();

		createPatternNode();
		createClassContextNode();
		createMethodContextNode();
		createDefinitionNode();
		createCallNodes();
		createParameterNodes();
	}

	private void createPatternNode() {
		patternNode = new Node(PATTERN_TITLE);

		int numTotalObservations = sumUpObservations();
		String[] states = new String[patterns.size()];
		double[] probabilities = new double[patterns.size()];

		int i = 0;
		for (Pattern<UsageFeature> p : patterns) {
			states[i] = p.getName();
			double probability = safeDivMaxMin(p.getNumberOfObservations(), numTotalObservations);
			probabilities[i] = NetworkMathUtils.roundToDefaultPrecision(probability);
			i++;
		}

		scaleMaximalValue(probabilities);
		ensureAllProbabilitiesInValidRange(probabilities);

		patternNode.setStates(states);
		patternNode.setProbabilities(probabilities);

		network.addNode(patternNode);
	}

	private int sumUpObservations() {
		int num = 0;
		for (Pattern<UsageFeature> p : patterns) {
			num += p.getNumberOfObservations();
		}
		return num;
	}

	private void createClassContextNode() {
		classNode = createNodeAndAddToNetwork(CLASS_CONTEXT_TITLE);
		addGenericPropabilities(classNode, dictionary.getClassContexts(), UNKNOWN_IN_CLASS);
	}

	private void createMethodContextNode() {
		methodNode = createNodeAndAddToNetwork(METHOD_CONTEXT_TITLE);
		addGenericPropabilities(methodNode, dictionary.getMethodContexts(), UNKNOWN_IN_METHOD);
	}

	private Node createNodeAndAddToNetwork(String nodeTitle) {
		Node node = new Node(nodeTitle);
		node.setParents(new Node[] { patternNode });
		network.addNode(node);
		return node;
	}

	private void createDefinitionNode() {
		defNode = createNodeAndAddToNetwork(DEFINITION_TITLE);
		addGenericPropabilities(defNode, dictionary.getDefinitions(), UNKNOWN_IN_DEFINITION);
	}

	private void createCallNodes() {

		for (CallFeature call : dictionary.getCallSites()) {

			ICoReMethodName methodName = call.getMethodName();
			// TODO re-enable rebasing to fix test (here and in
			// UsageRecommender)
			// ITypeName baseType = dictionary.getType();
			// if (!baseType.equals(methodName.getDeclaringType())) {
			// methodName = VmMethodName.rebase(baseType, methodName);
			// }
			// String title = newCallSite(rebase(baseType, methodName));
			String title = newCallSite(methodName);

			addBooleanNode(call, title);
		}
	}

	private void createParameterNodes() {
		for (ParameterFeature param : dictionary.getParameterSites()) {
			ICoReMethodName methodName = param.getMethodName();
			int argNum = param.getArgNum();
			String title = newParameterSite(methodName, argNum);
			addBooleanNode(param, title);
		}
	}

	private void addGenericPropabilities(Node node, Set<UsageFeature> statesSet, UsageFeature stateForUnknowns) {

		String[] states = new String[statesSet.size()];
		double[] probabilities = new double[patterns.size() * statesSet.size()];

		int i = 0;
		for (UsageFeature f : statesSet) {
			final String state = getTitle(f);
			Asserts.assertNotNull(state);
			states[i++] = state;
		}

		int j = 0;
		for (Pattern<UsageFeature> pattern : patterns) {

			double sumOfProbs = 0.0;

			double[] subprobs = new double[statesSet.size()];
			int k = 0;
			for (UsageFeature state : statesSet) {
				double probability = pattern.getProbability(state);

				probability = getProbabilityInMinMaxRange(probability);
				probability = NetworkMathUtils.roundToDefaultPrecision(probability);

				subprobs[k++] = probability;
				sumOfProbs += probability;
			}

			boolean isSumOfProbabilitiesToSmall = sumOfProbs < 1;
			if (isSumOfProbabilitiesToSmall) {
				// then add it to unknown
				double diff = Math.max(0, 1 - sumOfProbs);
				int idx = findIndex(statesSet, stateForUnknowns);
				// TODO write test case for version without call to
				// "getProbabilityInMinMaxRange", should fail, when all contexts
				// are dropped from a pattern and only the two dummy contexts
				// are set in the network
				// subprobs[idx] = getProbabilityInMinMaxRange(subprobs[idx] +
				// diff);
				subprobs[idx] = subprobs[idx] + diff;
			}

			scaleMaximalValue(subprobs);
			arraycopy(subprobs, 0, probabilities, j, statesSet.size());

			j += statesSet.size();
		}

		// ensureAllProbabilitiesInValidRange(probabilities);

		node.setStates(states);
		node.setProbabilities(probabilities);
	}

	private int findIndex(Set<UsageFeature> statesSet, UsageFeature stateForUnknowns) {
		int i = 0;
		for (UsageFeature f : statesSet) {
			if (f.equals(stateForUnknowns)) {
				return i;
			}
			i++;
		}
		Asserts.fail("unknown state not found");
		return -1;
	}

	private void addBooleanNode(UsageFeature feature, String title) {

		Node node = new Node(title);
		node.setParents(new Node[] { patternNode });
		network.addNode(node);

		node.setStates(new String[] { STATE_TRUE, STATE_FALSE });

		double[] probabilities = new double[2 * patterns.size()];

		int i = 0;
		for (Pattern<UsageFeature> pattern : patterns) {
			double probability = pattern.getProbability(feature);

			probability = getProbabilityInMinMaxRange(probability);
			probability = NetworkMathUtils.roundToDefaultPrecision(probability);

			probabilities[i++] = probability;
			probabilities[i++] = 1 - probability;
		}

		ensureAllProbabilitiesInValidRange(probabilities);

		node.setProbabilities(probabilities);
	}
}