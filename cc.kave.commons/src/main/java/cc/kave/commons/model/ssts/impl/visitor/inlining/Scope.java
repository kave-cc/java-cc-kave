/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class Scope {
	public Scope parent;
	public Set<IVariableReference> existingIds;
	public Map<IVariableReference, IVariableReference> changedNames;
	public List<IStatement> body;
	public String resultName;
	public String gotResultName;
	public boolean isInCondition;
	public boolean hasReturnInLoop;

	public Scope() {
		this.parent = null;
		this.existingIds = new HashSet<>();
		this.changedNames = new HashMap<>();
		this.body = new ArrayList<>();
		this.resultName = "";
		this.gotResultName = "";
		this.isInCondition = false;
		this.hasReturnInLoop = false;
	}

	public void resolve(IVariableReference ref) {
		if (changedNames.containsKey(ref)) {
			VariableReference variableReference = (VariableReference) ref;
			variableReference.setIdentifier(changedNames.get(ref).getIdentifier());
		}
	}
}
