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

import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.STATE_FALSE;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.STATE_TRUE;
import static cc.kave.rsse.calls.utils.NetworkMathUtils.MAX_PROBABILTY_DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.ClassContextFeature;
import cc.kave.rsse.calls.model.features.DefinitionFeature;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.MethodContextFeature;
import cc.kave.rsse.calls.model.features.Pattern;
import cc.kave.rsse.calls.model.features.TypeFeature;
import cc.kave.rsse.calls.model.features.UsageSiteFeature;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.Definitions;
import cc.kave.rsse.calls.recs.pbn.BayesianNetwork;
import cc.kave.rsse.calls.recs.pbn.Node;

public class PBNModelBuilderFixture {

	public static final ITypeName TYPE = Names.newType("La/type/Blubb");
	public static final ITypeName SUPERCLASS1 = Names.newType("La/super/Type");
	public static final ITypeName SUPERCLASS2 = Names.newType("Lother/super/Type");
	public static final IMethodName METHOD1 = Names.newMethod("Lsome/Context.m1()V");
	public static final IMethodName METHOD2 = Names.newMethod("Lother/Context.m2()V");
	public static final IDefinition DEF1 = createDef1();
	public static final IDefinition DEF2 = createDef2();
	public static final IMethodName CALL1 = Names.newMethod("La/type/Blubb.m3()V");
	public static final IMethodName CALL2 = Names.newMethod("La/type/Blubb.m4()V");
	public static final IMethodName CALL_WITH_DIFFERENT_TYPE = Names.newMethod("Lanother/type/Blubb.m5()V");
	public static final IMethodName PARAM1 = Names.newMethod("Lcompletely/different/Type.m6(Blubb;)V");
	public static final int PARAM1_ARGNUM = 135;
	public static final IMethodName PARAM2 = Names.newMethod("Lyat/Type.m7(Blubb;)V");
	public static final int PARAM2_ARGNUM = 246;

	private static IDefinition createDef1() {
		// DeclaringType'.'fieldName;FieldType
		return Definitions.definedByMemberAccessToField("[bla, P] [Foo, P].field");
	}

	private static IDefinition createDef2() {
		return Definitions.definedByConstructor("[p:void] [a.type.Blubb, P]..ctor()");
	}

	public List<Pattern> getSinglePattern() {
		List<Pattern> patterns = newArrayList();

		Pattern p = new Pattern();
		p.setName("p1");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassContextFeature(SUPERCLASS1), 1.0);
		p.setProbability(new MethodContextFeature(METHOD1), 1.0);
		p.setProbability(new DefinitionFeature(DEF1), 1.0);
		p.setProbability(new UsageSiteFeature(CALL1), 1.0);
		// p.setProbability(new ParameterFeature(PARAM1, PARAM1_ARGNUM), 1.0);

		return patterns;
	}

	public Dictionary<IFeature> getDictionaryForSinglePattern() {
		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();
		dictionary.add(new TypeFeature(TYPE));
		dictionary.add(new ClassContextFeature(SUPERCLASS1));
		dictionary.add(new MethodContextFeature(METHOD1));
		dictionary.add(new DefinitionFeature(DEF1));
		dictionary.add(new UsageSiteFeature(CALL1));
		// dictionary.add(new ParameterFeature(PARAM1, PARAM1_ARGNUM));
		return dictionary;
	}

	public List<Pattern> getPatterns() {
		List<Pattern> patterns = newArrayList();

		Pattern p = new Pattern();
		p.setName("p1");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassContextFeature(SUPERCLASS1), 1.0);
		p.setProbability(new MethodContextFeature(METHOD1), 1.0);
		p.setProbability(new DefinitionFeature(DEF1), 1.0);
		p.setProbability(new UsageSiteFeature(CALL1), 1.0);
		// p.setProbability(new ParameterFeature(PARAM1, PARAM1_ARGNUM), 1.0);

		p = new Pattern();
		p.setName("p2");
		p.setNumberOfObservations(1);
		patterns.add(p);

		p.setProbability(new TypeFeature(TYPE), 1.0);
		p.setProbability(new ClassContextFeature(SUPERCLASS2), 1.0);
		p.setProbability(new MethodContextFeature(METHOD2), 1.0);
		p.setProbability(new DefinitionFeature(DEF2), 1.0);
		p.setProbability(new UsageSiteFeature(CALL2), 1.0);
		// p.setProbability(new ParameterFeature(PARAM2, PARAM2_ARGNUM), 1.0);

		return patterns;
	}

	public Dictionary<IFeature> getDictionary() {
		Dictionary<IFeature> dictionary = new Dictionary<IFeature>();
		dictionary.add(new TypeFeature(TYPE));
		dictionary.add(new ClassContextFeature(SUPERCLASS1));
		dictionary.add(new MethodContextFeature(METHOD1));
		dictionary.add(new DefinitionFeature(DEF1));
		dictionary.add(new UsageSiteFeature(CALL1));
		// dictionary.add(new ParameterFeature(PARAM1, PARAM1_ARGNUM));
		dictionary.add(new ClassContextFeature(SUPERCLASS2));
		dictionary.add(new MethodContextFeature(METHOD2));
		dictionary.add(new DefinitionFeature(DEF2));
		dictionary.add(new UsageSiteFeature(CALL2));
		// dictionary.add(new ParameterFeature(PARAM2, PARAM2_ARGNUM));
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