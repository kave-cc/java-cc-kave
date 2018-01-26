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

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.pointsto.analysis.unification.SetRepresentative;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifier;

public class SimpleReferenceLocation extends ReferenceLocation {

	private static final String ILLEGAL_IDENTIFIER_MSG = "identifier must be either VALUE or FUNCTION";

	private Location valueLocation;
	private Location functionLocation;

	public SimpleReferenceLocation(SetRepresentative setRepresentative, Location valueLocation,
			Location functionLocation) {
		super(setRepresentative);
		this.valueLocation = valueLocation;
		this.functionLocation = functionLocation;
	}

	@Override
	public Location getLocation(LocationIdentifier identifier) {
		if (identifier == LocationIdentifier.VALUE) {
			return valueLocation;
		} else if (identifier == LocationIdentifier.FUNCTION) {
			return functionLocation;
		} else {
			throw new IllegalArgumentException(ILLEGAL_IDENTIFIER_MSG);
		}
	}

	@Override
	public void setLocation(LocationIdentifier identifier, Location location) {
		if (identifier == LocationIdentifier.VALUE) {
			valueLocation = location;
		} else if (identifier == LocationIdentifier.FUNCTION) {
			functionLocation = location;
		} else {
			throw new IllegalArgumentException(ILLEGAL_IDENTIFIER_MSG);
		}
	}

	@Override
	public Set<LocationIdentifier> getIdentifiers() {
		return Sets.newHashSet(LocationIdentifier.VALUE, LocationIdentifier.FUNCTION);
	}

}
