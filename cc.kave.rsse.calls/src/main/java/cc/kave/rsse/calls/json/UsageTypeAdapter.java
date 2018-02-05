package cc.kave.rsse.calls.json;

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.Names;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSiteKind;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class UsageTypeAdapter extends TypeAdapter<Usage> {

	// make sure the naming is consistent to the field names in "Query",
	// "DefinitionSite" and "CallSite"

	private static final String TYPE = "type";
	private static final String CLASS_CTX = "classCtx";
	private static final String METHOD_CTX = "methodCtx";
	private static final String DEFINITION = "definition";
	private static final String SITES = "sites";

	private static final String DEF_KIND = "kind";
	private static final String DEF_METHOD = "method";
	private static final String DEF_FIELD = "field";
	private static final String DEF_PROPERTY = "property";
	private static final String DEF_ARG = "argIndex";

	private static final String CS_KIND = "kind";
	private static final String CS_CALL = "method";
	private static final String CS_ARG = "argIndex";

	@Override
	public void write(JsonWriter out, Usage usage) throws IOException {
		if (usage instanceof NoUsage) {
			out.value("NoUsage");
		} else {
			writeQuery(out, usage);
		}
	}

	private void writeQuery(JsonWriter out, Usage usage) throws IOException {
		out.beginObject();

		if (usage.getType() != null) {
			out.name(TYPE).value(usage.getType().toString());
		}
		if (usage.getClassContext() != null) {
			out.name(CLASS_CTX).value(usage.getClassContext().toString());
		}
		if (usage.getMethodContext() != null) {
			out.name(METHOD_CTX).value(usage.getMethodContext().toString());
		}
		if (usage.getDefinitionSite() != null) {
			out.name(DEFINITION);
			writeDefinition(out, usage.getDefinitionSite());
		}

		if (usage.getAllCallsites() != null) {
			out.name(SITES);
			out.beginArray();
			for (CallSite m : usage.getAllCallsites()) {
				writeCallSite(out, m);
			}
			out.endArray();
		}

		out.endObject();
	}

	@Override
	public Usage read(JsonReader in) throws IOException {

		if (in.peek() == JsonToken.STRING) {
			String val = in.nextString();
			Asserts.assertEquals("NoUsage", val, "Invalid JSON. Expected 'NoUsage', but found '" + val + "'.");
			return new NoUsage();
		}

		Query q = new Query();
		q.setAllCallsites(null);

		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			if (TYPE.equals(name)) {
				q.setType(Names.newType(in.nextString()));
			} else if (CLASS_CTX.equals(name)) {
				q.setClassContext(Names.newType(in.nextString()));
			} else if (METHOD_CTX.equals(name)) {
				q.setMethodContext(Names.newMethod(in.nextString()));
			} else if (DEFINITION.equals(name)) {
				q.setDefinition(readDefinition(in));
			} else if (SITES.equals(name)) {
				q.setAllCallsites(readCallSites(in));
			} else {
				// skip value (most likely $type key from .net serialization)
				in.nextString();
			}
		}
		in.endObject();
		return q;
	}

	private void writeDefinition(JsonWriter out, DefinitionSite def) throws IOException {
		out.beginObject();
		if (def.getKind() != null) {
			out.name(DEF_KIND).value(def.getKind().toString());
		}
		if (def.getField() != null) {
			out.name(DEF_FIELD).value(def.getField().toString());
		}
		if (def.getMethod() != null) {
			out.name(DEF_METHOD).value(def.getMethod().toString());
		}
		boolean isNonDefaultArgIndex = def.getArgIndex() != DefinitionSites.createUnknownDefinitionSite().getArgIndex();
		if (isNonDefaultArgIndex) {
			out.name(DEF_ARG).value(def.getArgIndex());
		}
		out.endObject();
	}

	private DefinitionSite readDefinition(JsonReader in) throws IOException {
		DefinitionSite def = DefinitionSites.createUnknownDefinitionSite();
		def.setKind(null);

		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			if (DEF_KIND.equals(name)) {
				def.setKind(DefinitionSiteKind.valueOf(in.nextString()));
			} else if (DEF_ARG.equals(name)) {
				def.setArgIndex(in.nextInt());
			} else if (DEF_FIELD.equals(name)) {
				def.setField(Names.newField(in.nextString()));
			} else if (DEF_METHOD.equals(name)) {
				def.setMethod(Names.newMethod(in.nextString()));
			} else if (DEF_PROPERTY.equals(name)) {
				def.setProperty(Names.newProperty(in.nextString()));
			}
		}
		in.endObject();

		return def;
	}

	private Set<CallSite> readCallSites(JsonReader in) throws IOException {
		Set<CallSite> sites = Sets.newLinkedHashSet();
		in.beginArray();
		while (in.hasNext()) {
			sites.add(readCallSite(in));
		}
		in.endArray();
		return sites;
	}

	private void writeCallSite(JsonWriter out, CallSite site) throws IOException {
		out.beginObject();
		if (site.getKind() != null) {
			out.name(CS_KIND).value(site.getKind().toString());
		}
		if (site.getMethod() != null) {
			out.name(CS_CALL).value(site.getMethod().toString());
		}
		boolean isNonDefaultArgIndex = site.getArgIndex() != CallSites.createReceiverCallSite("LT.m()V").getArgIndex();
		if (isNonDefaultArgIndex) {
			out.name(CS_ARG).value(site.getArgIndex());
		}
		out.endObject();
	}

	private CallSite readCallSite(JsonReader in) throws IOException {
		CallSite site = CallSites.createReceiverCallSite("LT.m()V");
		site.setKind(null);
		site.setMethod(null);

		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			if (CS_ARG.equals(name)) {
				site.setArgIndex(in.nextInt());
			} else if (CS_CALL.equals(name)) {
				site.setMethod(Names.newMethod(in.nextString()));
			} else if (CS_KIND.equals(name)) {
				site.setKind(CallSiteKind.valueOf(in.nextString()));
			}
		}
		in.endObject();
		return site;
	}
}