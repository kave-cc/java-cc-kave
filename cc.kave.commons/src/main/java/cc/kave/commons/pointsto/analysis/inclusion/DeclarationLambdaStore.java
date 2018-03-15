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

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;
import cc.kave.commons.pointsto.analysis.names.DistinctMemberName;
import cc.kave.commons.pointsto.analysis.names.DistinctMemberNameFactory;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;

public final class DeclarationLambdaStore {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();
	private final DistinctMemberNameFactory nameFactory = new DistinctMemberNameFactory();

	private final Function<DistinctReference, SetVariable> variableResolver;
	private final SetVariableFactory variableFactory;

	private final Allocator allocator;
	private final DeclarationMapper declMapper;

	private final Map<DistinctMemberName, LambdaTerm> declarationLambdas = new HashMap<>();

	public DeclarationLambdaStore(Function<DistinctReference, SetVariable> variableResolver,
			SetVariableFactory variableFactory, Allocator allocator, DeclarationMapper declMapper) {
		this.variableResolver = variableResolver;
		this.variableFactory = variableFactory;
		this.allocator = allocator;
		this.declMapper = declMapper;
	}

	public DeclarationLambdaStore(DeclarationLambdaStore other,
			Function<DistinctReference, SetVariable> variableProvider, Allocator allocator) {
		this(variableProvider, other.variableFactory, allocator, other.declMapper);
		declarationLambdas.putAll(other.declarationLambdas);
	}

	public SetVariableFactory getVariableFactory() {
		return variableFactory;
	}

	public LambdaTerm getDeclarationLambda(IMemberName member) {
		DistinctMemberName name = nameFactory.create(member);
		LambdaTerm lambda = declarationLambdas.get(name);
		if (lambda == null) {
			if (member instanceof IMethodName) {
				lambda = createDeclarationLambda((IMethodName) member);
			} else if (member instanceof IPropertyName) {
				lambda = createDeclarationLambda((IPropertyName) member);
			} else {
				throw new UnexpectedNameException(member);
			}
			declarationLambdas.put(name, lambda);
		}

		return lambda;
	}

	private LambdaTerm createDeclarationLambda(IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);

		boolean isMethodWithoutDefinition = declMapper.get(method) == null;

		if (!method.isExtensionMethod()) {
			if (method.isStatic()) {
				variables.add(ConstructedTerm.BOTTOM);
			} else {
				variables.add(variableResolver.apply(
						new DistinctMethodParameterReference(parameter("this", method.getDeclaringType()), method)));
			}
		}

		for (IParameterName parameter : formalParameters) {
			DistinctReference parameterDistRef = new DistinctMethodParameterReference(parameter, method);
			SetVariable parameterVar = variableResolver.apply(parameterDistRef);
			variables.add(parameterVar);

			if (parameter.isOutput() && (isMethodWithoutDefinition || parameter.getValueType().isStructType())) {
				// methods without a definition require an object for their
				// out-parameters; struct out-parameters are
				// allocated even for methods which have a definition as they
				// already have a location on method entry
				// (although they remain uninitialized)
				allocator.allocateOutParameter(method, parameter, parameterVar);
			}
		}

		ITypeName returnType = method.getReturnType();
		if (!returnType.isVoidType()) {
			SetVariable returnVar = variableFactory.createReferenceVariable();
			// methods without a definition require an object to return
			if (isMethodWithoutDefinition) {
				allocator.allocateUndefinedReturnObject(method, returnVar, returnType);
			}
			variables.add(returnVar);
		}

		return LambdaTerm.newMethodLambda(variables, formalParameters, returnType);
	}

	private LambdaTerm createDeclarationLambda(IPropertyName property) {
		SetVariable thisVar = variableResolver
				.apply(new DistinctPropertyParameterReference("this", property.getDeclaringType(), property));
		SetVariable setParameterVar = variableResolver
				.apply(new DistinctPropertyParameterReference(languageOptions, property));
		SetVariable returnVar = variableFactory.createReferenceVariable();
		// properties without a definition require an object to return
		if (declMapper.get(property) == null) {
			allocator.allocateUndefinedReturnObject(property, returnVar, property.getValueType());
		}

		List<SetVariable> variables = Arrays.asList(thisVar, setParameterVar, returnVar);
		return LambdaTerm.newPropertyLambda(variables);
	}
}