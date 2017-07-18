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

import java.util.regex.Pattern;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;

public class AssemblyVersion extends BaseName implements IAssemblyVersion {

	Pattern _isValidVersionRegex = Pattern.compile("^-?\\d+\\.-?\\d+\\.-?\\d+\\.-?\\d+$");

	public AssemblyVersion() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public AssemblyVersion(String identifier) {
		super(identifier);

		if (!isUnknown()) {
			if (!_isValidVersionRegex.matcher(identifier).matches()) {
				throw new ValidationException(String.format("invalid assembly version '%s'", identifier));
			}
		}
	}

	@Override
	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}

	@Override
	public int getMajor() {
		return isUnknown() ? -1 : splitVersion()[0];
	}

	@Override
	public int getMinor() {
		return isUnknown() ? -1 : splitVersion()[1];
	}

	@Override
	public int getBuild() {
		return isUnknown() ? -1 : splitVersion()[2];
	}

	@Override
	public int getRevision() {
		return isUnknown() ? -1 : splitVersion()[3];
	}

	private int[] splitVersion() {
		int[] versions = new int[4];
		String[] split = identifier.split("\\.");
		for (int i = 0; i < split.length; i++) {
			versions[i] = Integer.valueOf(split[i]);
		}
		return versions;
	}

	@Override
	public int compareTo(IAssemblyVersion otherVersion) {
		if (otherVersion == null) {
			return Integer.MIN_VALUE;
		}
		int majorDiff = getMajor() - otherVersion.getMajor();
		if (majorDiff != 0) {
			return majorDiff;
		}
		int minorDiff = getMinor() - otherVersion.getMinor();
		if (minorDiff != 0) {
			return minorDiff;
		}
		int buildDiff = getBuild() - otherVersion.getBuild();
		if (buildDiff != 0) {
			return buildDiff;
		}
		return getRevision() - otherVersion.getRevision();
	}
}