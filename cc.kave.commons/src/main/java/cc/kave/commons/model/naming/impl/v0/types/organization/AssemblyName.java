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
package cc.kave.commons.model.naming.impl.v0.types.organization;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;

public class AssemblyName extends BaseName implements IAssemblyName {

	private IAssemblyVersion version;
	private boolean isLocalProject;

	public AssemblyName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public AssemblyName(String identifier) {
		super(identifier);

		version = new AssemblyVersion();

		if (!isUnknown() && !identifier.contains(",")) {
			isLocalProject = true;
		}

		String name = getName();
		for (String c : new String[] { "(", ")", "[", "]", "{", "}", ",", ";", ":", " " }) {
			if (name.contains(c)) {
				throw new ValidationException(String.format("identifier must not contain the char '%s'", c));
			}
		}

		String[] fragments = GetFragments();
		version = fragments.length <= 1 ? new AssemblyVersion() : new AssemblyVersion(fragments[1]);
	}

	@Override
	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}

	@Override
	public String getName() {
		return GetFragments()[0];
	}

	@Override
	public boolean isLocalProject() {
		return isLocalProject;
	}

	private String[] GetFragments() {
		int split = identifier.lastIndexOf(",");
		if (split == -1) {
			return new String[] { identifier };
		}
		String name = identifier.substring(0, split).trim();
		String version = identifier.substring(split + 1).trim();

		return new String[] { name, version };
	}

	@Override
	public IAssemblyVersion getVersion() {
		return version;
	}
}