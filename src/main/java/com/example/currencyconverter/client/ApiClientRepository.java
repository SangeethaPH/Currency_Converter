package com.example.currencyconverter.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiClientRepository extends JpaRepository<ApiClientEntity, Long> {
    boolean existsByUsernameIgnoreCase(String username);

    Optional<ApiClientEntity> findByApiKeyHash(String apiKeyHash);
}
