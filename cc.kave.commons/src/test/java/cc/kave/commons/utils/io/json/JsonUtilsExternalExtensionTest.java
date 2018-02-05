package cc.kave.commons.utils.io.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.kave.commons.utils.io.json.JsonUtils.IAdditionalBuilderConfiguration;

public class JsonUtilsExternalExtensionTest {

	@Before
	public void setup() {
		// you always have to assume that the JsonUtils have been used before
		// (so they are initialized)
		JsonUtils.resetAllConfiguration();
		JsonUtils.toJson(1);
		JsonUtils.toJsonFormatted(1);
	}

	@Test
	public void gson_toDefault() {
		String json = JsonUtils.toJson(new TestClass(123));
		Assert.assertEquals("{\"Num\":123}", json);
	}

	@Test
	public void gson_toPretty() {
		String json = JsonUtils.toJsonFormatted(new TestClass(123));
		Assert.assertEquals("{\n    \"Num\": 123\n}", json);
	}

	@Test
	public void gson_from() {
		TestClass obj = JsonUtils.fromJson("{\"Num\":123}", TestClass.class);
		Assert.assertEquals(new TestClass(123), obj);
	}

	@Test
	public void customConfig_toDefault() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());

		String json = JsonUtils.toJson(new TestClass(123));
		Assert.assertEquals("123", json);
	}

	@Test
	public void canBeReset() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());
		JsonUtils.resetAllConfiguration();
		Assert.assertEquals("{\"Num\":123}", JsonUtils.toJson(new TestClass(123)));
		Assert.assertEquals("{\n    \"Num\": 123\n}", JsonUtils.toJsonFormatted(new TestClass(123)));
	}

	@Test
	public void customConfig_toPretty() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());

		String json = JsonUtils.toJsonFormatted(new TestClass(123));
		Assert.assertEquals("123", json);
	}

	@Test
	public void customConfig_from() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());

		TestClass obj = JsonUtils.fromJson("123", TestClass.class);
		Assert.assertEquals(new TestClass(123), obj);
	}

	@Test
	public void multipleConfigs_differentOnesWorks() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());
		JsonUtils.registerBuilderConfig(new IAdditionalBuilderConfiguration() {
			@Override
			public void configure(GsonBuilder gb) {
				// no need for contents :D
			}
		});

		TestClass obj = JsonUtils.fromJson("123", TestClass.class);
		Assert.assertEquals(new TestClass(123), obj);
	}

	@Test(expected = IllegalArgumentException.class)
	public void multipleConfigs_sameCannotBeAddedTwice() {
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());
		JsonUtils.registerBuilderConfig(new TestBuilderConfiguration());
	}

	private class TestBuilderConfiguration implements IAdditionalBuilderConfiguration {
		@Override
		public void configure(GsonBuilder gb) {
			gb.registerTypeAdapter(TestClass.class, new TestClassAdapter());
		}
	}

	private class TestClassAdapter extends TypeAdapter<TestClass> {

		@Override
		public void write(JsonWriter out, TestClass value) throws IOException {
			out.value(value.num);
		}

		@Override
		public TestClass read(JsonReader in) throws IOException {
			return new TestClass(in.nextInt());
		}
	}

	private static class TestClass {
		public int num;

		public TestClass(int num) {
			this.num = num;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + num;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestClass other = (TestClass) obj;
			if (num != other.num)
				return false;
			return true;
		}
	}
}