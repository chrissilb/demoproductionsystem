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


@Service(type=PLIdle.class, inherits=PLRole.class)
public class PLIdleImpl {

	@Thiz
	private PLIdle thiz;

	public void registerProductionSystem(ProductionSystem ps) {
		thiz.getProductionLine().setProductionSystem(ps);
		thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
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
		
		int potentialcapacity = thiz.getProductionLine().getCapacity();

		for (ProductionLine pl : thiz.getProductionLine().getProductionLines()) {
			potentialcapacity += pl.getCapacity();
		}

		if (potentialcapacity < ps.getVolume()) {
			thiz.getProductionLine().setRole(PLWorking.class);
			printWorkStart();
		} 
		else {
			int curcapacity = 0;
			int curcount = 0;
			
			Distributor dr = new Distributor();
			dr.addEntry(thiz.getProductionLine().getCapacity());
			int possamecap = 0;

			for (Object obj : thiz.getProductionLine().getProductionLines()) {
				ProductionLine pl = (ProductionLine)obj;
				
				if (pl.getPLRoleEnum() == PLRoleEnum.PLWorking) {
					curcapacity += pl.getCapacity();
					curcount++;
				}
				
				dr.addEntry(pl.getCapacity());
				
				if (thiz.getProductionLine().getCapacity() == pl.getCapacity() && thiz.getProductionLine().compareTo(pl) > 0) {
						possamecap++;
				}
			}

			Distribution dn = dr.distribute(ps.getVolume());

			if (dn.countEntries(thiz.getProductionLine().getCapacity()) > possamecap) {
				
				int curtolerance = curcapacity - ps.getVolume();
				
				if (curtolerance < 0 || dn.getOptTolerance() < curtolerance || dn.getOptCount() < curcount) {					
					thiz.getProductionLine().setRole(PLWorking.class);
					printWorkStart();
				}
			}
		}
		
		thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
	}


	private void printWorkStart() {
		
		int workingcapacity = thiz.getProductionLine().getCapacity();

		for (ProductionLine pl : thiz.getProductionLine().getProductionLines()) {
			if (pl.getPLRoleEnum() != PLRoleEnum.PLWorking) continue;
			workingcapacity += pl.getCapacity();
		}
		
		System.out.println("-------------------------------");
		System.out.println(thiz.getProductionLine().getName() + " GOES WORKING!");
		System.out.println("NEW PRODUCTION CAPACITY: " + workingcapacity);
		System.out.println("-------------------------------");
	}
}
