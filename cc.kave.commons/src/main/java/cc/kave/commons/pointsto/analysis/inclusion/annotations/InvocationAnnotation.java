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
package cc.kave.commons.pointsto.analysis.inclusion.annotations;

import cc.kave.commons.model.naming.codeelements.IMemberName;

public class InvocationAnnotation extends MemberAnnotation {

	private final boolean dynamicallyDispatched;

	public InvocationAnnotation(IMemberName member) {
		this(member, true);
	}

	public InvocationAnnotation(IMemberName member, boolean dynamicallyDispatched) {
		super(member);
		this.dynamicallyDispatched = dynamicallyDispatched;
	}

	public boolean isDynamicallyDispatched() {
		return dynamicallyDispatched;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (dynamicallyDispatched ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvocationAnnotation other = (InvocationAnnotation) obj;
		if (dynamicallyDispatched != other.dynamicallyDispatched)
			return false;
		return true;
	}
}