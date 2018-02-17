/**
 * Copyright 2018 University of Zurich
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
package cc.kave.commons.utils.ssts.completioninfo;

import static cc.kave.commons.assertions.Throws.newIllegalArgumentException;
import static cc.kave.commons.utils.io.Logger.debug;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.utils.io.Logger;

public class VariableScope<T> {

	private VariableTable<T> vars = new VariableTable<>();
	private ErrorHandling errorHandlingStrategy;

	public VariableScope(ErrorHandling strategy) {
		this.errorHandlingStrategy = strategy;
	}

	public void open() {
		VariableTable<T> tmp = new VariableTable<>();
		tmp.parent = vars;
		vars = tmp;
	}

	public void close() {
		Asserts.assertNotNull(vars.parent);
		vars = vars.parent;
	}

	public void declare(String id, T value) {

		if (vars.values.containsKey(id)) {
			T oldVal = vars.values.get(id);
			if (oldVal.equals(value)) {
				debug("Redefining the variable '%s' with same type (%s).", id, value);
				return;
			} else {
				String msg = format("Trying to change the value of '%s' from '%s' to '%s'.", id, oldVal, value);
				if (errorHandlingStrategy == ErrorHandling.IGNORE) {
					debug(msg);
				}
				handleError(msg);
			}
		}

		vars.values.put(id, value);
	}

	private void handleError(String errorMsg) {
		switch (errorHandlingStrategy) {
		case IGNORE: // nothing to do
			return;
		case LOG:
			Logger.err(errorMsg);
			return;
		case THROW:
			Asserts.fail(errorMsg);
			return;
		default:
			Asserts.fail("unhandled case");
		}
	}

	public boolean isDeclared(String id) {
		VariableTable<T> cur = vars;
		while (cur != null) {
			if (cur.values.containsKey(id)) {
				return true;
			}
			cur = cur.parent;
		}
		return false;
	}

	public boolean isDeclaredInCurrentScope(String id) {
		return vars.values.containsKey(id);
	}

	public T get(String id) {
		VariableTable<T> cur = vars;
		while (cur != null) {
			if (cur.values.containsKey(id)) {
				return cur.values.get(id);
			}
			cur = cur.parent;
		}
		throw newIllegalArgumentException("undefined id '%s'", id);
	}

	public enum ErrorHandling {
		THROW, LOG, IGNORE
	}

	private class VariableTable<U> {
		public VariableTable<U> parent;
		public Map<String, U> values = new HashMap<>();
	}
}