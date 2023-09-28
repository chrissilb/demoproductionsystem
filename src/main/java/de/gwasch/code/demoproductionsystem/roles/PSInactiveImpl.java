package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PSDistribute;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;

@Service(type=PSInactive.class, inherits=PSRole.class)
public class PSInactiveImpl {

	@Thiz
	private PSInactive thiz;

	public void start() {
		thiz.getProductionSystem().setRole(PSDistribute.class);
	}
}
