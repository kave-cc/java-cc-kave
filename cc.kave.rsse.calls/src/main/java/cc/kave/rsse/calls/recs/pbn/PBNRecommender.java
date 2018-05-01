/**
 * Copyright (c) 2010-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.CALL_PREFIX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.CLASS_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DEFINITION_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.METHOD_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.PATTERN_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newClassContext;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newDefinition;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.newMethodContext;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.io.Logger;
import cc.kave.repackaged.jayes.BayesNet;
import cc.kave.repackaged.jayes.BayesNode;
import cc.kave.repackaged.jayes.inference.junctionTree.JunctionTreeAlgorithm;
import cc.kave.repackaged.jayes.util.NumericalInstabilityException;
import cc.kave.rsse.calls.IMemberRecommender;
import cc.kave.rsse.calls.mining.FeatureExtractor;
import cc.kave.rsse.calls.mining.Options;
import cc.kave.rsse.calls.model.usages.IMemberAccess;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.utils.ProposalHelper;

public class PBNRecommender implements IMemberRecommender<Usage> {

	private final FeatureExtractor fe;
	private final PBNModel model;
	private final Options options;

	private BayesNet bayesNet;
	private BayesNode patternNode;
	private BayesNode classContextNode;
	private BayesNode methodContextNode;
	private BayesNode definitionNode;
	private Map<String, BayesNode> paramNodes = newHashMap();

	private Map<IMemberName, BayesNode> memberNodes = newHashMap();

	private JunctionTreeAlgorithm junctionTreeAlgorithm;

	private Set<IMemberName> queriedMembers = newHashSet();

	public PBNRecommender(FeatureExtractor fe, PBNModel model, Options options) {
		this.fe = fe;
		this.model = model;
		this.options = options;

		// initializeNodes();
		// initializeArcs();
		// initializeProbabilities();

		junctionTreeAlgorithm = new JunctionTreeAlgorithm();
		if (!useDouble()) {
			junctionTreeAlgorithm.getFactory().setFloatingPointType(float.class);
		}
		junctionTreeAlgorithm.setNetwork(bayesNet);
	}

	private void initializeNodes() {
		bayesNet = new BayesNet();

		// pattern node
		patternNode = bayesNet.createNode(PBNModelConstants.PATTERN_TITLE);
		String[] outcomes = new String[model.patternProbabilities.length];
		for (int i = 0; i < outcomes.length; i++) {
			outcomes[i] = "p" + i;
		}
		patternNode.setProbabilities(model.patternProbabilities);

		// for (final Node node : network.getNodes()) {
		// BayesNode bayesNode = createNodeFrom(node);
		// assignToClassMember(node, bayesNode);
		// }
	}

	private BayesNode createNodeFrom(Node node) {
		BayesNode bayesNode = bayesNet.createNode(node.getIdentifier());
		String[] states = node.getStates();
		for (int i = 0; i < states.length; i++) {
			try {
				bayesNode.addOutcome(states[i]);
			} catch (IllegalArgumentException e) {
				Logger.err("error when adding outcome %s: %s", states[i], e.getMessage());
			}
		}
		return bayesNode;
	}

	private void assignToClassMember(Node node, BayesNode bayesNode) {
		String nodeTitle = node.getIdentifier();
		if (nodeTitle.equals(CLASS_CONTEXT_TITLE)) {
			classContextNode = bayesNode;
		} else if (nodeTitle.equals(METHOD_CONTEXT_TITLE)) {
			methodContextNode = bayesNode;
		} else if (nodeTitle.equals(DEFINITION_TITLE)) {
			definitionNode = bayesNode;
		} else if (nodeTitle.equals(PATTERN_TITLE)) {
			patternNode = bayesNode;
		} else if (nodeTitle.startsWith(CALL_PREFIX)) {
			// IMethodName call =
			// Names.newMethod(nodeTitle.substring(CALL_PREFIX.length()));
			// callNodes.put(call, bayesNode);
		} else {
			paramNodes.put(nodeTitle, bayesNode);
		}
	}

	private void initializeArcs(final BayesianNetwork network) {
		for (final Node node : network.getNodes()) {
			Node[] parents = node.getParents();
			BayesNode children = bayesNet.getNode(node.getIdentifier());
			List<BayesNode> bnParents = newArrayList();
			for (int i = 0; i < parents.length; i++) {
				String parentTitle = parents[i].getIdentifier();
				bnParents.add(bayesNet.getNode(parentTitle));
			}
			children.setParents(bnParents);
		}
	}

	private void initializeProbabilities(final BayesianNetwork network) {
		for (final Node node : network.getNodes()) {
			final BayesNode bayesNode = bayesNet.getNode(node.getIdentifier());
			bayesNode.setProbabilities(node.getProbabilities());
		}
	}

	protected void clearEvidence() {
		junctionTreeAlgorithm.setEvidence(new HashMap<BayesNode, String>());
		// queriedMethods.clear();
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Usage u) {
		clearEvidence();

		if (options.useClassCtx()) {
			addEvidenceIfAvailableInNetwork(classContextNode, newClassContext(u.getClassContext()));
		}
		if (options.useMethodCtx()) {
			addEvidenceIfAvailableInNetwork(getMethodContextNode(), newMethodContext(u.getMethodContext()));
		}
		if (options.useDef()) {
			addEvidenceIfAvailableInNetwork(definitionNode, newDefinition(u.getDefinition()));
		}

		ITypeName type = u.getType();
		// for (IMemberAccess site : u.getUsageSites()) {
		// markRebasedSite(type, site);
		// }

		return collectCallProbabilities();
	}

	private void addEvidenceIfAvailableInNetwork(BayesNode node, String outcome) {
		if (node.getOutcomes().contains(outcome)) {
			junctionTreeAlgorithm.addEvidence(node, outcome);
			// debug("outcome marked '%s'", node.getName());
		} else {
			debug("unknown outcome: %s (%s)", outcome, node.getName());
		}
	}

	private void markRebasedSite(ITypeName type, IMemberAccess site) {
		switch (site.getType()) {
		// case CALL_PARAMETER:
		// if (options.useParams()) {
		// String nodeTitle = newParameterSite((IMethodName) site.getMember(),
		// site.getArgIndex());
		// BayesNode node = paramNodes.get(nodeTitle);
		// if (node != null) {
		// junctionTreeAlgorithm.addEvidence(node, STATE_TRUE);
		// // debug("outcome marked 'parameter'");
		// } else {
		// debug("unknown node: %s (%s)", nodeTitle, type);
		// }
		// }
		// break;
		case METHOD_CALL:
			// TODO re-enable rebasing (here and in modelBuilder)
			// IMethodName rebasedName = rebase(type, site.targetMethod);
			// BayesNode node = callNodes.get(rebasedName);

			// it is not necessary to call OUMC.newCallSite(...), because the
			// prefix is already stripped in that map (see
			// assignToClassMember())
			// BayesNode node = callNodes.get((IMethodName) site.getMember());
			// if (node != null) {
			// // queriedMethods.add(rebasedName);
			// queriedMethods.add((IMethodName) site.getMember());
			// junctionTreeAlgorithm.addEvidence(node, STATE_TRUE);
			// // debug("outcome marked 'method call'");
			// } else {
			// debug("unknown node: %S%s (%s)", CALL_PREFIX, (IMethodName) site.getMember(),
			// type);
			// }
			break;
		}
	}

	private Set<Pair<IMemberName, Double>> collectCallProbabilities() {
		Set<Pair<IMemberName, Double>> res = ProposalHelper.createSortedSet();
		try {
			// for (IMethodName methodName : callNodes.keySet()) {
			// if (!isPartOfQuery(methodName)) {
			// BayesNode node = callNodes.get(methodName);
			// if (node == null) {
			// debug("no node found for %s", methodName);
			// } else {
			// double[] beliefs = junctionTreeAlgorithm.getBeliefs(node);
			// boolean isGreaterOrEqualToMinProbability = beliefs[0] >=
			// options.minProbability;
			// if (isGreaterOrEqualToMinProbability) {
			// Pair<IMemberName, Double> tuple = Pair.of(methodName, beliefs[0]);
			// res.add(tuple);
			// }
			// }
			// }
			// }
		} catch (NumericalInstabilityException e) {
			Logger.err("NumericalInstabilityException: %s", e.getMessage());
		}
		return res;
	}

	private boolean isPartOfQuery(IMethodName methodName) {
		return false;// queriedMethods.contains(methodName);
	}

	@Override
	public int getLastModelSize() {
		int size = 0;
		for (BayesNode n : bayesNet.getNodes()) {
			int numValues = n.getProbabilities().length;
			int bytePerValue = useDouble() ? 8 : 4;
			size += numValues * bytePerValue;
		}
		return size;
	}

	private boolean useDouble() {
		return "DOUBLE".equals(options.opts.get("prec"));
	}

	protected double[] getBeliefs(BayesNode node) {
		return this.junctionTreeAlgorithm.getBeliefs(node);
	}

	protected BayesNode getClassContextNode() {
		return this.classContextNode;
	}

	protected BayesNode getDefinitionNode() {
		return this.definitionNode;
	}

	protected BayesNode getMethodContextNode() {
		return this.methodContextNode;
	}

	protected BayesNode getPatternNode() {
		return this.patternNode;
	}

	private static void debug(String msg, Object... args) {
		// Logger.debug(msg, args);
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx) {
		return null;
	}

	@Override
	public Set<Pair<IMemberName, Double>> query(Context ctx, List<IName> ideProposals) {
		return query(ctx);
	}
}