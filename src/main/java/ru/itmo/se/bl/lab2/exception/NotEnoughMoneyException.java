package ru.itmo.se.bl.lab2.exception;

import ru.itmo.se.bl.lab2.model.PaymentResult;

public class NotEnoughMoneyException extends PaymentException {
    public NotEnoughMoneyException() {
        super(PaymentResult.NOT_ENOUGH_MONEY);
    }
}
