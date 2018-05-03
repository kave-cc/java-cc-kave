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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class PBNModelTest {

	@Test
	public void assertValidity() {
		assertValid(m -> {});
		assertInvalid(m -> m.type = null);
		assertInvalid(m -> m.numObservations = 0);

		assertInvalid(m -> m.patternProbabilities = null);
		assertInvalid(m -> m.patternProbabilities = new double[0]);
		assertInvalid(m -> m.patternProbabilities = new double[] { 0.5, 0.6 });

		assertInvalid(m -> m.classContexts = null);
		assertInvalid(m -> m.classContexts = new ITypeName[0]);
		assertInvalid(m -> m.classContexts = new ITypeName[1]);
		assertInvalid(m -> m.classContexts[0] = null);

		assertInvalid(m -> m.methodContexts = null);
		assertInvalid(m -> m.methodContexts = new IMethodName[0]);
		assertInvalid(m -> m.methodContexts = new IMethodName[1]);
		assertInvalid(m -> m.methodContexts[0] = null);

		assertInvalid(m -> m.definitions = null);
		assertInvalid(m -> m.definitions = new IDefinition[0]);
		assertInvalid(m -> m.definitions = new IDefinition[1]);
		assertInvalid(m -> m.definitions[0] = null);

		assertInvalid(m -> m.callParameters = null);
		assertInvalid(m -> m.callParameters = new ICallParameter[0]);
		assertInvalid(m -> m.callParameters = new ICallParameter[1]);
		assertInvalid(m -> m.callParameters[0] = null);

		assertInvalid(m -> m.members = null);
		assertInvalid(m -> m.members = new IMemberName[0]);
		assertInvalid(m -> m.members = new IMemberName[1]);
		assertInvalid(m -> m.members[0] = null);

		assertInvalid(m -> m.classContextProbabilities = null);
		assertInvalid(m -> m.classContextProbabilities = new double[0]);
		assertInvalid(m -> m.classContextProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });

		assertInvalid(m -> m.methodContextProbabilities = null);
		assertInvalid(m -> m.methodContextProbabilities = new double[0]);
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });

		assertInvalid(m -> m.definitionProbabilities = null);
		assertInvalid(m -> m.definitionProbabilities = new double[0]);
		assertInvalid(m -> m.definitionProbabilities = new double[] { 1.0 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, 0.5, 0.6 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, -0.1, 1.1 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.45, 0.55, 1.1, -0.1 });

		assertInvalid(m -> m.callParameterProbabilityTrue = null);
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[0]);
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 1.0 });
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 0.25, -0.1 });
		assertInvalid(m -> m.callParameterProbabilityTrue = new double[] { 0.25, 1.1 });

		assertInvalid(m -> m.memberProbabilityTrue = null);
		assertInvalid(m -> m.memberProbabilityTrue = new double[0]);
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 1.0 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.25, -0.1 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.25, 1.1 });

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

		// not rounded
		assertInvalid(m -> m.patternProbabilities = new double[] { 0.1234567, 0.8765433 });
		assertInvalid(m -> m.classContextProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(m -> m.methodContextProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(m -> m.definitionProbabilities = new double[] { 0.1234567, 0.8765433, 0.1234567, 0.8765433 });
		assertInvalid(
				m -> m.callParameterProbabilityTrue = new double[] { 0.1234567, 0.1234567, 0.1234567, 0.1234567 });
		assertInvalid(m -> m.memberProbabilityTrue = new double[] { 0.1234567, 0.1234567, 0.1234567, 0.1234567 });
	}

	private void assertValid(Consumer<PBNModel> c) {
		PBNModel model = createValidPBNModel();
		c.accept(model);
		model.assertValidity();
	}

	private void assertInvalid(Consumer<PBNModel> c) {
		PBNModel model = createValidPBNModel();
		c.accept(model);
		try {
			model.assertValidity();
			fail("Unexpected, should have caused an exception.");
		} catch (ValidationException e) {}
	}

	@Test
	public void todo() {
		// TODO test me
		Assert.fail("test me!");
	}

	@Test
	public void equality_default() {
		PBNModel a = new PBNModel();
		PBNModel b = new PBNModel();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_withValues() {
		PBNModel a = new PBNModel();
		a.patternProbabilities = new double[] { 0.1, 0.2 };
		PBNModel b = new PBNModel();
		b.patternProbabilities = new double[] { d(0.1), d(0.2) };
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	private static double d(double d) {
		return d + 0.00000000001;
	}

	@Test
	public void equality_diffPatterns() {
		PBNModel a = new PBNModel();
		a.patternProbabilities = new double[] { 0.1, 0.2 };
		PBNModel b = new PBNModel();
		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_diffX() {
		Assert.fail("test me!");
	}

	private static PBNModel createValidPBNModel() {
		PBNModel m = new PBNModel();
		m.type = mock(ITypeName.class);
		m.numObservations = 123;
		m.patternProbabilities = new double[] { 0.35, 0.65 };

		m.classContexts = new ITypeName[] { mock(ITypeName.class), mock(ITypeName.class) };
		m.classContextProbabilities = new double[] { 0.11, 0.89, 0.12, 0.88 };

		m.methodContexts = new IMethodName[] { mock(IMethodName.class), mock(IMethodName.class) };
		m.methodContextProbabilities = new double[] { 0.21, 0.79, 0.22, 0.78 };

		m.definitions = new IDefinition[] { mock(IDefinition.class), mock(IDefinition.class) };
		m.definitionProbabilities = new double[] { 0.31, 0.69, 0.32, 0.68 };

		m.callParameters = new ICallParameter[] { mock(ICallParameter.class), mock(ICallParameter.class) };
		m.callParameterProbabilityTrue = new double[] { 0.41, 0.42, 0.43, 0.44 };

		m.members = new IMemberName[] { mock(IMemberName.class), mock(IMemberName.class) };
		m.memberProbabilityTrue = new double[] { 0.51, 0.52, 0.53, 0.54 };

		return m;
	}
}