package de.gwasch.code.demoproductionsystem.interfaces.agents;

public interface ProductionSystemState extends ProductionState {

    int getVolume();

    void setVolume(int volume);

    boolean equals(Object obj);

    String toString();
}
