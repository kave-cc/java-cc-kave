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

import static java.lang.Double.parseDouble;
import static java.lang.Math.round;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.kave.commons.assertions.Asserts;

public class QueryOptions {

	public QueryType queryType = QueryType.NM;
	public boolean useClassContext = true;
	public boolean useMethodContext = true;
	public boolean useDefinition = true;
	public boolean useParameterSites = true;

	public double minProbability = 0;
	public boolean isIgnoringAfterFullRecall = false;
	public boolean useDoublePrecision = true;

	public static QueryOptions newQueryOptions(String in) {
		Asserts.assertNotNull(in);
		QueryOptions options = new QueryOptions();

		if (keyExists(in, "CLASS")) {
			options.useClassContext = parseExistance(in, "CLASS");
		}

		if (keyExists(in, "METHOD")) {
			options.useMethodContext = parseExistance(in, "METHOD");
		}

		if (keyExists(in, "DEF")) {
			options.useDefinition = parseExistance(in, "DEF");
		}

		if (keyExists(in, "PARAMS")) {
			options.useParameterSites = parseExistance(in, "PARAMS");
		}

		if (keyExists(in, "IGNORE")) {
			options.isIgnoringAfterFullRecall = parseExistance(in, "IGNORE");
		}

		if (keyExists(in, "DOUBLE")) {
			options.useDoublePrecision = parseExistance(in, "DOUBLE");
		}

		options.minProbability = parseMin(in);
		options.queryType = parseQueryType(in);

		return options;
	}

	private static QueryType parseQueryType(String in) {
		Pattern p = compile(".*\\+Q\\[([A-Z0-9]+)\\].*");
		Matcher m = p.matcher(in);
		if (m.matches()) {
			return QueryType.valueOf(m.group(1));
		} else {
			return QueryType.NM;
		}
	}

	private static boolean keyExists(String string, String key) {
		if (string.indexOf("+" + key) != -1)
			return true;
		else if (string.indexOf("-" + key) != -1)
			return true;
		else
			return false;
	}

	private static boolean parseExistance(String string, String key) {
		if (string.indexOf("+" + key) != -1)
			return true;
		else
			return false;
	}

	private static double parseMin(String in) {
		Pattern p = compile(".*\\+MIN([0-9]+).*");
		Matcher m = p.matcher(in);
		if (m.matches()) {
			return parseDouble(m.group(1)) / 100.0;
		} else {
			return 0.0;
		}
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("+Q[%s]", queryType));
		sb.append(sign(useClassContext) + "CLASS");
		sb.append(sign(useMethodContext) + "METHOD");
		sb.append(sign(useDefinition) + "DEF");
		sb.append(sign(useParameterSites) + "PARAMS");
		sb.append(sign(isIgnoringAfterFullRecall) + "IGNORE");
		sb.append(sign(useDoublePrecision) + "DOUBLE");

		if (minProbability > 0.0) {
			sb.append("+MIN" + round(minProbability * 100));
		}

		return sb.toString();
	}

	private String sign(boolean value) {
		if (value)
			return "+";
		else
			return "-";
	}

	/**
	 * Sets own options from other QueryOptions
	 * 
	 * @return self
	 */
	public QueryOptions setFrom(QueryOptions other) {
		useDoublePrecision = other.useDoublePrecision;
		useClassContext = other.useClassContext;
		useDefinition = other.useDefinition;
		useMethodContext = other.useMethodContext;
		useParameterSites = other.useParameterSites;
		minProbability = other.minProbability;
		isIgnoringAfterFullRecall = other.isIgnoringAfterFullRecall;
		queryType = other.queryType;
		return this;
	}

	public enum QueryType {
		ZERO, NM
	}

	public void setFrom(String options) {
		setFrom(QueryOptions.newQueryOptions(options));
	}
}