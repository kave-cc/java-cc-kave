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

import java.util.Arrays;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.exceptions.AssertionException;
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

	public IDefinition[] definitionSites;
	public double[] definitionSiteProbabilities;

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
		Asserts.assertNotNull(patternProbabilities);
		Asserts.assertNotNull(methodContexts);
		Asserts.assertNotNull(methodContextProbabilities);
		Asserts.assertNotNull(classContexts);
		Asserts.assertNotNull(classContextProbabilities);
		Asserts.assertNotNull(definitionSites);
		Asserts.assertNotNull(definitionSiteProbabilities);
		// Asserts.assertNotNull(callSites);
		// Asserts.assertNotNull(callSiteProbabilityTrue);
		Asserts.assertNotNull(callParameters);
		Asserts.assertNotNull(callParameterProbabilityTrue);

		Asserts.assertGreaterThan(patternProbabilities.length, 0);

		fail("check that all lengths % getNumPatterns == 0 and all lenghts.length > 0");
		fail("check that all nodes have minimum size");
		fail("check that all values are smoothed (val > 0 && val < 1) and rounded (round(val) == val)");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// result = prime * result + Arrays.hashCode(callSiteProbabilityTrue);
		// result = prime * result + Arrays.hashCode(callSites);
		result = prime * result + Arrays.hashCode(classContextProbabilities);
		result = prime * result + Arrays.hashCode(classContexts);
		result = prime * result + Arrays.hashCode(definitionSiteProbabilities);
		result = prime * result + Arrays.hashCode(definitionSites);
		result = prime * result + Arrays.hashCode(methodContextProbabilities);
		result = prime * result + Arrays.hashCode(methodContexts);
		result = prime * result + Arrays.hashCode(callParameterProbabilityTrue);
		result = prime * result + Arrays.hashCode(callParameters);
		result = prime * result + Arrays.hashCode(patternProbabilities);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PBNModel other = (PBNModel) obj;
		// if (!Arrays.equals(callSiteProbabilityTrue, other.callSiteProbabilityTrue))
		// return false;
		// if (!Arrays.equals(callSites, other.callSites))
		// return false;
		if (!Arrays.equals(classContextProbabilities, other.classContextProbabilities))
			return false;
		if (!Arrays.equals(classContexts, other.classContexts))
			return false;
		if (!Arrays.equals(definitionSiteProbabilities, other.definitionSiteProbabilities))
			return false;
		if (!Arrays.equals(definitionSites, other.definitionSites))
			return false;
		if (!Arrays.equals(methodContextProbabilities, other.methodContextProbabilities))
			return false;
		if (!Arrays.equals(methodContexts, other.methodContexts))
			return false;
		if (!Arrays.equals(callParameterProbabilityTrue, other.callParameterProbabilityTrue))
			return false;
		if (!Arrays.equals(callParameters, other.callParameters))
			return false;
		if (!Arrays.equals(patternProbabilities, other.patternProbabilities))
			return false;
		return true;
	}

	@Override
	public String toString() {
		fail("TODO");
		return super.toString();
	}

	// arr[patternId][itemId]
	private static double[][] splitByPattern(double[] probs, int numPatterns) {
		return splitByItem(probs, numPatterns);
	}

	// arr[itemId][patternId]
	private static double[][] splitByItem(double[] probs, int numPatterns) {
		// TODO Auto-generated method stub
		return null;
	}
}