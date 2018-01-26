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
package cc.recommenders.usages.features;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public abstract class UsageFeature {

    public static class ObjectUsageFeatureVisitor {
        public void visit(TypeFeature f) {
        }

        public void visit(ClassFeature f) {
        }

        public void visit(SuperMethodFeature f) {
        }

        public void visit(FirstMethodFeature f) {
        }

        public void visit(DefinitionFeature f) {
        }

        public void visit(CallFeature f) {
        }

        public void visit(ParameterFeature f) {
        }
    }

    public abstract void accept(ObjectUsageFeatureVisitor v);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}