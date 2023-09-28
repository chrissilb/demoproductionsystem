package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.escframework.components.annotations.Service;

@Service(type=PLRole.class, instantiable=false)
public class PLRoleImpl {
	
	private ProductionLine productionLine;
	
	public PLRoleImpl() {
	}
	
	public void init(ProductionLine productionLine) {
		this.productionLine = productionLine;
	}
	
	public ProductionLine getProductionLine() {
		return productionLine;
	}

	public void registerProductionSystem(ProductionSystem ps) {
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
