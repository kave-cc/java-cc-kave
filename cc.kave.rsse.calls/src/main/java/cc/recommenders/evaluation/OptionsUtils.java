/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.evaluation;

public class OptionsUtils {
	public static String getOptions(String algo, boolean useClass, boolean useDefinition, boolean useParameters) {
		OptionsBuilder ob = new OptionsBuilder(algo);
		ob.useClass = useClass;
		ob.useDefinition = useDefinition;
		ob.useParameters = useParameters;
		ob.useInit = false;
		return ob.get();
	}

	public static String getOptionsWithInit(String algo, boolean useClass, boolean useDefinition,
			boolean useParameters) {
		OptionsBuilder ob = new OptionsBuilder(algo);
		ob.useClass = useClass;
		ob.useDefinition = useDefinition;
		ob.useParameters = useParameters;
		ob.useInit = true;
		return ob.get();
	}

	public static OptionsBuilder bmn() {
		return new OptionsBuilder("BMN+MANHATTAN");
	}

	public static OptionsBuilder pbn(int i) {
		String t1, t2;
		if (i == 0) {
			t1 = "0.002";
			t2 = "0.001";
		} else {
			t1 = String.format("%.3f", i * 0.01 + 0.001);
			t2 = String.format("%.2f", i * 0.01);
		}
		return new OptionsBuilder(String.format("CANOPY[%s; %s]+COSINE", t1, t2));
	}

	public static class OptionsBuilder {

		// be aware that existing evaluations might depend on these defaults!!
		private String algo;
		private boolean useDouble = true;
		private boolean useInit = false;
		private boolean useNmQueries = true;
		private boolean useClass = false;
		private boolean useDefinition = false;
		private boolean useParameters = false;
		private boolean ignore = false;
		private boolean dropRare = false;
		private int min = 0;

		public OptionsBuilder(String algo) {
			this.algo = algo;
		}

		public String get() {
			String mOpts = "+W[0.00; 0.00; 0.00; 0.00]%sINIT%sDROP";
			String end = "%sIGNORE%sDOUBLE%s";
			String opts = algo + mOpts + "+Q[%s]%sCLASS+METHOD%sDEF%sPARAMS" + end;
			String queryType = useNmQueries ? "NM" : "ZERO";
			String minStr = min != 0 ? "+MIN" + min : "";
			return String.format(opts, has(useInit), has(dropRare), queryType, has(useClass), has(useDefinition),
					has(useParameters), has(ignore), has(useDouble), minStr);
		}

		private char has(boolean opt) {
			return opt ? '+' : '-';
		}

		public OptionsBuilder init(boolean useInit) {
			this.useInit = useInit;
			return this;
		}

		public OptionsBuilder c(boolean useClass) {
			this.useClass = useClass;
			return this;
		}

		public OptionsBuilder d(boolean useDefinition) {
			this.useDefinition = useDefinition;
			return this;
		}

		public OptionsBuilder p(boolean useParameters) {
			this.useParameters = useParameters;
			return this;
		}

		public OptionsBuilder useFloat() {
			useDouble = false;
			return this;
		}

		public OptionsBuilder useDouble() {
			useDouble = true;
			return this;
		}

		public OptionsBuilder q0() {
			useNmQueries = false;
			return this;
		}

		public OptionsBuilder qNM() {
			useNmQueries = true;
			return this;
		}

		/**
		 * You should really not use this option, it invalidates an evaluation!
		 */
		@Deprecated
		public OptionsBuilder ignore(boolean ignore) {
			this.ignore = ignore;
			return this;
		}

		public OptionsBuilder dropRareFeatures(boolean dropRare) {
			this.dropRare = dropRare;
			return this;
		}

		public OptionsBuilder min(int min) {
			this.min = min;
			return this;
		}
	}
}