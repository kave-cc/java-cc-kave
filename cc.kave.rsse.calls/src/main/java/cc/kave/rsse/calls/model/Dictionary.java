/**
 * Copyright 2011 Sebastian Proksch
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
package cc.kave.rsse.calls.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.utils.ToStringUtils;

public class Dictionary<T> {

	// keep transient to prevent serialization
	private transient final HashMap<T, Integer> cache = new HashMap<>();

	// keep concrete list type to enforce deserialized type
	// keep final to enforce usage of "add" that maintains cache
	private final ArrayList<T> entries = Lists.newArrayList();

	public int add(T entry) {
		if (contains(entry)) {
			return getId(entry);
		} else {
			entries.add(entry);
			Integer id = entries.size() - 1;
			cache.put(entry, id);
			return id;
		}
	}

	public void addAll(Collection<T> ts) {
		for (T t : ts) {
			add(t);
		}
	}

	public void remove(T entry) {
		entries.remove(entry);
		cache.remove(entry);
	}

	public int getId(T entry) {
		Integer id = cache.get(entry);
		if (id != null)
			return id;
		else
			return -1;
	}

	public T getEntry(int id) {
		return entries.get(id);
	}

	public Set<T> getAllEntries() {
		Set<T> allEntries = new LinkedHashSet<T>();
		allEntries.addAll(entries);
		return allEntries;
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S getFirstEntry(Class<S> classOfS) {
		for (T entry : getAllEntries()) {
			if (classOfS.isInstance(entry)) {
				return (S) entry;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <S extends T> Set<S> getAllEntries(Class<S> classOfS) {
		LinkedHashSet<S> res = new LinkedHashSet<S>();
		for (T entry : getAllEntries()) {
			if (classOfS.isInstance(entry)) {
				res.add((S) entry);
			}
		}
		return res;
	}

	public boolean contains(T entry) {
		return cache.containsKey(entry);
	}

	public int size() {
		return entries.size();
	}

	public void clear() {
		entries.clear();
		cache.clear();
	}

	public Set<String> diff(Dictionary<T> other) {
		Set<String> diff = Sets.newLinkedHashSet();
		for (T t : getAllEntries()) {
			if (!other.contains(t)) {
				diff.add("+" + t + "+");
			}
		}
		for (T t : other.getAllEntries()) {
			if (!contains(t)) {
				diff.add("-" + t + "-");
			}
		}
		return diff;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entries.hashCode();
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
		@SuppressWarnings("rawtypes")
		Dictionary other = (Dictionary) obj;
		if (!entries.equals(other.entries))
			return false;
		return true;
	}
}