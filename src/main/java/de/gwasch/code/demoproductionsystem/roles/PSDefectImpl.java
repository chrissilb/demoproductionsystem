package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PSDefect;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSDistribute;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;

@Service(type=PSDefect.class, inherits=PSRole.class)
public class PSDefectImpl {

	@Thiz
	private PSDefect thiz;

	public void repair() {
		thiz.getProductionSystem().setRole(PSDistribute.class);
	}
	
	public void stop() {
		thiz.getProductionSystem().setRole(PSInactive.class);
	}
}
