package ru.itmo.se.bl.lab2.exception;

import ru.itmo.se.bl.lab2.model.PaymentResult;

public class PaymentException extends Exception {
    private final PaymentResult paymentResult;

    public PaymentException(PaymentResult result) {
        super(result.getMessage());
        this.paymentResult = result;
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }
}
