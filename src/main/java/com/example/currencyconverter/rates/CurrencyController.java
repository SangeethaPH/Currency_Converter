package com.example.currencyconverter.rates;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "ApiKeyAuth")
public class CurrencyController {
    private final ExchangeRateService exchangeRateService;

    public CurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/currencies")
    public CurrencyListResponse getCurrencies() {
        return exchangeRateService.getSupportedCurrencies();
    }

    @GetMapping("/rates/{baseCurrency}")
    public RateListResponse getRates(@PathVariable String baseCurrency) {
        return exchangeRateService.getRates(baseCurrency);
    }
}
