/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kace.rsse.calls;

import static cc.kave.commons.utils.StringUtils.FindNext;

import cc.kave.commons.assertions.Throws;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.StringUtils;

public class LambdaContextUtils {

	protected static final String LAMBDA_KEYWORD = "$Lambda";

	public static boolean isLambdaName(IMethodName in) {
		String[] parts = split(in);
		return parts[1].contains(LAMBDA_KEYWORD);
	}

	public static IMethodName removeLambda(IMethodName with) {
		if (!isLambdaName(with)) {
			throw Throws.newIllegalArgumentException("provided method '%s' is not a lambda name", with);
		}
		String[] parts = split(with);
		int lastLambdaIdx = parts[1].lastIndexOf('$');
		parts[1] = parts[1].substring(0, lastLambdaIdx);
		return join(parts);
	}

	public static IMethodName addLambda(IMethodName without) {
		String[] parts = split(without);
		parts[1] = parts[1] + LAMBDA_KEYWORD;
		return join(parts);
	}

	private static String[] split(IMethodName in) {
		String id = in.getIdentifier();
		int openReturn = FindNext(id, 0, '[');
		int closeReturn = StringUtils.FindCorrespondingCloseBracket(id, openReturn);
		int openDecl = FindNext(id, closeReturn, '[');
		int closeDecl = StringUtils.FindCorrespondingCloseBracket(id, openDecl);
		int dot = StringUtils.FindNext(id, closeDecl, '.');
		int openPara = StringUtils.FindNext(id, dot, '(');

		String pre = id.substring(0, dot + 1);
		String name = id.substring(dot + 1, openPara).trim();
		String post = id.substring(openPara, id.length());
		return new String[] { pre, name, post };
	}

	private static IMethodName join(String[] parts) {
		return Names.newMethod(parts[0] + parts[1] + parts[2]);
	}
}