package com.example.currencyconverter.rates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.currencyconverter.common.CurrencyNotSupportedException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExchangeRateServiceTest {
    @Test
    void listsCodesFromTheProviderResponse() {
        ExchangeRateClient client = mock(ExchangeRateClient.class);
        when(client.getRates("USD")).thenReturn(new ExchangeRateResponse(
                "success", "USD", "today", Map.of("INR", new BigDecimal("83.5"), "USD", BigDecimal.ONE)));

        CurrencyListResponse response = new ExchangeRateService(client).getSupportedCurrencies();

        assertEquals(java.util.List.of("INR", "USD"), response.currencies());
    }

    @Test
    void rejectsMalformedCurrencyCodes() {
        ExchangeRateService service = new ExchangeRateService(mock(ExchangeRateClient.class));

        assertThrows(CurrencyNotSupportedException.class, () -> service.getRates("US dollars"));
    }
}
