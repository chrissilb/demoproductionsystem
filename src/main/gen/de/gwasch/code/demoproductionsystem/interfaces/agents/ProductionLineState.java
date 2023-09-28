package de.gwasch.code.demoproductionsystem.interfaces.agents;

public interface ProductionLineState extends ProductionState {

    int getCapacity();

    void setCapacity(int capacity);

    boolean equals(Object obj);

    String toString();
}
