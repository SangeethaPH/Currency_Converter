package com.example.currencyconverter.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "api_clients")
public class ApiClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 64)
    private String apiKeyHash;

    @Column(nullable = false)
    private Instant createdAt;

    protected ApiClientEntity() { }

    public ApiClientEntity(String username, String apiKeyHash) {
        this.username = username;
        this.apiKeyHash = apiKeyHash;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getApiKeyHash() {
        return apiKeyHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
