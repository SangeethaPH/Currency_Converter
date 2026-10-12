package com.example.currencyconverter.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRegistrationController {
    private final ApiClientService apiClientService;

    public ClientRegistrationController(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientRegistrationResponse register(@Valid @RequestBody ClientRegistrationRequest request) {
        ApiClientService.RegisteredClient client = apiClientService.register(request.username());
        return new ClientRegistrationResponse(client.id(), client.username(), client.apiKey(), client.createdAt());
    }

    public record ClientRegistrationRequest(@NotBlank @Size(min = 3, max = 50) String username) { }

    public record ClientRegistrationResponse(Long id, String username, String apiKey, Instant createdAt) { }
}
