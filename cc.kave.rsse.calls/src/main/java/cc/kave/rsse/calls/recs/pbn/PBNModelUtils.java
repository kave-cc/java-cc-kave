/**
 * Copyright 2018 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.rsse.calls.recs.pbn;

import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION;
import static cc.kave.rsse.calls.recs.pbn.PBNModel.PRECISION_SCALE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.math.util.MathUtils;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.commons.exceptions.ValidationException;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.rsse.calls.model.usages.ICallParameter;
import cc.kave.rsse.calls.model.usages.IDefinition;

public class PBNModelUtils {

	public static PBNModel normalize(PBNModel in) {
		PBNModel out = new PBNModel();
		out.type = in.type;
		out.numObservations = in.numObservations;

		out.patternProbabilities = round(in.patternProbabilities);

		if (out.patternProbabilities.length == 1) {
			out.patternProbabilities = new double[] { 0.5, 0.5 };

			out.classContexts = in.classContexts;
			out.classContextProbabilities = duplicate(in.classContextProbabilities);

			out.methodContexts = in.methodContexts;
			out.methodContextProbabilities = duplicate(in.methodContextProbabilities);

			out.definitions = in.definitions;
			out.definitionProbabilities = duplicate(in.definitionProbabilities);

			out.callParameters = in.callParameters;
			out.callParameterProbabilityTrue = duplicate(in.callParameterProbabilityTrue);

			out.members = in.members;
			out.memberProbabilityTrue = duplicate(in.memberProbabilityTrue);

			return normalize(out);
		}

		if (in.classContexts.length == 1) {
			out.classContexts = new ITypeName[] { in.classContexts[0], PBNModelConstants.DUMMY_CCTX };
			out.classContextProbabilities = round(extend(in.classContextProbabilities));
		} else {
			out.classContexts = in.classContexts;
			out.classContextProbabilities = round(in.classContextProbabilities);
		}

		if (in.methodContexts.length == 1) {
			out.methodContexts = new IMethodName[] { in.methodContexts[0], PBNModelConstants.DUMMY_MCTX };
			out.methodContextProbabilities = round(extend(in.methodContextProbabilities));
		} else {
			out.methodContexts = in.methodContexts;
			out.methodContextProbabilities = round(in.methodContextProbabilities);
		}

		if (in.definitions.length == 1) {
			out.definitions = new IDefinition[] { in.definitions[0], PBNModelConstants.DUMMY_DEFINITION };
			out.definitionProbabilities = round(extend(in.definitionProbabilities));
		} else {
			out.definitions = in.definitions;
			out.definitionProbabilities = round(in.definitionProbabilities);
		}

		out.callParameters = in.callParameters;
		out.callParameterProbabilityTrue = round(in.callParameterProbabilityTrue);

		out.members = in.members;
		out.memberProbabilityTrue = round(in.memberProbabilityTrue);

		return out;
	}

	private static double[] duplicate(double[] in) {
		double[] out = new double[2 * in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = in[i];
			out[i + in.length] = in[i];
		}
		return out;
	}

	private static double[] extend(double[] in) {
		double[] out = new double[2 * in.length];
		for (int i = 0; i < in.length; i++) {
			out[2 * i] = in[i];
			out[2 * i + 1] = 0;
		}
		return out;
	}

	private static double[] round(double[] in) {
		assertNotNull(in);
		double[] out = new double[in.length];
		for (int i = 0; i < in.length; i++) {
			double v = Math.max(in[i], PRECISION);
			v = MathUtils.round(v, PRECISION_SCALE);
			out[i] = Math.min(v, 1 - PRECISION);
		}
		return out;
	}

	/**
	 * validates that this PBN model contains data that can be represented in a
	 * Bayesian network.
	 * 
	 * @param m
	 *            the model to check
	 * @throws AssertionException
	 *             is thrown for invalid models
	 */
	public static void assertValidity(PBNModel m) {
		assertNotNull(m.type);
		assertArray(m.patternProbabilities);
		assertArray(m.classContexts);
		assertArray(m.classContextProbabilities);
		assertArray(m.methodContexts);
		assertArray(m.methodContextProbabilities);
		assertArray(m.definitions);
		assertArray(m.definitionProbabilities);
		assertArray(m.callParameters);
		assertArray(m.callParameterProbabilityTrue);
		assertArray(m.members);
		assertArray(m.memberProbabilityTrue);

		int numPatterns = m.patternProbabilities.length;
		assertArraySize(numPatterns, m.classContexts.length, m.classContextProbabilities.length);
		assertArraySize(numPatterns, m.methodContexts.length, m.methodContextProbabilities.length);
		assertArraySize(numPatterns, m.definitions.length, m.definitionProbabilities.length);
		assertArraySize(numPatterns, m.callParameters.length, m.callParameterProbabilityTrue.length);
		assertArraySize(numPatterns, m.members.length, m.memberProbabilityTrue.length);

		assertGreaterThan(m.numObservations, 0);
		assertGreaterThan(numPatterns, 1);
		assertVals(m.patternProbabilities);
		assertVals(m.classContextProbabilities);
		assertVals(m.methodContextProbabilities);
		assertVals(m.definitionProbabilities);
		assertVals(m.callParameterProbabilityTrue);
		assertVals(m.memberProbabilityTrue);

		assertSum(m.patternProbabilities);
		assertSums(m.classContextProbabilities, numPatterns);
		assertSums(m.methodContextProbabilities, numPatterns);
		assertSums(m.definitionProbabilities, numPatterns);

		assertTrue(m.classContexts.length > 1);
		assertTrue(m.methodContexts.length > 1);
		assertTrue(m.definitions.length > 1);
	}

	private static void assertArray(double[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
	}

	private static void assertArray(IName[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (IName n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertArray(IDefinition[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (IDefinition n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertArray(ICallParameter[] arr) {
		assertNotNull(arr);
		assertGreaterThan(arr.length, 0);
		for (ICallParameter n : arr) {
			assertNotNull(n);
		}
	}

	private static void assertNotNull(Object o) {
		if (o == null) {
			throw new ValidationException("Object should not be null.");
		}
	}

	private static void assertGreaterThan(int a, int b) {
		if (a <= b) {
			throw new ValidationException("Number is too small.");
		}
	}

	private static void assertArraySize(int numPatterns, int numItems, int actual) {
		int expected = numPatterns * numItems;
		if (expected != actual) {
			String msg = "Unexcpected array size, expected %d (%d patterns * %d items), but got %d";
			throw new ValidationException(format(msg, expected, numPatterns, numItems, actual));
		}
	}

	private static void assertSums(double[] sums, int numPatterns) {
		int numItems = sums.length / numPatterns;
		for (int start = 0; start < sums.length; start += numItems) {
			double sum = 0;
			for (int i = 0; i < numItems; i++) {
				sum += sums[start + i];
			}
			assertSum(sum);
		}
	}

	private static void assertSum(double[] ps) {
		double sum = 0;
		for (double p : ps) {
			sum += p;
		}
		assertSum(sum);
	}

	private static void assertVals(double[] vals) {
		for (double val : vals) {
			assertVal(val);
		}
	}

	private static void assertVal(double val) {
		if (val < PRECISION || val > 1 - PRECISION) {
			String msg = "Value %f exceeds allowed range [%f,%f]";
			throw new ValidationException(format(msg, val, PRECISION, 1 - PRECISION));
		}

		double rounded = MathUtils.round(val, PRECISION_SCALE);
		if (rounded != val) {
			String msg = "Unexpected, value %f has not been rounded to %f.";
			throw new ValidationException(format(msg, val, rounded));
		}
	}

	private static void assertSum(double sum) {
		double delta = sum - 1;
		if (delta < -0.01 || delta > 0.01) {
			String msg = "Value %f exceeds acceptable deviation [-1.01,1.01]";
			throw new ValidationException(format(msg, sum));
		}
	}

	private static void assertTrue(boolean condition) {
		if (!condition) {
			throw new ValidationException("Unexpected, should be true.");
		}
	}

	public static String toWekaXML(PBNModel m) {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<!-- DTD for the XMLBIF 0.3 format -->\n");
		sb.append("<!DOCTYPE BIF [\n");
		sb.append("  <!ELEMENT BIF ( NETWORK )*>\n");
		sb.append("    <!ATTLIST BIF VERSION CDATA #REQUIRED>\n");
		sb.append("  <!ELEMENT NETWORK ( NAME, ( PROPERTY | VARIABLE | DEFINITION )* )>\n");
		sb.append("  <!ELEMENT NAME (#PCDATA)>\n");
		sb.append("  <!ELEMENT VARIABLE ( NAME, ( OUTCOME |  PROPERTY )* ) >\n");
		sb.append("    <!ATTLIST VARIABLE TYPE (nature|decision|utility) \"nature\">\n");
		sb.append("  <!ELEMENT OUTCOME (#PCDATA)>\n");
		sb.append("  <!ELEMENT DEFINITION ( FOR | GIVEN | TABLE | PROPERTY )* >\n");
		sb.append("  <!ELEMENT FOR (#PCDATA)>\n");
		sb.append("  <!ELEMENT GIVEN (#PCDATA)>\n");
		sb.append("  <!ELEMENT TABLE (#PCDATA)>\n");
		sb.append("  <!ELEMENT PROPERTY (#PCDATA)>\n");
		sb.append("]>\n");
		sb.append("<BIF VERSION=\"0.3\">\n");
		sb.append("<NETWORK>\n");
		sb.append("<NAME>").append(StringEscapeUtils.escapeXml10(m.type.getIdentifier())).append("</NAME>\n");
		sb.append("<!-- numObervations: ").append(m.numObservations).append(" -->\n");

		// PATTERNS

		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("<NAME>patterns</NAME>\n");
		for (int i = 0; i < m.patternProbabilities.length; i++) {
			sb.append("<OUTCOME>p").append(i).append("</OUTCOME>\n");
		}
		sb.append("<PROPERTY>position = (0,0)</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("<FOR>patterns</FOR>\n");
		sb.append("<TABLE>\n");
		for (double pp : m.patternProbabilities) {
			sb.append(pp).append(' ');
		}
		sb.append("\n</TABLE>\n");
		sb.append("</DEFINITION>\n");

		List<String> states;

		// CCTXS
		states = toStates(m.classContexts, cctx -> cctx.getIdentifier());
		appendNode(sb, 0, 1, states, "ctxs", m.classContextProbabilities);

		// MCTXS
		states = toStates(m.methodContexts, mctx -> mctx.getIdentifier());
		appendNode(sb, 1, 1, states, "mctxs", m.classContextProbabilities);

		// DEFS
		states = toStates(m.definitions, def -> {
			StringBuilder sb2 = new StringBuilder();
			sb2.append(def.getType().toString());
			IMemberName member = def.getMember(IMemberName.class);
			if (member != null) {
				sb2.append(":").append(member.getIdentifier());
				if (def.getArgIndex() != -1) {
					sb2.append(":").append(def.getArgIndex());
				}
			}
			return sb2.toString();
		});
		appendNode(sb, 2, 1, states, "defs", m.definitionProbabilities);

		// PARAMS
		states = toStates(m.callParameters, cp -> "CP:" + cp.getMethod().getIdentifier() + ":" + cp.getArgIndex());
		appendMultiNode(sb, 3, 1, states, m.callParameterProbabilityTrue, m.patternProbabilities.length);

		// MEMBERS
		states = toStates(m.members, mb -> "MEMBER:" + mb.getIdentifier());
		appendMultiNode(sb, 4, 1, states, m.memberProbabilityTrue, m.patternProbabilities.length);

		sb.append("</NETWORK>\n");
		sb.append("</BIF>\n");

		return sb.toString();
	}

	private static void appendMultiNode(StringBuilder sb, int gridX, int gridY0, List<String> names, double[] probs,
			int numPatterns) {
		int numItems = names.size();
		int curItem = 0;
		for (String name : names) {
			double[] ps = new double[2 * numPatterns];
			for (int curPattern = 0; curPattern < numPatterns; curPattern++) {
				double p = probs[curPattern * numItems + curItem];
				ps[2 * curPattern] = p;
				ps[2 * curPattern + 1] = 1 - p;

			}
			appendNode(sb, gridX, gridY0++, asList("t", "f"), name, ps);
			curItem++;
		}
	}

	private static <T> List<String> toStates(T[] classContexts, Function<T, String> mapper) {
		return stream(classContexts).map(mapper).collect(Collectors.toList());
	}

	private static void appendNode(StringBuilder sb, int gridX, int gridY, List<String> states, String name,
			double[] probabilities) {
		final int STEP_X = 200;
		final int STEP_Y = 40;

		sb.append("<!-- definition for ").append(name).append(" -->\n");
		sb.append("<VARIABLE TYPE=\"nature\">\n");
		sb.append("  <NAME>").append(name).append("</NAME>\n");
		for (String state : states) {
			sb.append("  <OUTCOME>").append(state).append("</OUTCOME>\n");
		}
		sb.append("  <PROPERTY>position = (").append(gridX * STEP_X).append(",").append(gridY * STEP_Y)
				.append(")</PROPERTY>\n");
		sb.append("</VARIABLE>\n");
		sb.append("<DEFINITION>\n");
		sb.append("  <FOR>").append(name).append("</FOR>\n");
		sb.append("  <GIVEN>patterns</GIVEN>\n");
		sb.append("  <TABLE>\n    ");
		for (double p : probabilities) {
			sb.append(p).append(' ');
		}
		sb.append("\n  </TABLE>\n");
		sb.append("</DEFINITION>\n");
	}
}