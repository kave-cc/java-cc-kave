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
package cc.kave.commons.pointsto.analysis.references;

public interface DistinctReferenceVisitor<TReturn, TContext> {

	TReturn visit(DistinctKeywordReference keywordRef, TContext context);

	TReturn visit(DistinctFieldReference fieldRef, TContext context);

	TReturn visit(DistinctVariableReference varRef, TContext context);

	TReturn visit(DistinctPropertyReference propertyRef, TContext context);

	TReturn visit(DistinctPropertyParameterReference propertyParameterRef, TContext context);

	TReturn visit(DistinctCatchBlockParameterReference catchBlockParameterRef, TContext context);

	TReturn visit(DistinctLambdaParameterReference lambdaParameterRef, TContext context);

	TReturn visit(DistinctMethodParameterReference methodParameterRef, TContext context);

	TReturn visit(DistinctIndexAccessReference indexAccessRef, TContext context);

	TReturn visit(DistinctEventReference eventRef, TContext context);
}
