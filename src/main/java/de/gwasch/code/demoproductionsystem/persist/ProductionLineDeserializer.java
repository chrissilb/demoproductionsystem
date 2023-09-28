package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;

public class ProductionLineDeserializer extends StdDeserializer<ProductionLine> {

	private static final long serialVersionUID = -5897815145067582197L;

	public ProductionLineDeserializer() {
		this(null);
	}
	
	public ProductionLineDeserializer(Class<ProductionLine> cls) {
		super(cls);
	}

	public ProductionLine deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		
		ProductionLine pl = InstanceAllocator.create(ProductionLine.class);
		ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        
        String name = node.get("name").asText();
        int capacity = node.get("capacity").asInt();
        
        pl.init(name, capacity);
        return pl;
	}

}
