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

import static cc.kave.commons.assertions.Asserts.fail;
import static java.lang.String.format;

import org.apache.commons.math.util.MathUtils;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class PBNModel {

	public static int PRECISION_SCALE = 6;
	public static double PRECISION = Math.pow(0.1, PRECISION_SCALE);

	public ITypeName type;
	public int numObservations;
	public double[] patternProbabilities;

	// probability for observing mCtx by pattern
	// [p1item1, p1item2, ..., pNitemN]
	public IMethodName[] methodContexts;
	public double[] methodContextProbabilities;

	public ITypeName[] classContexts;
	public double[] classContextProbabilities;

	public IDefinition[] definitions;
	public double[] definitionProbabilities;

	public ICallParameter[] callParameters;
	public double[] callParameterProbabilityTrue;

	public IMemberName[] members;
	public double[] memberProbabilityTrue;

	/**
	 * Calculate the required memory for this model instance.
	 * 
	 * @return model size in Byte
	 */
	public long getSize() {
		return -1;
		// patterns = addNode(PATTERN_TITLE, numPatterns);
		// addConditionedNode(CLASS_CONTEXT_TITLE, numInClass);
		// addConditionedNode(METHOD_CONTEXT_TITLE, numInMethod);
		// addConditionedNode(DEFINITION_TITLE, numDef);
		// for (int i = 0; i < numMethods; i++) {
		// addConditionedNode(CALL_PREFIX + m(i), 2);
		// }
		// for (int i = 0; i < numParams; i++) {
		// addConditionedNode(PARAMETER_PREFIX + m(i), 2);
		// }
		// // Options opts = OptionsBuilder.pbn(1).option("prec", "DOUBLE").get();
		// // PBNRecommender rec = new PBNRecommender(null, opts);
		// return -1;
	}

	public double[][] getMethodCtxByPattern() {
		return splitByPattern(methodContextProbabilities, patternProbabilities.length);
	}

	public double[][] getPatternByMethodCtx() {
		return splitByItem(methodContextProbabilities, patternProbabilities.length);
	}

	/**
	 * validates that this PBN model contains data that can be represented in a
	 * Bayesian network.
	 * 
	 * @throws AssertionException
	 *             is thrown for invalid models
	 */
	public void assertValidity() {
		assertNotNull(type);
		assertArray(patternProbabilities);
		assertArray(classContexts);
		assertArray(classContextProbabilities);
		assertArray(methodContexts);
		assertArray(methodContextProbabilities);
		assertArray(definitions);
		assertArray(definitionProbabilities);
		assertArray(callParameters);
		assertArray(callParameterProbabilityTrue);
		assertArray(members);
		assertArray(memberProbabilityTrue);

		int numPatterns = patternProbabilities.length;
		assertArraySize(numPatterns, classContexts.length, classContextProbabilities.length);
		assertArraySize(numPatterns, methodContexts.length, methodContextProbabilities.length);
		assertArraySize(numPatterns, definitions.length, definitionProbabilities.length);
		assertArraySize(numPatterns, callParameters.length, callParameterProbabilityTrue.length);
		assertArraySize(numPatterns, members.length, memberProbabilityTrue.length);

		assertGreaterThan(numObservations, 0);
		assertVals(patternProbabilities);
		assertVals(classContextProbabilities);
		assertVals(methodContextProbabilities);
		assertVals(definitionProbabilities);
		assertVals(callParameterProbabilityTrue);
		assertVals(memberProbabilityTrue);

		assertSum(patternProbabilities);
		assertSums(classContextProbabilities, numPatterns);
		assertSums(methodContextProbabilities, numPatterns);
		assertSums(definitionProbabilities, numPatterns);

		assertTrue(classContexts.length > 1);
		assertTrue(methodContexts.length > 1);
		assertTrue(definitions.length > 1);
	}

	@Override
	public String toString() {
		fail("TODO");
		return super.toString();
	}

	// ###################################################################################################

	// arr[patternId][itemId]
	private static double[][] splitByPattern(double[] probs, int numPatterns) {
		return splitByItem(probs, numPatterns);
	}

	// arr[itemId][patternId]
	private static double[][] splitByItem(double[] probs, int numPatterns) {
		int numItems = probs.length / numPatterns;
		// TODO Auto-generated method stub
		return null;
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