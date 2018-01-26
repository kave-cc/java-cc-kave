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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.alg.util.UnionFind;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.DeclarationMapper;
import cc.kave.commons.pointsto.analysis.references.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.references.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.references.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMemberReference;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifier;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.unification.locations.BottomLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.ExtendedReferenceLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.FunctionLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.Location;
import cc.kave.commons.pointsto.analysis.unification.locations.ReferenceLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.SimpleReferenceLocation;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;
import cc.kave.commons.utils.io.Logger;

public class UnificationAnalysisVisitorContext extends DistinctReferenceVisitorContext {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	private DistinctReferenceCreationVisitor distinctReferenceCreationVisitor = new DistinctReferenceCreationVisitor();

	private Multimap<SetRepresentative, SetRepresentative> pending = HashMultimap.create();
	private UnionFind<SetRepresentative> unionFind = new UnionFind<>(new HashSet<>());

	private Map<DistinctReference, ReferenceLocation> referenceLocations = new HashMap<>();
	private Map<DistinctMemberReference, SetRepresentative> memberLocations = new HashMap<>();
	private Map<DistinctIndexAccessReference, SetRepresentative> indexAccessLocations = new HashMap<>();

	private IAssignment lastAssignment;
	private IMemberName currentMember;
	private Deque<Pair<ILambdaExpression, ReferenceLocation>> lambdaStack = new ArrayDeque<>();

	private final PropertyAsFieldPredicate treatPropertyAsField;

	private Map<IMemberName, ReferenceLocation> returnLocations = new HashMap<>();

	private DestinationLocationVisitor destLocationVisitor = new DestinationLocationVisitor();
	private SourceLocationVisitor srcLocationVisitor = new SourceLocationVisitor();

	private final LocationIdentifierFactory identifierFactory;

	private Multimap<ReferenceLocation, ReferenceLocation> pendingUnifications = HashMultimap.create();

	public UnificationAnalysisVisitorContext(Context context, LocationIdentifierFactory identifierFactory) {
		super(context, ThisReferenceOption.PER_CONTEXT);
		this.treatPropertyAsField = new PropertyAsFieldPredicate(new DeclarationMapper(context));
		this.identifierFactory = identifierFactory;

		createImplicitLocations(context);
	}

	public Map<DistinctReference, AbstractLocation> getReferenceLocations() {
		int totalReferences = referenceLocations.size() + memberLocations.size();
		Map<DistinctReference, AbstractLocation> result = new HashMap<>(totalReferences);
		Map<SetRepresentative, AbstractLocation> refToAbstractLocation = new HashMap<>(totalReferences);

		for (Map.Entry<DistinctReference, ReferenceLocation> entry : referenceLocations.entrySet()) {
			DistinctReference reference = entry.getKey();
			ReferenceLocation location = entry.getValue();

			SetRepresentative ecr;
			if (reference instanceof DistinctMemberReference) {
				ecr = unionFind.find(location.getSetRepresentative());
			} else {
				Location valueLocation = unionFind
						.find(location.getLocation(LocationIdentifier.VALUE).getSetRepresentative()).getLocation();
				Location funLocation = location.getLocation(LocationIdentifier.FUNCTION);
				if (funLocation != null) {
					funLocation = unionFind.find(funLocation.getSetRepresentative()).getLocation();
				}
				ecr = (valueLocation.isBottom() && funLocation != null && !funLocation.isBottom())
						? funLocation.getSetRepresentative() : valueLocation.getSetRepresentative();
			}

			AbstractLocation abstractLocation = refToAbstractLocation.get(ecr);
			if (abstractLocation == null) {
				abstractLocation = new AbstractLocation();
				refToAbstractLocation.put(ecr, abstractLocation);
			}

			result.put(reference, abstractLocation);
		}

		for (Map.Entry<? extends DistinctReference, SetRepresentative> entry : Iterables
				.concat(memberLocations.entrySet(), indexAccessLocations.entrySet())) {
			DistinctReference reference = entry.getKey();
			SetRepresentative ecr = unionFind.find(entry.getValue());

			AbstractLocation abstractLocation = refToAbstractLocation.get(ecr);
			if (abstractLocation == null) {
				abstractLocation = new AbstractLocation();
				refToAbstractLocation.put(ecr, abstractLocation);
			}

			result.put(reference, abstractLocation);
		}

		return result;
	}

	private void createImplicitLocations(Context context) {
		DistinctReference thisRef = namesToReferences.get(languageOptions.getThisName());
		allocate(thisRef);
		DistinctReference superRef = namesToReferences.get(languageOptions.getSuperName());

		// let 'this' and 'super' point to the same object
		alias(superRef, thisRef);
	}

	public void finalizeAnalysis() {
		finalizePendingJoins();
		finalizePendingUnifications();
	}

	private void finalizePendingJoins() {
		while (!pending.isEmpty()) {
			Map.Entry<SetRepresentative, SetRepresentative> entry = pending.entries().iterator().next();
			join(entry.getKey(), entry.getValue());
			pending.remove(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Reruns the unification until all lazily added locations have propagated
	 * and no more changes are detected.
	 * 
	 * {@link LocationIdentifier} are added lazily to
	 * {@link ExtendedReferenceLocation} instances. If a location is added to an
	 * already unified {@link ExtendedReferenceLocation}, the unification has to
	 * be applied again to ensure correctness of the result.
	 */
	private void finalizePendingUnifications() {
		Deque<Pair<ReferenceLocation, ReferenceLocation>> worklist = new ArrayDeque<>();
		for (Map.Entry<ReferenceLocation, ReferenceLocation> locations : pendingUnifications.entries()) {
			ReferenceLocation refLoc1 = locations.getKey();
			ReferenceLocation refLoc2 = locations.getValue();

			int loc1Identifiers = refLoc1.getIdentifiers().size();
			int loc2Identifiers = refLoc2.getIdentifiers().size();

			if (loc1Identifiers != loc2Identifiers) {
				worklist.addFirst(ImmutablePair.of(refLoc1, refLoc2));
			}
		}

		while (!worklist.isEmpty()) {
			Pair<ReferenceLocation, ReferenceLocation> locations = worklist.removeFirst();
			ReferenceLocation loc1 = locations.getLeft();
			ReferenceLocation loc2 = locations.getRight();

			int previousIdentifiersLoc1 = loc1.getIdentifiers().size();
			int previousIdentifiersLoc2 = loc2.getIdentifiers().size();
			unify(loc1, loc2);

			updateUnificationWorklist(worklist, previousIdentifiersLoc1, loc1, loc2);
			updateUnificationWorklist(worklist, previousIdentifiersLoc2, loc2, loc1);
		}
	}

	private void updateUnificationWorklist(Deque<Pair<ReferenceLocation, ReferenceLocation>> worklist,
			int previousIdentifiersSize, ReferenceLocation location, ReferenceLocation unificationPartner) {
		int newIdentifiersSize = location.getIdentifiers().size();
		if (newIdentifiersSize != previousIdentifiersSize) {
			for (ReferenceLocation dependentLocation : pendingUnifications.get(location)) {
				if (dependentLocation != unificationPartner) {
					worklist.addFirst(ImmutablePair.of(location, dependentLocation));
				}
			}
		}
	}

	private LocationIdentifier getIdentifierForSimpleRefLoc(ITypeName type) {
		if (type.isDelegateType()) {
			return LocationIdentifier.FUNCTION;
		} else {
			return LocationIdentifier.VALUE;
		}
	}

	private BottomLocation createBottomLocation() {
		SetRepresentative bottomRep = new SetRepresentative();
		unionFind.addElement(bottomRep);
		return new BottomLocation(bottomRep);
	}

	ReferenceLocation createSimpleReferenceLocation() {
		SetRepresentative valueBottomRep = new SetRepresentative();
		SetRepresentative funBottomRep = new SetRepresentative();

		SetRepresentative refRepresentative = new SetRepresentative();
		ReferenceLocation refLocation = new SimpleReferenceLocation(refRepresentative,
				new BottomLocation(valueBottomRep), new BottomLocation(funBottomRep));

		unionFind.addElement(valueBottomRep);
		unionFind.addElement(funBottomRep);
		unionFind.addElement(refRepresentative);
		return refLocation;
	}

	private ReferenceLocation createExtendedReferenceLocation() {
		SetRepresentative setRep = new SetRepresentative();
		unionFind.addElement(setRep);
		return new ExtendedReferenceLocation(setRep);
	}

	private void mergeIdentifiers(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		Set<LocationIdentifier> loc1Identifiers = refLoc1.getIdentifiers();
		Set<LocationIdentifier> loc2Identifiers = refLoc2.getIdentifiers();

		{
			Set<LocationIdentifier> missingInLoc1 = new HashSet<>(loc2Identifiers);
			missingInLoc1.removeAll(loc1Identifiers);
			for (LocationIdentifier identifier : missingInLoc1) {
				refLoc1.setLocation(identifier, createBottomLocation());
			}
		}

		{
			Set<LocationIdentifier> missingInLoc2 = new HashSet<>(loc1Identifiers);
			missingInLoc2.removeAll(loc2Identifiers);
			for (LocationIdentifier identifier : missingInLoc2) {
				refLoc2.setLocation(identifier, createBottomLocation());
			}
		}
	}

	private FunctionLocation createFunctionLocation(ILambdaExpression lambdaExpr) {
		SetRepresentative functionRep = new SetRepresentative();
		unionFind.addElement(functionRep);
		return createFunctionLocation(lambdaExpr, functionRep);
	}

	private FunctionLocation createFunctionLocation(ILambdaExpression lambdaExpr, SetRepresentative functionRep) {
		List<IParameterName> lambdaParameters = lambdaExpr.getName().getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(lambdaParameters.size());

		for (IParameterName formalParameter : lambdaParameters) {
			DistinctReference distRef = new DistinctLambdaParameterReference(formalParameter, lambdaExpr);
			ReferenceLocation formalParameterLocation = getOrCreateLocation(distRef);

			parameterLocations.add(formalParameterLocation);
		}

		Pair<ILambdaExpression, ReferenceLocation> currentLambdaEntry = lambdaStack.getFirst();
		Asserts.assertTrue(lambdaExpr == currentLambdaEntry.getKey());

		return new FunctionLocation(parameterLocations, currentLambdaEntry.getValue(), functionRep);
	}

	private FunctionLocation createFunctionLocation(IMethodName method) {
		SetRepresentative functionRep = new SetRepresentative();
		unionFind.addElement(functionRep);
		return createFunctionLocation(method, functionRep);
	}

	private FunctionLocation createFunctionLocation(IMethodName method, SetRepresentative functionRep) {
		List<IParameterName> methodParameters = method.getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(methodParameters.size());

		for (IParameterName formalParameter : methodParameters) {
			DistinctMethodParameterReference distRef = new DistinctMethodParameterReference(formalParameter, method);
			ReferenceLocation formalParameterLocation = getOrCreateLocation(distRef);

			parameterLocations.add(formalParameterLocation);
		}

		return new FunctionLocation(parameterLocations, getOrCreateReturnLocation(method), functionRep);
	}

	ReferenceLocation getOrCreateLocation(DistinctReference reference) {
		ReferenceLocation location = referenceLocations.get(reference);

		if (location == null) {
			location = createSimpleReferenceLocation();
			referenceLocations.put(reference, location);
		}

		return location;
	}

	ReferenceLocation getOrCreateReturnLocation(IMemberName member) {
		ReferenceLocation location = returnLocations.get(member);

		if (location == null) {
			location = createSimpleReferenceLocation();
			returnLocations.put(member, location);
		}

		return location;
	}

	private void registerMemberLocation(DistinctMemberReference memberRef, Location location) {
		if (!memberLocations.containsKey(memberRef)) {
			memberLocations.put(memberRef, location.getSetRepresentative());
		}
	}

	private void registerIndexAccessLocation(DistinctIndexAccessReference distRef, Location location) {
		if (!indexAccessLocations.containsKey(distRef)) {
			indexAccessLocations.put(distRef, location.getSetRepresentative());
		}
	}

	private void setLocation(SetRepresentative setRepresentative, Location newLoc) {
		setRepresentative.setLocation(newLoc);
		newLoc.setSetRepresentative(setRepresentative);

		for (SetRepresentative x : pending.get(setRepresentative)) {
			join(setRepresentative, x);
		}
		pending.removeAll(setRepresentative);

	}

	private void cjoin(SetRepresentative rep1, SetRepresentative rep2) {
		if (rep2.getLocation().isBottom()) {
			pending.put(rep2, rep1);
		} else {
			join(rep1, rep2);
		}
	}

	private void join(SetRepresentative rep1, SetRepresentative rep2) {
		rep1 = unionFind.find(rep1);
		rep2 = unionFind.find(rep2);
		if (rep1 == rep2) {
			// prevent indefinite loop when finalizing the set of pending joins
			return;
		}

		Location loc1 = rep1.getLocation();
		Location loc2 = rep2.getLocation();

		unionFind.union(rep1, rep2);
		SetRepresentative unionRep = unionFind.find(rep1);
		loc1.setSetRepresentative(unionRep);
		loc2.setSetRepresentative(unionRep);

		if (loc1.isBottom()) {
			unionRep.setLocation(loc2);
			if (loc2.isBottom()) {
				if (unionRep != rep1) {
					pending.putAll(unionRep, pending.get(rep1));
				}
				if (unionRep != rep2) {
					pending.putAll(unionRep, pending.get(rep2));
				}
			} else {
				for (SetRepresentative x : pending.get(rep1)) {
					join(unionRep, x);
				}
				pending.removeAll(rep1);
			}
		} else {
			unionRep.setLocation(loc1);
			if (loc2.isBottom()) {
				for (SetRepresentative x : pending.get(rep2)) {
					join(unionRep, x);
				}
				pending.removeAll(rep2);
			} else {
				unify(loc1, loc2);
			}

		}
	}

	private void unify(Location loc1, Location loc2) {
		if (loc1 instanceof ReferenceLocation && loc2 instanceof ReferenceLocation) {
			ReferenceLocation rloc1 = (ReferenceLocation) loc1;
			ReferenceLocation rloc2 = (ReferenceLocation) loc2;
			pendingUnifications.put(rloc1, rloc2);
			pendingUnifications.put(rloc2, rloc1);
			unify(rloc1, rloc2);
		} else if (loc1 instanceof FunctionLocation && loc2 instanceof FunctionLocation) {
			unify((FunctionLocation) loc1, (FunctionLocation) loc2);
		}
	}

	private void unify(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		mergeIdentifiers(refLoc1, refLoc2);
		// copy set to list for iteration to avoid concurrent modification
		// exceptions
		List<LocationIdentifier> commonIdentifiers = new ArrayList<>(refLoc1.getIdentifiers());

		for (LocationIdentifier identifier : commonIdentifiers) {
			Location loc1 = refLoc1.getLocation(identifier);
			Location loc2 = refLoc2.getLocation(identifier);

			SetRepresentative rep1 = unionFind.find(loc1.getSetRepresentative());
			SetRepresentative rep2 = unionFind.find(loc2.getSetRepresentative());

			if (rep1 != rep2) {
				join(rep1, rep2);
			}
		}
	}

	private void unify(FunctionLocation funLoc1, FunctionLocation funLoc2) {
		List<ReferenceLocation> fun1Parameters = funLoc1.getParameterLocations();
		List<ReferenceLocation> fun2Parameters = funLoc2.getParameterLocations();

		int minParametersSize = Math.min(fun1Parameters.size(), fun2Parameters.size());
		int maxParametersSize = Math.max(fun1Parameters.size(), fun2Parameters.size());
		for (int i = 0; i < minParametersSize; ++i) {
			unify(fun1Parameters.get(i), fun2Parameters.get(i));
		}

		// function locations have a different number of parameters: copy over
		// the additional ones
		if (minParametersSize != maxParametersSize) {
			List<ReferenceLocation> minParameters = (fun1Parameters.size() == minParametersSize) ? fun1Parameters
					: fun2Parameters;
			List<ReferenceLocation> maxParameters = (fun1Parameters.size() == maxParametersSize) ? fun1Parameters
					: fun2Parameters;

			minParameters.addAll(maxParameters.subList(minParametersSize, maxParametersSize));
		}

		unify(funLoc1.getReturnLocation(), funLoc2.getReturnLocation());
	}

	public DistinctReference getDistinctReference(IReference reference) {
		return reference.accept(distinctReferenceCreationVisitor, namesToReferences);
	}

	public ReferenceLocation getLocation(IVariableReference varRef) {
		DistinctReference distRef = varRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		return getOrCreateLocation(distRef);
	}

	@Override
	public void enterMember(IMemberName member) {
		super.enterMember(member);
		this.currentMember = member;
	}

	public void setLastAssignment(IAssignment assignment) {
		lastAssignment = assignment;
	}

	public IAssignableReference getDestinationForExpr(IAssignableExpression expr) {
		if (lastAssignment == null || lastAssignment.getReference() instanceof IUnknownReference
				|| lastAssignment.getExpression() != expr) {
			return null;
		} else {
			return lastAssignment.getReference();
		}
	}

	public void enterLambda(ILambdaExpression lambdaExpr) {
		ReferenceLocation lambdaReturnLocation = createSimpleReferenceLocation();
		lambdaStack.addFirst(ImmutablePair.of(lambdaExpr, lambdaReturnLocation));
	}

	public void leaveLambda() {
		lambdaStack.removeFirst();
	}

	public void allocate(IAssignableReference destRef) {
		if (destRef instanceof IUnknownReference) {
			Logger.err("Ignoring an allocation due to an unknown reference");
			return;
		}

		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, this);
		allocate(destLocation);
	}

	private void allocate(DistinctReference destRef) {
		allocate(getOrCreateLocation(destRef));
	}

	void allocate(ReferenceLocation destLocation) {
		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			ReferenceLocation allocatedLocation = createExtendedReferenceLocation();
			setLocation(derefDestLocation.getSetRepresentative(), allocatedLocation);
			destLocation.setLocation(LocationIdentifier.VALUE, allocatedLocation);
		}
	}

	private Location readDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation,
			LocationIdentifier destIdentifier, LocationIdentifier srcIdentifier) {
		SetRepresentative destDerefRep = unionFind
				.find(destLocation.getLocation(destIdentifier).getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind
				.find(srcLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative());

		if (srcDerefRep.getLocation().isBottom()) {
			ReferenceLocation srcDerefLoc = new ExtendedReferenceLocation(srcDerefRep);
			Location dereferenceTargetLoc = destDerefRep.getLocation();
			if (dereferenceTargetLoc.isBottom()) {
				dereferenceTargetLoc = new ExtendedReferenceLocation(destDerefRep);
				setLocation(destDerefRep, dereferenceTargetLoc);
				destLocation.setLocation(destIdentifier, dereferenceTargetLoc);
			}
			srcDerefLoc.setLocation(srcIdentifier, dereferenceTargetLoc);
			setLocation(srcDerefRep, srcDerefLoc);
			srcLocation.setLocation(LocationIdentifier.VALUE, srcDerefLoc);

			return destDerefRep.getLocation();
		} else {
			ReferenceLocation srcDerefLocation = (ReferenceLocation) srcDerefRep.getLocation();
			Location dereferenceTargetLoc = srcDerefLocation.getLocation(srcIdentifier);
			if (dereferenceTargetLoc == null) {
				dereferenceTargetLoc = createExtendedReferenceLocation();
				srcDerefLocation.setLocation(srcIdentifier, dereferenceTargetLoc);
			}

			SetRepresentative dereferenceTargetRep = unionFind.find(dereferenceTargetLoc.getSetRepresentative());
			if (destDerefRep != dereferenceTargetRep) {
				cjoin(destDerefRep, dereferenceTargetRep);
			}

			return dereferenceTargetRep.getLocation();
		}
	}

	private Location writeDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation,
			LocationIdentifier destIdentifier, LocationIdentifier srcIdentifier) {
		SetRepresentative destDerefRep = unionFind
				.find(destLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind.find(srcLocation.getLocation(srcIdentifier).getSetRepresentative());

		if (destDerefRep.getLocation().isBottom()) {
			ReferenceLocation destDerefLoc = new ExtendedReferenceLocation(destDerefRep);
			Location dereferenceTargetLoc = srcDerefRep.getLocation();
			if (dereferenceTargetLoc.isBottom()) {
				dereferenceTargetLoc = new ExtendedReferenceLocation(srcDerefRep);
				setLocation(srcDerefRep, dereferenceTargetLoc);
				srcLocation.setLocation(srcIdentifier, dereferenceTargetLoc);
			}
			destDerefLoc.setLocation(destIdentifier, srcDerefRep.getLocation());
			setLocation(destDerefRep, destDerefLoc);
			destLocation.setLocation(LocationIdentifier.VALUE, destDerefLoc);

			return srcDerefRep.getLocation();
		} else {
			ReferenceLocation destDerefLocation = (ReferenceLocation) destDerefRep.getLocation();
			Location dereferenceTargetLoc = destDerefLocation.getLocation(destIdentifier);
			if (dereferenceTargetLoc == null) {
				dereferenceTargetLoc = createExtendedReferenceLocation();
				destDerefLocation.setLocation(destIdentifier, dereferenceTargetLoc);
			}

			SetRepresentative dereferenceTargetRep = unionFind.find(dereferenceTargetLoc.getSetRepresentative());

			if (dereferenceTargetRep != srcDerefRep) {
				cjoin(dereferenceTargetRep, srcDerefRep);
			}

			return dereferenceTargetRep.getLocation();
		}
	}

	public void alias(IReference destRef, IVariableReference srcRef) {
		DistinctReference srcDistRef = srcRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		ReferenceLocation srcLocation = getOrCreateLocation(srcDistRef);
		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, this);
		alias(destLocation, srcLocation);
	}

	private void alias(DistinctReference dest, DistinctReference src) {
		Asserts.assertNotNull(dest);
		Asserts.assertNotNull(src);
		ReferenceLocation destLocation = getOrCreateLocation(dest);
		ReferenceLocation srcLocation = getOrCreateLocation(src);
		alias(destLocation, srcLocation);
	}

	void alias(ReferenceLocation destLocation, ReferenceLocation srcLocation) {
		mergeIdentifiers(destLocation, srcLocation);
		for (LocationIdentifier identifier : destLocation.getIdentifiers()) {
			alias(destLocation, srcLocation, identifier);
		}
	}

	private void alias(ReferenceLocation destLocation, ReferenceLocation srcLocation, LocationIdentifier identifier) {
		SetRepresentative derefDestRep = unionFind.find(destLocation.getLocation(identifier).getSetRepresentative());
		SetRepresentative derefSrcRep = unionFind.find(srcLocation.getLocation(identifier).getSetRepresentative());

		if (derefDestRep != derefSrcRep) {
			cjoin(derefDestRep, derefSrcRep);
		}
	}

	public void storeFunction(IReference destRef, ILambdaExpression lambdaExpr) {
		Asserts.assertTrue(lambdaExpr == lambdaStack.getFirst().getKey());

		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, this);
		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			FunctionLocation functionLocation = createFunctionLocation(lambdaExpr);
			setLocation(derefDestLocation.getSetRepresentative(), functionLocation);
			destLocation.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			FunctionLocation functionLocation = ensureProperFunctionLocation(
					rep -> createFunctionLocation(lambdaExpr, rep), destLocation, derefDestLocation);

			List<IParameterName> lambdaParameters = lambdaExpr.getName().getParameters();
			List<DistinctReference> distLambdaParameters = new ArrayList<>(lambdaParameters.size());
			for (IParameterName lambdaParameter : lambdaParameters) {
				distLambdaParameters.add(new DistinctLambdaParameterReference(lambdaParameter, lambdaExpr));
			}

			ReferenceLocation lambdaReturnLocation = lambdaStack.getFirst().getValue();
			ITypeName lambdaReturnType = lambdaStack.getFirst().getKey().getName().getReturnType();

			updateFunctionLocation(functionLocation, distLambdaParameters, lambdaReturnType, lambdaReturnLocation);
		}

	}

	public void storeFunction(IReference destRef, IMethodReference methodRef) {
		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, this);
		storeFunction(destLocation, methodRef);
	}

	void storeFunction(ReferenceLocation destLocation, IMethodReference methodRef) {
		IMethodName method = methodRef.getMethodName();

		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			FunctionLocation functionLocation = createFunctionLocation(method);
			setLocation(derefDestLocation.getSetRepresentative(), functionLocation);
			destLocation.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			FunctionLocation functionLocation = ensureProperFunctionLocation(rep -> createFunctionLocation(method, rep),
					destLocation, derefDestLocation);

			List<IParameterName> methodParameters = method.getParameters();
			List<DistinctReference> distMethodParameters = new ArrayList<>(methodParameters.size());
			for (IParameterName parameter : methodParameters) {
				distMethodParameters.add(new DistinctMethodParameterReference(parameter, method));
			}

			updateFunctionLocation(functionLocation, distMethodParameters, method.getReturnType(),
					getOrCreateReturnLocation(method));
		}
	}

	private void updateFunctionLocation(FunctionLocation functionLocation, List<DistinctReference> newParameters,
			ITypeName newReturnType, ReferenceLocation newReturnLocation) {
		List<ReferenceLocation> funLocParameters = functionLocation.getParameterLocations();
		List<ReferenceLocation> newParameterLocations = new ArrayList<>(newParameters.size());
		for (DistinctReference distParameterRef : newParameters) {
			ReferenceLocation newParameterLocation = getOrCreateLocation(distParameterRef);
			newParameterLocations.add(newParameterLocation);
		}

		// the number of parameters can be different in rare cases
		int commonNumParameters = Math.min(funLocParameters.size(), newParameters.size());
		for (int i = 0; i < commonNumParameters; ++i) {
			DistinctReference distParameterRef = newParameters.get(i);
			ReferenceLocation newParameterLocation = newParameterLocations.get(i);
			LocationIdentifier newParameterIdentifier = getIdentifierForSimpleRefLoc(distParameterRef.getType());
			SetRepresentative derefNewParameterLocationRep = unionFind
					.find(newParameterLocation.getLocation(newParameterIdentifier).getSetRepresentative());
			SetRepresentative derefFunLocParameterRep = unionFind
					.find(funLocParameters.get(i).getLocation(newParameterIdentifier).getSetRepresentative());

			if (derefFunLocParameterRep != derefNewParameterLocationRep) {
				join(derefNewParameterLocationRep, derefFunLocParameterRep);
			}
		}
		if (newParameterLocations.size() > funLocParameters.size()) {
			funLocParameters.addAll(newParameterLocations.subList(commonNumParameters, newParameterLocations.size()));
		}

		LocationIdentifier newReturnIdentifier = getIdentifierForSimpleRefLoc(newReturnType);
		SetRepresentative derefNewReturnLocRep = unionFind
				.find(newReturnLocation.getLocation(newReturnIdentifier).getSetRepresentative());
		SetRepresentative derefFunLocReturnRep = unionFind
				.find(functionLocation.getReturnLocation().getLocation(newReturnIdentifier).getSetRepresentative());
		if (derefFunLocReturnRep != derefNewReturnLocRep) {
			join(derefFunLocReturnRep, derefNewReturnLocRep);
		}
	}

	public FunctionLocation invokeDelegate(IInvocationExpression invocation) {
		IMethodName method = invocation.getMethodName();

		DistinctReference receiverRef = invocation.getReference().accept(distinctReferenceCreationVisitor,
				namesToReferences);
		ReferenceLocation receiverLoc = getOrCreateLocation(receiverRef);
		Location derefReceiverLoc = unionFind
				.find(receiverLoc.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		FunctionLocation functionLocation;
		if (derefReceiverLoc.isBottom()) {
			functionLocation = createFunctionLocation(method);
			setLocation(derefReceiverLoc.getSetRepresentative(), functionLocation);
			receiverLoc.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			functionLocation = ensureProperFunctionLocation(rep -> createFunctionLocation(method, rep), receiverLoc,
					derefReceiverLoc);
		}

		return functionLocation;
	}

	private FunctionLocation ensureProperFunctionLocation(Function<SetRepresentative, FunctionLocation> funLocCreator,
			ReferenceLocation location, Location derefLocation) {
		if (derefLocation instanceof FunctionLocation) {
			return (FunctionLocation) derefLocation;
		} else {
			// location got initialized by readDereference or writeDereference
			// to an ExtendedRefernceLocation as we
			// cannot simply extract the method name at that stage
			FunctionLocation functionLocation = funLocCreator.apply(derefLocation.getSetRepresentative());
			// swap locations
			derefLocation.getSetRepresentative().setLocation(functionLocation);
			location.setLocation(LocationIdentifier.FUNCTION, functionLocation);

			return functionLocation;
		}
	}

	public List<ReferenceLocation> getMethodParameterLocations(IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(formalParameters.size());
		for (IParameterName parameter : formalParameters) {
			DistinctReference distParameterRef = new DistinctMethodParameterReference(parameter, method);
			parameterLocations.add(getOrCreateLocation(distParameterRef));
		}

		return parameterLocations;
	}

	public ReferenceLocation getMethodReturnLocation(IMethodName method) {
		return getOrCreateReturnLocation(method);
	}

	public void assign(IFieldReference dest, IFieldReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IPropertyReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			alias(tempLoc, returnLocation);
		}

		IPropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			alias(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IPropertyReference dest, IFieldReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);

		IPropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			alias(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IFieldReference dest, IPropertyReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			alias(tempLoc, returnLocation);
		}

		writeMember(dest, tempLoc);
	}

	public void assign(IFieldReference dest, IIndexAccessReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IIndexAccessReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);

		IPropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			alias(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IIndexAccessReference dest, IFieldReference src) {
		assign((DistinctIndexAccessReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctIndexAccessReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IPropertyReference src) {
		assign((DistinctIndexAccessReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctIndexAccessReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();

		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			alias(tempLoc, returnLocation);
		}

		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IIndexAccessReference src) {
		DistinctIndexAccessReference distDestRef = (DistinctIndexAccessReference) dest
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		DistinctIndexAccessReference distSrcRef = (DistinctIndexAccessReference) src
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		assign(distDestRef, distSrcRef);
	}

	private void assign(DistinctIndexAccessReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void assign(IEventReference dest, IFieldReference src) {
		ReferenceLocation tempLoc = dest.accept(destLocationVisitor, this);
		readMember(tempLoc, src);
	}

	public void assign(IEventReference dest, IPropertyReference src) {
		ReferenceLocation tempLoc = dest.accept(destLocationVisitor, this);
		if (treatPropertyAsField(src)) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(src.getPropertyName());
			alias(tempLoc, returnLocation);
		}
	}

	public void assign(IEventReference dest, IEventReference src) {
		ReferenceLocation destLoc = dest.accept(destLocationVisitor, this);
		ReferenceLocation srcLoc = src.accept(srcLocationVisitor, this);
		alias(destLoc, srcLoc);
	}

	public void readField(IVariableReference destRef, IFieldReference fieldRef) {
		readField(destRef.accept(distinctReferenceCreationVisitor, namesToReferences), fieldRef);
	}

	private void readField(DistinctReference destRef, IFieldReference fieldRef) {
		readMember(getOrCreateLocation(destRef), fieldRef);
	}

	private void readMember(ReferenceLocation destLocation, IMemberReference memberRef) {
		DistinctMemberReference distinctMemberRef = (DistinctMemberReference) memberRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		readMember(destLocation, distinctMemberRef);
	}

	void readMember(ReferenceLocation destLocation, DistinctMemberReference memberRef) {
		if (memberRef.isStaticMember()) {
			// model static members (fields, properties) as global variables
			alias(destLocation, getOrCreateLocation(memberRef));
		} else {

			ReferenceLocation srcLocation = getOrCreateLocation(memberRef.getBaseReference());
			LocationIdentifier destIdentifier = getIdentifierForSimpleRefLoc(memberRef.getType());
			LocationIdentifier srcIdentifier = identifierFactory.create(memberRef.getReference());
			Location srcDerefLocation = readDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

			registerMemberLocation(memberRef, srcDerefLocation);
		}
	}

	public void writeField(IFieldReference fieldRef, IVariableReference srcRef) {
		writeField(fieldRef, srcRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void writeField(IFieldReference fieldRef, DistinctReference srcRef) {
		writeField((DistinctFieldReference) fieldRef.accept(distinctReferenceCreationVisitor, namesToReferences),
				srcRef);
	}

	private void writeField(DistinctFieldReference fieldRef, DistinctReference srcRef) {
		ReferenceLocation srcLocation = getOrCreateLocation(srcRef);
		writeMember(fieldRef, srcLocation);
	}

	void writeMember(DistinctMemberReference memberRef, ReferenceLocation srcLocation) {
		if (memberRef.isStaticMember()) {
			// model static members (fields, properties) as global variables
			alias(getOrCreateLocation(memberRef), srcLocation);
		} else {

			ReferenceLocation destLocation = getOrCreateLocation(memberRef.getBaseReference());
			LocationIdentifier destIdentifier = identifierFactory.create(memberRef.getReference());
			// we live in a type safe environment (src must be a delegate type
			// as well if the member is one)
			LocationIdentifier srcIdentifier = getIdentifierForSimpleRefLoc(memberRef.getType());
			Location destDerefLocation = writeDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

			registerMemberLocation(memberRef, destDerefLocation);
		}
	}

	public void readProperty(IVariableReference destRef, IPropertyReference propertyRef) {
		readProperty(destRef.accept(distinctReferenceCreationVisitor, namesToReferences), propertyRef);
	}

	private void readProperty(DistinctReference destRef, IPropertyReference propertyRef) {
		readProperty(destRef,
				(DistinctPropertyReference) propertyRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void readProperty(DistinctReference destRef, DistinctPropertyReference propertyRef) {
		ReferenceLocation destLocation = getOrCreateLocation(destRef);
		IPropertyName property = propertyRef.getReference().getPropertyName();
		if (treatPropertyAsField(propertyRef.getReference())) {
			readMember(destLocation, propertyRef);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(property);
			alias(destLocation, returnLocation);
		}
	}

	public void writeProperty(IPropertyReference propertyRef, IVariableReference srcRef) {
		writeProperty(propertyRef, srcRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void writeProperty(IPropertyReference propertyRef, DistinctReference srcRef) {
		writeProperty(
				(DistinctPropertyReference) propertyRef.accept(distinctReferenceCreationVisitor, namesToReferences),
				srcRef);
	}

	private void writeProperty(DistinctPropertyReference propertyRef, DistinctReference srcRef) {
		IPropertyName property = propertyRef.getReference().getPropertyName();
		if (treatPropertyAsField(propertyRef.getReference())) {
			ReferenceLocation srcLocation = getOrCreateLocation(srcRef);
			writeMember(propertyRef, srcLocation);
		} else {
			// map parameter of the property setter to the source
			alias(new DistinctPropertyParameterReference(languageOptions, property), srcRef);
		}
	}

	public boolean treatPropertyAsField(IPropertyReference propertyRef) {
		return treatPropertyAsField.test(propertyRef.getPropertyName());
	}

	public void readArray(IVariableReference destRef, IIndexAccessReference srcRef) {
		DistinctReference dest = destRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		DistinctIndexAccessReference src = (DistinctIndexAccessReference) srcRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		readArray(dest, src);
	}

	private void readArray(DistinctReference destRef, DistinctIndexAccessReference srcRef) {
		ReferenceLocation destLocation = getOrCreateLocation(destRef);
		readArray(destLocation, srcRef);
	}

	void readArray(ReferenceLocation destLocation, DistinctIndexAccessReference srcRef) {
		ReferenceLocation srcLocation = getOrCreateLocation(srcRef.getBaseReference());
		LocationIdentifier destIdentifier = getIdentifierForSimpleRefLoc(srcRef.getType());
		LocationIdentifier srcIdentifier = identifierFactory.create(srcRef.getReference(), srcRef.getType());
		Location srcDerefLocation = readDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

		registerIndexAccessLocation(srcRef, srcDerefLocation);
	}

	public void writeArray(IIndexAccessReference destRef, IReference srcRef) {
		DistinctIndexAccessReference distDestRef = (DistinctIndexAccessReference) destRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		writeArray(distDestRef, srcRef);
	}

	void writeArray(DistinctIndexAccessReference destRef, IReference srcRef) {
		ReferenceLocation srcLocaction = srcRef.accept(srcLocationVisitor, this);
		writeArray(destRef, srcLocaction);
	}

	void writeArray(DistinctIndexAccessReference destRef, ReferenceLocation srcLocation) {
		ReferenceLocation destLocation = getOrCreateLocation(destRef.getBaseReference());
		LocationIdentifier destIdentifier = identifierFactory.create(destRef.getReference(), destRef.getType());
		LocationIdentifier srcIdentifier = getIdentifierForSimpleRefLoc(destRef.getType());
		Location destDerefLocation = writeDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

		registerIndexAccessLocation(destRef, destDerefLocation);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IVariableReference actualParameter) {
		alias(parameterLocation, getLocation(actualParameter));
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IFieldReference actualParameter) {
		readMember(parameterLocation, actualParameter);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IPropertyReference actualParameter) {
		readMember(parameterLocation, actualParameter);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IMethodReference actualParameter) {
		storeFunction(parameterLocation, actualParameter);
	}

	public void registerReturnedReference(IReference ref) {
		if (ref instanceof IUnknownReference) {
			Logger.err("Ignoring returning of an unknown reference");
		} else {
			ReferenceLocation returnLocation;
			if (lambdaStack.isEmpty()) {
				returnLocation = getOrCreateReturnLocation(currentMember);
			} else {
				returnLocation = lambdaStack.getFirst().getValue();
			}
			ReferenceLocation srcLocation = ref.accept(srcLocationVisitor, this);

			alias(returnLocation, srcLocation);
		}
	}

	public void storeReturn(IAssignableReference destRef, ReferenceLocation returnLocation) {
		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, this);
		alias(destLocation, returnLocation);
	}
}