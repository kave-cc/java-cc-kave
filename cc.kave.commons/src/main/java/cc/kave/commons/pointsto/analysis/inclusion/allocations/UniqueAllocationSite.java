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

import cc.kave.commons.model.naming.types.ITypeName;

/**
 * A unique {@link AllocationSite} using instance equality. Should be phased out
 * if object sensitivity is to be used as contexts created by the analysis will
 * not be reproducible during query time.
 */
public final class UniqueAllocationSite implements AllocationSite {

	private final ITypeName type;

	public UniqueAllocationSite(ITypeName type) {
		this.type = type;
	}

	@Override
	public ITypeName getType() {
		return type;
	}
}