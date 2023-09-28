package de.gwasch.code.demoproductionsystem.models;

import java.util.ArrayList;
import java.util.List;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;
import de.gwasch.code.escframework.utils.logging.Filters;

public class Model implements Cloneable {
	
	public Configuration config;
	public Filters logFilters;
	
	public ProductionSystem productionSystem;
	public List<ProductionLine> productionLines;
	
	public Model() {
		config = new Configuration();
		logFilters = new Filters();
		productionSystem = null;
		productionLines = new ArrayList<>();
	}
	
	public Model clone() {
		Model model = new Model();
		model.config = config.clone();
		model.logFilters = logFilters.clone();
		model.productionSystem = InstanceAllocator.create(ProductionSystem.class);
		model.productionSystem.init(productionSystem.getName(), productionSystem.getVolume());
		model.productionSystem.setConfig(model.config);
		model.productionLines = new ArrayList<ProductionLine>();
		for (ProductionLine pl : productionLines) {
			ProductionLine plclone = InstanceAllocator.create(ProductionLine.class);
			plclone.init(pl.getName(), pl.getCapacity());
			plclone.setProductionSystem(model.productionSystem);
			model.productionLines.add(plclone);
		}
		return model;
	}
	
	public boolean hasChanged(Model cmp) {
		
		if (   !config.equals(cmp.config) 
			|| !logFilters.equals(cmp.logFilters)
			|| productionSystem.hasChanged(cmp.productionSystem)) {
			return true;
		}
		
		if (productionLines.size() != cmp.productionLines.size()) {
			return true;
		}
			
		for (int i = 0; i < productionLines.size(); i++) {
			if (productionLines.get(i).hasChanged(cmp.productionLines.get(i))) {
				return true;
			}
		}
		
		return false;
	}
	
//	public boolean equals(Object obj) {
//		Model cmp = (Model)obj;
//		return config.equals(cmp.config) //&& logFilters.equals(cmp.logFilters)
//			&& productionSystem.equals(cmp.productionSystem)
//			&& productionLines.equals(cmp.productionLines);
//	}
}
