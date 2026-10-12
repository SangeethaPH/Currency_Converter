package com.example.currencyconverter.common;

import java.time.Instant;

public record ApiError(String error, Instant timestamp) { }
