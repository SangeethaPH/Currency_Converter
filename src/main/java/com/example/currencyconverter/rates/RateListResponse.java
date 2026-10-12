package com.example.currencyconverter.rates;

import java.math.BigDecimal;
import java.util.Map;

public record RateListResponse(String baseCurrency, String lastUpdatedUtc, Map<String, BigDecimal> rates) { }
