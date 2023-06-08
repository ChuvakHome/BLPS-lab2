package ru.itmo.se.bl.lab2.exception;

import ru.itmo.se.bl.lab2.model.PaymentResult;

public class CardExpireException extends PaymentException {
    public CardExpireException() {
        super(PaymentResult.CARD_EXPIRED);
    }
}
