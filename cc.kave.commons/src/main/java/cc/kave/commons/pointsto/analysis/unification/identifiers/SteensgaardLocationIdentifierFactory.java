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

public class SteensgaardLocationIdentifierFactory extends AbstractLocationIdentifierFactory {

	@Override
	public LocationIdentifier create(IFieldReference fieldRef) {
		if (fieldRef.getFieldName().getValueType().isDelegateType()) {
			return LocationIdentifier.FUNCTION;
		} else {
			return LocationIdentifier.VALUE;
		}
	}

	@Override
	public LocationIdentifier create(IPropertyReference propertyRef) {
		if (propertyRef.getPropertyName().getValueType().isDelegateType()) {
			return LocationIdentifier.FUNCTION;
		} else {
			return LocationIdentifier.VALUE;
		}
	}

	@Override
	public LocationIdentifier create(IIndexAccessReference indexAccessRef, ITypeName baseType) {
		return LocationIdentifier.VALUE;
	}

	@Override
	protected LocationIdentifier create(IEventReference eventRef) {
		if (eventRef.getEventName().getHandlerType().isDelegateType()) {
			return LocationIdentifier.FUNCTION;
		} else {
			return LocationIdentifier.VALUE;
		}
	}
}