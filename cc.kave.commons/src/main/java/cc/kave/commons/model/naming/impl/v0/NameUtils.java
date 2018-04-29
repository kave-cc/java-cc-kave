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
package cc.kave.commons.model.naming.impl.v0;

import static cc.kave.commons.model.naming.Names.newType;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.codeelements.ParameterName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.utils.StringUtils;
import cc.kave.commons.utils.naming.TypeErasure;

public class NameUtils {

	public static List<ITypeParameterName> ParseTypeParameterList(String id, int open, int close) {
		if (StringUtils.isNullOrEmpty(id) || open < 0 || close >= id.length() || close < open || id.charAt(open) != '['
				|| id.charAt(close) != ']') {
			Asserts.fail("error parsing parameters from '%s' (%d, %d)", id, open, close);
		}

		List<ITypeParameterName> parameters = Lists.newLinkedList();
		for (int cur = open; cur < close;) {
			cur++; // skip open bracket or comma

			cur = StringUtils.FindNext(id, cur, '[');
			int closeParam = StringUtils.FindCorrespondingCloseBracket(id, cur);

			cur++; // skip bracket

			String tpId = id.substring(cur, closeParam);
			parameters.add(new TypeParameterName(tpId));

			closeParam++; // skip bracket

			cur = StringUtils.FindNext(id, closeParam, ',', ']');
		}
		return parameters;
	}

	/// <summary>
	/// Parses contents of a "ParameterListHolder"... just pass the complete
	/// identifier and the indices of the brackets
	/// </summary>
	public static List<IParameterName> GetParameterNamesFromSignature(String identifierWithParameters,
			int idxOpeningBrace, int idxClosingBrace) {
		// remove opening bracket
		idxOpeningBrace++;

		// strip leading whitespace
		while (identifierWithParameters.charAt(idxOpeningBrace) == ' ') {
			idxOpeningBrace++;
		}

		List<IParameterName> parameters = Lists.newLinkedList();
		boolean hasNoParams = idxOpeningBrace == idxClosingBrace;
		if (hasNoParams) {
			return parameters;
		}

		int current = idxOpeningBrace;
		while (current < idxClosingBrace) {
			int startOfParam = current;

			if (identifierWithParameters.charAt(current) != '[') {
				current = FindNext(identifierWithParameters, current, '[');
			}
			current = FindCorrespondingCloseBracket(identifierWithParameters, current);
			current = FindNext(identifierWithParameters, current, ',', ')');
			int endOfParam = current;

			String paramSubstring = identifierWithParameters.substring(startOfParam, endOfParam);
			try {
				parameters.add(new ParameterName(paramSubstring.trim()));
			} catch (ValidationException e) {
				// TODO test: add tests or get rid of it (helped me once already though, better add test :D)
				StringBuilder sb = new StringBuilder();
				sb.append("Failed to create a parameter name in NameUtils.GetParameterNamesFromSignature:\n");
				sb.append("identifierWithParameters: ").append(identifierWithParameters).append("\n");
				sb.append("--> new ParameterName(\"").append(paramSubstring.trim()).append("\")\n");
				sb.append("error: ").append(e.getMessage());
				throw new ValidationException(sb.toString());
			}

			// ignore comma
			current++;
		}

		return parameters;
	}

	public static IMethodName toAnonymousMethod(IMethodName m, boolean preserveTypeBindings) {
		IDelegateTypeName t = toValueType(m, preserveTypeBindings);
		String tId = t.getIdentifier();

		StringBuilder sb = new StringBuilder();
		sb.append("[");

		int openRt = tId.indexOf('[');
		int closeRt = StringUtils.FindCorrespondingCloseBracket(tId, openRt);
		sb.append(tId.substring(openRt + 1, closeRt));

		sb.append("] [").append(t.getIdentifier()).append("].Invoke");

		int closingSig = tId.lastIndexOf(')');
		int openingSit = StringUtils.FindCorrespondingOpenBracket(tId, closingSig);
		sb.append(tId.substring(openingSit, closingSig + 1));

		return Names.newMethod(sb.toString());
	}

	public static IDelegateTypeName toValueType(IMethodName m, boolean preserveTypeBindings) {

		boolean isVoid = m.getReturnType().isVoidType();
		String rt = isVoid ? "p:void" : "TResult";
		StringBuilder dt = new StringBuilder();
		StringBuilder params = new StringBuilder();

		int numParams = m.getParameters().size();
		int numGenParams = isVoid ? numParams : m.getParameters().size() + 1;

		if (isVoid && numParams == 0) {
			return newType("d:[p:void] [System.Action, mscorlib, 4.0.0.0].()").asDelegateTypeName();
		}

		dt.append("System.").append(isVoid ? "Action" : "Func").append('`').append(numGenParams).append('[');
		if (numParams == 1) {
			dt.append("[T -> ");
			dt.append(m.getParameters().get(0).getValueType().getIdentifier());
			dt.append(']');
			if (!isVoid) {
				dt.append(",[TResult -> ");
				dt.append(m.getReturnType().getIdentifier());
				dt.append(']');
			}
		} else {
			boolean isFirst = true;
			int num = 1;
			for (IParameterName p : m.getParameters()) {
				if (!isFirst) {
					dt.append(",");
				}
				isFirst = false;

				dt.append('[');
				dt.append('T').append(num++).append(" -> ");
				dt.append(p.getValueType().getIdentifier());
				dt.append(']');
			}
			if (!isVoid) {
				if (!isFirst) {
					dt.append(",");
				}
				dt.append("[TResult -> ");
				dt.append(m.getReturnType().getIdentifier());
				dt.append(']');
			}
		}
		dt.append(']');

		if (numParams == 1) {
			params.append("[T] ").append(isVoid ? "obj" : "arg");
		} else {
			for (int num = 1; num <= m.getParameters().size(); num++) {
				if (num > 1) {
					params.append(", ");
				}
				params.append("[T").append(num).append("] arg").append(num);
			}
		}

		IDelegateTypeName t = Names.newType("d:[%s] [%s, mscorlib, 4.0.0.0].(%s)", rt, dt, params).asDelegateTypeName();
		return preserveTypeBindings ? t : TypeErasure.of(t).asDelegateTypeName();
	}
}