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

import java.util.Arrays;

import cc.kave.commons.model.naming.idecomponents.ICommandName;
import cc.kave.commons.model.naming.impl.v0.BaseName;

public class CommandName extends BaseName implements ICommandName {

	public CommandName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public CommandName(String identifier) {
		super(identifier);
	}

	private String[] _parts;

	private String[] getParts() {
		if (_parts == null) {
			_parts = new String[3];

			String[] parts = identifier.split(":");
			if (parts.length >= 3) {
				_parts[0] = parts[0];
				_parts[1] = parts[1];
				_parts[2] = String.join(":", Arrays.copyOfRange(parts, 2, parts.length));
			} else {
				_parts[0] = UNKNOWN_NAME_IDENTIFIER;
				_parts[1] = "-1";
				_parts[2] = identifier;
			}
		}
		return _parts;
	}

	@Override
	public String getGuid() {
		return isUnknown() ? UNKNOWN_NAME_IDENTIFIER : getParts()[0];
	}

	@Override
	public int getId() {
		return isUnknown() ? -1 : Integer.parseInt(getParts()[1]);
	}

	@Override
	public String getName() {
		return isUnknown() ? UNKNOWN_NAME_IDENTIFIER : getParts()[2];
	}

	@Override
	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}
}