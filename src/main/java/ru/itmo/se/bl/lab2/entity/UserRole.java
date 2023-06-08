package ru.itmo.se.bl.lab2.entity;

public enum UserRole {
    USER,
    ADMIN
    ;

    public String getAsAuthority() {
        return "ROLE_" + name();
    }
}
