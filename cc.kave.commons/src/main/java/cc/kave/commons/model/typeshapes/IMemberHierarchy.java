/**
 * Copyright 2018 University of Zurich
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
package cc.kave.commons.model.typeshapes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cc.kave.commons.model.naming.codeelements.IMemberName;

public interface IMemberHierarchy<T extends IMemberName> {
	@Nonnull
	T getElement();

	@Nullable
	T getSuper();

	IMemberHierarchy<T> setSuper(T name);

	@Nullable
	T getFirst();

	IMemberHierarchy<T> setFirst(T name);

	boolean isDeclaredInParentHierarchy();
}