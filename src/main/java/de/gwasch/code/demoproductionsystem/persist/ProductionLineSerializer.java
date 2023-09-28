package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;

public class ProductionLineSerializer extends StdSerializer<ProductionLine> {

	private static final long serialVersionUID = -4432733821730673782L;
	
	public ProductionLineSerializer() {
		this(null);
	}
	
	public ProductionLineSerializer(Class<ProductionLine> cls) {
		super(cls);
	}
	
	public void serialize(ProductionLine value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("capacity", value.getCapacity());
        gen.writeEndObject();
	}
}
