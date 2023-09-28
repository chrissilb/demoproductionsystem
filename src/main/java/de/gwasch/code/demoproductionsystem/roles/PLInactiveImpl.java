package de.gwasch.code.demoproductionsystem.roles;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PLIdle;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLInactive;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import de.gwasch.code.escframework.components.annotations.Service;
import de.gwasch.code.escframework.components.annotations.Thiz;


@Service(type=PLInactive.class, inherits=PLRole.class)
public class PLInactiveImpl {	

	@Thiz
	private PLInactive thiz;

	public void start() {
		thiz.getProductionLine().setRole(PLIdle.class);
		thiz.getProductionLine().getProductionSystem().registerProductionLine(thiz.getProductionLine());
	}
}
