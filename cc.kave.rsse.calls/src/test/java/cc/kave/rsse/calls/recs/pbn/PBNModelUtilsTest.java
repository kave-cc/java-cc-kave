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

import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCast;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_CCTX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_DEFINITION;
import static cc.kave.rsse.calls.recs.pbn.PBNModelConstants.DUMMY_MCTX;
import static cc.kave.rsse.calls.recs.pbn.PBNModelUtils.normalize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.function.Consumer;

import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;

public class PBNModelUtilsTest {

	@Test
	public void normalization_roundsValues() {
		PBNModel actual = normalize(createRawPBNModel());
		assertRounded(actual.patternProbabilities);
		assertRounded(actual.classContextProbabilities);
		assertRounded(actual.methodContextProbabilities);
		assertRounded(actual.definitionProbabilities);
		assertRounded(actual.callParameterProbabilityTrue);
		assertRounded(actual.memberProbabilityTrue);
	}

	private static void assertRounded(double[] ds) {
		for (double d : ds) {
			assertRounded(d);
		}
	}

	private static void assertRounded(double a) {
		double b = MathUtils.round(a, PBNModel.PRECISION_SCALE);
		assertTrue(a == b);
	}

	@Test
	public void normalization_fixesMinMaxValues() {
		PBNModel in = createRawPBNModel();
		in.patternProbabilities = new double[] { 0, 1 };
		PBNModel actual = normalize(in);
		double[] expecteds = new double[] { PRECISION, 1 - PRECISION };
		assertArrayEquals(expecteds, actual.patternProbabilities, PRECISION * 0.1);
	}

	@Test
	public void normalization_addsDummyPatternAllAtOnc() {
		PBNModel in = new PBNModel();
		in.type = t(0);
		in.numObservations = 1;
		in.patternProbabilities = new double[] { 1 };
		in.classContexts = new ITypeName[] { t(1) };
		in.classContextProbabilities = new double[] { 1 };
		in.methodContexts = new IMethodName[] { m(1) };
		in.methodContextProbabilities = new double[] { 1 };
		in.definitions = new IDefinition[] { definedByCast() };
		in.definitionProbabilities = new double[] { 1 };
		in.callParameters = new ICallParameter[] { new CallParameter(m(3), 0) };
		in.callParameterProbabilityTrue = new double[] { 1 };
		in.members = new IMemberName[] { m(51) };
		in.memberProbabilityTrue = new double[] { 1 };

		PBNModel actual = normalize(in);
		PBNModel expected = new PBNModel();
		expected.type = t(0);
		expected.numObservations = 1;
		expected.patternProbabilities = new double[] { 0.5, 0.5 };
		expected.classContexts = new ITypeName[] { t(1), DUMMY_CCTX };
		expected.classContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };
		expected.methodContexts = new IMethodName[] { m(1), DUMMY_MCTX };
		expected.methodContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };
		expected.definitions = new IDefinition[] { definedByCast(), DUMMY_DEFINITION };
		expected.definitionProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };
		expected.callParameters = new ICallParameter[] { new CallParameter(m(3), 0) };
		expected.callParameterProbabilityTrue = new double[] { 0.999999, 0.999999 };
		expected.members = new IMemberName[] { m(51) };
		expected.memberProbabilityTrue = new double[] { 0.999999, 0.999999 };

		assertEquals(expected, actual);
	}

	@Test
	public void normalization_addsDummyCCtx() {
		PBNModel in = createRawPBNModel();
		in.classContexts = new ITypeName[] { t(1) };
		in.classContextProbabilities = new double[] { 1, 1 };
		PBNModel actual = normalize(in);

		PBNModel expected = new PBNModel();
		expected.classContexts = new ITypeName[] { t(1), DUMMY_CCTX };
		expected.classContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertArrayEquals(expected.classContexts, actual.classContexts);
		assertArrayEquals(expected.classContextProbabilities, actual.classContextProbabilities, PRECISION * 0.1);
	}

	@Test
	public void normalization_addsDummyMCtx() {
		PBNModel in = createRawPBNModel();
		in.methodContexts = new IMethodName[] { m(1) };
		in.methodContextProbabilities = new double[] { 1, 1 };
		PBNModel actual = normalize(in);

		PBNModel expected = new PBNModel();
		expected.methodContexts = new IMethodName[] { m(1), DUMMY_MCTX };
		expected.methodContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertArrayEquals(expected.methodContexts, actual.methodContexts);
		assertArrayEquals(expected.methodContextProbabilities, actual.methodContextProbabilities, PRECISION * 0.1);
	}

	@Test
	public void normalization_addsDummyDef() {
		PBNModel in = createRawPBNModel();
		in.definitions = new IDefinition[] { definedByCast() };
		in.definitionProbabilities = new double[] { 1, 1 };
		PBNModel actual = normalize(in);

		PBNModel expected = new PBNModel();
		expected.definitions = new IDefinition[] { definedByCast(), DUMMY_DEFINITION };
		expected.definitionProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertArrayEquals(expected.definitions, actual.definitions);
		assertArrayEquals(expected.definitionProbabilities, actual.definitionProbabilities, PRECISION * 0.1);
	}

	private static PBNModel createRawPBNModel() {
		PBNModel m = new PBNModel();
		m.type = t(0);
		m.numObservations = 123;
		m.patternProbabilities = new double[] { 0.1734, -1 };
		fillOdd(m.patternProbabilities);

		m.classContexts = new ITypeName[] { t(1), t(2) };
		m.classContextProbabilities = new double[] { 0.9835, -1, 0.7823, -1 };
		fillOdd(m.classContextProbabilities);

		m.methodContexts = new IMethodName[] { m(1), m(2) };
		m.methodContextProbabilities = new double[] { 0.2394, -1, 0.4532, -1 };
		fillOdd(m.methodContextProbabilities);

		m.definitions = new IDefinition[] { definedByCast(), definedByConstant() };
		m.definitionProbabilities = new double[] { 0.2345, -1, 0.9081, -1 };
		fillOdd(m.definitionProbabilities);

		m.callParameters = new ICallParameter[] { new CallParameter(m(3), 0), new CallParameter(m(4), 0) };
		m.callParameterProbabilityTrue = new double[] { 0.7804, 0.3659, 0.1978, 0.4957 };

		m.members = new IMemberName[] { m(5), m(6) };
		m.memberProbabilityTrue = new double[] { 0.3450, 0.5399, 0.2567, 0.7134 };

		return m;
	}

	private static void fillOdd(double[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (i % 2 == 1) {
				arr[i] = 1 - arr[i - 1];
			}
		}
	}

	private static ITypeName t(int i) {
		return Names.newType("T%d, P", i);
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[p:void] [T, P].m%d([p:int] p)", i);
	}

	private static PBNModel createNormalizedPBNModel() {
		return normalize(createRawPBNModel());
	}

	@Test
	public void assertValidity_Basic() {
		assertValid(m -> {});
		assertInvalid(m -> m.type = null);
		assertInvalid(m -> m.numObservations = 0);
	}

	@Test
	public void assertValidity_PatternProbability() {
		assertInvalid(m -> m.patternProbabilities = null);
		assertInvalid(m -> m.patternProbabilities = new double[0]);
		assertInvalid(m -> m.patternProbabilities = new double[] { 0.5, 0.6 });
	}

	@Test
	public void assertValidity_CCtx() {
		assertInvalid(m -> m.classContexts = null);
		assertInvalid(m -> m.classContexts = new ITypeName[0]);
		assertInvalid(m -> m.classContexts = new ITypeName[1]);
		assertInvalid(m -> m.classContexts[0] = null);
	}

	@Test
	public void assertValidity_MCtx() {
		assertInvalid(m -> m.methodContexts = null);
		assertInvalid(m -> m.methodContexts = new IMethodName[0]);
		assertInvalid(m -> m.methodContexts = new IMethodName[1]);
		assertInvalid(m -> m.methodContexts[0] = null);
	}

	@Test
	public void assertValidity_Def() {
		assertInvalid(m -> m.definitions = null);
		assertInvalid(m -> m.definitions = new IDefinition[0]);
		assertInvalid(m -> m.definitions = new IDefinition[1]);
		assertInvalid(m -> m.definitions[0] = null);
	}

	@Test
	public void assertValidity_Param() {
		assertInvalid(m -> m.callParameters = null);
		assertInvalid(m -> m.callParameters = new ICallParameter[0]);
		assertInvalid(m -> m.callParameters = new ICallParameter[1]);
		assertInvalid(m -> m.callParameters[0] = null);
	}

	@Test
	public void assertValidity_Member() {
		assertInvalid(m -> m.members = null);
		assertInvalid(m -> m.members = new IMemberName[0]);
		assertInvalid(m -> m.members = new IMemberName[1]);
		assertInvalid(m -> m.members[0] = null);
	}

	@Test
	public void assertValidity_CCtxProbabilities() {
		assertInvalid(m -> m.classContextProbabilities = null);
		assertInvalid(m -> m.classContextProbabilities = new double[0]);
		assertInvalid(m -> m.classContextProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });
	}

	@Test
	public void assertValidity_MCtxProbabilities() {
		assertInvalid(m -> m.methodContextProbabilities = null);
		assertInvalid(m -> m.methodContextProbabilities = new double[0]);
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });
	}

	@Test
	public void assertValidity_DefProbabilities() {
		assertInvalid(m -> m.definitionProbabilities = null);
		assertInvalid(m -> m.definitionProbabilities = new double[0]);
		assertInvalid(m -> m.definitionProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });
	}

	@Test
	public void assertValidity_ParamProbabilities() {
		assertInvalid(m -> m.callParameterProbabilityTrue = null);
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[0]);
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 1.0 });
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 0.25, -0.1 });
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 0.25, 1.1 });
	}

	@Test
	public void assertValidity_MemberProbabilities() {
		assertInvalid(m -> m.memberProbabilityTrue = null);
		assertInvalid(m -> m.memberProbabilityTrue = new double[0]);
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 1.0 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.25, -0.1 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.25, 1.1 });
	}

	@Test
	public void assertValidity_Bayes() {
		// violation of Bayesian networks not to have two states
		assertInvalid(m -> {
			m.classContexts = new ITypeName[] { mock(ITypeName.class) };
			m.classContextProbabilities = new double[] { 0.99999, 0.99999 };
		});
		assertInvalid(m -> {
			m.methodContexts = new IMethodName[] { mock(IMethodName.class) };
			m.methodContextProbabilities = new double[] { 0.99999, 0.99999 };
		});
		assertInvalid(m -> {
			m.definitions = new IDefinition[] { mock(IDefinition.class) };
			m.definitionProbabilities = new double[] { 0.99999, 0.99999 };
		});
	}

	@Test
	public void assertValidity_Rounding() {
		assertInvalid(m -> m.patternProbabilities = new double[] { 0.1234567, 0.8765433 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(
				m -> m.callParameterProbabilityTrue = new double[] { 0.1234567, 0.1234567, 0.1234567, 0.1234567 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.1234567, 0.1234567, 0.1234567, 0.1234567 });
	}

	private void assertValid(Consumer<PBNModel> c) {
		PBNModel model = createNormalizedPBNModel();
		c.accept(model);
		PBNModelUtils.assertValidity(model);
	}

	private void assertInvalid(Consumer<PBNModel> c) {
		PBNModel model = createNormalizedPBNModel();
		c.accept(model);
		try {
			PBNModelUtils.assertValidity(model);
			fail("Unexpected, should have caused an exception.");
		} catch (ValidationException e) {}
	}

	@Test
	public void toWekaNet_() {
		String actual = PBNModelUtils.toWekaXML(createNormalizedPBNModel());
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<!-- DTD for the XMLBIF 0.3 format -->\n");
		sb.append("<!DOCTYPE BIF [\n");
		sb.append("  <!ELEMENT BIF ( NETWORK )*>\n");
		sb.append("    <!ATTLIST BIF VERSION CDATA #REQUIRED>\n");
		sb.append("  <!ELEMENT NETWORK ( NAME, ( PROPERTY | VARIABLE | DEFINITION )* )>\n");
		sb.append("  <!ELEMENT NAME (#PCDATA)>\n");
		sb.append("  <!ELEMENT VARIABLE ( NAME, ( OUTCOME |  PROPERTY )* ) >\n");
		sb.append("    <!ATTLIST VARIABLE TYPE (nature|decision|utility) \"nature\">\n");
		sb.append("  <!ELEMENT OUTCOME (#PCDATA)>\n");
		sb.append("  <!ELEMENT DEFINITION ( FOR | GIVEN | TABLE | PROPERTY )* >\n");
		sb.append("  <!ELEMENT FOR (#PCDATA)>\n");
		sb.append("  <!ELEMENT GIVEN (#PCDATA)>\n");
		sb.append("  <!ELEMENT TABLE (#PCDATA)>\n");
		sb.append("  <!ELEMENT PROPERTY (#PCDATA)>\n");
		sb.append("]>\n");
		sb.append("<BIF VERSION=\"0.3\">\n");
		sb.append("<NETWORK>\n");
		sb.append("<NAME>T0, P</NAME>\n");
		sb.append("<!-- numObervations: 123 -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("<NAME>patterns</NAME>\n");
		sb.append("<OUTCOME>p0</OUTCOME>\n");
		sb.append("<OUTCOME>p1</OUTCOME>\n");
		sb.append("<PROPERTY>position = (0,0)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("<FOR>patterns</FOR>\n");
		sb.append("<TABLE>\n");
		sb.append("0.1734 0.8266 \n");
		sb.append("</TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for ctxs -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>ctxs</NAME>\n");
		sb.append("  <OUTCOME>T1, P</OUTCOME>\n");
		sb.append("  <OUTCOME>T2, P</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (0,40)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>ctxs</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.9835 0.0165 0.7823 0.2177 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for mctxs -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>mctxs</NAME>\n");
		sb.append("  <OUTCOME>[p:void] [T, P].m1([p:int] p)</OUTCOME>\n");
		sb.append("  <OUTCOME>[p:void] [T, P].m2([p:int] p)</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (200,40)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>mctxs</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.9835 0.0165 0.7823 0.2177 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for defs -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>defs</NAME>\n");
		sb.append("  <OUTCOME>CAST</OUTCOME>\n");
		sb.append("  <OUTCOME>CONSTANT</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (400,40)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>defs</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.2345 0.7655 0.9081 0.0919 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for CP:[p:void] [T, P].m3([p:int] p):0 -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>CP:[p:void] [T, P].m3([p:int] p):0</NAME>\n");
		sb.append("  <OUTCOME>t</OUTCOME>\n");
		sb.append("  <OUTCOME>f</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (600,40)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>CP:[p:void] [T, P].m3([p:int] p):0</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.7804 0.21960000000000002 0.1978 0.8022 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for CP:[p:void] [T, P].m4([p:int] p):0 -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>CP:[p:void] [T, P].m4([p:int] p):0</NAME>\n");
		sb.append("  <OUTCOME>t</OUTCOME>\n");
		sb.append("  <OUTCOME>f</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (600,80)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>CP:[p:void] [T, P].m4([p:int] p):0</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.3659 0.6341 0.4957 0.5043 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for MEMBER:[p:void] [T, P].m5([p:int] p) -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>MEMBER:[p:void] [T, P].m5([p:int] p)</NAME>\n");
		sb.append("  <OUTCOME>t</OUTCOME>\n");
		sb.append("  <OUTCOME>f</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (800,40)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>MEMBER:[p:void] [T, P].m5([p:int] p)</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.345 0.655 0.2567 0.7433000000000001 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("<!-- definition for MEMBER:[p:void] [T, P].m6([p:int] p) -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>MEMBER:[p:void] [T, P].m6([p:int] p)</NAME>\n");
		sb.append("  <OUTCOME>t</OUTCOME>\n");
		sb.append("  <OUTCOME>f</OUTCOME>\n");
		sb.append("  <PROPERTY>position = (800,80)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>MEMBER:[p:void] [T, P].m6([p:int] p)</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n");
		sb.append("    0.5399 0.46009999999999995 0.7134 0.28659999999999997 \n");
		sb.append("  </TABLE>\n");
		sb.append("</DEFINITION>\n");
		sb.append("</NETWORK>\n");
		sb.append("</BIF>\n");
		assertEquals(sb.toString(), actual);
	}
}