package de.gwasch.code.demoproductionsystem.interfaces.roles;

public interface PSDefect extends PSRole {

    void repair();

    void stop();

    PSRoleEnum getPSRoleEnum();
}
