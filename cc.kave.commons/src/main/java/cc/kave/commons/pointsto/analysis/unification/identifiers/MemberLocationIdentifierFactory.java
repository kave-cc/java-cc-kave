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
package cc.kave.commons.pointsto.analysis.unification.identifiers;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;

public class MemberLocationIdentifierFactory extends AbstractLocationIdentifierFactory {

	private static final LocationIdentifier INDEX_ACCESS_IDENTIFIER = new LocationIdentifier() {
		public String toString() {
			return "INDEX_ACCESS";
		};
	};

	@Override
	public LocationIdentifier create(IFieldReference fieldRef) {
		return new MemberLocationIdentifier(fieldRef.getFieldName());
	}

	@Override
	public LocationIdentifier create(IPropertyReference propertyRef) {
		return new MemberLocationIdentifier(propertyRef.getPropertyName());
	}

	@Override
	public LocationIdentifier create(IIndexAccessReference indexAccessRef, ITypeName baseType) {
		return INDEX_ACCESS_IDENTIFIER;
	}

	@Override
	protected LocationIdentifier create(IEventReference eventRef) {
		return new MemberLocationIdentifier(eventRef.getEventName());
	}
}