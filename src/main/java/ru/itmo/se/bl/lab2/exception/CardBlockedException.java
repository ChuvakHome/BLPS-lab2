package ru.itmo.se.bl.lab2.exception;

import ru.itmo.se.bl.lab2.model.PaymentResult;

public class CardBlockedException extends PaymentException {
    public CardBlockedException() {
        super(PaymentResult.CARD_BLOCKED);
    }
}
