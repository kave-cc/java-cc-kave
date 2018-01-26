/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.unification;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventSubscriptionAssignment implements IAssignment {

	private final IEventSubscriptionStatement eventSubscription;

	public EventSubscriptionAssignment(IEventSubscriptionStatement eventSubscription) {
		this.eventSubscription = eventSubscription;
	}

	@Override
	public Iterable<ISSTNode> getChildren() {
		return eventSubscription.getChildren();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IAssignableReference getReference() {
		return eventSubscription.getReference();
	}

	@Override
	public IAssignableExpression getExpression() {
		return eventSubscription.getExpression();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventSubscription == null) ? 0 : eventSubscription.hashCode());
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
		EventSubscriptionAssignment other = (EventSubscriptionAssignment) obj;
		if (eventSubscription == null) {
			if (other.eventSubscription != null)
				return false;
		} else if (!eventSubscription.equals(other.eventSubscription))
			return false;
		return true;
	}

}
