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
package cc.recommenders.mining.calls;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Pattern<Feature> {

	private String name = "";
	private int numberOfObservations = 0;
	private Map<Feature, Double> probabilities = new LinkedHashMap<Feature, Double>();

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNumberOfObservations(int numberOfObservations) {
		this.numberOfObservations = numberOfObservations;
	}

	public int getNumberOfObservations() {
		return numberOfObservations;
	}

	public void setProbability(Feature feature, double probability) {
		probabilities.put(feature, probability);
	}

	public double getProbability(Feature feature) {
		Double propability = probabilities.get(feature);

		if (propability == null)
			return 0.0;
		else
			return propability;
	}

	@Override
	public String toString() {
		String out = "[pattern '" + name + "' (" + numberOfObservations + "x):\n";

		for (Feature f : probabilities.keySet()) {
			String name = f.toString();
			out += String.format("\t%.3f : %s\n", probabilities.get(f), name);
		}

		return out + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}

	public Pattern<Feature> clone(String nameOfNewPattern) {
		Pattern<Feature> clone = new Pattern<Feature>();
		clone.name = nameOfNewPattern;
		clone.numberOfObservations = numberOfObservations;
		for (Feature f : probabilities.keySet()) {
			Double probability = probabilities.get(f);
			clone.probabilities.put(f, probability);
		}
		return clone;
	}

	public static <T> Pattern<T> newPattern(String name, int numberOfObservations) {
		Pattern<T> p = new Pattern<T>();
		p.name = name;
		p.numberOfObservations = numberOfObservations;
		return p;
	}
}