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

import java.util.Arrays;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.impl.Definitions;

public class PBNModel {

	public ITypeName type;
	public double[] patternProbabilities;

	// [ctxIdx, pattern] == true
	public IMethodName[] methodContexts;
	public double[][] methodContextProbabilities;

	// [ctxIdx, pattern] == true
	public ITypeName[] classContexts;
	public double[][] classContextProbabilities;

	// [methodIdx, patternIdx] = probability for true (false = 1-p(true))
	public Definitions[] definitionSites;
	public double[][] definitionSiteProbabilities;

	// [methodIdx, patternIdx] = probability for true (false = 1-p(true))
	public IMethodName[] callSites;
	public double[][] callSiteProbabilityTrue;

	// [methodIdx, patternIdx] = probability for true (false = 1-p(true))
	public IMethodName[] parameterSites;
	public double[][] parameterSiteProbabilityTrue;

	/**
	 * Calculate the required memory for this model instance.
	 * @return model size in Byte
	 */
	public long getSize() {
		return -1;
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
		Asserts.assertNotNull(callSites);
		Asserts.assertNotNull(callSiteProbabilityTrue);
		Asserts.assertNotNull(parameterSites);
		Asserts.assertNotNull(parameterSiteProbabilityTrue);

		Asserts.assertGreaterThan(patternProbabilities.length, 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(callSiteProbabilityTrue);
		result = prime * result + Arrays.hashCode(callSites);
		result = prime * result + Arrays.deepHashCode(classContextProbabilities);
		result = prime * result + Arrays.hashCode(classContexts);
		result = prime * result + Arrays.deepHashCode(definitionSiteProbabilities);
		result = prime * result + Arrays.hashCode(definitionSites);
		result = prime * result + Arrays.deepHashCode(methodContextProbabilities);
		result = prime * result + Arrays.hashCode(methodContexts);
		result = prime * result + Arrays.deepHashCode(parameterSiteProbabilityTrue);
		result = prime * result + Arrays.hashCode(parameterSites);
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
		if (!Arrays.deepEquals(callSiteProbabilityTrue, other.callSiteProbabilityTrue))
			return false;
		if (!Arrays.equals(callSites, other.callSites))
			return false;
		if (!Arrays.deepEquals(classContextProbabilities, other.classContextProbabilities))
			return false;
		if (!Arrays.equals(classContexts, other.classContexts))
			return false;
		if (!Arrays.deepEquals(definitionSiteProbabilities, other.definitionSiteProbabilities))
			return false;
		if (!Arrays.equals(definitionSites, other.definitionSites))
			return false;
		if (!Arrays.deepEquals(methodContextProbabilities, other.methodContextProbabilities))
			return false;
		if (!Arrays.equals(methodContexts, other.methodContexts))
			return false;
		if (!Arrays.deepEquals(parameterSiteProbabilityTrue, other.parameterSiteProbabilityTrue))
			return false;
		if (!Arrays.equals(parameterSites, other.parameterSites))
			return false;
		if (!Arrays.equals(patternProbabilities, other.patternProbabilities))
			return false;
		return true;
	}
}