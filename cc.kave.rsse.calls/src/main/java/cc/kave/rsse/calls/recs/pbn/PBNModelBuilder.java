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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.CLASS_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DEFINITION_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.METHOD_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.PATTERN_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.STATE_FALSE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.STATE_TRUE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.getTitle;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newCallSite;
import static cc.kave.rsse.calls.utils.DictionaryHelper.UNKNOWN_IN_CLASS;
import static cc.kave.rsse.calls.utils.DictionaryHelper.UNKNOWN_IN_DEFINITION;
import static cc.kave.rsse.calls.utils.DictionaryHelper.UNKNOWN_IN_METHOD;
import static cc.kave.rsse.calls.utils.NetworkMathUtils.ensureAllProbabilitiesInValidRange;
import static cc.kave.rsse.calls.utils.NetworkMathUtils.getProbabilityInMinMaxRange;
import static cc.kave.rsse.calls.utils.NetworkMathUtils.safeDivMaxMin;
import static cc.kave.rsse.calls.utils.NetworkMathUtils.smoothValues;
import static java.lang.System.arraycopy;

import java.util.List;
import java.util.Set;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.utils.DictionaryHelper;
import cc.kave.rsse.calls.utils.NetworkMathUtils;

public class PBNModelBuilder {

	private DictionaryHelper dictionary;
	private List<Pattern> patterns;

	private BayesianNetwork network;
	private Node patternNode;
	private Node classNode;
	private Node methodNode;
	private Node defNode;

	public BayesianNetwork build(List<Pattern> _patterns, Dictionary<IFeature> _dictionary) {
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
		for (Pattern p : patterns) {
			states[i] = p.name;
			double probability = safeDivMaxMin(p.numObservations, numTotalObservations);
			probabilities[i] = NetworkMathUtils.roundToDefaultPrecision(probability);
			i++;
		}

		smoothValues(probabilities);
		ensureAllProbabilitiesInValidRange(probabilities);

		patternNode.setStates(states);
		patternNode.setProbabilities(probabilities);

		network.addNode(patternNode);
	}

	private int sumUpObservations() {
		int num = 0;
		for (Pattern p : patterns) {
			num += p.numObservations;
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

		for (UsageSiteFeature call : dictionary.getUsageSites()) {

			IMethodName methodName = call.site.getMember(IMethodName.class);
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
		// for (ParameterFeature param : dictionary.getParameterSites()) {
		// IMethodName methodName = param.getMethodName();
		// int argNum = param.getArgNum();
		// String title = newParameterSite(methodName, argNum);
		// addBooleanNode(param, title);
		// }
	}

	private void addGenericPropabilities(Node node, Set<IFeature> statesSet, IFeature stateForUnknowns) {

		String[] states = new String[statesSet.size()];
		double[] probabilities = new double[patterns.size() * statesSet.size()];

		int i = 0;
		for (IFeature f : statesSet) {
			final String state = getTitle(f);
			Asserts.assertNotNull(state);
			states[i++] = state;
		}

		int j = 0;
		for (Pattern pattern : patterns) {

			double sumOfProbs = 0.0;

			double[] subprobs = new double[statesSet.size()];
			int k = 0;
			for (IFeature state : statesSet) {
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

			smoothValues(subprobs);
			arraycopy(subprobs, 0, probabilities, j, statesSet.size());

			j += statesSet.size();
		}

		// ensureAllProbabilitiesInValidRange(probabilities);

		node.setStates(states);
		node.setProbabilities(probabilities);
	}

	private int findIndex(Set<IFeature> statesSet, IFeature stateForUnknowns) {
		int i = 0;
		for (IFeature f : statesSet) {
			if (f.equals(stateForUnknowns)) {
				return i;
			}
			i++;
		}
		Asserts.fail("unknown state not found");
		return -1;
	}

	private void addBooleanNode(IFeature feature, String title) {

		Node node = new Node(title);
		node.setParents(new Node[] { patternNode });
		network.addNode(node);

		node.setStates(new String[] { STATE_TRUE, STATE_FALSE });

		double[] probabilities = new double[2 * patterns.size()];

		int i = 0;
		for (Pattern pattern : patterns) {
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