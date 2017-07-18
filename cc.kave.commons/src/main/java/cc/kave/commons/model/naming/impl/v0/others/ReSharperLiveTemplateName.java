/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.naming.impl.v0.others;

import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.others.IReSharperLiveTemplateName;

public class ReSharperLiveTemplateName extends BaseName implements IReSharperLiveTemplateName {

	private static final String Separator = ":";

	public ReSharperLiveTemplateName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public ReSharperLiveTemplateName(String identifier) {
		super(identifier);
		if (!isUnknown() && !identifier.contains(Separator)) {
			throw new ValidationException("must contain separator");
		}
	}

	@Override
	public String getName() {
		int endOfName = identifier.indexOf(Separator);
		return isUnknown() ? UNKNOWN_NAME_IDENTIFIER : identifier.substring(0, endOfName);
	}

	@Override
	public String getDescription() {
		int startOfDescription = identifier.indexOf(Separator) + 1;
		return isUnknown() ? UNKNOWN_NAME_IDENTIFIER : identifier.substring(startOfDescription);
	}

	@Override
	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}
}