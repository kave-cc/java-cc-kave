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

import static cc.kave.commons.utils.StringUtils.f;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;

import org.junit.Test;

import cc.kave.commons.utils.io.json.JsonUtils;

public class JsonUtilsTimeTest {

	@Test
	public void duration0() {

		assertDuration(ldt -> ldt, 0, 0, "00:00:00");
	}

	@Test
	public void duration1() {

		assertDuration(ldt -> ldt.plusDays(1), 0, 0, "1.00:00:00");
	}

	@Test
	public void duration2() {

		assertDuration(ldt -> ldt, 49, 0, "00:00:00");
	}

	@Test
	public void duration2b() {

		assertDuration(ldt -> ldt, 50, 100, "00:00:00.0000001");
	}

	@Test
	public void duration3() {

		assertDuration(ldt -> ldt.plusDays(12).plusHours(1).plusMinutes(2).plusSeconds(3), 4005006, 4005000,
				"12.01:02:03.0040050");
	}

	public void assertDuration(Function<LocalDateTime, LocalDateTime> createOffset, int plusNanosIn, int plusNanosOut,
			String expectedSerialization) {
		LocalDateTime a = LocalDateTime.now();
		LocalDateTime b = createOffset.apply(a);
		// rounding makes it necessary to distinguish both cases
		Duration durIn = Duration.between(a, b.plusNanos(plusNanosIn));
		Duration durExpected = Duration.between(a, b.plusNanos(plusNanosOut));

		String json = JsonUtils.toJson(durIn);
		assertEquals(f("\"%s\"", expectedSerialization), json);
		Duration durOut = JsonUtils.fromJson(json, Duration.class);
		assertEquals(durExpected, durOut);
	}
}