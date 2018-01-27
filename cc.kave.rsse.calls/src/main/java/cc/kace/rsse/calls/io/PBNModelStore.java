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
package cc.kace.rsse.calls.io;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import cc.kace.rsse.calls.ICallsRecommender;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.recommenders.usages.Query;

public class PBNModelStore implements IModelStore<BayesianNetwork> {

	public PBNModelStore(String dirmodels, String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void store(ITypeName t, BayesianNetwork net) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasModel(ITypeName object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BayesianNetwork getModel(ITypeName object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICallsRecommender<Query> getRecommender(ITypeName object) {
		// TODO Auto-generated method stub
		return null;
	}
}