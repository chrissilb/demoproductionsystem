package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.escframework.components.annotations.Service;

@Service(type=PSRole.class, instantiable=false)
public class PSRoleImpl {

	private ProductionSystem productionSystem;
	
	public void init(ProductionSystem productionSystem) {
		this.productionSystem = productionSystem;
	}
	
	public ProductionSystem getProductionSystem() {
		return productionSystem;
	}
	
	public void changeVolume() {		
	}
	
	public void registerProductionLine(ProductionLine pl) {
	}

	public void unregisterProductionLine(ProductionLine pl) {
	}
	
	public void destroy() {
	}
	
	public void repair() {
	}
	
	public void start() {
	}
	
	public void stop() {
	}
	
	public void tick() {
//		System.out.println("ABSTRACT.tick()");
	}
}
