package com.example.currencyconverter.conversion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record ConversionRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z]{3}") String fromCurrency,
        @NotBlank @Pattern(regexp = "[A-Za-z]{3}") String toCurrency,
        @NotNull @DecimalMin(value = "0.000001") @Digits(integer = 15, fraction = 6) BigDecimal amount) { }
