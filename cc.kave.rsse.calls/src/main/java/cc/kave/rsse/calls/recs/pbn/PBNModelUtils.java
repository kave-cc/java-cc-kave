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

import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION;
import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION_SCALE;
import static java.lang.String.format;

import org.apache.commons.math.util.MathUtils;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class PBNModelUtils {

	public static PBNModel normalize(PBNModel in) {
		PBNModel out = new PBNModel();
		out.type = in.type;
		out.numObservations = in.numObservations;

		out.patternProbabilities = round(in.patternProbabilities);

		if (out.patternProbabilities.length == 1) {
			out.patternProbabilities = new double[] { 0.5, 0.5 };

			out.classContexts = in.classContexts;
			out.classContextProbabilities = duplicate(in.classContextProbabilities);

			out.methodContexts = in.methodContexts;
			out.methodContextProbabilities = duplicate(in.methodContextProbabilities);

			out.definitions = in.definitions;
			out.definitionProbabilities = duplicate(in.definitionProbabilities);

			out.callParameters = in.callParameters;
			out.callParameterProbabilityTrue = duplicate(in.callParameterProbabilityTrue);

			out.members = in.members;
			out.memberProbabilityTrue = duplicate(in.memberProbabilityTrue);

			return normalize(out);
		}

		if (in.classContexts.length == 1) {
			out.classContexts = new ITypeName[] { in.classContexts[0], PBNModelConstants.DUMMY_CCTX };
			out.classContextProbabilities = round(extend(in.classContextProbabilities));
		} else {
			out.classContexts = in.classContexts;
			out.classContextProbabilities = round(in.classContextProbabilities);
		}

		if (in.methodContexts.length == 1) {
			out.methodContexts = new IMethodName[] { in.methodContexts[0], PBNModelConstants.DUMMY_MCTX };
			out.methodContextProbabilities = round(extend(in.methodContextProbabilities));
		} else {
			out.methodContexts = in.methodContexts;
			out.methodContextProbabilities = round(in.methodContextProbabilities);
		}

		if (in.definitions.length == 1) {
			out.definitions = new IDefinition[] { in.definitions[0], PBNModelConstants.DUMMY_DEFINITION };
			out.definitionProbabilities = round(extend(in.definitionProbabilities));
		} else {
			out.definitions = in.definitions;
			out.definitionProbabilities = round(in.definitionProbabilities);
		}

		out.callParameters = in.callParameters;
		out.callParameterProbabilityTrue = round(in.callParameterProbabilityTrue);

		out.members = in.members;
		out.memberProbabilityTrue = round(in.memberProbabilityTrue);

		return out;
	}

	private static double[] duplicate(double[] in) {
		double[] out = new double[2 * in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = in[i];
			out[i + in.length] = in[i];
		}
		return out;
	}

	private static double[] extend(double[] in) {
		double[] out = new double[2 * in.length];
		for (int i = 0; i < in.length; i++) {
			out[2 * i] = in[i];
			out[2 * i + 1] = 0;
		}
		return out;
	}

	private static double[] round(double[] in) {
		assertNotNull(in);
		double[] out = new double[in.length];
		for (int i = 0; i < in.length; i++) {
			double v = Math.max(in[i], PRECISION);
			v = MathUtils.round(v, PRECISION_SCALE);
			out[i] = Math.min(v, 1 - PRECISION);
		}
		return out;
	}

	/**
	 * validates that this PBN model contains data that can be represented in a
	 * Bayesian network.
	 * 
	 * @throws AssertionException
	 *             is thrown for invalid models
	 */
	public static void assertValidity(PBNModel m) {
		assertNotNull(m.type);
		assertArray(m.patternProbabilities);
		assertArray(m.classContexts);
		assertArray(m.classContextProbabilities);
		assertArray(m.methodContexts);
		assertArray(m.methodContextProbabilities);
		assertArray(m.definitions);
		assertArray(m.definitionProbabilities);
		assertArray(m.callParameters);
		assertArray(m.callParameterProbabilityTrue);
		assertArray(m.members);
		assertArray(m.memberProbabilityTrue);

		int numPatterns = m.patternProbabilities.length;
		assertArraySize(numPatterns, m.classContexts.length, m.classContextProbabilities.length);
		assertArraySize(numPatterns, m.methodContexts.length, m.methodContextProbabilities.length);
		assertArraySize(numPatterns, m.definitions.length, m.definitionProbabilities.length);
		assertArraySize(numPatterns, m.callParameters.length, m.callParameterProbabilityTrue.length);
		assertArraySize(numPatterns, m.members.length, m.memberProbabilityTrue.length);

		assertGreaterThan(m.numObservations, 0);
		assertGreaterThan(numPatterns, 1);
		assertVals(m.patternProbabilities);
		assertVals(m.classContextProbabilities);
		assertVals(m.methodContextProbabilities);
		assertVals(m.definitionProbabilities);
		assertVals(m.callParameterProbabilityTrue);
		assertVals(m.memberProbabilityTrue);

		assertSum(m.patternProbabilities);
		assertSums(m.classContextProbabilities, numPatterns);
		assertSums(m.methodContextProbabilities, numPatterns);
		assertSums(m.definitionProbabilities, numPatterns);

		assertTrue(m.classContexts.length > 1);
		assertTrue(m.methodContexts.length > 1);
		assertTrue(m.definitions.length > 1);
	}

	private static void assertArray(double[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
	}

	private static void assertArray(IName[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (IName n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertArray(IDefinition[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (IDefinition n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertArray(ICallParameter[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (ICallParameter n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertNotNull(Object o) {
		if (o == null) {
			throw new ValidationException("Object should not be null.");
		}
	}

	private static void assertGreaterThan(int a, int b) {
		if (a <= b) {
			throw new ValidationException("Number is too small.");
		}
	}

	private static void assertArraySize(int numPatterns, int numItems, int actual) {
		int expected = numPatterns * numItems;
		if (expected != actual) {
			String msg = "Unexcpected array size, expected %d (%d patterns * %d items), but got %d";
			throw new ValidationException(format(msg, expected, numPatterns, numItems, actual));
		}
	}

	private static void assertSums(double[] sums, int numPatterns) {
		int numItems = sums.length / numPatterns;
		for (int start = 0; start < sums.length; start += numItems) {
			double sum = 0;
			for (int i = 0; i < numItems; i++) {
				sum += sums[start + i];
			}
			assertSum(sum);
		}
	}

	private static void assertSum(double[] ps) {
		double sum = 0;
		for (double p : ps) {
			sum += p;
		}
		assertSum(sum);
	}

	private static void assertVals(double[] vals) {
		for (double val : vals) {
			assertVal(val);
		}
	}

	private static void assertVal(double val) {
		if (val < PRECISION || val > 1 - PRECISION) {
			String msg = "Value %f exceeds allowed range [%f,%f]";
			throw new ValidationException(format(msg, val, PRECISION, 1 - PRECISION));
		}

		double rounded = MathUtils.round(val, PRECISION_SCALE);
		if (rounded != val) {
			String msg = "Unexpected, value %f has not been rounded to %f.";
			throw new ValidationException(format(msg, val, rounded));
		}
	}

	private static void assertSum(double sum) {
		double delta = sum - 1;
		if (delta < -0.01 || delta > 0.01) {
			String msg = "Value %f exceeds acceptable deviation [-1.01,1.01]";
			throw new ValidationException(format(msg, sum));
		}
	}

	private static void assertTrue(boolean condition) {
		if (!condition) {
			throw new ValidationException("Unexpected, should be true.");
		}
	}
}