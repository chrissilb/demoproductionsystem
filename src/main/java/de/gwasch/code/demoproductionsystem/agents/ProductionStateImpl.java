package de.gwasch.code.demoproductionsystem.agents;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionState;
import de.gwasch.code.escframework.components.annotations.Service;

@Service(type=ProductionState.class)
public class ProductionStateImpl {

	private int id;
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object obj) {
		ProductionState state = (ProductionState)obj;

		return name.equals(state.getName());
	}
	
	public String toString() {
		return name;
	}
}
