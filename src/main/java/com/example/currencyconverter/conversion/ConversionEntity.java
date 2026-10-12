package com.example.currencyconverter.conversion;

import com.example.currencyconverter.client.ApiClientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "conversion_history")
public class ConversionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "api_client_id", nullable = false)
    private ApiClientEntity apiClient;

    @Column(nullable = false, length = 3)
    private String fromCurrency;

    @Column(nullable = false, length = 3)
    private String toCurrency;

    @Column(nullable = false, precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(nullable = false, precision = 21, scale = 10)
    private BigDecimal exchangeRate;

    @Column(nullable = false, precision = 21, scale = 4)
    private BigDecimal convertedAmount;

    @Column(nullable = false)
    private Instant createdAt;

    protected ConversionEntity() { }

    public ConversionEntity(
            ApiClientEntity apiClient,
            String fromCurrency,
            String toCurrency,
            BigDecimal amount,
            BigDecimal exchangeRate,
            BigDecimal convertedAmount) {
        this.apiClient = apiClient;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public ApiClientEntity getApiClient() { return apiClient; }
    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public Instant getCreatedAt() { return createdAt; }
}
