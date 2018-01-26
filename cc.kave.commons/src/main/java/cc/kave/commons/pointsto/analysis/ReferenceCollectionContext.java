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

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.ScopedMap;
import cc.kave.commons.pointsto.analysis.visitors.ScopingVisitorContext;

public class ReferenceCollectionContext implements ScopingVisitorContext {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final ScopedMap<String, ITypeName> types = new ScopedMap<>();
	private final Multimap<IReference, ITypeName> references = HashMultimap.create();

	public ReferenceCollectionContext() {
		types.enter();
	}

	public Multimap<IReference, ITypeName> getReferences() {
		return references;
	}

	public void addReference(IReference ref, ITypeName type) {
		references.put(ref, type);
	}

	public void addIndexAccessReference(IIndexAccessReference indexAccessRef) {
		IVariableReference baseRef = indexAccessRef.getExpression().getReference();
		ITypeName type = types.get(baseRef.getIdentifier());
		if (type != null && type.isArray()) {
			references.put(indexAccessRef, type.asArrayTypeName().getArrayBaseType());
		} else {
			// cannot infer type if not a real array access
			references.put(indexAccessRef, Names.getUnknownType());
		}
	}

	@Override
	public void enterScope() {
		types.enter();
	}

	@Override
	public void leaveScope() {
		types.leave();
	}

	@Override
	public void declareParameter(IParameterName parameter, IMethodName method) {
		String name = parameter.getName();
		ITypeName type = parameter.getValueType();
		types.createOrUpdate(name, type);
		references.put(variableReference(name), type);
	}

	@Override
	public void declareParameter(IParameterName parameter, ILambdaExpression lambdaExpr) {
		String name = parameter.getName();
		ITypeName type = parameter.getValueType();
		types.createOrUpdate(name, type);
		references.put(variableReference(name), type);
	}

	@Override
	public void declareParameter(IParameterName parameter, ICatchBlock catchBlock) {
		String name = parameter.getName();
		types.create(name, parameter.getValueType());
		references.put(variableReference(name), parameter.getValueType());
	}

	@Override
	public void declarePropertySetParameter(IPropertyDeclaration propertyDecl) {
		String name = languageOptions.getPropertyParameterName();
		ITypeName type = propertyDecl.getName().getValueType();
		types.create(name, type);
		references.put(variableReference(name), type);
	}

	@Override
	public void declareVariable(IVariableDeclaration varDecl) {
		types.createOrUpdate(varDecl.getReference().getIdentifier(), varDecl.getType());
		references.put(varDecl.getReference(), varDecl.getType());
	}

}
