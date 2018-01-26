/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.mining.calls;

import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public interface ICallsRecommender<Query> {

	Set<Tuple<IMethodName, Double>> query(Context ctx);

	Set<Tuple<IMethodName, Double>> query2(Query query);

	Set<Tuple<ICoReMethodName, Double>> query(Query query);

	/**
	 * @return the number of bytes necessary to store the model
	 */
	int getSize();
}
