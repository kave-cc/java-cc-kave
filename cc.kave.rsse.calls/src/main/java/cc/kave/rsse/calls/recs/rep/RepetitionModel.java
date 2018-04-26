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
package cc.kave.rsse.calls.recs.rep;

import static org.apache.commons.math.util.MathUtils.round;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;

public class RepetitionModel {

	public static transient final int PRECISION_SCALE = 6;
	public static transient final double PRECISION = Math.pow(0.1, PRECISION_SCALE);

	public ITypeName type;
	private final Map<IMemberName, Double> probabilities = new HashMap<>();

	public RepetitionModel() {
	}

	public RepetitionModel(ITypeName type) {
		assertNotNull(type);
		this.type = type;
	}

	public boolean hasRepetitionProbability(IMemberName m) {
		assertNotNull(m);
		return probabilities.containsKey(m);
	}

	public Set<IMemberName> getRepetitionKeys() {
		return probabilities.keySet();
	}

	public void setRepetitionProbability(IMemberName m, double probability) {
		assertNotNull(m);
		assertProbability(probability);
		probability = round(probability, PRECISION_SCALE);
		if (probability != 0) {
			probabilities.put(m, probability);
		}
	}

	public double getRepetitionProbability(IMemberName m) {
		assertNotNull(m);
		if (probabilities.containsKey(m)) {
			return probabilities.get(m);
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + probabilities.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		RepetitionModel other = (RepetitionModel) obj;
		if (!probabilities.equals(other.probabilities))
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

	private static void assertNotNull(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("Argument is null.");
		}
	}

	private static void assertProbability(double p) {
		if (p < 0 || p > 1) {
			throw new IllegalArgumentException("Probability exceeds the allowed [0,1] range.");
		}
	}
}