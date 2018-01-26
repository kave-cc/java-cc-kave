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

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.types.ITypeName;

public class ArrayEntryAllocationSite implements AllocationSite {

	private final AllocationSite arrayAllocationSite;

	public ArrayEntryAllocationSite(AllocationSite arrayAllocationSite) {
		Asserts.assertTrue(arrayAllocationSite.getType().isArray());
		this.arrayAllocationSite = arrayAllocationSite;
	}

	@Override
	public ITypeName getType() {
		return arrayAllocationSite.getType().asArrayTypeName().getArrayBaseType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrayAllocationSite == null) ? 0 : arrayAllocationSite.hashCode());
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
		ArrayEntryAllocationSite other = (ArrayEntryAllocationSite) obj;
		if (arrayAllocationSite == null) {
			if (other.arrayAllocationSite != null)
				return false;
		} else if (!arrayAllocationSite.equals(other.arrayAllocationSite))
			return false;
		return true;
	}
}