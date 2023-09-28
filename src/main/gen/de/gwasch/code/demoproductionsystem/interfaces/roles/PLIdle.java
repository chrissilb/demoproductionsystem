package de.gwasch.code.demoproductionsystem.interfaces.roles;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;

public interface PLIdle extends PLRole {

    void registerProductionSystem(ProductionSystem ps);

    void registerProductionLine(ProductionLine pl);

    void unregisterProductionLine(ProductionLine pl);

    void destroy();

    void stop();

    void tick();

    PLRoleEnum getPLRoleEnum();
}
