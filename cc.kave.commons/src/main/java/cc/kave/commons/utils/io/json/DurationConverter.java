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

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DurationConverter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

	@Override
	public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
		LocalTime plus = LocalTime.MIDNIGHT.plus(duration);
		String middle = plus.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		long days = duration.toDays();
		// 1 tick = 100 ns
		long ticks = ((duration.toNanos() + 50) % 1000000000) / 100;

		String left = days == 0 ? "" : f("%d.", days);
		String right = ticks == 0 ? "" : f(".%07d", ticks);
		return new JsonPrimitive(f("%s%s%s", left, middle, right));
	}

	@Override
	public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String in = json.getAsString();

		int dot = in.indexOf('.');
		if (dot == -1) {
			in = "0." + in + ".0";
		} else if (dot > in.indexOf(':')) {
			in = "0." + in;
		} else if (in.lastIndexOf('.') < in.lastIndexOf(':')) {
			in = in + ".0";
		}

		String daysStr = in.substring(0, in.indexOf('.'));
		String hoursStr = in.substring(in.indexOf('.') + 1, in.indexOf(':'));
		String minStr = in.substring(in.indexOf(':') + 1, in.lastIndexOf(':'));
		String secStr = in.substring(in.lastIndexOf(':') + 1, in.lastIndexOf('.'));
		String tickStr = in.substring(in.lastIndexOf('.') + 1, in.length());

		long days = Long.parseLong(daysStr);
		long hours = Long.parseLong(hoursStr);
		long min = Long.parseLong(minStr);
		long seconds = Long.parseLong(secStr);
		long ticks = Long.parseLong(tickStr);
		long nanos = 100 * ticks;

		LocalDateTime start = LocalDateTime.MIN;
		LocalDateTime end = start.plusDays(days).plusHours(hours).plusMinutes(min).plusSeconds(seconds)
				.plusNanos(nanos);
		return Duration.between(start, end);
	}
}