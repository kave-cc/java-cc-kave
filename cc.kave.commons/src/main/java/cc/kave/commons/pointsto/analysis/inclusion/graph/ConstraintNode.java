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

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.MoreObjects;

import cc.kave.commons.pointsto.analysis.inclusion.SetExpression;

public final class ConstraintNode {

	private final SetExpression setExpression;

	private final Set<ConstraintEdge> predecessors;
	private final Set<ConstraintEdge> successors;

	public ConstraintNode(SetExpression setExpression) {
		this.setExpression = setExpression;
		this.predecessors = new HashSet<>();
		this.successors = new HashSet<>();
	}

	public boolean addPredecessor(ConstraintEdge edge) {
		return predecessors.add(edge);
	}

	public boolean addSuccessor(ConstraintEdge edge) {
		return successors.add(edge);
	}

	public SetExpression getSetExpression() {
		return setExpression;
	}

	public Set<ConstraintEdge> getPredecessors() {
		return predecessors;
	}

	public Set<ConstraintEdge> getSuccessors() {
		return successors;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(ConstraintNode.class).add("setExpr", setExpression).toString();
	}
}
