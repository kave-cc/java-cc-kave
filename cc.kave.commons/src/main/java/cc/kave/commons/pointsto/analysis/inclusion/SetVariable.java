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

public class SetVariable implements SetExpression, Comparable<SetVariable> {

	private final VariableKind kind;
	private final int order;

	public SetVariable(VariableKind kind, int order) {
		this.kind = kind;
		this.order = order;
	}

	@Override
	public int compareTo(SetVariable other) {
		VariableKind otherKind = other.kind;
		if (kind == otherKind) {
			return this.order - other.order;
		} else if (kind == VariableKind.REFERENCE_VARIABLE) {
			// ref < obj < proj
			return -1;
		} else if (kind == VariableKind.OBJECT_VARIABLE) {
			if (otherKind == VariableKind.REFERENCE_VARIABLE) {
				// obj > ref
				return 1;
			} else if (otherKind == VariableKind.PROJECTION_VARIABLE) {
				// obj < proj
				return -1;
			} else {
				throw new IllegalArgumentException("Unknown VariableKind values");
			}
		} else if (kind == VariableKind.PROJECTION_VARIABLE) {
			// proj > obj > ref
			return 1;
		} else {
			throw new IllegalArgumentException("Unknown VariableKind values");
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(SetVariable.class).add("kind", kind).add("order", order).toString();
	}
}
