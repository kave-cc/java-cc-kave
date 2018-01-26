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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;

public class LambdaTerm implements ConstructedTerm {

	private final SetVariable[] args;
	private final Variance[] variances;

	private LambdaTerm(List<SetVariable> args, List<Variance> variances) {
		Asserts.assertEquals(args.size(), variances.size());
		this.args = args.toArray(new SetVariable[args.size()]);
		this.variances = variances.toArray(new Variance[variances.size()]);
	}

	public static LambdaTerm newMethodLambda(List<SetVariable> variables, List<IParameterName> formalParameters,
			ITypeName returnType) {
		List<Variance> variances = determineMethodVariances(variables.size(), formalParameters, returnType);
		return new LambdaTerm(variables, variances);
	}

	public static LambdaTerm newPropertyLambda(List<SetVariable> variables) {
		List<Variance> variances = Arrays.asList(Variance.CONTRAVARIANT, Variance.CONTRAVARIANT, Variance.COVARIANT);
		return new LambdaTerm(variables, variances);
	}

	private static List<Variance> determineMethodVariances(int numVars, List<IParameterName> formalParameters,
			ITypeName returnType) {
		List<Variance> variances = new ArrayList<>(numVars);

		// this-parameter is always contravariant
		variances.add(Variance.CONTRAVARIANT);

		for (IParameterName parameter : formalParameters) {
			if (parameter.isExtensionMethodParameter()) {
				// variance of this-parameter already set
				continue;
			}

			if (parameter.isOutput()) {
				variances.add(Variance.COVARIANT);
			} else {
				variances.add(Variance.CONTRAVARIANT);
			}
		}

		if (!returnType.isVoidType()) {
			variances.add(Variance.COVARIANT);
		}

		return variances;
	}

	@Override
	public int getNumberOfArguments() {
		return args.length;
	}

	@Override
	public SetVariable getArgument(int index) {
		return args[index];
	}

	@Override
	public Variance getArgumentVariance(int index) {
		return variances[index];
	}
}