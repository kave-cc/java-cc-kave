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
package cc.kave.commons.pointsto.analysis.references.conversion;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.pointsto.analysis.exceptions.MissingVariableException;
import cc.kave.commons.pointsto.analysis.exceptions.UndeclaredVariableException;
import cc.kave.commons.pointsto.analysis.references.DistinctMemberReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.visitors.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;
import cc.kave.commons.utils.io.Logger;

public class DistinctReferenceContextCollector extends DistinctReferenceVisitorContext {

	private static final String SKIPPING_REF_MSG = "Skipping reference: {}";

	private DistinctReferenceCreationVisitor distRefCreationVisitor = new DistinctReferenceCreationVisitor();

	private IMemberName currentMember;
	private IStatement currentStatement;

	private Multimap<DistinctReference, IMemberName> referenceToMembers = HashMultimap.create();
	private Multimap<DistinctReference, IStatement> referenceToStmts = Multimaps.newSetMultimap(new HashMap<>(),
			Sets::newIdentityHashSet);

	public DistinctReferenceContextCollector(Context context, ThisReferenceOption thisReferenceOption) {
		super(context, thisReferenceOption);
	}

	public Collection<IMemberName> getMembers(DistinctReference ref) {
		return referenceToMembers.get(ref);
	}

	public Collection<IStatement> getStatements(DistinctReference ref) {
		return referenceToStmts.get(ref);
	}

	public void setCurrentMember(IMemberName member) {
		this.currentMember = member;
	}

	public void setCurrentStatement(IStatement stmt) {
		this.currentStatement = stmt;
	}

	private DistinctReference getDistinctReference(IReference reference) {
		return reference.accept(distRefCreationVisitor, namesToReferences);
	}

	private void registerReference(DistinctReference distRef) {
		referenceToMembers.put(distRef, currentMember);
		referenceToStmts.put(distRef, currentStatement);
	}

	public void useReference(IReference reference) {
		try {
			DistinctReference distRef = getDistinctReference(reference);
			registerReference(distRef);
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err(SKIPPING_REF_MSG, ex.getMessage());
		}
	}

	public void useReference(IMemberReference reference) {
		try {
			DistinctMemberReference distRef = (DistinctMemberReference) getDistinctReference(reference);
			registerReference(distRef);
			if (!distRef.isStaticMember()) {
				registerReference(distRef.getBaseReference());
			}
		} catch (MissingVariableException | UndeclaredVariableException ex) {
			Logger.err(SKIPPING_REF_MSG, ex.getMessage());
		}
	}
}