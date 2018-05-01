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

import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.model.usages.IMemberAccess;

public class MemberAccessFeature implements IFeature {

	public final IMemberAccess memberAccess;

	public MemberAccessFeature(IMemberAccess memberAccess) {
		assertNotNull(memberAccess);
		this.memberAccess = memberAccess;
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
		result = prime * result + memberAccess.hashCode();
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
		MemberAccessFeature other = (MemberAccessFeature) obj;
		if (!memberAccess.equals(other.memberAccess))
			return false;
		return true;
	}
}