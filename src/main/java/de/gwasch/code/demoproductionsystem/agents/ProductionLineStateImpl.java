package de.gwasch.code.demoproductionsystem.agents;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLineState;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionState;
import de.gwasch.code.escframework.components.annotations.Base;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=ProductionLineState.class, inherits=ProductionState.class)
public class ProductionLineStateImpl {
	
	@Thiz
	private ProductionLineState thiz;
	
	@Base
	private ProductionState base;
	
	private int capacity;


//	public ProductionLineState clone() {
//		ProductionLineState state = InstanceAllocator.create(ProductionLineState.class);
//		state.setId(thiz.getId());
//		state.setName(thiz.getName());
//		state.setRole(getRole());
//		state.setCapacity(getCapacity());
//		return state;
//	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public boolean equals(Object obj) {
		ProductionLineState cmp = (ProductionLineState)obj;

		return base.equals(cmp) && capacity == cmp.getCapacity();
	}
	
	public String toString() {
		return base.toString() + ", " + capacity;
	}
}
