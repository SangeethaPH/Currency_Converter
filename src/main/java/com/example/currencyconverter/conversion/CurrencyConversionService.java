package com.example.currencyconverter.conversion;

import com.example.currencyconverter.client.ApiClientEntity;
import com.example.currencyconverter.client.ApiClientRepository;
import com.example.currencyconverter.common.CurrencyNotSupportedException;
import com.example.currencyconverter.rates.ExchangeRateService;
import com.example.currencyconverter.security.ApiClientPrincipal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyConversionService {
    private final ExchangeRateService exchangeRateService;
    private final ConversionHistoryRepository historyRepository;
    private final ApiClientRepository apiClientRepository;

    public CurrencyConversionService(
            ExchangeRateService exchangeRateService,
            ConversionHistoryRepository historyRepository,
            ApiClientRepository apiClientRepository) {
        this.exchangeRateService = exchangeRateService;
        this.historyRepository = historyRepository;
        this.apiClientRepository = apiClientRepository;
    }

    @Transactional
    public ConversionResponse convert(ApiClientPrincipal principal, ConversionRequest request) {
        String fromCurrency = normalize(request.fromCurrency());
        String toCurrency = normalize(request.toCurrency());
        BigDecimal exchangeRate = exchangeRateService.getRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = request.amount().multiply(exchangeRate).setScale(4, RoundingMode.HALF_UP);

        ApiClientEntity client = apiClientRepository.findById(principal.id())
                .orElseThrow(() -> new IllegalStateException("Authenticated API client no longer exists."));
        ConversionEntity saved = historyRepository.save(new ConversionEntity(
                client, fromCurrency, toCurrency, request.amount(), exchangeRate, convertedAmount));
        return ConversionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public java.util.List<ConversionResponse> getHistory(ApiClientPrincipal principal, int limit) {
        return historyRepository.findAllByApiClient_IdOrderByCreatedAtDesc(
                        principal.id(), PageRequest.of(0, limit))
                .getContent().stream().map(ConversionResponse::from).toList();
    }

    private String normalize(String currencyCode) {
        String code = currencyCode.trim().toUpperCase(Locale.ROOT);
        if (!code.matches("[A-Z]{3}")) {
            throw new CurrencyNotSupportedException(code);
        }
        return code;
    }
}
