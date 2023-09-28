package de.gwasch.code.demoproductionsystem.persist;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.models.Model;
import de.gwasch.code.escframework.utils.logging.Filters;

public class FilePersistManager implements PersistManager {
	private File file;
	
	public FilePersistManager(File file) {
		this.file = file;
	}
	
	public String getResourceName() {
		return file.getName();
	}
	
	public Model load() throws IOException {
		
		ObjectMapper jsonMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("FiltersDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(Filters.class, new FiltersDeserializer());
		jsonMapper.registerModule(module);
		module = new SimpleModule("ProductionSystemDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(ProductionSystem.class, new ProductionSystemDeserializer());
		jsonMapper.registerModule(module);
		module = new SimpleModule("ProductionLineDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(ProductionLine.class, new ProductionLineDeserializer());
		jsonMapper.registerModule(module);
		
		Model model = jsonMapper.readValue(file, Model.class);
		model.productionSystem.setConfig(model.config);
		
		for (ProductionLine pl : model.productionLines) {
			pl.setProductionSystem(model.productionSystem);
		}
		
		return model;
	}
	
	public void save(Model model) throws IOException {
        
		ObjectMapper jsonMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("ProductionSystemSerializer", new Version(1, 0, 0, null, null, null));
		module.addSerializer(ProductionSystem.class, new ProductionSystemSerializer());
		jsonMapper.registerModule(module);
		module = new SimpleModule("ProductionLineSerializer", new Version(1, 0, 0, null, null, null));
		module.addSerializer(ProductionLine.class, new ProductionLineSerializer());
		jsonMapper.registerModule(module);
		
		jsonMapper.writeValue(file, model);   
	}
}
