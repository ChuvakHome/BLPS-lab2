package ru.itmo.se.bl.lab2.exception;

public class TravelNotFoundException extends Exception {
    public TravelNotFoundException(int id) {
        super("Travel with id: " + id + " cannot be found");
    }
}
