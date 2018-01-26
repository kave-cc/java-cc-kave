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
package cc.kave.commons.pointsto.analysis.exceptions;

import java.util.Locale;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class MissingTypeNameException extends RuntimeException {

	private static final long serialVersionUID = -4618121475023802924L;

	public MissingTypeNameException(IMethodName method) {
		super(String.format(Locale.US, "Encountered a missing type name in \"%s\"", method.getIdentifier()));
	}
}