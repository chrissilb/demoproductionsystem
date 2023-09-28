package de.gwasch.code.demoproductionsystem.interfaces.roles;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;

public interface PSRole {

    void init(ProductionSystem productionSystem);

    ProductionSystem getProductionSystem();

    void changeVolume();

    void registerProductionLine(ProductionLine pl);

    void unregisterProductionLine(ProductionLine pl);

    void destroy();

    void repair();

    void start();

    void stop();

    void tick();

    PSRoleEnum getPSRoleEnum();
}
