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
package cc.kave.rsse.calls.pbn.clustering;

import java.util.List;

import cc.kave.rsse.calls.datastructures.Dictionary;

public interface PatternFinder<Feature> {

    public List<Pattern<Feature>> find(List<List<Feature>> usages, Dictionary<Feature> dictionary);
}