package com.example.currencyconverter.conversion;

import com.example.currencyconverter.security.ApiClientPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping("/api/v1/conversions")
@SecurityRequirement(name = "ApiKeyAuth")
public class ConversionController {
    private final CurrencyConversionService conversionService;

    public ConversionController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping
    public ConversionResponse convert(
            @AuthenticationPrincipal ApiClientPrincipal client,
            @Valid @RequestBody ConversionRequest request) {
        return conversionService.convert(client, request);
    }

    @GetMapping
    public List<ConversionResponse> getHistory(
            @AuthenticationPrincipal ApiClientPrincipal client,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
        return conversionService.getHistory(client, limit);
    }
}
