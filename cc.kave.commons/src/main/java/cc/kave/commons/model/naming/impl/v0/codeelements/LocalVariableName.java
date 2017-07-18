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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import cc.kave.commons.model.naming.codeelements.ILocalVariableName;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;

public class LocalVariableName extends BaseName implements ILocalVariableName {

	private static final String UnknownLocalVariableName = "[?] ???";

	public LocalVariableName() {
		this(UnknownLocalVariableName);
	}

	public LocalVariableName(String identifier) {
		super(identifier);
	}

	public String getName() {
		int indexOfName = identifier.lastIndexOf(']') + 1;
		return identifier.substring(indexOfName).trim();
	}

	public ITypeName getValueType() {
		int lengthOfTypeIdentifier = identifier.lastIndexOf(']');
		return TypeUtils.createTypeName(identifier.substring(1, lengthOfTypeIdentifier));
	}

	public boolean isUnknown() {
		return UnknownLocalVariableName.equals(identifier);
	}
}