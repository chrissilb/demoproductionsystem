package de.gwasch.code.demoproductionsystem.interfaces.agents;

public interface ProductionAgent extends ProductionState, Comparable<ProductionAgent> {

    void init(ProductionState state);

    ProductionState getState();

    void tick();

    void stopTick();

    void startTick();

    void suspendTick();

    void resumeTick();

    int compareTo(ProductionAgent cmp);

    boolean hasChanged(ProductionAgent cmp);

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
