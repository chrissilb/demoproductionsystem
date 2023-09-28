package de.gwasch.code.demoproductionsystem.roles;

import java.util.HashSet;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSDefect;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSDistribute;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.demoproductionsystem.models.PLEntry;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=PSDistribute.class, inherits=PSRole.class)
public class PSDistributeImpl {

	@Thiz
	private PSDistribute thiz;

	public void changeVolume() {		
		for (PLEntry entry : thiz.getProductionSystem().getPLEntries()) {
			entry.getProductionLine().registerProductionSystem(thiz.getProductionSystem());
		}
	}
	
	public void registerProductionLine(ProductionLine productionLine) {
		
//		System.out.println("PS: register " + productionline.getName());
		
		if (!thiz.getProductionSystem().hasProductionLine(productionLine)) {

			for (PLEntry entry : thiz.getProductionSystem().getPLEntries()) {				
				entry.getProductionLine().registerProductionLine(productionLine);
				productionLine.registerProductionLine(entry.getProductionLine());
			}
		}
		//todo, equals auf basis von productionline nicht auf state-basis
		else if (!thiz.getProductionSystem().getProductionLine(productionLine).getState().equals(productionLine.getState())) {
			
			for (PLEntry entry : thiz.getProductionSystem().getPLEntries()) {
				entry.getProductionLine().registerProductionLine(productionLine);
			}
		}

		thiz.getProductionSystem().putProductionLine(productionLine);
	}

	
	
	public void unregisterProductionLine(ProductionLine productionline) {

		if (thiz.getProductionSystem().hasProductionLine(productionline)) {

			thiz.getProductionSystem().removeProductionLine(productionline);

			for (PLEntry entry : thiz.getProductionSystem().getPLEntries()) {
				entry.getProductionLine().unregisterProductionLine(productionline);
			}
		}
	}
	
	public void destroy() {
		thiz.getProductionSystem().clearProductionLines();
		thiz.getProductionSystem().setRole(PSDefect.class);
	}
	
	
	
	public void stop() {
		thiz.getProductionSystem().clearProductionLines();
		thiz.getProductionSystem().setRole(PSInactive.class);
	}
	
	
	
	public void tick() {
		HashSet<ProductionLine> obsoletes = new HashSet<ProductionLine>();

		for (PLEntry entry : thiz.getProductionSystem().getPLEntries()) {
			if (!thiz.getProductionSystem().isRegistrationObsolete(entry)) continue;
			
			obsoletes.add(entry.getProductionLine());
		}
		
		for (ProductionLine pl : obsoletes) {
			thiz.getProductionSystem().unregisterProductionLine(pl);
			System.out.println("-------------------------------");
			System.out.println(pl.getName() + " IS DEFECT!");
			System.out.println("-------------------------------");
		}
	}
}
