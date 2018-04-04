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
package cc.kave.rsse.calls;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.usages.Usage;

public interface IModelStore<TModel> {
	
	// TODO should not be part of modelStore, create helper class
	void clear();
	
	void store(ITypeName t, TModel model);

	boolean hasModel(ITypeName t);

	TModel getModel(ITypeName t);
}