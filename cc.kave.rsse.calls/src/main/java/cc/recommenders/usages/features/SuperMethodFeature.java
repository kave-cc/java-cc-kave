/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.usages.features;

import static cc.kave.commons.assertions.Asserts.assertNotNull;

import cc.recommenders.names.ICoReMethodName;

public class SuperMethodFeature extends UsageFeature {

	private final ICoReMethodName methodName;

	public SuperMethodFeature(ICoReMethodName methodName) {
		assertNotNull(methodName);
		this.methodName = methodName;
	}

	public ICoReMethodName getMethodName() {
		return methodName;
	}

	@Override
	public void accept(ObjectUsageFeatureVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return "SuperMethod@" + hashCode() + ":" + methodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
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
		SuperMethodFeature other = (SuperMethodFeature) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}
}