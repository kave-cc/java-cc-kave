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

import static cc.kave.rsse.calls.pbn.PBNModelConstants.STATE_FALSE;
import static cc.kave.rsse.calls.pbn.PBNModelConstants.STATE_TRUE;
import static cc.recommenders.mining.calls.NetworkMathUtils.MAX_PROBABILTY_DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.commons.bayesnet.Node;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.pbn.clustering.Pattern;
import cc.kave.rsse.calls.usages.DefinitionSite;
import cc.kave.rsse.calls.usages.DefinitionSites;
import cc.kave.rsse.calls.usages.features.CallFeature;
import cc.kave.rsse.calls.usages.features.ClassFeature;
import cc.kave.rsse.calls.usages.features.DefinitionFeature;
import cc.kave.rsse.calls.usages.features.FirstMethodFeature;
import cc.kave.rsse.calls.usages.features.ParameterFeature;
import cc.kave.rsse.calls.usages.features.TypeFeature;
import cc.kave.rsse.calls.usages.features.UsageFeature;

public class PBNModelBuilderFixture {

	public static final ITypeName TYPE = Names.newType("La/type/Blubb");
	public static final ITypeName SUPERCLASS1 = Names.newType("La/super/Type");
	public static final ITypeName SUPERCLASS2 = Names.newType("Lother/super/Type");
	public static final IMethodName METHOD1 = Names.newMethod("Lsome/Context.m1()V");
	public static final IMethodName METHOD2 = Names.newMethod("Lother/Context.m2()V");
	public static final DefinitionSite DEF1 = createDef1();
	public static final DefinitionSite DEF2 = createDef2();
	public static final IMethodName CALL1 = Names.newMethod("La/type/Blubb.m3()V");
	public static final IMethodName CALL2 = Names.newMethod("La/type/Blubb.m4()V");
	public static final IMethodName CALL_WITH_DIFFERENT_TYPE = Names.newMethod("Lanother/type/Blubb.m5()V");
	public static final IMethodName PARAM1 = Names.newMethod("Lcompletely/different/Type.m6(Blubb;)V");
	public static final int PARAM1_ARGNUM = 135;
	public static final IMethodName PARAM2 = Names.newMethod("Lyat/Type.m7(Blubb;)V");
	public static final int PARAM2_ARGNUM = 246;

	private static DefinitionSite createDef1() {
		// DeclaringType'.'fieldName;FieldType
		return DefinitionSites.createDefinitionByField("[bla, P] [Foo, P].field");
	}

	private static DefinitionSite createDef2() {
		return DefinitionSites.createDefinitionByConstructor("[p:void] [a.type.Blubb, P]..ctor()");
	}

	public List<Pattern<UsageFeature>> getSinglePattern() {
		List<Pattern<UsageFeature>> patterns = newArrayList();

		Pattern<UsageFeature> p = new Pattern<UsageFeature>();
		p.setName("p1");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassFeature(SUPERCLASS1), 1.0);
		p.setProbability(new FirstMethodFeature(METHOD1), 1.0);
		p.setProbability(new DefinitionFeature(DEF1), 1.0);
		p.setProbability(new CallFeature(CALL1), 1.0);
		p.setProbability(new ParameterFeature(PARAM1, PARAM1_ARGNUM), 1.0);

		return patterns;
	}

	public Dictionary<UsageFeature> getDictionaryForSinglePattern() {
		Dictionary<UsageFeature> dictionary = new Dictionary<UsageFeature>();
		dictionary.add(new TypeFeature(TYPE));
		dictionary.add(new ClassFeature(SUPERCLASS1));
		dictionary.add(new FirstMethodFeature(METHOD1));
		dictionary.add(new DefinitionFeature(DEF1));
		dictionary.add(new CallFeature(CALL1));
		dictionary.add(new ParameterFeature(PARAM1, PARAM1_ARGNUM));
		return dictionary;
	}

	public List<Pattern<UsageFeature>> getPatterns() {
		List<Pattern<UsageFeature>> patterns = newArrayList();

		Pattern<UsageFeature> p = new Pattern<UsageFeature>();
		p.setName("p1");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassFeature(SUPERCLASS1), 1.0);
		p.setProbability(new FirstMethodFeature(METHOD1), 1.0);
		p.setProbability(new DefinitionFeature(DEF1), 1.0);
		p.setProbability(new CallFeature(CALL1), 1.0);
		p.setProbability(new ParameterFeature(PARAM1, PARAM1_ARGNUM), 1.0);

		p = new Pattern<UsageFeature>();
		p.setName("p2");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassFeature(SUPERCLASS2), 1.0);
		p.setProbability(new FirstMethodFeature(METHOD2), 1.0);
		p.setProbability(new DefinitionFeature(DEF2), 1.0);
		p.setProbability(new CallFeature(CALL2), 1.0);
		p.setProbability(new ParameterFeature(PARAM2, PARAM2_ARGNUM), 1.0);

		return patterns;
	}

	public Dictionary<UsageFeature> getDictionary() {
		Dictionary<UsageFeature> dictionary = new Dictionary<UsageFeature>();
		dictionary.add(new TypeFeature(TYPE));
		dictionary.add(new ClassFeature(SUPERCLASS1));
		dictionary.add(new FirstMethodFeature(METHOD1));
		dictionary.add(new DefinitionFeature(DEF1));
		dictionary.add(new CallFeature(CALL1));
		dictionary.add(new ParameterFeature(PARAM1, PARAM1_ARGNUM));
		dictionary.add(new ClassFeature(SUPERCLASS2));
		dictionary.add(new FirstMethodFeature(METHOD2));
		dictionary.add(new DefinitionFeature(DEF2));
		dictionary.add(new CallFeature(CALL2));
		dictionary.add(new ParameterFeature(PARAM2, PARAM2_ARGNUM));
		return dictionary;
	}

	public static void assertNodeExists(BayesianNetwork net, String title) {
		Node node = net.getNode(title);
		assertNotNull(node);
	}

	public static void assertNodesExist(BayesianNetwork net, String... titles) {
		for (String title : titles) {
			assertNodeExists(net, title);
		}
	}

	public static void assertProbabilities(BayesianNetwork network, String nodeTitle, double... expectedProbabilities) {
		Node node = network.getNode(nodeTitle);
		double[] actual = node.getProbabilities();

		assertArrayEquals(expectedProbabilities, actual, MAX_PROBABILTY_DELTA);
	}

	public static void assertStates(BayesianNetwork network, String nodeTitle, String... expectedStates) {
		Node node = network.getNode(nodeTitle);
		String[] actual = node.getStates();

		assertArrayEquals(expectedStates, actual);
	}

	public static void assertBooleanNode(BayesianNetwork network, String nodeTitle) {
		Node node = network.getNode(nodeTitle);
		String[] actuals = node.getStates();
		String[] expecteds = new String[] { STATE_TRUE, STATE_FALSE };
		assertArrayEquals(expecteds, actuals);
	}
}