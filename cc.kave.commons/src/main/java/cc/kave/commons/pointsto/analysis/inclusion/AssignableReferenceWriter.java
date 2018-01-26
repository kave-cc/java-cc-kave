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

import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraphBuilder;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.FailSafeNodeVisitor;

public class AssignableReferenceWriter extends FailSafeNodeVisitor<SetVariable, Void> {

	private final ConstraintGraphBuilder builder;
	private final PropertyAsFieldPredicate treatPropertyAsField;

	public AssignableReferenceWriter(ConstraintGraphBuilder builder, PropertyAsFieldPredicate treatPropertyAsField) {
		this.builder = builder;
		this.treatPropertyAsField = treatPropertyAsField;
	}

	public void assign(IAssignableReference dest, SetVariable source) {
		dest.accept(this, source);
	}

	@Override
	public Void visit(IVariableReference varRef, SetVariable source) {
		builder.alias(builder.getVariable(varRef), source);
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, SetVariable source) {
		builder.writeMember(eventRef, source, eventRef.getEventName());
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, SetVariable source) {
		builder.writeMember(fieldRef, source, fieldRef.getFieldName());
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, SetVariable source) {
		builder.writeArray(indexAccessRef, source);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, SetVariable source) {
		IPropertyName property = propertyRef.getPropertyName();
		if (treatPropertyAsField.test(property)) {
			builder.writeMember(propertyRef, source, property);
		} else {
			builder.invokeSetProperty(propertyRef, source);
		}
		return null;
	}

	public static class BuilderSourcePair {
		public final ConstraintGraphBuilder builder;
		public final SetVariable source;

		public BuilderSourcePair(ConstraintGraphBuilder builder, SetVariable source) {
			this.builder = builder;
			this.source = source;
		}
	}
}