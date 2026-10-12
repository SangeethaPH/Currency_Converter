package com.example.currencyconverter.rates;

import com.example.currencyconverter.common.ExchangeRateProviderException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ExchangeRateClient {
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

    private final RestClient restClient;
    private final Map<String, CachedRates> cache = new ConcurrentHashMap<>();

    public ExchangeRateClient(RestClient exchangeRateRestClient) {
        this.restClient = exchangeRateRestClient;
    }

    public ExchangeRateResponse getRates(String requestedBaseCurrency) {
        String baseCurrency = requestedBaseCurrency.trim().toUpperCase(java.util.Locale.ROOT);
        CachedRates cached = cache.get(baseCurrency);
        if (cached != null && cached.expiresAt().isAfter(Instant.now())) {
            return cached.response();
        }

        ExchangeRateResponse response;
        try {
            response = restClient.get()
                    .uri("/{baseCurrency}", baseCurrency)
                    .retrieve()
                    .body(ExchangeRateResponse.class);
        } catch (RestClientException exception) {
            throw new ExchangeRateProviderException("The exchange-rate provider could not be reached.", exception);
        }

        if (response == null || !"success".equalsIgnoreCase(response.result())
                || response.rates() == null || response.rates().isEmpty()) {
            throw new ExchangeRateProviderException("The exchange-rate provider returned an invalid response.");
        }
        if (!baseCurrency.equalsIgnoreCase(response.baseCode())) {
            throw new ExchangeRateProviderException("The provider returned rates for an unexpected base currency.");
        }

        ExchangeRateResponse stableResponse = new ExchangeRateResponse(
                response.result(), response.baseCode(), response.timeLastUpdateUtc(), Map.copyOf(response.rates()));
        cache.put(baseCurrency, new CachedRates(stableResponse, Instant.now().plus(CACHE_DURATION)));
        return stableResponse;
    }

    private record CachedRates(ExchangeRateResponse response, Instant expiresAt) { }
}
