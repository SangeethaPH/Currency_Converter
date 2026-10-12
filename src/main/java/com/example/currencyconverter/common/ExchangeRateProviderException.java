package com.example.currencyconverter.common;

public class ExchangeRateProviderException extends RuntimeException {
    public ExchangeRateProviderException(String message) {
        super(message);
    }

    public ExchangeRateProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
