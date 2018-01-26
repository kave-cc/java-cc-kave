/**
 * Copyright (c) 2010 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 */
package cc.recommenders.names;

/**
 * References are basically full qualified names which allow clients to resolve
 * and map these references to something else.
 */
public interface ICoReName {
    /**
     * As a guideline, names should return a meaningful representation of
     * themselves here.
     */
    String getIdentifier();
}
