package de.gwasch.code.demoproductionsystem.agents;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionState;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystemState;
import de.gwasch.code.escframework.components.annotations.Base;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=ProductionSystemState.class, inherits=ProductionState.class)
public class ProductionSystemStateImpl {

	@Thiz
	private ProductionSystemState thiz;
	
	@Base
	private ProductionState base;
	
	private int volume;

//	public ProductionSystemState clone() {
//		ProductionSystemState state = InstanceAllocator.create(ProductionSystemState.class);
//		state.setId(thiz.getId());
//		state.setName(thiz.getName());
//		state.setRole(getRole());
//		state.setVolume(getVolume());
//		return state;
//	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public boolean equals(Object obj) {
		ProductionSystemState cmp = (ProductionSystemState)obj;
		return base.equals(cmp) && volume == cmp.getVolume();
	}
	
	public String toString() {
		return base.toString() + ", " + volume;
	}
}
