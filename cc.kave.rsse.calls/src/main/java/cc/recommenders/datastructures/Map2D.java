/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.datastructures;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Maps;

public class Map2D<K1, K2, V> {

	private Map<K1, Map<K2, V>> vs = Maps.newLinkedHashMap();

	public V getOrAdd(K1 k1, K2 k2, V vDefault) {
		Map<K2, V> m2 = vs.get(k1);
		if (m2 == null) {
			m2 = Maps.newLinkedHashMap();
			vs.put(k1, m2);
		}
		V v = m2.get(k2);
		if (v == null) {
			v = vDefault;
			m2.put(k2, v);
		}
		return v;
	}

	public Map<K2, V> get(K1 k1) {
		return vs.get(k1);
	}

	public static <K1, K2, V> Map2D<K1, K2, V> create() {
		return new Map2D<K1, K2, V>();
	}

	public void put(K1 k1, Map<K2, V> map) {
		vs.put(k1, map);
	}

	public boolean containsKey(K1 k1) {
		return vs.containsKey(k1);
	}

	public Set<K1> keySet() {
		return vs.keySet();
	}

	public V get(K1 k1, K2 k2) {
		return vs.get(k1).get(k2);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}