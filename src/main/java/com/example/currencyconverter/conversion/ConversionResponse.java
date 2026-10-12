package com.example.currencyconverter.conversion;

import java.math.BigDecimal;
import java.time.Instant;

public record ConversionResponse(
        Long id,
        String fromCurrency,
        String toCurrency,
        BigDecimal amount,
        BigDecimal exchangeRate,
        BigDecimal convertedAmount,
        Instant createdAt) {
    static ConversionResponse from(ConversionEntity conversion) {
        return new ConversionResponse(
                conversion.getId(), conversion.getFromCurrency(), conversion.getToCurrency(),
                conversion.getAmount(), conversion.getExchangeRate(),
                conversion.getConvertedAmount(), conversion.getCreatedAt());
    }
}
