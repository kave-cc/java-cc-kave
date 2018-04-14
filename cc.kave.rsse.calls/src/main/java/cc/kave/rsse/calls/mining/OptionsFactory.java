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
package cc.kave.rsse.calls.mining;

public class OptionsFactory {

	private Options options;

	public OptionsFactory() {
		// provide default constructor to relief users from providing an instance on
		// startup, e.g., in dependency injection contexts
	}

	public OptionsFactory(Options options) {
		assertNotNull(options);
		this.options = options;
	}

	public void set(Options options) {
		assertNotNull(options);
		this.options = options;
	}

	public Options get() {
		if (options == null) {
			throw new IllegalStateException("Options have not been initialized yet.");
		}
		return options;
	}

	private static void assertNotNull(Options options) {
		if (options == null) {
			throw new IllegalArgumentException("Options cannot be set to null.");
		}
	}
}