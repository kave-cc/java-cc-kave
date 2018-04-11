/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.rsse.calls.analysis;

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;

import cc.kave.rsse.calls.model.usages.DefinitionType;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class DefinitionSitePriorityComparator implements Comparator<IDefinition>, Serializable {

	private static final long serialVersionUID = 850239537351939837L;

	private EnumMap<DefinitionType, Integer> priorites = new EnumMap<>(DefinitionType.class);

	public DefinitionSitePriorityComparator() {
		priorites.put(DefinitionType.UNKNOWN, 0);
		priorites.put(DefinitionType.THIS, 1);
		priorites.put(DefinitionType.MEMBER_ACCESS, 2);
		priorites.put(DefinitionType.METHOD_PARAMETER, 3);
		priorites.put(DefinitionType.CONSTANT, 4);
		priorites.put(DefinitionType.RETURN_VALUE, 5);
		priorites.put(DefinitionType.NEW, 5);

		if (priorites.size() != DefinitionType.values().length) {
			throw new RuntimeException("Number of entries in the priority map does not match number of enum values");
		}
	}

	@Override
	public int compare(IDefinition defSite1, IDefinition defSite2) {
		return priorites.get(defSite1.getKind()) - priorites.get(defSite2.getKind());
	}
}