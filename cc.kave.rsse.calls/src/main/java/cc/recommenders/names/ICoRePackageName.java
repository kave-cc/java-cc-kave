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
 * A {@link ICoRePackageName} is simply a special formatted string that expresses a package path.
 */
public interface ICoRePackageName extends ICoReName, Comparable<ICoRePackageName> {

    /**
     * @return True, if the package is the default package, i.e. its identifier is empty.
     */
    public boolean isDefaultPackage();

}
