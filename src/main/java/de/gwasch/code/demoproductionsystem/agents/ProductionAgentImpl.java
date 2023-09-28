package de.gwasch.code.demoproductionsystem.agents;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionAgent;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionState;
import de.gwasch.code.escframework.components.annotations.Expansion;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;
import de.gwasch.code.escframework.components.annotations.Tick;

//todo, als agent-framework bereitstellen?

@Service(type=ProductionAgent.class, instantiable=false)
public class ProductionAgentImpl implements Comparable<ProductionAgent> {

	private static int lastId = 1;
	
	@Thiz
	private ProductionAgent thiz;

	@Expansion
	private ProductionState state;
	

	
	public void init(ProductionState state) {
		this.state = state;
		if (state.getId() == 0) {
			state.setId(lastId++);
		}
	}

	public ProductionState getState() {
		return state;
	}
	
	@Tick(activateMethod="startTick", deactivateMethod="stopTick", suspendMethod="suspendTick", resumeMethod="resumeTick")
	public void tick() {
	}
	
	public int compareTo(ProductionAgent cmp) {
		return thiz.getId() - cmp.getId();
	}
	
	public boolean hasChanged(ProductionAgent cmp) {
		boolean res = !state.equals(cmp.getState());
		return res;
	}
	
	public boolean equals(Object obj) {
		ProductionAgent cmp = (ProductionAgent)obj;
		
		boolean res = thiz.getId() == cmp.getId();
		return res;
	}
	
	public int hashCode() {
		return thiz.getId();
	}
	
	public String toString() {
		return state.toString();
	}
}
