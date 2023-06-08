package ru.itmo.se.bl.lab2.exception;

import ru.itmo.se.bl.lab2.model.PaymentResult;

public class ConnectionUnestablishedException extends PaymentException {
    public ConnectionUnestablishedException() {
        super(PaymentResult.CONNECTION_UNESTABLISHED_PROBLEM);
    }
}
