package cc.kave.rsse.calls.recs.pbn;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.rsse.calls.model.Dictionary;
import cc.kave.rsse.calls.model.features.IFeature;
import cc.kave.rsse.calls.model.features.Pattern;

public class PBNModelBuilder2Test {

	private Dictionary<IFeature> dict;
	private List<Pattern> patterns;

	private PBNModelBuilder2 sut;

	@Ignore
	@Test
	public void testMe() {
		fail();
	}
	// @Before
	// public void setup() {
	// dict = new Dictionary<>();
	// patterns = new LinkedList<>();
	// sut = new PBNModelBuilder2();
	//
	// dict.add(TYPE);
	// dict.add(CLASS1);
	// dict.add(CLASS2);
	// dict.add(SUPERMETHOD1);
	// dict.add(SUPERMETHOD2);
	// dict.add(FIRSTMETHOD1);
	// dict.add(FIRSTMETHOD2);
	// dict.add(DEF1);
	// dict.add(DEF2);
	// dict.add(CALL1);
	// dict.add(CALL2);
	// dict.add(PARAM1);
	// dict.add(PARAM2);
	// dict.add(CALL1);
	// dict.add(CALL2);
	// dict.add(PARAM1);
	// dict.add(PARAM2);
	// }
	//
	// public List<Pattern<UsageFeature>> getPatterns() {
	//
	// // patterns.add(p);
	//
	// return patterns;
	// }
	//
	// @Test
	// public void integrationTest() {
	// Pattern p = new Pattern();
	// p.setName("p1");
	// p.setNumberOfObservations(1);
	// p.setProbability(TYPE, 1.0);
	// p.setProbability(CLASS1, 1.0);
	// p.setProbability(FIRSTMETHOD1, 1.0);
	// p.setProbability(DEF1, 1.0);
	// p.setProbability(CALL1, 1.0);
	// p.setProbability(PARAM1, 1.0);
	// patterns.add(p);
	//
	// p = new Pattern();
	// p.setName("p2");
	// p.setNumberOfObservations(1);
	// p.setProbability(TYPE, 1.0);
	// p.setProbability(CLASS2, 1.0);
	// p.setProbability(FIRSTMETHOD2, 1.0);
	// p.setProbability(DEF2, 1.0);
	// p.setProbability(CALL2, 1.0);
	// p.setProbability(PARAM2, 1.0);
	// patterns.add(p);
	//
	// PBNModel expected = new PBNModel();
	// expected.patternProbabilities = new double[] { 0.5, 0.5 };
	//
	// assertModel(patterns, expected);
	//
	// }
	//
	// private void assertModel(List<Pattern> patterns, PBNModel expected) {
	// PBNModel actual = sut.build(patterns, dict);
	// Assert.assertEquals(expected, actual);
	// }

	// private static final TypeFeature TYPE = new
	// TypeFeature(Names.newType("T,P"));
	// private static final ClassFeature CLASS1 = new
	// ClassFeature(Names.newType("S1,P"));
	// private static final ClassFeature CLASS2 = new
	// ClassFeature(Names.newType("S2,P"));
	// private static final SuperMethodFeature SUPERMETHOD1 = new
	// SuperMethodFeature(
	// Names.newMethod("[p:void] [S,P].superCtx1()"));
	// private static final SuperMethodFeature SUPERMETHOD2 = new
	// SuperMethodFeature(
	// Names.newMethod("[p:void] [S,P].superCtx1()"));
	// private static final FirstMethodFeature FIRSTMETHOD1 = new
	// FirstMethodFeature(
	// Names.newMethod("[p:void] [IT,P].ctx1()"));
	// private static final FirstMethodFeature FIRSTMETHOD2 = new
	// FirstMethodFeature(
	// Names.newMethod("[p:void] [IT,P].ctx2()"));
	// private static final DefinitionFeature DEF1 = new
	// DefinitionFeature(DefinitionSites.createDefinitionByConstant());
	// private static final DefinitionFeature DEF2 = new
	// DefinitionFeature(DefinitionSites.createDefinitionByThis());
	// private static final CallFeature CALL1 = new
	// CallFeature(Names.newMethod("[p:void] [IT,P].ctx1()"));
	// private static final CallFeature CALL2 = new
	// CallFeature(Names.newMethod("[p:void] [IT,P].ctx1()"));
	// private static final ParameterFeature PARAM1 = new
	// ParameterFeature(Names.newMethod("[p:void] [IT,P].ctx1()"), 12);
	// private static final ParameterFeature PARAM2 = new
	// ParameterFeature(Names.newMethod("[p:void] [IT,P].ctx1()"), 12);
}