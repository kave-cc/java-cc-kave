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

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;
import cc.kave.commons.pointsto.analysis.references.DistinctCatchBlockParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.references.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctVariableReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.SSTBuilder;
import cc.kave.commons.pointsto.analysis.utils.ScopedMap;

public abstract class DistinctReferenceVisitorContext implements ScopingVisitorContext {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final ThisReferenceOption thisReferenceOption;
	private final ITypeName thisType;
	private final ITypeName superType;

	protected ScopedMap<String, DistinctReference> namesToReferences = new ScopedMap<>();

	public DistinctReferenceVisitorContext(Context context, ThisReferenceOption thisReferenceOption) {
		this.thisReferenceOption = thisReferenceOption;

		ITypeHierarchy typeHierarchy = context.getTypeShape().getTypeHierarchy();
		this.thisType = typeHierarchy.getElement();
		this.superType = languageOptions.getSuperType(typeHierarchy);

		namesToReferences.enter();
		createImplicitReferences(context);
	}

	private void createImplicitReferences(Context context) {
		if (thisReferenceOption == ThisReferenceOption.PER_CONTEXT) {
			DistinctReference thisRef = new DistinctKeywordReference(languageOptions.getThisName(), thisType);
			namesToReferences.create(languageOptions.getThisName(), thisRef);
			DistinctReference superRef = new DistinctKeywordReference(languageOptions.getSuperName(), superType);
			namesToReferences.create(languageOptions.getSuperName(), superRef);
		}
	}

	public void enterMember(IMemberName member) {
		if (thisReferenceOption == ThisReferenceOption.PER_MEMBER) {
			enterScope();
			DistinctReference thisDistRef;
			DistinctReference superDistRef;
			if (member instanceof IMethodName) {
				IMethodName method = (IMethodName) member;
				thisDistRef = new DistinctMethodParameterReference(
						SSTBuilder.parameter(languageOptions.getThisName(), thisType), method);
				superDistRef = new DistinctMethodParameterReference(
						SSTBuilder.parameter(languageOptions.getSuperName(), superType), method);
			} else if (member instanceof IPropertyName) {
				IPropertyName property = (IPropertyName) member;
				thisDistRef = new DistinctPropertyParameterReference(languageOptions.getThisName(), thisType, property);
				superDistRef = new DistinctPropertyParameterReference(languageOptions.getSuperName(), superType,
						property);
			} else {
				throw new UnexpectedNameException(member);
			}
			namesToReferences.create(languageOptions.getThisName(), thisDistRef);
			namesToReferences.create(languageOptions.getSuperName(), superDistRef);
		}
	}

	public void leaveMember() {
		if (thisReferenceOption == ThisReferenceOption.PER_MEMBER) {
			leaveScope();
		}
	}

	@Override
	public void enterScope() {
		namesToReferences.enter();
	}

	@Override
	public void leaveScope() {
		namesToReferences.leave();
	}

	@Override
	public void declareParameter(IParameterName parameter, IMethodName method) {
		namesToReferences.create(parameter.getName(), new DistinctMethodParameterReference(parameter, method));
	}

	@Override
	public void declareParameter(IParameterName parameter, ILambdaExpression lambdaExpr) {
		namesToReferences.create(parameter.getName(), new DistinctLambdaParameterReference(parameter, lambdaExpr));
	}

	@Override
	public void declareParameter(IParameterName parameter, ICatchBlock catchBlock) {
		namesToReferences.create(parameter.getName(), new DistinctCatchBlockParameterReference(catchBlock));
	}

	@Override
	public void declarePropertySetParameter(IPropertyDeclaration propertyDecl) {
		namesToReferences.create(languageOptions.getPropertyParameterName(),
				new DistinctPropertyParameterReference(languageOptions, propertyDecl.getName()));
	}

	@Override
	public void declareVariable(IVariableDeclaration varDecl) {
		namesToReferences.createOrUpdate(varDecl.getReference().getIdentifier(),
				new DistinctVariableReference(varDecl));
	}
}