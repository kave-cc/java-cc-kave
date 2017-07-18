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

import static cc.kave.commons.model.naming.impl.v0.NameUtils.GetParameterNamesFromSignature;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;

public class PropertyName extends MemberName implements IPropertyName {

	public static final String SetterModifier = "set";
	public static final String GetterModifier = "get";

	public PropertyName() {
		this(UnknownMemberIdentifier);
	}

	public PropertyName(String identifier) {
		super(identifier);
		if (!isUnknown()) {
			validate(identifier.endsWith(")"), "must contain (empty) parameter list");
			validate(hasGetter() || hasSetter(), "must have either a getter or a setter");
		}
	}

	public boolean isUnknown() {
		return UnknownMemberIdentifier.equals(identifier);
	}

	public boolean hasSetter() {
		return getModifiers().contains(SetterModifier);
	}

	public boolean hasGetter() {
		return getModifiers().contains(GetterModifier);
	}

	public boolean isIndexer() {
		return hasParameters();
	}

	public boolean hasParameters() {
		return getParameters().size() > 0;
	}

	private List<IParameterName> _parameters;

	public List<IParameterName> getParameters() {
		if (_parameters == null) {
			if (isUnknown()) {
				_parameters = Lists.newLinkedList();
			} else {
				int endOfParameters = identifier.lastIndexOf(')');
				int startOfParameters = FindCorrespondingOpenBracket(identifier, endOfParameters);
				_parameters = GetParameterNamesFromSignature(identifier, startOfParameters, endOfParameters);
			}
		}
		return _parameters;
	}
}