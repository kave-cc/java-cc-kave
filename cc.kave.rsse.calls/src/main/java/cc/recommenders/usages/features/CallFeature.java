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

public class CallFeature extends UsageFeature {

	private final ICoReMethodName call;

	public CallFeature(ICoReMethodName call) {
		assertNotNull(call);
		this.call = call;
	}

	public ICoReMethodName getMethodName() {
		return call;
	}

	@Override
	public void accept(ObjectUsageFeatureVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return "Call@" + hashCode() + ":" + call;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((call == null) ? 0 : call.hashCode());
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
		CallFeature other = (CallFeature) obj;
		if (call == null) {
			if (other.call != null)
				return false;
		} else if (!call.equals(other.call))
			return false;
		return true;
	}
}