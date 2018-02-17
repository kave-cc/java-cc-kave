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
package cc.kave.rsse.calls.pbn;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.ICallsRecommender;
import cc.kave.rsse.calls.IModelStore;
import cc.kave.rsse.calls.usages.Query;

public class PBNModelStore implements IModelStore<PBNModel> {

	public PBNModelStore(String dirmodels, String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(ITypeName t, PBNModel net) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasModel(ITypeName t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PBNModel getModel(ITypeName t) {
		// TODO Auto-generated method stub
		return null;
	}
}