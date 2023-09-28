package de.gwasch.code.demoproductionsystem.interfaces.agents;

import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRole;
import java.util.Set;

public interface ProductionLine extends ProductionAgent, ProductionLineState, PLRole {

    void init(String name, int capacity);

    ProductionLineState getState();

    void setState(ProductionLineState state);

    void setRole(Class<? extends PLRole> roleClass);

    void tick();

    ProductionSystem getProductionSystem();

    void setProductionSystem(ProductionSystem ps);

    void clearProductionLines();

    Set<ProductionLine> getProductionLines();

    boolean hasProductionLine(ProductionLine pl);

    void putProductionLine(ProductionLine pl);

    void removeProductionLine(ProductionLine pl);
}
