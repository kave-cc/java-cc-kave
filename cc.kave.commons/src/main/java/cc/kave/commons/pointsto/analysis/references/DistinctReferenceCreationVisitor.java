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
package cc.kave.commons.pointsto.analysis.references;

import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.exceptions.MissingBaseVariableException;
import cc.kave.commons.pointsto.analysis.exceptions.MissingVariableException;
import cc.kave.commons.pointsto.analysis.exceptions.UndeclaredVariableException;
import cc.kave.commons.pointsto.analysis.utils.ScopedMap;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;

public class DistinctReferenceCreationVisitor
		extends FailSafeNodeVisitor<ScopedMap<String, DistinctReference>, DistinctReference> {

	@Override
	public DistinctReference visit(IFieldReference fieldRef, ScopedMap<String, DistinctReference> context) {
		IVariableReference baseRef = fieldRef.getReference();
		DistinctReference distBaseRef = context.get(baseRef.getIdentifier());

		if (!fieldRef.getFieldName().isStatic()) {
			if (baseRef.isMissing()) {
				throw new MissingBaseVariableException(fieldRef);
			}

			if (distBaseRef == null) {
				throw new UndeclaredVariableException(baseRef);
			}
		}

		return new DistinctFieldReference(fieldRef, distBaseRef);
	}

	@Override
	public DistinctReference visit(IPropertyReference propertyRef, ScopedMap<String, DistinctReference> context) {
		IVariableReference baseRef = propertyRef.getReference();
		DistinctReference distBaseRef = context.get(baseRef.getIdentifier());

		if (!propertyRef.getPropertyName().isStatic()) {
			if (baseRef.isMissing()) {
				throw new MissingBaseVariableException(propertyRef);
			}

			if (distBaseRef == null) {
				throw new UndeclaredVariableException(baseRef);
			}
		}

		return new DistinctPropertyReference(propertyRef, distBaseRef);
	}

	@Override
	public DistinctReference visit(IVariableReference varRef, ScopedMap<String, DistinctReference> context) {
		if (varRef.isMissing()) {
			throw new MissingVariableException(varRef);
		}

		DistinctReference distRef = context.get(varRef.getIdentifier());
		if (distRef == null) {
			throw new UndeclaredVariableException(varRef);
		}

		return distRef;
	}

	@Override
	public DistinctReference visit(IIndexAccessReference indexAccessRef, ScopedMap<String, DistinctReference> context) {
		IVariableReference baseRef = indexAccessRef.getExpression().getReference();

		return new DistinctIndexAccessReference(indexAccessRef, baseRef.accept(this, context));
	}

	@Override
	public DistinctReference visit(IEventReference eventRef, ScopedMap<String, DistinctReference> context) {
		IVariableReference baseRef = eventRef.getReference();
		DistinctReference distBaseRef = context.get(baseRef.getIdentifier());

		if (!eventRef.getEventName().isStatic()) {
			if (baseRef.isMissing()) {
				throw new MissingBaseVariableException(eventRef);
			}

			if (distBaseRef == null) {
				throw new UndeclaredVariableException(baseRef);
			}
		}

		return new DistinctEventReference(eventRef, distBaseRef);
	}

}
