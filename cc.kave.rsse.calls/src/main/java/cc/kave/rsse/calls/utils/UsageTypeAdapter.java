package cc.kave.rsse.calls.utils;

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import cc.kave.commons.assertions.Asserts;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.rsse.calls.usages.model.IDefinition;
import cc.kave.rsse.calls.usages.model.IUsage;
import cc.kave.rsse.calls.usages.model.IUsageSite;
import cc.kave.rsse.calls.usages.model.impl.Definition;
import cc.kave.rsse.calls.usages.model.impl.Definitions;
import cc.kave.rsse.calls.usages.model.impl.NoUsage;
import cc.kave.rsse.calls.usages.model.impl.Usage;
import cc.kave.rsse.calls.usages.model.impl.UsageSite;
import cc.kave.rsse.calls.usages.model.impl.UsageSites;

public class UsageTypeAdapter extends TypeAdapter<IUsage> {

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
	public void write(JsonWriter out, IUsage usage) throws IOException {
		if (usage instanceof NoUsage) {
			out.value("NoUsage");
		} else {
			writeQuery(out, usage);
		}
	}

	private void writeQuery(JsonWriter out, IUsage usage) throws IOException {
		out.beginObject();

		if (usage.getType() != null) {
			out.name(TYPE).value(usage.getType().getIdentifier());
		}
		if (usage.getClassContext() != null) {
			out.name(CLASS_CTX).value(usage.getClassContext().getIdentifier());
		}
		if (usage.getMethodContext() != null) {
			out.name(METHOD_CTX).value(usage.getMethodContext().getIdentifier());
		}
		if (usage.getDefinition() != null) {
			out.name(DEFINITION);
			writeDefinition(out, usage.getDefinition());
		}

		if (usage.getUsageSites() != null) {
			out.name(SITES);
			out.beginArray();
			for (IUsageSite m : usage.getUsageSites()) {
				writeCallSite(out, m);
			}
			out.endArray();
		}

		out.endObject();
	}

	@Override
	public IUsage read(JsonReader in) throws IOException {

		if (in.peek() == JsonToken.STRING) {
			String val = in.nextString();
			Asserts.assertEquals("NoUsage", val, "Invalid JSON. Expected 'NoUsage', but found '" + val + "'.");
			return new NoUsage();
		}

		Usage q = new Usage();

		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			// if (TYPE.equals(name)) {
			// q.setType(Names.newType(in.nextString()));
			// } else if (CLASS_CTX.equals(name)) {
			// q.setClassContext(Names.newType(in.nextString()));
			// } else if (METHOD_CTX.equals(name)) {
			// q.setMethodContext(Names.newMethod(in.nextString()));
			// } else if (DEFINITION.equals(name)) {
			// q.setDefinition(readDefinition(in));
			// } else if (SITES.equals(name)) {
			// q.accesses.addAll(readCallSites(in));
			// } else {
			// // skip value (most likely $type key from .net serialization)
			// in.nextString();
			// }
		}
		in.endObject();
		return q;
	}

	private void writeDefinition(JsonWriter out, IDefinition def) throws IOException {
		out.beginObject();
		if (def.getKind() != null) {
			out.name(DEF_KIND).value(def.getKind().toString());
		}
		// if (def.getField() != null) {
		// out.name(DEF_FIELD).value(def.getField().getIdentifier());
		// }
		// if (def.getProperty() != null) {
		// out.name(DEF_PROPERTY).value(def.getProperty().getIdentifier());
		// }
		// if (def.getMethod() != null) {
		// out.name(DEF_METHOD).value(def.getMethod().getIdentifier());
		// }
		boolean isNonDefaultArgIndex = def.getArgIndex() != Definitions.definedByUnknown().getArgIndex();
		if (isNonDefaultArgIndex) {
			out.name(DEF_ARG).value(def.getArgIndex());
		}
		out.endObject();
	}

	private Definition readDefinition(JsonReader in) throws IOException {
		Definition def = Definitions.definedByUnknown();
		// def.setKind(null);
		//
		// in.beginObject();
		// while (in.hasNext()) {
		// String name = in.nextName();
		// if (DEF_KIND.equals(name)) {
		// def.setKind(DefinitionSiteKind.valueOf(in.nextString()));
		// } else if (DEF_ARG.equals(name)) {
		// def.setArgIndex(in.nextInt());
		// } else if (DEF_FIELD.equals(name)) {
		// def.setField(Names.newField(in.nextString()));
		// } else if (DEF_METHOD.equals(name)) {
		// def.setMethod(Names.newMethod(in.nextString()));
		// } else if (DEF_PROPERTY.equals(name)) {
		// def.setProperty(Names.newProperty(in.nextString()));
		// }
		// }
		// in.endObject();

		return def;
	}

	private Set<UsageSite> readCallSites(JsonReader in) throws IOException {
		Set<UsageSite> sites = Sets.newLinkedHashSet();
		in.beginArray();
		while (in.hasNext()) {
			sites.add(readCallSite(in));
		}
		in.endArray();
		return sites;
	}

	private void writeCallSite(JsonWriter out, IUsageSite site) throws IOException {
		out.beginObject();
		if (site.getType() != null) {
			out.name(CS_KIND).value(site.getType().toString());
		}
		IMemberName member = site.getMember(IMemberName.class);
		if (member != null) {
			out.name(CS_CALL).value(member.getIdentifier());
		}
		boolean isNonDefaultArgIndex = site.getArgIndex() != UsageSites.call("LT.m()V").getArgIndex();
		if (isNonDefaultArgIndex) {
			out.name(CS_ARG).value(site.getArgIndex());
		}
		out.endObject();
	}

	private UsageSite readCallSite(JsonReader in) throws IOException {
		UsageSite site = UsageSites.call("LT.m()V");
		// site.setKind(null);
		// site.setMethod(null);
		//
		// in.beginObject();
		// while (in.hasNext()) {
		// String name = in.nextName();
		// if (CS_ARG.equals(name)) {
		// site.setArgIndex(in.nextInt());
		// } else if (CS_CALL.equals(name)) {
		// site.setMethod(Names.newMethod(in.nextString()));
		// } else if (CS_KIND.equals(name)) {
		// site.setKind(UsageAccessType.valueOf(in.nextString()));
		// }
		// }
		// in.endObject();
		return site;
	}
}