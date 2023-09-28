package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;

public class ProductionSystemDeserializer extends StdDeserializer<ProductionSystem> {

	private static final long serialVersionUID = -2942919786350484402L;
	
	public ProductionSystemDeserializer() {
		this(null);
	}
	
	public ProductionSystemDeserializer(Class<ProductionSystem> cls) {
		super(cls);
	}

	public ProductionSystem deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		
		ProductionSystem ps = InstanceAllocator.create(ProductionSystem.class);
		ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        
        String name = node.get("name").asText();
        int volume = node.get("volume").asInt();
        
        ps.init(name, volume);
        return ps;
	}

}
