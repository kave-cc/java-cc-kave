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

import static cc.kave.commons.assertions.Asserts.assertTrue;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.utils.StringUtils;

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
		// init the parameters (esp. in the artificial setter and getter)
		getParameters();
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
		return !isUnknown() && !identifier.endsWith("()");
	}

	public boolean hasParameters() {
		return !isUnknown() && !identifier.endsWith("()");
	}

	private IMethodName getter;
	private IMethodName setter;

	@Override
	public IMethodName getExplicitSetterName() {
		if (setter == null) {
			Asserts.assertTrue(hasSetter());
			String id = getIdentifier();
			id = id.substring(0, id.length() - 1);
			if (isIndexer()) {
				id += ", ";
			}

			int open = id.indexOf('[');
			int close = FindCorrespondingCloseBracket(id, open);
			int nextOpen = id.indexOf('[', close);

			id = "[p:void] " + id.substring(nextOpen);
			id = id + "[" + getValueType().getIdentifier() + "] value)";

			int paramClose = id.length() - 1;
			int paramOpen = StringUtils.FindCorrespondingOpenBracket(id, paramClose);

			id = id.substring(0, paramOpen) + "__set__" + id.substring(paramOpen, paramClose + 1);

			setter = Names.newMethod(id, getValueType().getIdentifier());
		}
		return setter;
	}

	@Override
	public IMethodName getExplicitGetterName() {
		if (getter == null) {
			Asserts.assertTrue(hasGetter());
			String id = getIdentifier();
			id = id.substring(id.indexOf('['));

			int paramClose = id.length() - 1;
			int paramOpen = StringUtils.FindCorrespondingOpenBracket(id, paramClose);
			id = id.substring(0, paramOpen) + "__get__" + id.substring(paramOpen, paramClose + 1);
			getter = Names.newMethod(id);
		}
		return getter;
	}

	private List<IParameterName> _parameters;

	public List<IParameterName> getParameters() {
		if (_parameters == null) {
			if (isUnknown()) {
				_parameters = Lists.newLinkedList();
			} else {

				if (hasSetter() && hasGetter()) {
					_parameters = getExplicitGetterName().getParameters();
					_parameters.clear();
					_parameters.addAll(getExplicitSetterName().getParameters());
					_parameters.remove(_parameters.size() - 1);

				} else if (hasGetter()) {
					_parameters = getExplicitGetterName().getParameters();
				} else {
					_parameters = new LinkedList<>(getExplicitSetterName().getParameters());
					_parameters.remove(_parameters.get(_parameters.size() - 1));
				}
			}
		}
		return _parameters;
	}

	@Override
	public IParameterName getSetterValueParam() {
		assertTrue(hasSetter());
		List<IParameterName> ps = getExplicitSetterName().getParameters();
		return ps.get(ps.size() - 1);
	}
}