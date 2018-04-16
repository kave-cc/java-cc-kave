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
package cc.kave.rsse.calls.model.features;

import static cc.kave.commons.assertions.Asserts.assertGreaterOrEqual;
import static cc.kave.commons.assertions.Asserts.assertLessOrEqual;
import static java.lang.String.format;
import static java.lang.System.arraycopy;

import java.util.Arrays;

import org.apache.commons.math.util.MathUtils;

import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.Dictionary;

public class Pattern {

	public static final int PRECISION_SCALE = 6;
	public static final double PRECISION = Math.pow(0.1, PRECISION_SCALE);

	public final int numObservations;
	private final double[] probabilities;
	private final Dictionary<IFeature> dict;

	public Pattern(int numObservations, double[] probabilities, Dictionary<IFeature> dict) {
		assertPositive(numObservations);
		assertDict(dict);
		assertArr(probabilities, dict);
		this.numObservations = numObservations;
		this.probabilities = probabilities;
		this.dict = dict;

		for (int i = 0; i < probabilities.length; i++) {
			this.probabilities[i] = round(probabilities[i]);
		}
	}

	public Pattern(int numObservations, Dictionary<IFeature> dict) {
		assertPositive(numObservations);
		assertDict(dict);
		this.numObservations = numObservations;
		this.probabilities = new double[dict.size()];
		this.dict = dict;
	}

	public void setProbability(IFeature feature, double probability) {
		assertFeature(feature);
		assertGreaterOrEqual(probability, 0);
		assertLessOrEqual(probability, 1);

		int i = dict.getId(feature);
		probability = round(probability);
		probabilities[i] = probability;
	}

	public double getProbability(IFeature feature) {
		assertFeature(feature);
		int i = dict.getId(feature);
		return probabilities[i];
	}

	public Pattern clone() {
		return new Pattern(numObservations, probabilities, dict);
	}

	public double[] cloneProbabilities() {
		double[] out = new double[probabilities.length];
		arraycopy(probabilities, 0, out, 0, probabilities.length);
		return out;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dict.hashCode();
		result = prime * result + numObservations;
		result = prime * result + Arrays.hashCode(probabilities);
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
		Pattern other = (Pattern) obj;
		if (!dict.equals(other.dict))
			return false;
		if (numObservations != other.numObservations)
			return false;
		if (!Arrays.equals(probabilities, other.probabilities))
			return false;
		return true;
	}

	private static void assertNotNull(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("Reference should not be null.");
		}
	}

	private void assertFeature(IFeature f) {
		assertNotNull(f);
		if (!dict.contains(f)) {
			throw new IllegalArgumentException(format("Feature '%s' does not exist in dictionary.", f));
		}
	}

	private static void assertDict(Dictionary<IFeature> d) {
		assertNotNull(d);
		if (d.size() == 0) {
			throw new IllegalArgumentException("Dictionary is empty.");
		}
	}

	private static void assertPositive(int i) {
		if (i < 1) {
			throw new IllegalArgumentException("Unexpected, numObservations should be positive.");
		}
	}

	private static void assertArr(double[] arr, Dictionary<?> dict) {
		assertNotNull(arr);
		for (double d : arr) {
			if (d < 0 || d > 1) {
				throw new IllegalArgumentException(format("Array contains invalid probability (%f).", d));
			}
		}
		if (arr.length != dict.size()) {
			throw new IllegalArgumentException(
					format("Sizes odo not match: array has %d entries, dictionary has %d.", arr.length, dict.size()));
		}
	}

	private static double round(double p) {
		return MathUtils.round(p, PRECISION_SCALE);
	}
}