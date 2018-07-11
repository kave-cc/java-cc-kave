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
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.commons.assertions.Asserts.assertEquals;
import static cc.kave.commons.assertions.Asserts.assertNotNull;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.repackaged.jayes.BayesNet;
import cc.kave.repackaged.jayes.BayesNode;
import cc.kave.repackaged.jayes.inference.junctionTree.JunctionTreeAlgorithm;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.usages.IUsage;

public class PBNRecommenderInstance {

	private PBNModel m;
	private Options o;

	private BayesNet bayesNet;
	private BayesNode patternNode;
	private BayesNode classContextNode;
	private BayesNode methodContextNode;
	private BayesNode definitionNode;
	private Map<String, BayesNode> paramNodes = newHashMap();
	// TODO ...

	private JunctionTreeAlgorithm junctionTreeAlgorithm;

	private Set<IMemberName> queriedMembers = newHashSet();

	public PBNRecommenderInstance(PBNModel m, Options o) {
		this.m = m;
		this.o = o;
		// initializeNodes(m);
		assertEquals("pbn", o.approachName, "non PBN options");
	}

	private void initializeNodes(PBNModel m) {

		junctionTreeAlgorithm = new JunctionTreeAlgorithm();
		// TODO if opts != useDouble
		// junctionTreeAlgorithm.getFactory().setFloatingPointType(float.class);
		junctionTreeAlgorithm.setNetwork(bayesNet);

		bayesNet = new BayesNet();

		// pattern node
		// patternNode = bayesNet.createNode(PBNModelConstants.PATTERN_TITLE);
		String[] outcomes = new String[m.patternProbabilities.length];
		for (int i = 0; i < outcomes.length; i++) {
			outcomes[i] = "p" + i;
		}
		patternNode.setProbabilities(m.patternProbabilities);

		// for (final Node node : network.getNodes()) {
		// BayesNode bayesNode = createNodeFrom(node);
		// assignToClassMember(node, bayesNode);
		// }
	}

	public Set<Pair<IMemberName, Double>> query(IUsage u) {
		assertNotNull(u, "query cannot be null");
		assertEquals(m.type, u.getType(), "incorrect type");
		return new HashSet<>();
	}

	//
	//
	// private void createNetwork() {
	// network = new BayesianNetwork();
	//
	// createPatternNode();
	// createClassContextNode();
	// createMethodContextNode();
	// createDefinitionNode();
	// createCallNodes();
	// createParameterNodes();
	// }
	//
	// private void createPatternNode() {
	// // patternNode = new Node(PATTERN_TITLE);
	//
	// int numTotalObservations = sumUpObservations();
	// String[] states = new String[patterns.size()];
	// double[] probabilities = new double[patterns.size()];
	//
	// int i = 0;
	// for (Pattern p : patterns) {
	// states[i] = "p" + i;
	// double probability = safeDivMaxMin(p.numObservations, numTotalObservations);
	// probabilities[i] = NetworkMathUtils.roundToDefaultPrecision(probability);
	// i++;
	// }
	//
	// smoothValues(probabilities);
	// ensureAllProbabilitiesInValidRange(probabilities);
	//
	// patternNode.setStates(states);
	// patternNode.setProbabilities(probabilities);
	//
	// network.addNode(patternNode);
	// }
	//
	// private int sumUpObservations() {
	// int num = 0;
	// for (Pattern p : patterns) {
	// num += p.numObservations;
	// }
	// return num;
	// }
	//
	// private void createClassContextNode() {
	// // classNode = createNodeAndAddToNetwork(CLASS_CONTEXT_TITLE);
	// // addGenericPropabilities(classNode, dictionary.getClassContexts(),
	// // UNKNOWN_IN_CLASS);
	// }
	//
	// private void createMethodContextNode() {
	// // methodNode = createNodeAndAddToNetwork(METHOD_CONTEXT_TITLE);
	// // addGenericPropabilities(methodNode, dictionary.getMethodContexts(),
	// // UNKNOWN_IN_METHOD);
	// }
	//
	// private Node createNodeAndAddToNetwork(String nodeTitle) {
	// Node node = new Node(nodeTitle);
	// node.setParents(new Node[] { patternNode });
	// network.addNode(node);
	// return node;
	// }
	//
	// private void createDefinitionNode() {
	// // defNode = createNodeAndAddToNetwork(DEFINITION_TITLE);
	// // addGenericPropabilities(defNode, dictionary.getDefinitions(),
	// // UNKNOWN_IN_DEFINITION);
	// }
	//
	// private void createCallNodes() {
	//
	// Set<MemberAccessFeature> calls =
	// dictionary.getAllEntries(MemberAccessFeature.class);
	// for (MemberAccessFeature call : calls) {
	//
	// String title = "XX";// newCallSite(call.memberAccess.getMember());
	//
	// addBooleanNode(call, title);
	// }
	// }
	//
	// private void createParameterNodes() {
	// // for (ParameterFeature param : dictionary.getParameterSites()) {
	// // IMethodName methodName = param.getMethodName();
	// // int argNum = param.getArgNum();
	// // String title = newParameterSite(methodName, argNum);
	// // addBooleanNode(param, title);
	// // }
	// }
	//
	// private void addGenericPropabilities(Node node, Set<IFeature> statesSet,
	// IFeature stateForUnknowns) {
	//
	// String[] states = new String[statesSet.size()];
	// double[] probabilities = new double[patterns.size() * statesSet.size()];
	//
	// int i = 0;
	// for (IFeature f : statesSet) {
	// final String state = "XX";// getTitle(f);
	// Asserts.assertNotNull(state);
	// states[i++] = state;
	// }
	//
	// int j = 0;
	// for (Pattern pattern : patterns) {
	//
	// double sumOfProbs = 0.0;
	//
	// double[] subprobs = new double[statesSet.size()];
	// int k = 0;
	// for (IFeature state : statesSet) {
	// double probability = pattern.getProbability(state);
	//
	// probability = getProbabilityInMinMaxRange(probability);
	// // probability = NetworkMathUtils.roundToDefaultPrecision(probability);
	//
	// subprobs[k++] = probability;
	// sumOfProbs += probability;
	// }
	//
	// boolean isSumOfProbabilitiesToSmall = sumOfProbs < 1;
	// if (isSumOfProbabilitiesToSmall) {
	// // then add it to unknown
	// double diff = Math.max(0, 1 - sumOfProbs);
	// int idx = findIndex(statesSet, stateForUnknowns);
	// subprobs[idx] = subprobs[idx] + diff;
	// }
	//
	// smoothValues(subprobs);
	// arraycopy(subprobs, 0, probabilities, j, statesSet.size());
	//
	// j += statesSet.size();
	// }
	//
	// // ensureAllProbabilitiesInValidRange(probabilities);
	//
	// node.setStates(states);
	// node.setProbabilities(probabilities);
	// }
	//
	// private int findIndex(Set<IFeature> statesSet, IFeature stateForUnknowns) {
	// int i = 0;
	// for (IFeature f : statesSet) {
	// if (f.equals(stateForUnknowns)) {
	// return i;
	// }
	// i++;
	// }
	// Asserts.fail("unknown state not found");
	// return -1;
	// }
	//
	// private void addBooleanNode(IFeature feature, String title) {
	//
	// Node node = new Node(title);
	// node.setParents(new Node[] { patternNode });
	// network.addNode(node);
	//
	// node.setStates(new String[] { STATE_TRUE, STATE_FALSE });
	//
	// double[] probabilities = new double[2 * patterns.size()];
	//
	// int i = 0;
	// for (Pattern pattern : patterns) {
	// double probability = pattern.getProbability(feature);
	//
	// probability = getProbabilityInMinMaxRange(probability);
	// probability = NetworkMathUtils.roundToDefaultPrecision(probability);
	//
	// probabilities[i++] = probability;
	// probabilities[i++] = 1 - probability;
	// }
	//
	// ensureAllProbabilitiesInValidRange(probabilities);
	//
	// node.setProbabilities(probabilities);
	// }
	//
	// private void initializeArcs(PBNModel m) {
	// // ..?
	// // Node[] parents = node.getParents();
	// // BayesNode children = bayesNet.getNode(node.getIdentifier());
	// // List<BayesNode> bnParents = newArrayList();
	// // for (int i = 0; i < parents.length; i++) {
	// // String parentTitle = parents[i].getIdentifier();
	// // bnParents.add(bayesNet.getNode(parentTitle));
	// // }
	// // children.setParents(bnParents);
	// }
	//
	// private void initializeProbabilities(PBNModel m) {
	// // for (final Node node : network.getNodes()) {
	// // final BayesNode bayesNode = bayesNet.getNode(node.getIdentifier());
	// // bayesNode.setProbabilities(node.getProbabilities());
	// // }
	// }
	//
	// protected double[] getBeliefs(BayesNode node) {
	// return null;// this.junctionTreeAlgorithm.getBeliefs(node);
	// }
	//
	// private BayesNode createNodeFrom(Object node) {
	// BayesNode bayesNode = bayesNet.createNode("... id ...");
	// String[] states = null;//
	// for (int i = 0; i < states.length; i++) {
	// try {
	// bayesNode.addOutcome(states[i]);
	// } catch (IllegalArgumentException e) {
	// Logger.err("error when adding outcome %s: %s", states[i], e.getMessage());
	// }
	// }
	// return bayesNode;
	// }
	//
	// protected void clearEvidence() {
	// junctionTreeAlgorithm.setEvidence(new HashMap<BayesNode, String>());
	// // queriedMethods.clear();
	// }
	//
	// private void addEvidenceIfAvailableInNetwork(BayesNode node, String outcome)
	// {
	// if (node.getOutcomes().contains(outcome)) {
	// junctionTreeAlgorithm.addEvidence(node, outcome);
	// // debug("outcome marked '%s'", node.getName());
	// } else {
	// Logger.debug("unknown outcome: %s (%s)", outcome, node.getName());
	// }
	// }
	//
	// private void markRebasedSite(ITypeName type, IMemberAccess site) {
	// switch (site.getType()) {
	// // case CALL_PARAMETER:
	// // if (options.useParams()) {
	// // String nodeTitle = newParameterSite((IMethodName) site.getMember(),
	// // site.getArgIndex());
	// // BayesNode node = paramNodes.get(nodeTitle);
	// // if (node != null) {
	// // junctionTreeAlgorithm.addEvidence(node, STATE_TRUE);
	// // // debug("outcome marked 'parameter'");
	// // } else {
	// // debug("unknown node: %s (%s)", nodeTitle, type);
	// // }
	// // }
	// // break;
	// case METHOD_CALL:
	// // TODO re-enable rebasing (here and in modelBuilder)
	// // IMethodName rebasedName = rebase(type, site.targetMethod);
	// // BayesNode node = callNodes.get(rebasedName);
	//
	// // it is not necessary to call OUMC.newCallSite(...), because the
	// // prefix is already stripped in that map (see
	// // assignToClassMember())
	// // BayesNode node = callNodes.get((IMethodName) site.getMember());
	// // if (node != null) {
	// // // queriedMethods.add(rebasedName);
	// // queriedMethods.add((IMethodName) site.getMember());
	// // junctionTreeAlgorithm.addEvidence(node, STATE_TRUE);
	// // // debug("outcome marked 'method call'");
	// // } else {
	// // debug("unknown node: %S%s (%s)", CALL_PREFIX, (IMethodName)
	// site.getMember(),
	// // type);
	// // }
	// break;
	// }
	// }
	//
	// private Set<Pair<IMemberName, Double>> collectCallProbabilities() {
	// Set<Pair<IMemberName, Double>> res = ProposalHelper.createSortedSet();
	// try {
	// // for (IMethodName methodName : callNodes.keySet()) {
	// // if (!isPartOfQuery(methodName)) {
	// // BayesNode node = callNodes.get(methodName);
	// // if (node == null) {
	// // debug("no node found for %s", methodName);
	// // } else {
	// // double[] beliefs = junctionTreeAlgorithm.getBeliefs(node);
	// // boolean isGreaterOrEqualToMinProbability = beliefs[0] >=
	// // options.minProbability;
	// // if (isGreaterOrEqualToMinProbability) {
	// // Pair<IMemberName, Double> tuple = Pair.of(methodName, beliefs[0]);
	// // res.add(tuple);
	// // }
	// // }
	// // }
	// // }
	// } catch (NumericalInstabilityException e) {
	// Logger.err("NumericalInstabilityException: %s", e.getMessage());
	// }
	// return res;
	// }
	//
	// private boolean isPartOfQuery(IMethodName methodName) {
	// return false;// queriedMethods.contains(methodName);
	// }
	//
	// private boolean useDouble() {
	// return "DOUBLE".equals(o.opts.get("prec"));
	// }
}