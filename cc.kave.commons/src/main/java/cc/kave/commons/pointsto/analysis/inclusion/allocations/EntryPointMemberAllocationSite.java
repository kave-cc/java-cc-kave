/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.inclusion.allocations;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;

public class EntryPointMemberAllocationSite implements AllocationSite {

	private final EntryPointAllocationSite entryPointAllocationSite;
	private final IMemberName member;

	public EntryPointMemberAllocationSite(EntryPointAllocationSite entryPointAllocationSite, IMemberName member) {
		this.entryPointAllocationSite = entryPointAllocationSite;
		this.member = member;
	}

	@Override
	public ITypeName getType() {
		return member.getValueType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryPointAllocationSite == null) ? 0 : entryPointAllocationSite.hashCode());
		result = prime * result + ((member == null) ? 0 : member.hashCode());
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
		EntryPointMemberAllocationSite other = (EntryPointMemberAllocationSite) obj;
		if (entryPointAllocationSite == null) {
			if (other.entryPointAllocationSite != null)
				return false;
		} else if (!entryPointAllocationSite.equals(other.entryPointAllocationSite))
			return false;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		return true;
	}
}