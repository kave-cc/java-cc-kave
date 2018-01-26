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

import java.util.Map;
import java.util.Set;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

public class CoRePackageName implements ICoRePackageName {

    private static Map<String/* name 2 */, CoRePackageName> index = new MapMaker().weakValues().makeMap();
    public static ICoRePackageName DEFAULT_PACKAGE = get("");

    public static CoRePackageName get(final String vmPackageName) {
        CoRePackageName res = index.get(vmPackageName);
        if (res == null) {
            res = new CoRePackageName(vmPackageName);
            index.put(vmPackageName, res);
        }
        return res;
    }

    /**
     * @return the packages of the given types as returned by {@link ICoReTypeName#getPackage()}
     */
    public static Set<ICoRePackageName> packages(Set<ICoReTypeName> types) {
        Set<ICoRePackageName> res = Sets.newTreeSet();
        for (ICoReTypeName type : types) {
            res.add(type.getPackage());
        }
        return res;
    }

    private final String identifier;

    /**
     * @see #get(String)
     */
    private CoRePackageName(final String vmPackageName) {
        identifier = vmPackageName;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isDefaultPackage() {
        return getIdentifier().isEmpty();
    }

    public int compareTo(final ICoRePackageName o) {
        return getIdentifier().compareTo(o.getIdentifier());
    }

    @Override
    public String toString() {
        return getIdentifier();
    }

}
