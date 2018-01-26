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
package cc.recommenders.usages;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.names.ICoReTypeName;

import com.google.common.collect.Maps;

public class ProjectFoldingIndex {

	private Map<ICoReTypeName, Map<String, Integer>> index = Maps.newHashMap();

	public void setCount(ICoReTypeName type, String projectName, int num) {
		Map<String, Integer> counts = index.get(type);
		if (counts == null) {
			counts = Maps.newHashMap();
			index.put(type, counts);
		}
		counts.put(projectName, num);
	}

	public void count(ICoReTypeName type, String projectName) {
		Map<String, Integer> counts = index.get(type);
		if (counts == null) {
			counts = Maps.newHashMap();
			index.put(type, counts);
		}
		if (counts.containsKey(projectName)) {
			int old = counts.get(projectName);
			counts.put(projectName, old + 1);
		} else {
			counts.put(projectName, 1);
		}
	}

	public Set<ICoReTypeName> getTypes() {
		return index.keySet();
	}

	public Map<String, Integer> getCounts(ICoReTypeName type) {
		Map<String, Integer> map = index.get(type);
		if (map == null) {
			map = Maps.newHashMap();
		}
		return map;
	}

	public int getTotalCount(ICoReTypeName type) {
		int total = 0;
		for (int count : getCounts(type).values()) {
			total += count;
		}
		return total;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}