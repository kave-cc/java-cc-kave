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
package cc.kave.rsse.calls.mining;

import java.util.List;

import cc.kave.rsse.calls.datastructures.Dictionary;
import cc.kave.rsse.calls.pbn.clustering.Pattern;

public interface ModelBuilder<Feature, Model> {

    public Model build(List<Pattern<Feature>> patterns, Dictionary<Feature> dictionary);
}