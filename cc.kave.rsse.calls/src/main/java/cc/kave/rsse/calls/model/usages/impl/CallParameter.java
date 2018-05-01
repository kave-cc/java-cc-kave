/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.model.usages.impl;

import static java.lang.String.format;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.usages.ICallParameter;

public class CallParameter implements ICallParameter {

	public final IMethodName method;
	public final int argIndex;

	public CallParameter(IMethodName method, int argIndex) {
		if (method == null) {
			throw new IllegalArgumentException("Method cannot be null.");
		}
		this.method = method;
		this.argIndex = argIndex;
		assertParams(argIndex);
	}

	public CallParameter(String id, int argIndex) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("Id is null or empty.");
		}
		this.method = Names.newMethod(id);
		this.argIndex = argIndex;
		assertParams(argIndex);
	}

	private void assertParams(int argIndex) {
		int numParam = method.getParameters().size();
		if (argIndex < 0 || argIndex >= numParam) {
			throw new IllegalArgumentException(
					format("Invalid argIndex %d, number of parameters is %s.", argIndex, numParam));
		}
	}

	@Override
	public IMethodName getMethod() {
		return method;
	}

	@Override
	public int getArgIndex() {
		return argIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + argIndex;
		result = prime * result + method.hashCode();
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
		CallParameter other = (CallParameter) obj;
		if (argIndex != other.argIndex)
			return false;
		if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}