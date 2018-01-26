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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.pointsto.analysis.unification.SetRepresentative;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifier;

public class ExtendedReferenceLocation extends ReferenceLocation {

	private Map<LocationIdentifier, Location> locations = new HashMap<>();

	public ExtendedReferenceLocation(SetRepresentative setRepresentative) {
		super(setRepresentative);
	}

	@Override
	public Location getLocation(LocationIdentifier identifier) {
		return locations.get(identifier);
	}

	@Override
	public void setLocation(LocationIdentifier identifier, Location location) {
		locations.put(identifier, location);
	}

	@Override
	public Set<LocationIdentifier> getIdentifiers() {
		return Collections.unmodifiableSet(locations.keySet());
	}

}
