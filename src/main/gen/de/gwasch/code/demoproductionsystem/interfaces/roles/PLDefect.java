package de.gwasch.code.demoproductionsystem.interfaces.roles;

public interface PLDefect extends PLRole {

    void stop();

    void repair();

    PLRoleEnum getPLRoleEnum();
}
