/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.extraction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import com.google.common.collect.Iterators;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class Callpath implements Iterable<IMethodName> {

	private Deque<IMethodName> path = new ArrayDeque<>();

	public Callpath(IMethodName root) {
		path.addLast(root);
	}

	public void enterMethod(IMethodName method) {
		path.addLast(method);
	}

	public void leaveMethod() {
		path.removeLast();
		if (path.isEmpty()) {
			throw new IllegalStateException("Path must not be empty");
		}
	}

	/**
	 * Retrieves the method at the start of this path.
	 */
	public IMethodName getFirst() {
		return path.getFirst();
	}

	/**
	 * Retrieves the method at the end of this path.
	 */
	public IMethodName getLast() {
		return path.getLast();
	}

	public boolean contains(IMethodName method) {
		return path.contains(method);
	}

	public int size() {
		return path.size();
	}

	@Override
	public Iterator<IMethodName> iterator() {
		return path.iterator();
	}

	public Iterator<IMethodName> reverseIterator() {
		return path.descendingIterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');

		Iterator<IMethodName> iter = path.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next().toString());
			if (iter.hasNext()) {
				builder.append(", ");
			}
		}

		builder.append(']');
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (IMethodName method : path) {
			result = prime * result + method.hashCode();
		}

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
		Callpath other = (Callpath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (path.size() != other.path.size()) {
			return false;
		} else if (!Iterators.elementsEqual(path.iterator(), other.path.iterator())) {
			return false;
		}

		return true;
	}
}