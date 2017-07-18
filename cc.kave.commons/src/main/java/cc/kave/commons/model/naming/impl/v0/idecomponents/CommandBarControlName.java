/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.naming.impl.v0.idecomponents;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.idecomponents.ICommandBarControlName;
import cc.kave.commons.model.naming.impl.v0.BaseName;

public class CommandBarControlName extends BaseName implements ICommandBarControlName {

	public static final char HierarchySeperator = '|';

	public CommandBarControlName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public CommandBarControlName(String identifier) {
		super(identifier);
		if (identifier.contains("||")) {
			throw new ValidationException("double separator");
		}
	}

	@Override
	public ICommandBarControlName getParent() {
		int endOfParentIdentifier = identifier.lastIndexOf(HierarchySeperator);
		return endOfParentIdentifier < 0 ? null
				: new CommandBarControlName(identifier.substring(0, endOfParentIdentifier));
	}

	@Override
	public String getName() {
		int startOfName = identifier.lastIndexOf(HierarchySeperator) + 1;
		return identifier.substring(startOfName);
	}

	@Override
	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}
}