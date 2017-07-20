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
package cc.kave.commons.model.ssts.impl.declarations;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.utils.ToStringUtils;

public class EventDeclaration implements IEventDeclaration {

	private IEventName name;

	public EventDeclaration() {
		this.name = Names.getUnknownEvent();
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IEventName getName() {
		return name;
	}

	public void setName(IEventName name) {
		this.name = name;
	}

	private boolean equals(EventDeclaration other) {
		return this.name.equals(other.getName());
	}

	public boolean equals(Object obj) {
		return obj instanceof EventDeclaration ? this.equals((EventDeclaration) obj) : false;
	}

	public int hashCode() {
		return 22 + this.name.hashCode();
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