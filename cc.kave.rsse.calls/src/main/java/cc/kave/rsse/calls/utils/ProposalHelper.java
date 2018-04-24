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

import cc.kave.commons.model.naming.codeelements.IMemberName;

public class ProposalHelper {
	public static <T extends IMemberName> TreeSet<Pair<T, Double>> createSortedSet() {
		final TreeSet<Pair<T, Double>> res = Sets.newTreeSet(new Comparator<Pair<T, Double>>() {
			@Override
			public int compare(final Pair<T, Double> o1, final Pair<T, Double> o2) {

				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getRight(), o1.getRight());
				if (valueOrdering != 0) {
					return valueOrdering;
				}

				// second, order by simpleName of the member
				T a = o1.getLeft();
				T b = o2.getLeft();
				int nameOrdering = a.getName().compareTo(b.getName());
				if (nameOrdering != 0) {
					return nameOrdering;
				}

				// third, order by the full identifier
				return a.getIdentifier().compareTo(b.getIdentifier());
			}
		});
		return res;
	}
}