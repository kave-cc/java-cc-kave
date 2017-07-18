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
package cc.kave.commons.model.naming.impl.v0;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;

public abstract class BaseName implements IName {

	protected static final String UNKNOWN_NAME_IDENTIFIER = "???";

	protected String identifier;

	protected BaseName(String id) {
		validate(id != null, "identifier must not be null");
		this.identifier = id;
	}

	protected static void validate(boolean condition, String msg) {
		if (!condition) {
			throw new ValidationException(msg);
		}
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isHashed() {
		return identifier.contains("==");
	}

	@Override
	public abstract boolean isUnknown();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IName other = (IName) obj;
		if (identifier == null) {
			if (other.getIdentifier() != null)
				return false;
		} else if (!identifier.equals(other.getIdentifier()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", getClass().getSimpleName(), getIdentifier());
	}
}