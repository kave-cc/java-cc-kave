/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.StringUtils;
import cc.kave.commons.utils.ToStringUtils;

public class SST implements ISST {

	private ITypeName enclosingType;
	private String partialClassIdentifier;

	// keep linked sets to have order guarantees for tests
	private final LinkedHashSet<IFieldDeclaration> fields;
	private final LinkedHashSet<IPropertyDeclaration> properties;
	private final LinkedHashSet<IMethodDeclaration> methods;
	private final LinkedHashSet<IEventDeclaration> events;
	private final LinkedHashSet<IDelegateDeclaration> delegates;

	public SST() {
		this.enclosingType = Names.getUnknownType();
		this.fields = new LinkedHashSet<IFieldDeclaration>();
		this.properties = new LinkedHashSet<IPropertyDeclaration>();
		this.methods = new LinkedHashSet<IMethodDeclaration>();
		this.events = new LinkedHashSet<IEventDeclaration>();
		this.delegates = new LinkedHashSet<IDelegateDeclaration>();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public String getPartialClassIdentifier() {
		return this.partialClassIdentifier;
	}

	public void setPartialClassIdentifier(String identifier) {
		this.partialClassIdentifier = identifier;
	}

	@Override
	public boolean isPartialClass() {
		return !StringUtils.isNullOrEmpty(this.partialClassIdentifier);
	}

	@Override
	public ITypeName getEnclosingType() {
		return this.enclosingType;
	}

	@Override
	public Set<IFieldDeclaration> getFields() {
		return this.fields;
	}

	@Override
	public Set<IPropertyDeclaration> getProperties() {
		return this.properties;
	}

	@Override
	public Set<IMethodDeclaration> getMethods() {
		return this.methods;
	}

	@Override
	public Set<IEventDeclaration> getEvents() {
		return this.events;
	}

	@Override
	public Set<IDelegateDeclaration> getDelegates() {
		return this.delegates;
	}

	public void setEnclosingType(ITypeName enclosingType) {
		this.enclosingType = enclosingType;
	}

	public void setFields(Set<IFieldDeclaration> fields) {
		this.fields.clear();
		this.fields.addAll(fields);
	}

	public void setProperties(Set<IPropertyDeclaration> properties) {
		this.properties.clear();
		this.properties.addAll(properties);
	}

	public void setMethods(Set<IMethodDeclaration> methods) {
		this.methods.clear();
		this.methods.addAll(methods);
	}

	public void setEvents(Set<IEventDeclaration> events) {
		this.events.clear();
		this.events.addAll(events);
	}

	public void setDelegates(Set<IDelegateDeclaration> delegates) {
		this.delegates.clear();
		this.delegates.addAll(delegates);
	}

	@Override
	public Set<IMethodDeclaration> getEntryPoints() {
		return Sets.newHashSet(methods.stream().filter(m -> m.isEntryPoint()).collect(Collectors.toSet()));
	}

	@Override
	public Set<IMethodDeclaration> getNonEntryPoints() {
		return Sets.newHashSet(methods.stream().filter(m -> !m.isEntryPoint()).collect(Collectors.toSet()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delegates == null) ? 0 : delegates.hashCode());
		result = prime * result + ((enclosingType == null) ? 0 : enclosingType.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((methods == null) ? 0 : methods.hashCode());
		result = prime * result
				+ (StringUtils.isNullOrEmpty(partialClassIdentifier) ? 0 : partialClassIdentifier.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
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
		SST other = (SST) obj;
		if (delegates == null) {
			if (other.delegates != null)
				return false;
		} else if (!delegates.equals(other.delegates))
			return false;
		if (enclosingType == null) {
			if (other.enclosingType != null)
				return false;
		} else if (!enclosingType.equals(other.enclosingType))
			return false;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (methods == null) {
			if (other.methods != null)
				return false;
		} else if (!methods.equals(other.methods))
			return false;
		if (StringUtils.isNullOrEmpty(partialClassIdentifier)) {
			if (!StringUtils.isNullOrEmpty(other.partialClassIdentifier))
				return false;
		} else if (!partialClassIdentifier.equals(other.partialClassIdentifier))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}