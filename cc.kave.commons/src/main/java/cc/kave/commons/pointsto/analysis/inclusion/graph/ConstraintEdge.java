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
package cc.kave.commons.pointsto.analysis.inclusion.graph;

import com.google.common.base.MoreObjects;

import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;

public final class ConstraintEdge {

	private final ConstraintNode target;
	private final InclusionAnnotation inclusionAnnotation;
	private final ContextAnnotation contextAnnotation;

	public ConstraintEdge(ConstraintNode target, InclusionAnnotation inclusionAnnotation,
			ContextAnnotation contextAnnotation) {
		this.target = target;
		this.inclusionAnnotation = inclusionAnnotation;
		this.contextAnnotation = contextAnnotation;
	}

	public ConstraintNode getTarget() {
		return target;
	}

	public InclusionAnnotation getInclusionAnnotation() {
		return inclusionAnnotation;
	}

	public ContextAnnotation getContextAnnotation() {
		return contextAnnotation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contextAnnotation == null) ? 0 : contextAnnotation.hashCode());
		result = prime * result + ((inclusionAnnotation == null) ? 0 : inclusionAnnotation.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		ConstraintEdge other = (ConstraintEdge) obj;
		if (contextAnnotation == null) {
			if (other.contextAnnotation != null)
				return false;
		} else if (!contextAnnotation.equals(other.contextAnnotation))
			return false;
		if (inclusionAnnotation == null) {
			if (other.inclusionAnnotation != null)
				return false;
		} else if (!inclusionAnnotation.equals(other.inclusionAnnotation))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(ConstraintEdge.class).add("target", target)
				.add("annotation", inclusionAnnotation).add("context", contextAnnotation).toString();
	}

}
