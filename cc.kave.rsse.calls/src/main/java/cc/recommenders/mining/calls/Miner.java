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

import java.util.List;

import cc.recommenders.mining.calls.ICallsRecommender;

public interface Miner<Input, Query> {

    public Object learnModel(List<Input> in);

    public ICallsRecommender<Query> createRecommender(List<Input> in);
}
