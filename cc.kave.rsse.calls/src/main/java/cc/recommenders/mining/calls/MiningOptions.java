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

import static cc.recommenders.mining.calls.MiningOptions.Algorithm.CANOPY;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.COSINE;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.MANHATTAN;
import static cc.recommenders.mining.calls.MiningOptions.DistanceMeasure.valueOf;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;

import cc.kave.commons.assertions.Asserts;

public class MiningOptions {

	private double weightClassContext = 1.0;
	private double weightMethodContext = 1.0;
	private double weightDefinition = 1.0;
	private double weightParameterSites = 1.0;

	private Algorithm algorithm = CANOPY;
	private DistanceMeasure distanceMeasure = COSINE;

	private double canopyT1 = 2.0;
	private double canopyT2 = 1.0;

	private int kmeansCluster = 10;
	private double kmeansConvergenceThreshold = 0.1;
	private int kmeansIterations = 10;

	private boolean isFeatureDropping = false;
	private boolean isInitUsedAsCall = false;

	public double getWeightClassContext() {
		return weightClassContext;
	}

	public double getWeightMethodContext() {
		return weightMethodContext;
	}

	public double getWeightDefinition() {
		return weightDefinition;
	}

	public double getWeightParameterSites() {
		return weightParameterSites;
	}

	public MiningOptions setAlgorithm(Algorithm a) {
		algorithm = a;
		return this;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public boolean isInitUsedAsCall() {
		return isInitUsedAsCall;
	}

	public boolean setInitUsedAsCall(boolean isUsed) {
		return isInitUsedAsCall = isUsed;
	}

	public double getT1() {
		return canopyT1;
	}

	public double getT2() {
		return canopyT2;
	}

	public int getClusterCount() {
		return kmeansCluster;
	}

	public int getNumberOfIterations() {
		return kmeansIterations;
	}

	public double getConvergenceThreshold() {
		return kmeansConvergenceThreshold;
	}

	public enum Algorithm {
		KMEANS, CANOPY, COMBINED, CALLGROUP, BMN
	}

	public enum DistanceMeasure {
		MANHATTAN, COSINE
	}

	public void setDistanceMeasure(DistanceMeasure measure) {
		distanceMeasure = measure;
	}

	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	public boolean isFeatureDropping() {
		return isFeatureDropping;
	}

	public void setFeatureDropping(boolean isFeatureDropping) {
		this.isFeatureDropping = isFeatureDropping;
	}

	public MiningOptions setWeightClassContext(double wClass) {
		weightClassContext = wClass;
		return this;
	}

	public MiningOptions setWeightMethodContext(double wMethod) {
		weightMethodContext = wMethod;
		return this;
	}

	public MiningOptions setWeightDefinition(double wDefinition) {
		weightDefinition = wDefinition;
		return this;
	}

	public MiningOptions setWeightParameterSites(double wParameter) {
		weightParameterSites = wParameter;
		return this;
	}

	public void setConvergenceThreshold(double d) {
		kmeansConvergenceThreshold = d;
	}

	public void setNumberOfIterations(int i) {
		kmeansIterations = i;
	}

	public void setClusterCount(int i) {
		kmeansCluster = i;
	}

	public void setT1(double d) {
		canopyT1 = d;
	}

	public void setT2(double d) {
		canopyT2 = d;
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return String.format("%s+%s+W[%.2f; %.2f; %.2f; %.2f]%sINIT%sDROP", toStringAlgorithm(), distanceMeasure,
				weightClassContext, weightMethodContext, weightDefinition, weightParameterSites, isInitUsedAsCall ? '+'
						: "-", isFeatureDropping ? '+' : "-");
	}

	public String toStringAlgorithm() {
		switch (algorithm) {
		case CANOPY:
			return String.format("CANOPY[%.2f; %.2f]", canopyT1, canopyT2);
		case KMEANS:
			return String.format("KMEANS[%.2f; %d; %d]", kmeansConvergenceThreshold, kmeansIterations, kmeansCluster);
		case COMBINED:
			return String.format("COMBINED[%.2f; %.2f; %.2f; %d]", canopyT1, canopyT2, kmeansConvergenceThreshold,
					kmeansIterations);
		case BMN:
			return "BMN";
		default:
			return "CALLGROUP";

		}
	}

	public static MiningOptions newMiningOptions(String optionsString) {
		Asserts.assertNotNull(optionsString);
		MiningOptions options = new MiningOptions();
		parseAlgorithm(options, optionsString);
		parseDistanceMeasure(options, optionsString);
		parseWeights(options, optionsString);
		parseInitAndDrop(options, optionsString);
		return options;
	}

	private static void parseDistanceMeasure(MiningOptions options, String optionsString) {
		String regex = String.format(".*\\+(%s|%s)(?:\\+|-).*", COSINE, MANHATTAN);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		options.distanceMeasure = valueOf(matcher.group(1));
	}

	private static void parseAlgorithm(MiningOptions options, String optionsString) {
		if (optionsString.startsWith("CALLGROUP")) {
			options.algorithm = Algorithm.CALLGROUP;
		} else if (optionsString.startsWith("CANOPY")) {
			parseCanopyAlgorithm(options, optionsString);
		} else if (optionsString.startsWith("KMEANS")) {
			parseKmeansAlgorithm(options, optionsString);
		} else if (optionsString.startsWith("COMBINED")) {
			parseCombinedAlgorithm(options, optionsString);
		} else if (optionsString.startsWith("BMN")) {
			options.algorithm = Algorithm.BMN;
		} else {
			Asserts.fail("algorithm cannot be parsed");
		}
	}

	private static void parseCanopyAlgorithm(MiningOptions options, String optionsString) {
		Pattern pattern = Pattern.compile(".*CANOPY\\[([^];]+);([^];]+)\\].*");
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		options.algorithm = Algorithm.CANOPY;
		options.setT1(parseDouble(matcher.group(1)));
		options.setT2(parseDouble(matcher.group(2)));
	}

	private static void parseKmeansAlgorithm(MiningOptions options, String optionsString) {
		Pattern pattern = Pattern.compile(".*KMEANS\\[([^];]+);([^];]+);([^];]+)\\].*");
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		options.algorithm = Algorithm.KMEANS;
		options.setConvergenceThreshold(parseDouble(matcher.group(1)));
		options.setNumberOfIterations(parseInt(matcher.group(2).trim()));
		options.setClusterCount(parseInt(matcher.group(3).trim()));
	}

	private static void parseCombinedAlgorithm(MiningOptions options, String optionsString) {
		Pattern pattern = Pattern.compile(".*COMBINED\\[([^];]+);([^];]+);([^];]+);([^];]+)\\].*");
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		options.algorithm = Algorithm.COMBINED;
		options.setT1(parseDouble(matcher.group(1)));
		options.setT2(parseDouble(matcher.group(2)));
		options.setConvergenceThreshold(parseDouble(matcher.group(3)));
		options.setNumberOfIterations(parseInt(matcher.group(4).trim()));
	}

	private static void parseWeights(MiningOptions options, String optionsString) {
		Pattern pattern = Pattern.compile(".*\\+W\\[([^];]+);([^];]+);([^];]+);([^];]+)\\].*");
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		options.setWeightClassContext(parseDouble(matcher.group(1)));
		options.setWeightMethodContext(parseDouble(matcher.group(2)));
		options.setWeightDefinition(parseDouble(matcher.group(3)));
		options.setWeightParameterSites(parseDouble(matcher.group(4)));
	}

	private static double parseDouble(String numString) {
		return Double.parseDouble(numString.replace(',', '.'));
	}

	private static void parseInitAndDrop(MiningOptions options, String optionsString) {
		options.isFeatureDropping = parseBoolean("DROP", optionsString);
		options.isInitUsedAsCall = parseBoolean("INIT", optionsString);
	}

	private static boolean parseBoolean(String token, String optionsString) {
		Pattern pattern = Pattern.compile(".*((?:\\+|-)" + token + ").*");
		Matcher matcher = pattern.matcher(optionsString);

		Asserts.assertTrue(matcher.matches());

		if (("+" + token).equals(matcher.group(1).trim())) {
			return true;
		} else {
			return false;
		}
	}

	public MiningOptions setFrom(MiningOptions other) {
		setAlgorithm(other.getAlgorithm());
		setDistanceMeasure(other.getDistanceMeasure());
		setFeatureDropping(other.isFeatureDropping());
		isInitUsedAsCall = other.isInitUsedAsCall;
		setClusterCount(other.getClusterCount());
		setConvergenceThreshold(other.getConvergenceThreshold());
		setNumberOfIterations(other.getNumberOfIterations());
		setT1(other.getT1());
		setT2(other.getT2());
		setWeightClassContext(other.getWeightClassContext());
		setWeightDefinition(other.getWeightDefinition());
		setWeightMethodContext(other.getWeightMethodContext());
		setWeightParameterSites(other.getWeightParameterSites());
		return this;
	}

	public void setFrom(String options) {
		setFrom(MiningOptions.newMiningOptions(options));
	}
}