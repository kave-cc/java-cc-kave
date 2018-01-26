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
package cc.kave.commons.pointsto.analysis.visitors;

import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class ReferenceNormalizationVisitor extends FailSafeNodeVisitor<Void, IReference> {

	@Override
	public IReference visit(IUnknownReference unknownRef, Void context) {
		return null;
	}

	@Override
	public IReference visit(IFieldReference fieldRef, Void context) {
		IFieldName field = fieldRef.getFieldName();
		if (field.isUnknown()) {
			return null;
		} else if (!field.isStatic() && fieldRef.getReference().isMissing()) {
			return null;
		} else {
			return fieldRef;
		}
	}

	@Override
	public IReference visit(IVariableReference varRef, Void context) {
		return varRef.isMissing() ? null : varRef;
	}

	@Override
	public IReference visit(IPropertyReference propertyRef, Void context) {
		IPropertyName property = propertyRef.getPropertyName();
		if (property.isUnknown()) {
			return null;
		} else if (!property.isStatic() && propertyRef.getReference().isMissing()) {
			return null;
		} else {
			return propertyRef;
		}
	}

	@Override
	public IReference visit(IIndexAccessReference indexAccessRef, Void context) {
		IReference baseRef = indexAccessRef.getExpression().getReference().accept(this, null);
		if (baseRef == null) {
			return null;
		}
		return indexAccessRef;
	}

	@Override
	public IReference visit(IMethodReference methodRef, Void context) {
		if (methodRef.getMethodName().isUnknown()) {
			return null;
		} else {
			return methodRef;
		}
	}

	@Override
	public IReference visit(IEventReference eventRef, Void context) {
		IEventName event = eventRef.getEventName();
		if (event.isUnknown()) {
			return null;
		} else if (!event.isStatic() && eventRef.getReference().isMissing()) {
			return null;
		} else {
			return eventRef;
		}
	}
}