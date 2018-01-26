/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.assertions;

public class Throws {

	public static RuntimeException throwNotImplemented() {
		return new RuntimeException("not implemented");
	}
	
	public static RuntimeException throwIllegalArgumentException(String string, Object... args) {
		throw throwNotImplemented();
	}

	public static RuntimeException throwIllegalStateException(String string) {
		throw throwNotImplemented();
	}

	public static RuntimeException throwUnreachable(String string, Object... args) {
		throw throwNotImplemented();
	}
}