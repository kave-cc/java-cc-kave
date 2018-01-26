/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.mining.calls;

import java.util.Comparator;
import java.util.TreeSet;

import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Sets;

public class ProposalHelper {
	public static <T extends Comparable<T>> TreeSet<Tuple<T, Double>> createSortedSet() {
		final TreeSet<Tuple<T, Double>> res = Sets.newTreeSet(new Comparator<Tuple<T, Double>>() {
			@Override
			public int compare(final Tuple<T, Double> o1, final Tuple<T, Double> o2) {
				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int orderOfFirstTupleMember = o1.getFirst().compareTo(o2.getFirst());
					return orderOfFirstTupleMember;
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
}