package de.gwasch.code.demoproductionsystem.persist;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;

public class ProductionSystemSerializer extends StdSerializer<ProductionSystem> {

	private static final long serialVersionUID = -8774570159139898973L;
	
	public ProductionSystemSerializer() {
		this(null);
	}
	
	public ProductionSystemSerializer(Class<ProductionSystem> cls) {
		super(cls);
	}
	
	public void serialize(ProductionSystem value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("volume", value.getVolume());
        gen.writeEndObject();
	}
}
