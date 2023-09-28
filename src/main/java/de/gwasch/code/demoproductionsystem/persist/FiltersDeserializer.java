package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.gwasch.code.escframework.utils.logging.Filters;

public class FiltersDeserializer extends StdDeserializer<Filters> {

	private static final long serialVersionUID = -2942919786350484402L;
	
	public FiltersDeserializer() {
		this(null);
	}
	
	public FiltersDeserializer(Class<Filters> cls) {
		super(cls);
	}

	public Filters deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		
		Filters filters = new Filters();
		ObjectCodec codec = parser.getCodec();
        ArrayNode node = (ArrayNode)codec.readTree(parser);
        
        for (JsonNode element : node) {
        	filters.add(element.get("pattern").asText(), element.get("allow").asBoolean(), element.get("deactivated").asBoolean());
        }

        return filters;
	}

}
