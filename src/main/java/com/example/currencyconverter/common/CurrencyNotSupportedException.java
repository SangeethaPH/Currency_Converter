package com.example.currencyconverter.common;

public class CurrencyNotSupportedException extends RuntimeException {
    public CurrencyNotSupportedException(String currencyCode) {
        super("Currency code is not supported: " + currencyCode);
    }
}
