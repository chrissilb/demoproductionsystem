package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PLDefect;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLIdle;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=PLDefect.class, inherits=PLRole.class)
public class PLDefectImpl {

	@Thiz
	private PLDefect thiz;

	public void stop() {
		thiz.getProductionLine().setRole(PLInactive.class);
	}
	
	public void repair() {
		thiz.getProductionLine().setRole(PLIdle.class);
		thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
	}
}
