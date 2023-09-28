package de.gwasch.code.demoproductionsystem.agents;

import java.util.Set;
import java.util.TreeSet;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionAgent;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLineState;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.escframework.components.annotations.Base;
import de.gwasch.code.escframework.components.annotations.Expansion;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;
import de.gwasch.code.escframework.components.annotations.Tick;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;

@Service(type=ProductionLine.class, inherits=ProductionAgent.class)
public class ProductionLineImpl {

	@Thiz
	private ProductionLine thiz;

	@Base
	private ProductionAgent base;
	
	@Expansion
	private ProductionLineState state;

	@Expansion
	private PLRole role;
	
	private ProductionSystem productionSystem;
	private Set<ProductionLine> productionLines;
		
	public void init(String name, int capacity) {

		productionSystem = null;
		productionLines = new TreeSet<ProductionLine>();
		
		ProductionLineState state = InstanceAllocator.create(ProductionLineState.class);
		state.setName(name);
		state.setCapacity(capacity);
		thiz.setState(state);
		base.init(state);
		
		setRole(PLInactive.class);
	}
	
	public ProductionLineState getState() {
		return state;
	}
	
	public void setState(ProductionLineState state) {
		this.state = state;
	}
		
//	public boolean isWorking() {
//		return PLWorking.class.isAssignableFrom(role.getClass());
//	}

	//todo, in Oberklasse verschieben?
	public void setRole(Class<? extends PLRole> roleClass) {
		
		if (role != null && roleClass.isAssignableFrom(role.getClass())) {
			return;
		}
		
		this.role = InstanceAllocator.create(roleClass);
		this.role.init(thiz);
	}
	
	public void tick() {
		System.out.println("ProductionLine.tick(): " + thiz);
		role.tick();
	}
	
	public ProductionSystem getProductionSystem() {
		return productionSystem;
	}

	public void setProductionSystem(ProductionSystem ps) {
		productionSystem = ps;
	}

	public void clearProductionLines() {
		productionLines.clear();
	}

	public Set<ProductionLine> getProductionLines() {
		return productionLines;
	}
		
	public boolean hasProductionLine(ProductionLine pl) {
		return productionLines.contains(pl);
	}
	
	public void putProductionLine(ProductionLine pl) {
		if (equals(pl)) return;
		
		productionLines.add(pl);
	}
	
	public void removeProductionLine(ProductionLine pl) {
		productionLines.remove(pl);
	}
}
