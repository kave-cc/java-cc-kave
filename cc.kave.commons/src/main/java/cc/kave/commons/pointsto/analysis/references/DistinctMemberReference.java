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
package cc.kave.commons.pointsto.analysis.references;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.ssts.references.IMemberReference;

public abstract class DistinctMemberReference implements DistinctReference {

	protected final IMemberReference memberReference;
	private final DistinctReference baseReference;

	public DistinctMemberReference(IMemberReference memberReference, DistinctReference baseReference) {
		this.memberReference = memberReference;
		this.baseReference = baseReference;

		if (!isStaticMember()) {
			Asserts.assertTrue(memberReference.getReference().equals(baseReference.getReference()));
		}
	}

	public abstract boolean isStaticMember();

	public abstract IMemberName getMemberName();

	@Override
	public IMemberReference getReference() {
		return memberReference;
	}

	public DistinctReference getBaseReference() {
		return baseReference;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseReference == null) ? 0 : baseReference.hashCode());
		result = prime * result + ((memberReference == null) ? 0 : memberReference.hashCode());
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
		DistinctMemberReference other = (DistinctMemberReference) obj;
		if (baseReference == null) {
			if (other.baseReference != null)
				return false;
		} else if (!baseReference.equals(other.baseReference))
			return false;
		if (memberReference == null) {
			if (other.memberReference != null)
				return false;
		} else if (!memberReference.equals(other.memberReference))
			return false;
		return true;
	}
}