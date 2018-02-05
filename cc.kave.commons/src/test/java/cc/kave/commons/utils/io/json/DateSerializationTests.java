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
package cc.kave.commons.utils.io.json;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.utils.io.json.JsonUtils;

@Ignore
public class DateSerializationTests {
	@Test
	public void asd() {
		LocalDateTime o = LocalDateTime.now();
		String json = JsonUtils.toJson(o);
		Object o2 = JsonUtils.fromJson(json, LocalDateTime.class);
		assertEquals(o, o2);
	}

	@Test
	public void fromString_zoned() {
		String in = "\"2012-02-23T18:54:59.549+01:00\"";
		String out = "\"2012-02-23T18:54:59.549\"";
		assertEquals(out, j2o2j(in));
	}

	@Test
	public void fromString_withMillis() {
		String json = "\"2012-02-23T18:54:59.549\"";
		assertEquals(json, j2o2j(json));
	}

	@Test
	public void fromString_withNanos() {
		String json = "\"2012-02-23T18:54:59.123456789\"";
		assertEquals(json, j2o2j(json));
	}

	@Test
	public void fromString_withNanosAndZoned() {
		String in = "\"2015-10-17T15:53:33.8265398+02:00\"";
		String out = "\"2015-10-17T15:53:33.8265398\"";
		assertEquals(out, j2o2j(in));
	}

	private static String j2o2j(String in) {
		Object o = JsonUtils.fromJson(in, LocalDateTime.class);
		String json = JsonUtils.toJson(o);
		return json;
	}
}