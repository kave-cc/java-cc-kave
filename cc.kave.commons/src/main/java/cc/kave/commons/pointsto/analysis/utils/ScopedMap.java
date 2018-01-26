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
package cc.kave.commons.pointsto.analysis.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A map that retains its entries according to scopes. Can be used to realize a symbol table.
 */
public class ScopedMap<K, V> {

	private Map<K, Deque<V>> entries = new HashMap<>();
	private Deque<Set<K>> keyStack = new ArrayDeque<>();

	public void enter() {
		keyStack.addFirst(new HashSet<>());
	}

	public void leave() {
		Set<K> levelKeys = keyStack.removeFirst();
		for (K key : levelKeys) {
			Deque<V> values = entries.get(key);
			values.removeFirst();

			// remove empty stacks
			if (values.isEmpty()) {
				entries.remove(key);
			}
		}
	}

	/**
	 * Checks whether the key exists in any scope of the map.
	 */
	public boolean contains(K key) {
		Deque<V> values = entries.get(key);
		return values != null && !values.isEmpty();
	}

	public boolean currentScopeContains(K key) {
		return keyStack.getFirst().contains(key);
	}

	public V get(K key) {
		Deque<V> values = entries.get(key);

		if (values == null || values.isEmpty()) {
			return null;
		}

		return values.getFirst();
	}

	/**
	 * @throws DuplicateKeyException
	 *             If the key already exists in the current scope
	 */
	public void create(K key, V value) {
		if (keyStack.getFirst().contains(key)) {
			throw new DuplicateKeyException(key);
		}

		keyStack.getFirst().add(key);
		Deque<V> values = entries.get(key);
		if (values == null) {
			values = new ArrayDeque<>();
			entries.put(key, values);
		}
		values.addFirst(value);
	}

	/**
	 * @return {@code true} if an existing entry in the current scope has been updated, {@code false} if a new entry has
	 *         been created
	 */
	public boolean createOrUpdate(K key, V value) {
		boolean update = keyStack.getFirst().contains(key);

		Deque<V> values = entries.get(key);
		if (values == null) {
			values = new ArrayDeque<>();
			entries.put(key, values);
		}

		if (update) {
			values.removeFirst();
			values.addFirst(value);
		} else {
			keyStack.getFirst().add(key);
			values.addFirst(value);
		}

		return update;
	}

	public static class DuplicateKeyException extends IllegalArgumentException {

		private static final long serialVersionUID = 8432691707057053261L;

		public DuplicateKeyException(Object key) {
			super("Key '" + key.toString() + "' already exists in the current scope");
		}
	}
}
