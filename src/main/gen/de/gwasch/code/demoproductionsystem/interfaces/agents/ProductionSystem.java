package de.gwasch.code.demoproductionsystem.interfaces.agents;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRole;
import de.gwasch.code.demoproductionsystem.models.Configuration;
import de.gwasch.code.demoproductionsystem.models.PLEntry;
import java.util.Collection;

public interface ProductionSystem extends ProductionAgent, ProductionSystemState, PSRole {

    void init(String name, int volume);

    Configuration getConfig();

    void setConfig(Configuration config);

    ProductionSystemState getState();

    void setState(ProductionSystemState state);

    void setRole(Class<? extends PSRole> roleClass);

    void tick();

    void clearProductionLines();

    void putProductionLine(ProductionLine pl);

    void removeProductionLine(ProductionLine pl);

    ProductionLine getProductionLine(ProductionLine pl);

    boolean hasProductionLine(ProductionLine pl);

    Collection<PLEntry> getPLEntries();

    boolean isRegistrationObsolete(PLEntry entry);
}
