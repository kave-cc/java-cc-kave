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

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class PBNModel {

	public static final transient int PRECISION_SCALE = 6;
	public static final transient double PRECISION = 0.000001;

	public ITypeName type;
	public int numObservations;

	public ITypeName[] classContexts;
	public IMethodName[] methodContexts;
	public IDefinition[] definitions;
	public ICallParameter[] callParameters;
	public IMemberName[] members;

	public double[] patternProbabilities;
	// [p1item1, p1item2, ..., pNitemN]
	public double[] classContextProbabilities;
	public double[] methodContextProbabilities;
	public double[] definitionProbabilities;
	public double[] callParameterProbabilityTrue;
	public double[] memberProbabilityTrue;

	/**
	 * Calculate the required memory for this model instance. For now, it only
	 * reflects the stored numbers and ignores all text labels (e.g., type name).
	 * 
	 * @return model size in Byte
	 */
	public long getSize() {
		long size = 4; // numObservations
		size += 4 * patternProbabilities.length;
		size += 4 * classContextProbabilities.length;
		size += 4 * methodContextProbabilities.length;
		size += 4 * definitionProbabilities.length;
		size += 4 * callParameterProbabilityTrue.length;
		size += 4 * memberProbabilityTrue.length;
		return size;
	}

	public double[][] getCCtxByPattern() {
		return splitByPattern(classContextProbabilities, patternProbabilities.length);
	}

	public double[][] getMCtxByPattern() {
		return splitByPattern(methodContextProbabilities, patternProbabilities.length);
	}

	public double[][] getDefByPattern() {
		return splitByPattern(definitionProbabilities, patternProbabilities.length);
	}

	public double[][] getParamByPattern() {
		return splitByPattern(callParameterProbabilityTrue, patternProbabilities.length);
	}

	public double[][] getMemberByPattern() {
		return splitByPattern(memberProbabilityTrue, patternProbabilities.length);
	}

	public double[][] getPatternByCCtx() {
		return splitByItem(classContextProbabilities, patternProbabilities.length);
	}

	public double[][] getPatternByMCtx() {
		return splitByItem(methodContextProbabilities, patternProbabilities.length);
	}

	public double[][] getPatternByDef() {
		return splitByItem(definitionProbabilities, patternProbabilities.length);
	}

	public double[][] getPatternByParam() {
		return splitByItem(callParameterProbabilityTrue, patternProbabilities.length);
	}

	public double[][] getPatternByMember() {
		return splitByItem(memberProbabilityTrue, patternProbabilities.length);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + numObservations;
		result = prime * result + Arrays.hashCode(classContexts);
		result = prime * result + Arrays.hashCode(methodContexts);
		result = prime * result + Arrays.hashCode(definitions);
		result = prime * result + Arrays.hashCode(callParameters);
		result = prime * result + Arrays.hashCode(members);
		result = prime * result + Arrays.hashCode(patternProbabilities);
		result = prime * result + Arrays.hashCode(classContextProbabilities);
		result = prime * result + Arrays.hashCode(methodContextProbabilities);
		result = prime * result + Arrays.hashCode(definitionProbabilities);
		result = prime * result + Arrays.hashCode(callParameterProbabilityTrue);
		result = prime * result + Arrays.hashCode(memberProbabilityTrue);
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
		if (!Arrays.equals(callParameterProbabilityTrue, other.callParameterProbabilityTrue))
			return false;
		if (!Arrays.equals(callParameters, other.callParameters))
			return false;
		if (!Arrays.equals(classContextProbabilities, other.classContextProbabilities))
			return false;
		if (!Arrays.equals(classContexts, other.classContexts))
			return false;
		if (!Arrays.equals(definitionProbabilities, other.definitionProbabilities))
			return false;
		if (!Arrays.equals(definitions, other.definitions))
			return false;
		if (!Arrays.equals(memberProbabilityTrue, other.memberProbabilityTrue))
			return false;
		if (!Arrays.equals(members, other.members))
			return false;
		if (!Arrays.equals(methodContextProbabilities, other.methodContextProbabilities))
			return false;
		if (!Arrays.equals(methodContexts, other.methodContexts))
			return false;
		if (numObservations != other.numObservations)
			return false;
		if (!Arrays.equals(patternProbabilities, other.patternProbabilities))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	// ###################################################################################################

	// arr[patternId][itemId]
	private static double[][] splitByPattern(double[] probs, int numPatterns) {
		int numItems = probs.length / numPatterns;
		double[][] out = new double[numPatterns][];
		int idx = 0;
		for (int np = 0; np < numPatterns; np++) {
			out[np] = new double[numItems];
			for (int ni = 0; ni < numItems; ni++) {
				out[np][ni] = probs[idx++];
			}
		}
		return out;
	}

	// arr[itemId][patternId]
	private static double[][] splitByItem(double[] probs, int numPatterns) {
		int numItems = probs.length / numPatterns;
		double[][] out = new double[numItems][];
		int idx = 0;
		for (int np = 0; np < numPatterns; np++) {
			for (int ni = 0; ni < numItems; ni++) {
				if (np == 0) {
					out[ni] = new double[numPatterns];
				}
				out[ni][np] = probs[idx++];
			}
		}
		return out;
	}
}