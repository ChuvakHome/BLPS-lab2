package ru.itmo.se.bl.lab2.exception;

public class HotelNotFoundException extends Exception {
    public HotelNotFoundException(int id) {
        super("Hotel with id: " + id + " cannot be found");
    }
}
