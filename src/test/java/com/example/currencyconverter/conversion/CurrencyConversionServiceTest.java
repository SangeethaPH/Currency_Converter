package com.example.currencyconverter.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.currencyconverter.client.ApiClientEntity;
import com.example.currencyconverter.client.ApiClientRepository;
import com.example.currencyconverter.rates.ExchangeRateService;
import com.example.currencyconverter.security.ApiClientPrincipal;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CurrencyConversionServiceTest {
    @Test
    void convertsAndStoresTheRateUsedForTheConversion() {
        ExchangeRateService rates = mock(ExchangeRateService.class);
        ConversionHistoryRepository history = mock(ConversionHistoryRepository.class);
        ApiClientRepository clients = mock(ApiClientRepository.class);
        ApiClientEntity client = new ApiClientEntity("learner", "hashed-key");
        when(clients.findById(7L)).thenReturn(Optional.of(client));
        when(rates.getRate("USD", "INR")).thenReturn(new BigDecimal("83.2500"));
        when(history.save(any(ConversionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CurrencyConversionService service = new CurrencyConversionService(rates, history, clients);
        ConversionResponse result = service.convert(
                new ApiClientPrincipal(7L, "learner"),
                new ConversionRequest("usd", "inr", new BigDecimal("12.50")));

        assertEquals("USD", result.fromCurrency());
        assertEquals("INR", result.toCurrency());
        assertEquals(new BigDecimal("83.2500"), result.exchangeRate());
        assertEquals(new BigDecimal("1040.6250"), result.convertedAmount());
        verify(history).save(any(ConversionEntity.class));
    }
}
