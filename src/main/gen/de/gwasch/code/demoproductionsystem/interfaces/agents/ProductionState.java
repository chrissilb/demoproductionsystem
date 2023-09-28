package de.gwasch.code.demoproductionsystem.interfaces.agents;

public interface ProductionState {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    boolean equals(Object obj);

    String toString();
}
