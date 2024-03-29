package ru.itmo.se.bl.lab2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentRequest {
    private CardInfo cardInfo;
    private int charge;
}
