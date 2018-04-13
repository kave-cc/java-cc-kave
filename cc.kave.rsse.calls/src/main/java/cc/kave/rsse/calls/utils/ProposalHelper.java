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
package cc.kave.rsse.calls.utils;

import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

public class ProposalHelper {
	public static <T extends Comparable<T>> TreeSet<Pair<T, Double>> createSortedSet() {
		final TreeSet<Pair<T, Double>> res = Sets.newTreeSet(new Comparator<Pair<T, Double>>() {
			@Override
			public int compare(final Pair<T, Double> o1, final Pair<T, Double> o2) {
				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getRight(), o1.getRight());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int orderOfFirstTupleMember = o1.getLeft().compareTo(o2.getLeft());
					return orderOfFirstTupleMember;
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
}