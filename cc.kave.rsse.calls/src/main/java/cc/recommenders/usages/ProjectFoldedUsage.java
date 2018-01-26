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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.names.ICoReTypeName;

public class ProjectFoldedUsage {

	private Usage usage;
	private String name;

	public ProjectFoldedUsage(Usage u, String name) {
		this.usage = u;
		this.name = name;
	}

	public String getProjectName() {
		return name;
	}

	public ICoReTypeName getType() {
		return usage.getType();
	}

	public Usage getRawUsage() {
		return usage;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return String.format("[(%s) %s]", name, usage.toString());
	}
}