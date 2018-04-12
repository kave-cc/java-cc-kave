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
package cc.kave.caret.analyses;

import static cc.kave.commons.assertions.Asserts.assertNotNull;

import java.util.IdentityHashMap;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;

public class PathInsensitivePointsToInfo implements IPathInsensitivePointsToInfo {

	private IdentityHashMap<Object, Object> abstractObjects = new IdentityHashMap<>();

	public void set(Object k, Object v) {
		Asserts.assertNotNull(k);
		Asserts.assertNotNull(v);
		abstractObjects.put(k, v);
	}

	public boolean hasKey(Object k) {
		assertNotNull(k);
		return abstractObjects.containsKey(k);
	}

	public Object getAbstractObject(Object k) {
		if (!hasKey(k)) {
			Asserts.fail("No abstract object available, key is not defined:\n%s", k);
		}
		return abstractObjects.get(k);
	}

	@Override
	public boolean hasKey(ISST k) {
		return hasKey((Object) k);
	}

	@Override
	public Object getAbstractObject(ISST k) {
		return getAbstractObject((Object) k);
	}

	@Override
	public boolean hasKey(IMemberDeclaration k) {
		return hasKey((Object) k);
	}

	@Override
	public Object getAbstractObject(IMemberDeclaration k) {
		return getAbstractObject((Object) k);
	}

	@Override
	public boolean hasKey(IParameterName k) {
		return hasKey((Object) k);
	}

	@Override
	public Object getAbstractObject(IParameterName k) {
		return getAbstractObject((Object) k);
	}

	@Override
	public boolean hasKey(IReference k) {
		return hasKey((Object) k);
	}

	@Override
	public Object getAbstractObject(IReference k) {
		return getAbstractObject((Object) k);
	}
}