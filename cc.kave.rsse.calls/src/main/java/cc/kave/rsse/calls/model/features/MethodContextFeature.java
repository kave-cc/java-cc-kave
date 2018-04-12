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
package cc.kave.rsse.calls.model.features;

import static cc.kave.commons.assertions.Asserts.assertNotNull;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.ToStringUtils;

public class MethodContextFeature implements IFeature {

	public final IMethodName method;

	public MethodContextFeature(IMethodName method) {
		assertNotNull(method);
		this.method = method;
	}

	@Override
	public void accept(IFeatureVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		MethodContextFeature other = (MethodContextFeature) obj;
		if (!method.equals(other.method))
			return false;
		return true;
	}
}