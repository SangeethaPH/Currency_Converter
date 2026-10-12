package com.example.currencyconverter.rates;

import com.example.currencyconverter.common.CurrencyNotSupportedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
    private final ExchangeRateClient exchangeRateClient;

    public ExchangeRateService(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    public RateListResponse getRates(String requestedBaseCurrency) {
        String baseCurrency = normalize(requestedBaseCurrency);
        ExchangeRateResponse response = exchangeRateClient.getRates(baseCurrency);
        return new RateListResponse(baseCurrency, response.timeLastUpdateUtc(), response.rates());
    }

    public CurrencyListResponse getSupportedCurrencies() {
        ExchangeRateResponse response = exchangeRateClient.getRates("USD");
        List<String> currencies = response.rates().keySet().stream().sorted().toList();
        return new CurrencyListResponse("USD", currencies, response.timeLastUpdateUtc());
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        String from = normalize(fromCurrency);
        String to = normalize(toCurrency);
        ExchangeRateResponse response = exchangeRateClient.getRates(from);
        BigDecimal rate = response.rates().get(to);
        if (rate == null) {
            throw new CurrencyNotSupportedException(to);
        }
        return rate;
    }

    private String normalize(String currencyCode) {
        String code = currencyCode.trim().toUpperCase(Locale.ROOT);
        if (!code.matches("[A-Z]{3}")) {
            throw new CurrencyNotSupportedException(code);
        }
        return code;
    }
}
