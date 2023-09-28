package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.distribution.Distribution;
import de.gwasch.code.demoproductionsystem.distribution.Distributor;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLDefect;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLIdle;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRoleEnum;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLWorking;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=PLWorking.class, inherits=PLRole.class)
public class PLWorkingImpl {
	
	@Thiz
	private PLWorking thiz;
	
	public void registerProductionSystem(ProductionSystem ps) {
		thiz.getProductionLine().setProductionSystem(ps);
	}
	
	public void registerProductionLine(ProductionLine pl) {
		thiz.getProductionLine().putProductionLine(pl);
	}
	
	public void unregisterProductionLine(ProductionLine pl) {
		thiz.getProductionLine().removeProductionLine(pl);
	}
	
	
	public void destroy() {
		thiz.getProductionLine().clearProductionLines();
		thiz.getProductionLine().setRole(PLDefect.class);
	}
	
	public void stop() {
		thiz.getProductionLine().setRole(PLInactive.class);
		thiz.getProductionLine().getProductionSystem().unregisterProductionLine(thiz.getProductionLine());
		thiz.getProductionLine().clearProductionLines(); 
	}
	
	public void tick() {
		
		ProductionSystem ps = thiz.getProductionLine().getProductionSystem();
		if (ps == null) return;
		
		int curcapacity = 0; // current capacity without me...

		for (ProductionLine pl : thiz.getProductionLine().getProductionLines()) {
			if (pl.getPLRoleEnum() == PLRoleEnum.PLWorking) {
				curcapacity += pl.getCapacity();
			}
		}

		if (curcapacity >= ps.getVolume()) {

			Distributor dr = new Distributor();
			dr.addEntry(thiz.getProductionLine().getCapacity());
			int possamecap = 0;

			for (ProductionLine pl : thiz.getProductionLine().getProductionLines()) {
				
				dr.addEntry(pl.getCapacity());
				
				if (thiz.getProductionLine().getCapacity() == pl.getCapacity() && thiz.getProductionLine().compareTo(pl) > 0) {
						possamecap++;
				}
			}

			Distribution dn = dr.distribute(ps.getVolume());
			
			if (dn.countEntries(thiz.getProductionLine().getCapacity()) <= possamecap) {
				thiz.getProductionLine().setRole(PLIdle.class);
				printIdleStart();
			}
		}
		
		thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
	}
	
	
	private void printIdleStart() {
		
		int workingcapacity = 0;

		for (ProductionLine pl : thiz.getProductionLine().getProductionLines()) {
			if (pl.getPLRoleEnum() != PLRoleEnum.PLWorking) continue;
			workingcapacity += pl.getCapacity();
		}
		
		System.out.println("-------------------------------");
		System.out.println(thiz.getProductionLine().getName() + " GOES IDLE!");
		System.out.println("NEW PRODUCTION CAPACITY: " + workingcapacity);
		System.out.println("-------------------------------");
	}
}
