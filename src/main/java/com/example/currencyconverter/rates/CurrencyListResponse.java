package com.example.currencyconverter.rates;

import java.util.List;

public record CurrencyListResponse(String baseCurrency, List<String> currencies, String lastUpdatedUtc) { }
