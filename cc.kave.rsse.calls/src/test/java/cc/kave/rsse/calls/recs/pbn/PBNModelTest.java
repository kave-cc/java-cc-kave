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

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertEqualDataStructures;
import static cc.kave.commons.testing.DataStructureEqualityAsserts.assertNotEqualDataStructures;
import static cc.kave.commons.testing.ToStringAsserts.assertToStringUtils;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByCast;
import static cc.kave.rsse.calls.model.usages.impl.Definitions.definedByConstant;
import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION;
import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION_SCALE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;
import cc.kave.rsse.calls.model.usages.impl.CallParameter;

public class PBNModelTest {

	@Test
	public void defaults() {

		assertEquals(0.000001, PRECISION, 0.0000001);
		assertEquals(6, PRECISION_SCALE);

		PBNModel sut = new PBNModel();
		assertNull(sut.type);
		assertEquals(0, sut.numObservations);
		assertNull(sut.patternProbabilities);

		assertNull(sut.classContexts);
		assertNull(sut.methodContexts);
		assertNull(sut.definitions);
		assertNull(sut.callParameters);
		assertNull(sut.members);

		assertNull(sut.classContextProbabilities);
		assertNull(sut.methodContextProbabilities);
		assertNull(sut.definitionProbabilities);
		assertNull(sut.callParameterProbabilityTrue);
		assertNull(sut.memberProbabilityTrue);
	}

	@Test
	public void splittingCCtx_byPattern() {
		double[][] actuals = createModel().getCCtxByPattern();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.21, 0.22 };
		expecteds[1] = new double[] { 0.23, 0.24 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingCCtx_byItem() {
		double[][] actuals = createModel().getPatternByCCtx();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.21, 0.23 };
		expecteds[1] = new double[] { 0.22, 0.24 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingMCtx_byPattern() {
		double[][] actuals = createModel().getMCtxByPattern();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.31, 0.32 };
		expecteds[1] = new double[] { 0.33, 0.34 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingMCtx_byItem() {
		double[][] actuals = createModel().getPatternByMCtx();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.31, 0.33 };
		expecteds[1] = new double[] { 0.32, 0.34 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingDef_byPattern() {
		double[][] actuals = createModel().getDefByPattern();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.41, 0.42 };
		expecteds[1] = new double[] { 0.43, 0.44 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingDef_byItem() {
		double[][] actuals = createModel().getPatternByDef();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.41, 0.43 };
		expecteds[1] = new double[] { 0.42, 0.44 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingParam_byPattern() {
		double[][] actuals = createModel().getParamByPattern();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.51, 0.52 };
		expecteds[1] = new double[] { 0.53, 0.54 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingParam_byItem() {
		double[][] actuals = createModel().getPatternByParam();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.51, 0.53 };
		expecteds[1] = new double[] { 0.52, 0.54 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingMembers_byPattern() {
		double[][] actuals = createModel().getMemberByPattern();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.61, 0.62 };
		expecteds[1] = new double[] { 0.63, 0.64 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void splittingMembers_byItem() {
		double[][] actuals = createModel().getPatternByMember();
		double[][] expecteds = new double[2][];
		expecteds[0] = new double[] { 0.61, 0.63 };
		expecteds[1] = new double[] { 0.62, 0.64 };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	public void size() {
		long actual = createModel().getSize();
		long expected = 4 + 2 * 4 + 4 * 4 + 4 * 4 + 4 * 4 + 4 * 4 + 4 * 4;
		assertEquals(expected, actual);
	}

	@Test
	public void equality_default() {
		PBNModel a = new PBNModel();
		PBNModel b = new PBNModel();
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_withValues() {
		PBNModel a = createModel();
		PBNModel b = createModel();
		assertEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffType() {
		PBNModel a = new PBNModel();
		a.type = mock(ITypeName.class);
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffType2() {
		PBNModel a = new PBNModel();
		PBNModel b = new PBNModel();
		b.type = mock(ITypeName.class);
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffNumObservations() {
		PBNModel a = new PBNModel();
		a.numObservations = 1;
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffPatterns() {
		PBNModel a = new PBNModel();
		a.patternProbabilities = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffCCtxs() {
		PBNModel a = new PBNModel();
		a.classContexts = new ITypeName[] { mock(ITypeName.class) };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMCtxs() {
		PBNModel a = new PBNModel();
		a.methodContexts = new IMethodName[] { mock(IMethodName.class) };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffDefs() {
		PBNModel a = new PBNModel();
		a.definitions = new IDefinition[] { mock(IDefinition.class) };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffParams() {
		PBNModel a = new PBNModel();
		a.callParameters = new ICallParameter[] { mock(ICallParameter.class) };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMembers() {
		PBNModel a = new PBNModel();
		a.members = new IMemberName[] { mock(IMemberName.class) };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffCCtxProbs() {
		PBNModel a = new PBNModel();
		a.classContextProbabilities = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMCtxProbs() {
		PBNModel a = new PBNModel();
		a.methodContextProbabilities = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffDefProbs() {
		PBNModel a = new PBNModel();
		a.definitionProbabilities = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffParamProbs() {
		PBNModel a = new PBNModel();
		a.callParameterProbabilityTrue = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void equality_diffMemberProbs() {
		PBNModel a = new PBNModel();
		a.memberProbabilityTrue = new double[] { 0.1 };
		PBNModel b = new PBNModel();
		assertNotEqualDataStructures(a, b);
	}

	@Test
	public void toStringIsImplemented() {
		assertToStringUtils(new PBNModel());
	}

	private static PBNModel createModel() {
		PBNModel a = new PBNModel();
		a.type = Names.newType("T, P");
		a.numObservations = 123;
		a.patternProbabilities = new double[] { 0.11, 0.12 };
		a.classContexts = new ITypeName[] { newType("C1, P"), newType("C2, P") };
		a.classContextProbabilities = new double[] { 0.21, 0.22, 0.23, 0.24 };
		a.methodContexts = new IMethodName[] { m(1), m(2) };
		a.methodContextProbabilities = new double[] { 0.31, 0.32, 0.33, 0.34 };
		a.definitions = new IDefinition[] { definedByCast(), definedByConstant() };
		a.definitionProbabilities = new double[] { 0.41, 0.42, 0.43, 0.44 };
		a.callParameters = new ICallParameter[] { new CallParameter(m(3), 0), new CallParameter(m(4), 0) };
		a.callParameterProbabilityTrue = new double[] { 0.51, 0.52, 0.53, 0.54 };
		a.members = new IMemberName[] { m(5), m(6), m(7), m(8) };
		a.memberProbabilityTrue = new double[] { 0.61, 0.62, 0.63, 0.64 };
		return a;
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[p:void] [T, P].m%d([p:int] p)", i);
	}
}