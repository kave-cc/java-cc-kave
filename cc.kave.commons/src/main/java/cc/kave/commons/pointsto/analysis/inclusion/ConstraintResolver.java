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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintEdge;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintNode;
import cc.kave.commons.utils.io.Logger;

public class ConstraintResolver {

	private Function<SetExpression, ConstraintNode> nodeResolver;

	public ConstraintResolver(Function<SetExpression, ConstraintNode> nodeResolver) {
		this.nodeResolver = nodeResolver;
	}

	public Set<ConstraintNode> addConstraint(SetExpression setExpr1, SetExpression setExpr2,
			InclusionAnnotation inclusionAnnotation, ContextAnnotation contextAnnotation) {
		if (ConstructedTerm.BOTTOM.equals(setExpr1)) {
			return Collections.emptySet();
		}

		if (setExpr1 instanceof SetVariable) {
			SetVariable var1 = (SetVariable) setExpr1;

			if (setExpr2 instanceof SetVariable) {
				SetVariable var2 = (SetVariable) setExpr2;
				return addConstraint(var1, var2, inclusionAnnotation, contextAnnotation);
			} else if ((setExpr2 instanceof ConstructedTerm) || (setExpr2 instanceof Projection)) {
				return addConstraintVarSink(var1, setExpr2, inclusionAnnotation, contextAnnotation);
			} else {
				throw new IllegalArgumentException(setExpr2.getClass().getName());
			}
		} else if (setExpr1 instanceof ConstructedTerm) {
			ConstructedTerm cTerm1 = (ConstructedTerm) setExpr1;

			if (setExpr2 instanceof SetVariable) {
				return addConstraintSourceVar(cTerm1, (SetVariable) setExpr2, inclusionAnnotation, contextAnnotation);
			} else if (setExpr2 instanceof ConstructedTerm) {
				return resolve(cTerm1, (ConstructedTerm) setExpr2, inclusionAnnotation, contextAnnotation);
			} else if (setExpr2 instanceof Projection) {
				return resolve(cTerm1, (Projection) setExpr2, inclusionAnnotation, contextAnnotation);
			} else {
				throw new IllegalArgumentException(setExpr2.getClass().getName());
			}
		} else {
			throw new IllegalArgumentException(setExpr1.getClass().getName());
		}
	}

	public Set<ConstraintNode> addConstraint(SetVariable var1, SetVariable var2,
			InclusionAnnotation inclusionAnnotation, ContextAnnotation contextAnnotation) {
		if (ConstructedTerm.BOTTOM.equals(var1)) {
			// BOTTOM ⊆ x
			return Collections.emptySet();
		}

		ConstraintNode n1 = nodeResolver.apply(var1);
		ConstraintNode n2 = nodeResolver.apply(var2);

		if (var1.compareTo(var2) < 0) {
			if (n2.addPredecessor(new ConstraintEdge(n1, inclusionAnnotation, contextAnnotation))) {
				return ImmutableSet.of(n2);
			}
		} else {
			if (n1.addSuccessor(new ConstraintEdge(n2, inclusionAnnotation, contextAnnotation))) {
				return ImmutableSet.of(n1);
			}
		}

		return Collections.emptySet();
	}

	private Set<ConstraintNode> addConstraintVarSink(SetVariable var, SetExpression setExpr,
			InclusionAnnotation inclusionAnnotation, ContextAnnotation contextAnnotation) {
		ConstraintNode n1 = nodeResolver.apply(var);
		ConstraintNode n2 = nodeResolver.apply(setExpr);
		if (n1.addSuccessor(new ConstraintEdge(n2, inclusionAnnotation, contextAnnotation))) {
			return ImmutableSet.of(n1);
		}
		return Collections.emptySet();
	}

	private Set<ConstraintNode> addConstraintSourceVar(SetExpression setExpr, SetVariable var,
			InclusionAnnotation inclusionAnnotation, ContextAnnotation contextAnnotation) {
		ConstraintNode n1 = nodeResolver.apply(setExpr);
		ConstraintNode n2 = nodeResolver.apply(var);
		if (n2.addPredecessor(new ConstraintEdge(n1, inclusionAnnotation, contextAnnotation))) {
			return ImmutableSet.of(n2);
		}
		return Collections.emptySet();
	}

	public Set<ConstraintNode> resolve(ConstructedTerm c1, ConstructedTerm c2, InclusionAnnotation inclusionAnnotation,
			ContextAnnotation contextAnnotation) {
		final int numArgs = c1.getNumberOfArguments();
		if (numArgs != c2.getNumberOfArguments()) {
			if (c1 instanceof LambdaTerm && c2 instanceof LambdaTerm) {
				// this happens when a delegate invocation does not match the type of the associated lambda: the SST
				// generation analysis currently does not seem to support anonymous delegates and simply maps them to
				// empty lambdas (which violate the delegate type)
				Logger.log("Skipping resolution of a constraint between two LambdaTerms because of a different number of arguments ({} vs. {})",
						numArgs, c2.getNumberOfArguments());
				return Collections.emptySet();
			} else {
				Asserts.fail(
						"Cannot resolve constraint between two ConstructedTerms because of a different number of arguments ("
								+ numArgs + " vs. " + c2.getNumberOfArguments() + ")");
			}
		}
		if (!checkForEqualVariance(c1, c2, numArgs)) {
			Logger.err(
					"Cannot resolve constraint between {} and {} because at least one argument pair has different variances",
					c1.getClass().getSimpleName(), c2.getClass().getSimpleName());
			return Collections.emptySet();
		}

		Set<ConstraintNode> changedNodes = new HashSet<>(numArgs);
		for (int i = 0; i < numArgs; ++i) {
			Variance variance = c1.getArgumentVariance(i);

			SetVariable v1 = c1.getArgument(i);
			SetVariable v2 = c2.getArgument(i);

			switch (variance) {
			case COVARIANT:
				changedNodes.addAll(addConstraint(v1, v2, inclusionAnnotation, contextAnnotation));
				break;

			case CONTRAVARIANT:
				changedNodes.addAll(addConstraint(v2, v1, inclusionAnnotation, transpose(contextAnnotation)));
				break;

			default:
				Asserts.fail("Illegal variance value");
				break;
			}
		}

		return changedNodes;
	}

	public Set<ConstraintNode> resolve(ConstructedTerm constTerm, Projection projection,
			InclusionAnnotation inclusionAnnotation, ContextAnnotation contextAnnotation) {
		if (projection.getConstructor() != constTerm.getClass()) {
			Logger.log("Constructed term does not match projection: {} != {}", constTerm.getClass().getSimpleName(),
					projection.getConstructor().getSimpleName());
			return Collections.emptySet();
		}

		int index = projection.getArgIndex();
		SetVariable cVar = constTerm.getArgument(index);
		SetVariable projVar = projection.getVariable();

		switch (constTerm.getArgumentVariance(index)) {
		case COVARIANT:
			return addConstraint(cVar, projVar, inclusionAnnotation, contextAnnotation);

		case CONTRAVARIANT:
			return addConstraint(projVar, cVar, inclusionAnnotation, transpose(contextAnnotation));

		default:
			Asserts.fail("Illegal variance value");
			break;
		}

		return Collections.emptySet();
	}

	private ContextAnnotation transpose(ContextAnnotation contextAnnotation) {
		if (ContextAnnotation.EMPTY.equals(contextAnnotation)) {
			return ContextAnnotation.EMPTY;
		} else {
			return new ContextAnnotation(contextAnnotation.getRight(), contextAnnotation.getLeft());
		}
	}

	private boolean checkForEqualVariance(ConstructedTerm c1, ConstructedTerm c2, int numArgs) {
		for (int i = 0; i < numArgs; ++i) {
			if (c1.getArgumentVariance(i) != c2.getArgumentVariance(i)) {
				return false;
			}
		}
		return true;
	}
}
