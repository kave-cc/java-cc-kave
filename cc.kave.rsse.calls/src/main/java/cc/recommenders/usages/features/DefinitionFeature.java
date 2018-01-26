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

import static cc.kave.commons.assertions.Asserts.assertNotNull;

import cc.recommenders.usages.DefinitionSite;

public class DefinitionFeature extends UsageFeature {

	private final DefinitionSite definitionSite;

	public DefinitionFeature(DefinitionSite definitionSite) {
		assertNotNull(definitionSite);
		this.definitionSite = definitionSite;
	}

	public DefinitionSite getDefinitionSite() {
		return definitionSite;
	}

	@Override
	public void accept(ObjectUsageFeatureVisitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return String.format("Definition@%d:%s", hashCode(), definitionSite.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definitionSite == null) ? 0 : definitionSite.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefinitionFeature other = (DefinitionFeature) obj;
		if (definitionSite == null) {
			if (other.definitionSite != null)
				return false;
		} else if (!definitionSite.equals(other.definitionSite))
			return false;
		return true;
	}
}