package com.example.currencyconverter.rates;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateResponse(
        String result,
        @JsonProperty("base_code") String baseCode,
        @JsonProperty("time_last_update_utc") String timeLastUpdateUtc,
        Map<String, BigDecimal> rates) { }
