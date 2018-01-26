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
package cc.kave.commons.pointsto.analysis.unification.locations;

import java.util.List;

import cc.kave.commons.pointsto.analysis.unification.SetRepresentative;

public class FunctionLocation extends Location {

	private List<ReferenceLocation> parameterLocations;
	private ReferenceLocation returnLocation;

	public FunctionLocation(List<ReferenceLocation> parameterLocations, ReferenceLocation returnLocation,
			SetRepresentative setRepresentative) {
		super(setRepresentative);
		this.parameterLocations = parameterLocations;
		this.returnLocation = returnLocation;
	}

	public List<ReferenceLocation> getParameterLocations() {
		return parameterLocations;
	}

	public ReferenceLocation getReturnLocation() {
		return returnLocation;
	}

	@Override
	public final boolean isBottom() {
		return false;
	}

}
