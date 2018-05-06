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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.function.Consumer;

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
		PBNModel expected = createNormalizedPBNModel();
		assertEquals(expected, actual);
	}

	@Test
	public void normalization_fixesMinMaxValues() {
		PBNModel in = createRawPBNModel();
		in.patternProbabilities = new double[] { 0, 1 };
		PBNModel actual = normalize(in);
		PBNModel expected = createNormalizedPBNModel();
		expected.patternProbabilities = new double[] { PRECISION, 1 - PRECISION };
		assertEquals(expected, actual);
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
		PBNModel expected = createNormalizedPBNModel();
		expected.classContexts = new ITypeName[] { t(1), DUMMY_CCTX };
		expected.classContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertEquals(expected, actual);
	}

	@Test
	public void normalization_addsDummyMCtx() {
		PBNModel in = createRawPBNModel();
		in.methodContexts = new IMethodName[] { m(1) };
		in.methodContextProbabilities = new double[] { 1, 1 };

		PBNModel actual = normalize(in);
		PBNModel expected = createNormalizedPBNModel();
		expected.methodContexts = new IMethodName[] { m(1), DUMMY_MCTX };
		expected.methodContextProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertEquals(expected, actual);
	}

	@Test
	public void normalization_addsDummyDef() {
		PBNModel in = createRawPBNModel();
		in.definitions = new IDefinition[] { definedByCast() };
		in.definitionProbabilities = new double[] { 1, 1 };

		PBNModel actual = normalize(in);
		PBNModel expected = createNormalizedPBNModel();
		expected.definitions = new IDefinition[] { definedByCast(), DUMMY_DEFINITION };
		expected.definitionProbabilities = new double[] { 0.999999, 0.000001, 0.999999, 0.000001 };

		assertEquals(expected, actual);
	}

	private static PBNModel createRawPBNModel() {
		PBNModel m = new PBNModel();
		m.type = t(0);
		m.numObservations = 123;
		m.patternProbabilities = new double[] { 0.3500001, 0.6500001 };

		m.classContexts = new ITypeName[] { t(1), t(2) };
		m.classContextProbabilities = new double[] { 0.1100001, 0.8900001, 0.1200001, 0.8800001 };

		m.methodContexts = new IMethodName[] { m(1), m(2) };
		m.methodContextProbabilities = new double[] { 0.2100001, 0.7900001, 0.2200001, 0.7800001 };

		m.definitions = new IDefinition[] { definedByCast(), definedByConstant() };
		m.definitionProbabilities = new double[] { 0.3100001, 0.6900001, 0.3200001, 0.6800001 };

		m.callParameters = new ICallParameter[] { new CallParameter(m(3), 0), new CallParameter(m(4), 0) };
		m.callParameterProbabilityTrue = new double[] { 0.4100001, 0.4200001, 0.4300001, 0.4400001 };

		m.members = new IMemberName[] { m(5), m(6) };
		m.memberProbabilityTrue = new double[] { 0.5100001, 0.5200001, 0.5300001, 0.5400001 };

		return m;
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
}