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
package cc.kave.commons.pointsto.analysis.inclusion;

import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class ConstructorMemberInitializationVisitor extends TraversingVisitor<Set<IMemberName>, Void> {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final FailSafeNodeVisitor<Set<IMemberName>, Void> memberReferenceVisitor = new FailSafeNodeVisitor<Set<IMemberName>, Void>() {

		public Void visit(IFieldReference fieldRef, Set<IMemberName> context) {
			context.add(fieldRef.getFieldName());
			return null;
		}

		@Override
		public Void visit(IPropertyReference propertyRef, Set<IMemberName> context) {
			IPropertyName property = propertyRef.getPropertyName();
			if (treatPropertyAsField.test(property)) {
				context.add(property);
			}
			return null;
		}

		@Override
		public Void visit(IEventReference eventRef, Set<IMemberName> context) {
			context.add(eventRef.getEventName());
			return null;
		}

		@Override
		public Void visit(IMethodReference methodRef, Set<IMemberName> context) {
			return null;
		}
	};

	private final PropertyAsFieldPredicate treatPropertyAsField;

	public ConstructorMemberInitializationVisitor(PropertyAsFieldPredicate treatPropertyAsField) {
		this.treatPropertyAsField = treatPropertyAsField;
	}

	@Override
	public Void visit(IAssignment stmt, Set<IMemberName> context) {
		if (stmt.getReference() instanceof IMemberReference) {
			IMemberReference memberRef = (IMemberReference) stmt.getReference();
			if (memberRef.getReference().getIdentifier().equals(languageOptions.getThisName())) {
				memberRef.accept(memberReferenceVisitor, context);
			}
		}

		return null;
	}
}