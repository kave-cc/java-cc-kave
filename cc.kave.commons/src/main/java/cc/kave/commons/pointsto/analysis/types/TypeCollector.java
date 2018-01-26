/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;

public class TypeCollector {

	private IdentityHashMap<IReference, ITypeName> referenceTypes = new IdentityHashMap<>();
	private Set<ITypeName> allTypes = new HashSet<>();

	private final ReferenceTypeVisitor typeVisitor = new ReferenceTypeVisitor();

	public TypeCollector(Context context) {
		TypeCollectorVisitor visitor = new TypeCollectorVisitor();
		TypeCollectorVisitorContext visitorContext = new TypeCollectorVisitorContext(context);

		visitor.visit(context.getSST(), visitorContext);

		referenceTypes = visitorContext.getReferenceTypes();
		allTypes = visitorContext.getTypes();
	}

	public ITypeName getType(IReference reference) {
		ITypeName type = referenceTypes.get(reference);
		if (type == null) {
			return reference.accept(typeVisitor, null);
		}
		return type;
	}

	public Set<ITypeName> getTypes() {
		return Collections.unmodifiableSet(allTypes);
	}

	private static class ReferenceTypeVisitor extends FailSafeNodeVisitor<Void, ITypeName> {

		@Override
		public ITypeName visit(IFieldReference fieldRef, Void context) {
			return fieldRef.getFieldName().getValueType();
		}

		@Override
		public ITypeName visit(IPropertyReference propertyRef, Void context) {
			return propertyRef.getPropertyName().getValueType();
		}

		@Override
		public ITypeName visit(IEventReference eventRef, Void context) {
			return eventRef.getEventName().getValueType();
		}

		@Override
		public ITypeName visit(IMethodReference methodRef, Void context) {
			return null;
		}

		@Override
		public ITypeName visit(IIndexAccessReference indexAccessRef, Void context) {
			return null;
		}

		@Override
		public ITypeName visit(IUnknownReference unknownRef, Void context) {
			return null;
		}

		@Override
		public ITypeName visit(IVariableReference varRef, Void context) {
			return null;
		}
	}
}
