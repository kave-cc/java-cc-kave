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
package cc.kave.commons.pointsto.analysis.inclusion;

import com.google.common.base.MoreObjects;

import cc.kave.commons.pointsto.analysis.inclusion.allocations.AllocationSite;

public class RefTerm implements ConstructedTerm {

	public static final int READ_INDEX = 0;
	public static final int WRITE_INDEX = 1;

	private final AllocationSite allocationSite;
	private final SetVariable variable;

	public RefTerm(AllocationSite allocationSite, SetVariable variable) {
		this.allocationSite = allocationSite;
		this.variable = variable;
	}

	public AllocationSite getAllocationSite() {
		return allocationSite;
	}

	@Override
	public int getNumberOfArguments() {
		return 2;
	}

	@Override
	public SetVariable getArgument(int index) {
		if (index < 0 || index > getNumberOfArguments() - 1) {
			throw new IndexOutOfBoundsException();
		}

		return variable;
	}

	@Override
	public Variance getArgumentVariance(int index) {
		switch (index) {
		case 0:
			return Variance.COVARIANT;
		case 1:
			return Variance.CONTRAVARIANT;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(RefTerm.class).add("allocationSite", allocationSite).add("variable", variable)
				.toString();
	}

}
