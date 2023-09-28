package de.gwasch.code.demoproductionsystem.interfaces.roles;

public interface PSInactive extends PSRole {

    void start();

    PSRoleEnum getPSRoleEnum();
}
