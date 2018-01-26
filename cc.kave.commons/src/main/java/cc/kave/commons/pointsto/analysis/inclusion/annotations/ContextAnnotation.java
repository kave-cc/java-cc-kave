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

import com.google.common.base.MoreObjects;

import cc.kave.commons.pointsto.analysis.inclusion.contexts.Context;

public class ContextAnnotation {

	public static final ContextAnnotation EMPTY = new ContextAnnotation(Context.EMPTY, Context.EMPTY);

	private Context left;
	private Context right;

	public ContextAnnotation(Context left, Context right) {
		this.left = left;
		this.right = right;
	}

	public Context getLeft() {
		return left;
	}

	public Context getRight() {
		return right;
	}

	public boolean isEmpty() {
		return Context.EMPTY.equals(left) && Context.EMPTY.equals(right);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		ContextAnnotation other = (ContextAnnotation) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(ContextAnnotation.class).add("left", left).add("right", right).toString();
	}

}
