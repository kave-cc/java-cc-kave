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
package cc.kave.commons.pointsto.analysis;

import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.pointsto.analysis.visitors.ScopingVisitor;

public class ReferenceCollectionVisitor extends ScopingVisitor<ReferenceCollectionContext, Void> {

	@Override
	public Void visit(IFieldDeclaration fieldDecl, ReferenceCollectionContext context) {
		IFieldName field = fieldDecl.getName();
		IReference fieldRef = SSTBuilder.fieldReference(field);
		context.addReference(fieldRef, field.getValueType());
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, ReferenceCollectionContext context) {
		context.addReference(fieldRef, fieldRef.getFieldName().getValueType());
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration propertyDecl, ReferenceCollectionContext context) {
		IPropertyName property = propertyDecl.getName();
		IReference propertyRef = SSTBuilder.propertyReference(property);
		context.addReference(propertyRef, property.getValueType());

		return super.visit(propertyDecl, context);
	}

	@Override
	public Void visit(IPropertyReference propertyRef, ReferenceCollectionContext context) {
		context.addReference(propertyRef, propertyRef.getPropertyName().getValueType());
		return null;
	}

	@Override
	public Void visit(IIndexAccessReference indexAccessRef, ReferenceCollectionContext context) {
		context.addIndexAccessReference(indexAccessRef);
		return super.visit(indexAccessRef, context);
	}
}