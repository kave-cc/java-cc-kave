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

import java.io.Serializable;

/**
 * A {@link ICoReFieldName} is basically the full qualified method name. This class
 * provides an easy way to access the information available in such a method
 * name (like isInit, isSyntetic etc.) and provides some safety checks for the
 * format of such a full qualified name.
 */
public interface ICoReFieldName extends ICoReName, Comparable<ICoReFieldName>, Serializable {
    /**
     * Returns the {@link ICoReTypeName} of the declaring class, i.e., the class
     * that statically defines this method.
     */
    public abstract ICoReTypeName getDeclaringType();

    public abstract ICoReTypeName getFieldType();

    public abstract String getFieldName();
}
