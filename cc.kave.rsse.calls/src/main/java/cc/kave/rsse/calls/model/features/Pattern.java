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

import static cc.kave.commons.assertions.Asserts.assertFalse;
import static cc.kave.commons.assertions.Asserts.assertGreaterOrEqual;
import static cc.kave.commons.assertions.Asserts.assertGreaterThan;
import static cc.kave.commons.assertions.Asserts.assertLessOrEqual;
import static org.apache.commons.math.util.MathUtils.round;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.Dictionary;

public class Pattern {

	public static final int PRECISION_SCALE = 6;
	public static final double PRECISION = Math.pow(0.1, PRECISION_SCALE);

	public final int numObservations;

	private final Map<IFeature, Double> probabilities = new LinkedHashMap<IFeature, Double>();

	public Pattern(String name, int numObservations) {
		assertFalse(name == null);
		assertFalse(name.isEmpty());
		assertGreaterThan(numObservations, 0);
		this.numObservations = numObservations;
	}

	public Pattern(int count, double[] arr, Dictionary<IFeature> dict) {
		numObservations = count;
	}

	public void setProbability(IFeature feature, double probability) {
		probability = round(probability, PRECISION_SCALE);
		assertGreaterOrEqual(probability, 0);
		assertLessOrEqual(probability, 1);

		probabilities.put(feature, probability);
	}

	public double getProbability(IFeature feature) {
		Double propability = probabilities.get(feature);
		if (propability == null)
			return 0.0;
		else
			return propability;
	}

	public Pattern clone(String nameOfNewPattern) {
		Pattern clone = new Pattern(nameOfNewPattern, numObservations);
		for (IFeature f : probabilities.keySet()) {
			Double probability = probabilities.get(f);
			clone.probabilities.put(f, probability);
		}
		return clone;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numObservations;
		result = prime * result + probabilities.hashCode();
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
		if (numObservations != other.numObservations)
			return false;
		if (!probabilities.equals(other.probabilities))
			return false;
		return true;
	}
}