package com.example.currencyconverter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    RestClient exchangeRateRestClient(
            RestClient.Builder builder,
            @Value("${exchange-rate.api.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
