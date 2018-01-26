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

public class SetVariableFactory {

	private int nextVariableOrder = 0;

	protected SetVariable create(VariableKind kind) {
		if (nextVariableOrder == Integer.MAX_VALUE) {
			throw new RuntimeException("Reached maximal number of set variables");
		}

		return new SetVariable(kind, nextVariableOrder++);
	}

	public SetVariable createReferenceVariable() {
		return create(VariableKind.REFERENCE_VARIABLE);
	}

	public SetVariable createObjectVariable() {
		return create(VariableKind.OBJECT_VARIABLE);
	}

	public SetVariable createProjectionVariable() {
		return create(VariableKind.PROJECTION_VARIABLE);
	}
}
