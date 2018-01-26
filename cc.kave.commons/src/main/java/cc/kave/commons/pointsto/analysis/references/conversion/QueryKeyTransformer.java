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
package cc.kave.commons.pointsto.analysis.references.conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.references.DistinctCatchBlockParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctEventReference;
import cc.kave.commons.pointsto.analysis.references.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.references.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.references.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.references.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReferenceVisitor;
import cc.kave.commons.pointsto.analysis.references.DistinctVariableReference;

public class QueryKeyTransformer
		implements DistinctReferenceVisitor<List<PointsToQuery>, DistinctReferenceContextCollector> {

	private boolean enableStmtsForVariables = false;

	public QueryKeyTransformer(boolean enableStmtsForVariables) {
		this.enableStmtsForVariables = enableStmtsForVariables;
	}

	@Override
	public List<PointsToQuery> visit(DistinctKeywordReference keywordRef, DistinctReferenceContextCollector context) {
		PointsToQuery query = new PointsToQuery(keywordRef.getReference(), keywordRef.getType(), null, null);
		return Arrays.asList(query);
	}

	@Override
	public List<PointsToQuery> visit(DistinctFieldReference fieldRef, DistinctReferenceContextCollector context) {
		PointsToQuery query = new PointsToQuery(fieldRef.getReference(), fieldRef.getType(), null, null);
		return Arrays.asList(query);
	}

	@Override
	public List<PointsToQuery> visit(DistinctVariableReference varRef, DistinctReferenceContextCollector context) {
		Collection<IMemberName> members = context.getMembers(varRef);
		// if a declared variable is not used in a member, there will be no
		// associated members or statements
		Asserts.assertLessOrEqual(members.size(), 1);
		IMemberName member = members.isEmpty() ? null : members.iterator().next();

		if (enableStmtsForVariables) {
			Collection<IStatement> statements = context.getStatements(varRef);
			List<PointsToQuery> queryKeys = new ArrayList<>(statements.size());

			for (IStatement stmt : statements) {
				PointsToQuery query = new PointsToQuery(varRef.getReference(), varRef.getType(), stmt, member);
				queryKeys.add(query);
			}

			return queryKeys;
		} else {
			PointsToQuery query = new PointsToQuery(varRef.getReference(), varRef.getType(), null, member);
			return Arrays.asList(query);
		}
	}

	@Override
	public List<PointsToQuery> visit(DistinctPropertyReference propertyRef, DistinctReferenceContextCollector context) {
		PointsToQuery query = new PointsToQuery(propertyRef.getReference(), propertyRef.getType(), null, null);
		return Arrays.asList(query);
	}

	@Override
	public List<PointsToQuery> visit(DistinctPropertyParameterReference propertyParameterRef,
			DistinctReferenceContextCollector context) {
		IReference reference = propertyParameterRef.getReference();
		ITypeName type = propertyParameterRef.getType();
		Collection<IStatement> statements = context.getStatements(propertyParameterRef);
		Collection<IMemberName> members = context.getMembers(propertyParameterRef);
		IMemberName member = members.isEmpty() ? null : members.iterator().next();
		List<PointsToQuery> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			PointsToQuery query = new PointsToQuery(reference, type, stmt, member);
			queryKeys.add(query);
		}

		return queryKeys;
	}

	@Override
	public List<PointsToQuery> visit(DistinctCatchBlockParameterReference catchBlockParameterRef,
			DistinctReferenceContextCollector context) {
		Collection<IStatement> statements = context.getStatements(catchBlockParameterRef);
		Collection<IMemberName> members = context.getMembers(catchBlockParameterRef);
		Asserts.assertLessOrEqual(members.size(), 1);
		IMemberName member = members.isEmpty() ? null : members.iterator().next();
		IReference reference = catchBlockParameterRef.getReference();
		ITypeName type = catchBlockParameterRef.getType();
		List<PointsToQuery> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			PointsToQuery query = new PointsToQuery(reference, type, stmt, member);
			queryKeys.add(query);
		}

		return queryKeys;
	}

	@Override
	public List<PointsToQuery> visit(DistinctLambdaParameterReference lambdaParameterRef,
			DistinctReferenceContextCollector context) {
		Collection<IStatement> statements = context.getStatements(lambdaParameterRef);
		Collection<IMemberName> members = context.getMembers(lambdaParameterRef);
		// the user is free to write lambdas which do not use a parameter
		Asserts.assertLessOrEqual(members.size(), 1);
		IMemberName member = members.isEmpty() ? null : members.iterator().next();
		ITypeName type = lambdaParameterRef.getType();
		IReference reference = lambdaParameterRef.getReference();
		List<PointsToQuery> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			PointsToQuery query = new PointsToQuery(reference, type, stmt, member);
			queryKeys.add(query);
		}

		return queryKeys;
	}

	@Override
	public List<PointsToQuery> visit(DistinctMethodParameterReference methodParameterRef,
			DistinctReferenceContextCollector context) {
		IMemberName member = methodParameterRef.getMethod();
		ITypeName type = methodParameterRef.getType();
		IReference reference = methodParameterRef.getReference();
		ArrayList<PointsToQuery> queryKeys = new ArrayList<>();

		if (enableStmtsForVariables) {
			Collection<IStatement> statements = context.getStatements(methodParameterRef);
			queryKeys.ensureCapacity(statements.size());

			for (IStatement stmt : statements) {
				PointsToQuery query = new PointsToQuery(reference, type, stmt, member);
				queryKeys.add(query);
			}
		}

		// parameters are available regardless of statements that use them so
		// that they can be queried by only looking
		// at the declaring method
		queryKeys.add(new PointsToQuery(reference, type, null, member));

		return queryKeys;
	}

	@Override
	public List<PointsToQuery> visit(DistinctIndexAccessReference indexAccessRef,
			DistinctReferenceContextCollector context) {
		Collection<IMemberName> members = context.getMembers(indexAccessRef);
		if (members.isEmpty()) {
			members = Collections.singletonList(null);
		}
		Collection<IStatement> statements = context.getStatements(indexAccessRef);

		List<PointsToQuery> queries = new ArrayList<>(members.size() * statements.size());
		for (IMemberName member : members) {
			for (IStatement stmt : statements) {
				queries.add(new PointsToQuery(indexAccessRef.getReference(), indexAccessRef.getType(), stmt, member));
			}
		}

		return queries;
	}

	@Override
	public List<PointsToQuery> visit(DistinctEventReference eventRef, DistinctReferenceContextCollector context) {
		PointsToQuery query = new PointsToQuery(eventRef.getReference(), eventRef.getType(), null, null);
		return Arrays.asList(query);
	}
}