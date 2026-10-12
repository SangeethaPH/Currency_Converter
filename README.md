# Currency Converter API

A beginner-friendly REST API built with Spring Boot. It registers API clients, retrieves exchange rates, converts amounts, and saves each client's conversion history in H2.

## Requirements and run

- JDK 17 or newer
- Maven 3.6.3 or newer

From this folder, start the app:

```bash
mvn spring-boot:run
```

Run the unit tests with:

```bash
mvn test
```

The first run needs an internet connection so Maven can download dependencies and the app can request rates.

## Test from your browser

1. Start the app with `mvn spring-boot:run`.
2. Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
3. Expand `POST /api/v1/clients`, choose **Try it out**, and register a username. Copy the `apiKey` from the response.
4. Choose **Authorize** near the top of the page, paste the key into the `X-API-Key` field, and select **Authorize**.
5. Use **Try it out** on the currencies, rates, conversions, and history endpoints.

The client-registration endpoint is public; the other API endpoints require the key. Do not add `Bearer` before the key. Re-register after restarting the app because its H2 database is in memory.

## Try the API

Register a client. The generated API key is returned once, so copy it for the next requests:

```bash
curl -X POST http://localhost:8080/api/v1/clients \
  -H 'Content-Type: application/json' \
  -d '{"username":"sangeetha"}'
```

Use the returned key in the `X-API-Key` header. List currencies:

```bash
curl http://localhost:8080/api/v1/currencies \
  -H 'X-API-Key: YOUR_API_KEY'
```

Get all rates based on USD:

```bash
curl http://localhost:8080/api/v1/rates/USD \
  -H 'X-API-Key: YOUR_API_KEY'
```

Convert an amount:

```bash
curl -X POST http://localhost:8080/api/v1/conversions \
  -H 'Content-Type: application/json' \
  -H 'X-API-Key: YOUR_API_KEY' \
  -d '{"fromCurrency":"USD","toCurrency":"INR","amount":12.50}'
```

Read this client's most recent conversions (default 20; limit may be 1–100):

```bash
curl 'http://localhost:8080/api/v1/conversions?limit=20' \
  -H 'X-API-Key: YOUR_API_KEY'
```

Currency codes use three-letter ISO 4217 style codes. Amounts must be positive and support up to six fractional digits. Converted amounts are rounded to four decimal places for this learning project.

## Request flow

1. `ClientRegistrationController` asks `ApiClientService` to generate a random API key. The database stores only its SHA-256 hash; the raw key is returned once.
2. `ApiKeyAuthenticationFilter` hashes the incoming `X-API-Key`, finds its client, and sets the authenticated client for the request. `SecurityConfig` requires this key for rates, currencies, conversions, and history.
3. `ConversionController` passes the request and authenticated client to `CurrencyConversionService`.
4. `ExchangeRateClient` calls the external JSON API with Spring's `RestClient`. `ExchangeRateService` validates the currency codes and selects a rate.
5. `CurrencyConversionService` calculates the amount and saves the rate, result, timestamp, and owning client through Spring Data JPA.

## Data and exchange-rate notes

- Client keys and conversion history are stored in the H2 in-memory database. Data resets when the app stops.
- Rate responses are cached in memory for 24 hours. The selected no-key ExchangeRate-API open-access endpoint updates once per day and asks users to provide attribution. The provider link is included below and the response includes the update time.
- This is a learning/demo project. Do not use indicative public rates for financial settlement.

Rates provided by [ExchangeRate-API Open Access](https://www.exchangerate-api.com).

## Main packages

- `client`: client registration, API-key creation, and key lookup.
- `security`: stateless API-key authentication.
- `rates`: HTTP client, response mapping, supported currencies, and rate endpoints.
- `conversion`: conversion requests, calculations, persistence, history, and endpoints.
- `common`: API error responses and exception handling.
