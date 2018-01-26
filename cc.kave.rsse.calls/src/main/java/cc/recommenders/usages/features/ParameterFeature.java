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

import static cc.kave.commons.assertions.Asserts.assertNotNegative;
import static cc.kave.commons.assertions.Asserts.assertNotNull;

import cc.recommenders.names.ICoReMethodName;

public class ParameterFeature extends UsageFeature {

	private final ICoReMethodName param;
	private final int argNum;

	public ParameterFeature(ICoReMethodName param, int argNum) {
		assertNotNull(param);
		this.param = param;
		assertNotNegative(argNum);
		this.argNum = argNum;
	}

	public int getArgNum() {
		return argNum;
	}

	public ICoReMethodName getMethodName() {
		return param;
	}

	@Override
	public void accept(ObjectUsageFeatureVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return "Parameter@" + hashCode() + ":" + param + "#" + argNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + argNum;
		result = prime * result + ((param == null) ? 0 : param.hashCode());
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
		ParameterFeature other = (ParameterFeature) obj;
		if (argNum != other.argNum)
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		return true;
	}
}