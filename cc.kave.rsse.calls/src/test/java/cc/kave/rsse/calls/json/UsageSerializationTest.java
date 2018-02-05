package cc.kave.rsse.calls.json;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class UsageSerializationTest {

	@Before
	public void setup() {
		RsseCallsJsonUtils.registerJsonAdapters();
	}

	@After
	public void teardown() {
		JsonUtils.resetAllConfiguration();
	}

	@Test
	public void differentDefinitionSites_constant() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByConstant());
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_init() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByConstructor("[p:void] [T, P]..ctor()"));
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_field() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByField("[p:int] [T, P]._f"));
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_param() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByParam("[p:int] [T3, P].M()", 2));
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_property() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByProperty("get [p:int] [T4, P].P()"));
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_return() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByReturn("[p:int] [T5, P].P()"));
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void differentDefinitionSites_this() {
		Query q = q();
		q.setDefinition(DefinitionSites.createDefinitionByThis());
		Assert.assertEquals(q, JsonUtils.fromJson(JsonUtils.toJson(q), Usage.class));
	}

	@Test
	public void queryToUsage() {
		Usage q = q();
		String json = JsonUtils.toJson(q);
		Usage u = JsonUtils.fromJson(json, Usage.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void queryToJson() {
		String json = JsonUtils.toJson(q());
		Assert.assertEquals("", json);
	}

	@Test
	public void queryToQuery() {
		Usage q = q();
		String json = JsonUtils.toJson(q);
		Query u = JsonUtils.fromJson(json, Query.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void noUsageToUsage() {
		Usage q = new NoUsage();
		String json = JsonUtils.toJson(q);
		Usage u = JsonUtils.fromJson(json, Usage.class);
		Assert.assertEquals(q, u);
	}

	@Test
	public void noUsageToNoUsage() {
		Usage q = new NoUsage();
		String json = JsonUtils.toJson(q);
		NoUsage u = JsonUtils.fromJson(json, NoUsage.class);
		Assert.assertEquals(q, u);
	}

	private static Query q() {
		Query q = new Query();
		q.setType(Names.newType("T, P"));
		q.setClassContext(Names.newType("S, P"));
		q.setMethodContext(Names.newMethod("[p:int] [T, P].M()"));
		q.setDefinition(DefinitionSites.createDefinitionByConstant());
		q.getAllCallsites().add(CallSites.createParameterCallSite("[p:int] [T2, P].M()", 1));
		q.getAllCallsites().add(CallSites.createReceiverCallSite("[p:int] [T3, P].M()"));
		return q;
	}
}