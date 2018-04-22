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

import static java.lang.String.format;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.utils.StringUtils;

public abstract class BaseName implements IName {

	protected static final String UNKNOWN_NAME_IDENTIFIER = "???";

	protected final String identifier;

	protected BaseName(String id) {
		validate(id != null, "identifier must not be null");
		// TODO test: reflections seems to be super expensive... test this simple
		// solution

		// String invalidPrefix = this.getClass().getSimpleName() + "(";
		int idx = StringUtils.FindNext(id, 0, ' ', '.', '[', ']', '(', '`');
		if (idx != -1) {
			// boolean doesNotStartWithPrefix = !id.startsWith(invalidPrefix);
			boolean doesNotStartWithPrefix = !id.substring(0, idx + 1).endsWith("Name(");
			validate(doesNotStartWithPrefix,
					"Invalid identifier \"%s\". Did you forget to call getIdentifier at some point?", id);
		}
		this.identifier = id;
	}

	protected static void validate(boolean condition, String msg, Object... args) {
		if (!condition) {
			throw new ValidationException(format(msg, args));
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