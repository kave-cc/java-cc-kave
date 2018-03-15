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

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.pointsto.analysis.utils.EnclosingNodeHelper;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;

/**
 * A convenience class for building {@link PointsToQuery} objects for a
 * {@link PointsToAnalysis}.
 * 
 * Uses the provided arguments and its internal information about the associated
 * {@link Context} to create a complete {@link PointsToQuery}.
 */
public class PointsToQueryBuilder {

	private final TypeCollector typeCollector;
	private final EnclosingNodeHelper enclosingNodes;
	private final SSTNodeHierarchy sstHierarchy;

	public PointsToQueryBuilder(Context context) {
		this.typeCollector = new TypeCollector(context);
		sstHierarchy = new SSTNodeHierarchy(context.getSST());
		this.enclosingNodes = new EnclosingNodeHelper(sstHierarchy);
	}

	/**
	 * Creates a new {@link PointsToQuery} for a {@link PointsToAnalysis} using the
	 * supplied {@link IReference} and {@link IStatement}. The {@link ITypeName} and
	 * the potentially surrounding method is inferred using stored information about
	 * the {@link ISST}. The enclosing {@link IMemberName} (method or property) will
	 * be inferred.
	 * 
	 * @param reference
	 *            ?
	 * @param stmt
	 *            ?
	 * @return ?
	 */
	public PointsToQuery newQuery(IReference reference, IStatement stmt) {
		// the following is possible but makes querying a lot harder!
		// IStatement stmt = enclosingNodes.getEnclosingStatement(reference);
		IMemberName member = enclosingNodes.getEnclosingMember(stmt);
		return newQuery(reference, stmt, member);
	}

	/**
	 * Creates a new {@link PointsToQuery} for a {@link PointsToAnalysis} using the
	 * supplied {@link IReference}, {@link IStatement} and {@link IMemberName}. The
	 * {@link ITypeName} is inferred using stored information about the
	 * {@link ISST}.
	 * 
	 * @param reference
	 *            ?
	 * @param stmt
	 *            ?
	 * @param member
	 *            ?
	 * @return ?
	 */
	public PointsToQuery newQuery(IReference reference, IStatement stmt, IMemberName member) {
		ITypeName type = typeCollector.getType(reference);
		return new PointsToQuery(reference, type, stmt, member);
	}
}