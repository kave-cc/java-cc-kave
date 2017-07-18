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

import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.StringUtils;

public class LambdaName extends BaseName implements ILambdaName {

	public LambdaName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	public LambdaName(String identifier) {
		super(identifier);
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

	public boolean hasParameters() {
		return getParameters().size() > 0;
	}

	public ITypeName getReturnType() {
		if (isUnknown()) {
			return new TypeName();
		}
		int openR = identifier.indexOf('[');
		int closeR = StringUtils.FindCorrespondingCloseBracket(identifier, openR);
		openR++; // skip brackets
		return TypeUtils.createTypeName(identifier.substring(openR, closeR));
	}

	public boolean isUnknown() {
		return UNKNOWN_NAME_IDENTIFIER.equals(identifier);
	}
}