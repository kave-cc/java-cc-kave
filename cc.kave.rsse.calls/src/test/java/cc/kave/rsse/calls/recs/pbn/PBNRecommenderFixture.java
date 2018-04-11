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

import static cc.kave.commons.model.naming.Names.newMethod;
import static cc.kave.rsse.calls.model.Tuple.newTuple;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstructor;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.call;
import static cc.kave.rsse.calls.model.usages.impl.UsageSites.callParameter;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.CALL_PREFIX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.CLASS_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DEFINITION_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.METHOD_CONTEXT_TITLE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.PARAMETER_PREFIX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.PATTERN_TITLE;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Set;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.rsse.calls.mining.QueryOptions;
import cc.kave.rsse.calls.model.Tuple;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.model.usages.impl.Usage;
import cc.kave.rsse.calls.recs.pbn.BayesianNetwork;
import cc.kave.rsse.calls.recs.pbn.Node;
import cc.kave.rsse.calls.recs.pbn.PBNRecommender;

public class PBNRecommenderFixture {

	// this network is given in the resource folder as genie file
	public static BayesianNetwork createSampleNetwork() {
		BayesianNetwork net = new BayesianNetwork();

		Node patterns = createAndAddNode("patterns", net, null);
		patterns.setStates(new String[] { "p1", "p2" });
		patterns.setProbabilities(new double[] { 0.4, 0.6 });

		Node inClass = createAndAddNode("inClass", net, patterns);
		inClass.setStates(new String[] { "LC1", "LC2", "LC3" });
		inClass.setProbabilities(new double[] { 0.3, 0.3, 0.4, 0.2, 0.7, 0.1 });

		Node inMethod = createAndAddNode("inMethod", net, patterns);
		inMethod.setStates(new String[] { "LC1.m1()V", "LC2.m2()V", "LC3.m3()V" });
		inMethod.setProbabilities(new double[] { 0.15, 0.55, 0.3, 0.45, 0.1, 0.45 });

		Node definition = createAndAddNode("definition", net, patterns);
		definition.setStates(new String[] { "CONSTANT", "PARAM#2", "LC.<init>()V" });
		definition.setProbabilities(new double[] { 0.22, 0.47, 0.31, 0.4, 0.25, 0.35 });

		Node call1 = createAndAddNode("C_LC.m1()V", net, patterns);
		call1.setStates(new String[] { "t", "f" });
		call1.setProbabilities(new double[] { 0.6, 0.4, 0.1, 0.9 });

		Node call2 = createAndAddNode("C_LC.m2()V", net, patterns);
		call2.setStates(new String[] { "t", "f" });
		call2.setProbabilities(new double[] { 0.7, 0.3, 0.2, 0.8 });

		Node call3 = createAndAddNode("C_LC.m3()V", net, patterns);
		call3.setStates(new String[] { "t", "f" });
		call3.setProbabilities(new double[] { 0.45, 0.55, 0.25, 0.75 });

		Node param1 = createAndAddNode("P_LSomeClassWithParams.m1(LC;)V#2", net, patterns);
		param1.setStates(new String[] { "t", "f" });
		param1.setProbabilities(new double[] { 0.25, 0.75, 0.6, 0.4 });

		Node param2 = createAndAddNode("P_LSomeClassWithParams.m2(LC;)V#5", net, patterns);
		param2.setStates(new String[] { "t", "f" });
		param2.setProbabilities(new double[] { 0.7, 0.3, 0.35, 0.65 });

		return net;
	}

	public static Node createAndAddNode(String title, BayesianNetwork net, Node parent) {
		Node node = new Node(title);
		net.addNode(node);
		if (parent != null) {
			node.setParents(new Node[] { parent });
		}
		return node;
	}

	public static Usage createQuery() {

		Usage q = new Usage();

		q.type = Names.newType("LC");
		q.classCtx = Names.newType("LC1");
		q.methodCtx = Names.newMethod("LC1.m1()V");
		q.definition = Definitions.definedByConstant();

		q.usageSites.add(call("LC.m1()V"));
		q.usageSites.add(callParameter("LSomeClassWithParams.m1(LC;)V", 2));

		return q;
	}

	public static Usage createQueryWithAllCallsSet() {

		Usage q = new Usage();

		q.type = Names.newType("LC");
		q.classCtx = Names.newType("LC1");
		q.methodCtx = Names.newMethod("LC1.m1()V");
		q.definition = Definitions.definedByConstant();

		q.usageSites.add(call("LC.m1()V"));
		q.usageSites.add(call("LC.m2()V"));
		q.usageSites.add(call("LC.m3()V"));
		q.usageSites.add(callParameter("LSomeClassWithParams.m1(LC;)V", 2));

		return q;
	}

	// TODO remove old code
	// public static Query createQueryWithMultipleSequences() {
	//
	// ObjectUsage usage = new ObjectUsage();
	//
	// usage.type = VmTypeName.get("LC");
	// usage.supertypeOfDeclaringClass = VmTypeName.get("LC1");
	// usage.firstMethodDeclaration = VmMethodName.get("LC1.m1()V");
	// usage.definition = DefinitionSite.createDefinitionByConstant();
	//
	// List<CallSite> sequence = newArrayList();
	// sequence.add(createReceiverCallSite("LC.m1()V"));
	// sequence.add(createParameterCallSite("LSomeClassWithParams.m1(LC;)V",
	// 2));
	//
	// usage.paths = newLinkedHashSet();
	// usage.paths.add(sequence);
	//
	// sequence = newArrayList();
	// sequence.add(createReceiverCallSite("LC.m3()V"));
	// sequence.add(createParameterCallSite("LSomeClassWithParams.m1(LC;)V",
	// 2));
	//
	// usage.paths.add(sequence);
	// return new DecoratedObjectUsage(usage);
	// }

	public static Usage createQueryWithUnobservedData() {

		Usage q = new Usage();

		q.type = Names.newType("LC");
		q.classCtx = Names.newType("LUnobservedClass");
		q.methodCtx = Names.newMethod("LUnobservedClass.someMethod()V");
		q.definition = definedByConstructor(newMethod("LC.<init>(LUnobservedInit;)V"));

		q.usageSites.add(call("LC.unobservedCall()V"));
		q.usageSites.add(callParameter("LUnobservedClassWithParams.aMethod(LC;)V", 3));

		return q;
	}

	@SafeVarargs
	public static Set<Tuple<IMethodName, Double>> createResult(Tuple<IMethodName, Double>... tuples) {
		Set<Tuple<IMethodName, Double>> res = newLinkedHashSet();
		for (Tuple<IMethodName, Double> t : tuples) {
			res.add(t);
		}
		return res;
	}

	public static Tuple<IMethodName, Double> createTuple(String method, double probability) {
		IMethodName methodName = Names.newMethod(method);
		Tuple<IMethodName, Double> tuple = newTuple(methodName, probability);
		return tuple;
	}

	public static BayesianNetworkBuilder newFloatRecommender() {
		return new BayesianNetworkBuilder(false);
	}

	public static BayesianNetworkBuilder newDoubleRecommender() {
		return new BayesianNetworkBuilder(true);
	}

	public static class BayesianNetworkBuilder {

		private boolean isDoubleNetwork;
		private BayesianNetwork net = new BayesianNetwork();
		private int numPatterns = 2;
		private int numInClass = 2;
		private int numInMethod = 2;
		private int numDef = 2;
		private int numMethods = 0;
		private int numParams = 0;
		private Node patterns;

		public BayesianNetworkBuilder(boolean isDoubleNetwork) {
			this.isDoubleNetwork = isDoubleNetwork;
		}

		public BayesianNetworkBuilder patterns(int numPatterns) {
			this.numPatterns = numPatterns;
			return this;
		}

		public BayesianNetworkBuilder inClass(int numInClass) {
			this.numInClass = numInClass;
			return this;
		}

		public BayesianNetworkBuilder inMethod(int numInMethod) {
			this.numInMethod = numInMethod;
			return this;
		}

		public BayesianNetworkBuilder def(int numDef) {
			this.numDef = numDef;
			return this;
		}

		public BayesianNetworkBuilder methods(int numMethods) {
			this.numMethods = numMethods;
			return this;
		}

		public BayesianNetworkBuilder params(int numParams) {
			this.numParams = numParams;
			return this;
		}

		public int getSize() {
			patterns = addNode(PATTERN_TITLE, numPatterns);
			addConditionedNode(CLASS_CONTEXT_TITLE, numInClass);
			addConditionedNode(METHOD_CONTEXT_TITLE, numInMethod);
			addConditionedNode(DEFINITION_TITLE, numDef);
			for (int i = 0; i < numMethods; i++) {
				addConditionedNode(CALL_PREFIX + m(i), 2);
			}
			for (int i = 0; i < numParams; i++) {
				addConditionedNode(PARAMETER_PREFIX + m(i), 2);
			}
			QueryOptions qOpts = new QueryOptions();
			qOpts.useDoublePrecision = isDoubleNetwork;
			PBNRecommender rec = new PBNRecommender(net, qOpts);
			return rec.getSize();
		}

		private String m(int i) {
			return "LT.m" + i + "()V";
		}

		public Node addNode(String title, int numStates) {
			Node node = new Node(title);
			node.setStates(getStates(numStates));
			node.setProbabilities(getProbabilities(numStates));
			net.addNode(node);
			return node;
		}

		public Node addConditionedNode(String title, int numStates) {
			Node node = new Node(title);
			node.setStates(getStates(numStates));
			node.setProbabilities(getProbabilities(numStates * numPatterns));
			node.setParents(new Node[] { patterns });
			net.addNode(node);
			return node;
		}

		private static String[] getStates(int numStates) {
			String[] states = new String[numStates];
			for (int i = 0; i < numStates; i++) {
				states[i] = "s" + i;
			}
			return states;
		}

		private static double[] getProbabilities(int numStates) {
			double p = 1 / (numStates * 1.0);
			double[] probabilities = new double[numStates];
			for (int i = 0; i < numStates; i++) {
				probabilities[i] = p;
			}
			return probabilities;
		}
	}
}