/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SublistSelector {

	public static <T> List<T> pickRandomSublist(List<T> in, int numToPick) {

		List<T> sublist = shuffle(in);

		if (sublist.size() > numToPick) {

			Iterator<T> iterator = sublist.iterator();
			skip(numToPick, iterator);
			removeAllRemaining(iterator);
		}

		return sublist;
	}

	public static <T> List<T> shuffle(List<T> in) {
		LinkedList<T> sublist = Lists.<T>newLinkedList();
		copyTo(in, sublist);
		Collections.shuffle(sublist);
		return sublist;
	}

	public static <T> Set<T> shuffle(Set<T> in) {
		List<T> sublist = Lists.<T>newLinkedList();
		copyTo(in, sublist);
		Collections.shuffle(sublist);
		Set<T> subset = Sets.newLinkedHashSet(sublist);
		return subset;
	}

	public static <T> List<T> forceShuffle(List<T> in) {
		LinkedList<T> sublist = Lists.<T>newLinkedList();
		copyTo(in, sublist);
		if (sublist.size() > 1) {
			while (sublist.equals(in)) {
				Collections.shuffle(sublist);
			}
		}
		return sublist;
	}

	private static void skip(int numToPick, Iterator<?> iterator) {
		int i = 0;
		while (i < numToPick) {
			iterator.next();
			i++;
		}
	}

	private static void removeAllRemaining(Iterator<?> iterator) {
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	private static <T> void copyTo(Iterable<T> in, Collection<T> to) {
		for (T elem : in) {
			to.add(elem);
		}
	}
}