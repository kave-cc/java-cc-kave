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

public class Map3D<K1, K2, K3, V> {

	private Map<K1, Map2D<K2, K3, V>> vs = Maps.newLinkedHashMap();

	public V getOrAdd(K1 k1, K2 k2, K3 k3, V vDefault) {
		Map2D<K2, K3, V> m2 = vs.get(k1);
		if (m2 == null) {
			m2 = Map2D.create();
			vs.put(k1, m2);
		}
		Map<K3, V> m3 = m2.get(k2);
		if (m3 == null) {
			m3 = Maps.newLinkedHashMap();
			m2.put(k2, m3);
		}
		V v = m3.get(k2);
		if (v == null) {
			v = vDefault;
			m3.put(k3, v);
		}
		return v;

	}

	public Map2D<K2, K3, V> get(K1 k1) {
		return vs.get(k1);
	}

	public Set<K1> keySet() {
		return vs.keySet();
	}
	
	public static <K1, K2, K3, V> Map3D<K1, K2, K3, V> create() {
		return new Map3D<K1, K2, K3, V>();
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