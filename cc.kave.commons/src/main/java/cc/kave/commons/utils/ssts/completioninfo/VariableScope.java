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

import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.assertions.Asserts;

public class VariableScope<T> {

	private VariableTable<T> vars = new VariableTable<>();

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
		Asserts.assertFalse(vars.values.containsKey(id));
		vars.values.put(id, value);
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

	private class VariableTable<U> {
		public VariableTable<U> parent;
		public Map<String, U> values = new HashMap<>();
	}
}