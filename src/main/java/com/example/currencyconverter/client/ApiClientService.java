package com.example.currencyconverter.client;

import com.example.currencyconverter.common.DuplicateUsernameException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiClientService {
    private final ApiClientRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiClientService(ApiClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RegisteredClient register(String requestedUsername) {
        String username = requestedUsername.trim();
        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateUsernameException("That username is already registered.");
        }

        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        String rawApiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
        ApiClientEntity client = repository.save(new ApiClientEntity(username, hashApiKey(rawApiKey)));
        return new RegisteredClient(client.getId(), client.getUsername(), rawApiKey, client.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public Optional<ApiClientEntity> findByApiKey(String rawApiKey) {
        return repository.findByApiKeyHash(hashApiKey(rawApiKey));
    }

    static String hashApiKey(String rawApiKey) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(rawApiKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    public record RegisteredClient(Long id, String username, String apiKey, java.time.Instant createdAt) { }
}
