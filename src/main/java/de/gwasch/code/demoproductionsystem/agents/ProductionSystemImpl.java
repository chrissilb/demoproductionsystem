package de.gwasch.code.demoproductionsystem.agents;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionAgent;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystemState;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.demoproductionsystem.models.Configuration;
import de.gwasch.code.demoproductionsystem.models.PLEntry;
import de.gwasch.code.escframework.components.annotations.Base;
import de.gwasch.code.escframework.components.annotations.Expansion;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;
import de.gwasch.code.escframework.components.annotations.Tick;
import de.gwasch.code.escframework.components.utils.InstanceAllocator;


@Service(type=ProductionSystem.class, inherits=ProductionAgent.class)
public class ProductionSystemImpl {
	
	@Thiz
	private ProductionSystem thiz;

	@Base
	private ProductionAgent base;
	
	@Expansion
	private ProductionSystemState state;
	
	@Expansion
	private PSRole role;
	
	private Configuration config;
	private Map<Integer, PLEntry> productionLines;
	
	public void init(String name, int volume) {

		this.config = null;
		
		productionLines = new TreeMap<Integer, PLEntry>();
		
		ProductionSystemState state = InstanceAllocator.create(ProductionSystemState.class);
		state.setName(name);
		state.setVolume(volume);
		thiz.setState(state);
		base.init(state);
		
		setRole(PSInactive.class);
	}
	
//	public ProductionSystem clone() {
//		ProductionSystemImpl clone = InstanceAllocator.create(ProductionSystem.class).getProductionSystemImpl();
//		clone.state
//		clone.config = config;
//		clone.productionLines = productionLines;
//		
//		return clone;
//	}
	
	public Configuration getConfig() {
		return config;
	}
	
	public void setConfig(Configuration config) {
		this.config = config;
	}
	
	public ProductionSystemState getState() {
		return state;
	}
	
	public void setState(ProductionSystemState state) {
		this.state = state;
	}
	
	public void setRole(Class<? extends PSRole> roleClass) {
		
		if (role != null && roleClass.isAssignableFrom(role.getClass())) {
			return;
		}
		
		this.role = InstanceAllocator.create(roleClass);
		this.role.init(thiz);
	}
	
	public void tick() {
		System.out.println("ProductionSystem.tick(): " + thiz);
		role.tick();
	}
	
	public void clearProductionLines() {
		productionLines.clear();
	}

	public void putProductionLine(ProductionLine pl) {
		
		PLEntry entry = new PLEntry(pl, System.currentTimeMillis());
		
		productionLines.put(pl.getId(), entry);
	}

	public void removeProductionLine(ProductionLine pl) {
		productionLines.remove(pl.getId());
	}

	public ProductionLine getProductionLine(ProductionLine pl) {
		return productionLines.get(pl.getId()).getProductionLine();
	}
	
	public boolean hasProductionLine(ProductionLine pl) {
		return productionLines.containsKey(pl.getId());
	}

	public Collection<PLEntry> getPLEntries() {
		return productionLines.values();
	}

	public boolean isRegistrationObsolete(PLEntry entry) {
		
		long timestamp = entry.getTimeStamp();
		long age = System.currentTimeMillis() - timestamp;
		long maxage = (long)(config.getAvgTickPause() * config.getTimeoutFactor());
		boolean isObsolete = age > maxage;
		return isObsolete;
	}
}
