package de.gwasch.code.demoproductionsystem.interfaces.roles;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;

public interface PSDistribute extends PSRole {

    void changeVolume();

    void registerProductionLine(ProductionLine productionLine);

    void unregisterProductionLine(ProductionLine productionline);

    void destroy();

    void stop();

    void tick();

    PSRoleEnum getPSRoleEnum();
}
