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
package cc.kave.rsse.calls.usages.model.impl;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.utils.ToStringUtils;
import cc.kave.rsse.calls.usages.model.IUsageSite;
import cc.kave.rsse.calls.usages.model.UsageSiteType;

public class UsageSite implements IUsageSite {

	public UsageSiteType type;
	public IMemberName member;
	public int argIndex = -1;

	@Override
	public UsageSiteType getType() {
		return type;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IMemberName> T getMember(Class<T> classOfT) {
		return (T) member;
	}

	@Override
	public int getArgIndex() {
		return argIndex;
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + argIndex;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		UsageSite other = (UsageSite) obj;
		if (argIndex != other.argIndex)
			return false;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}