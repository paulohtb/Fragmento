package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import com.pgalaxyp.fragmento.tiers.api.TierStatus;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class TierApiClient {

    private final HttpClient client;
    private final String baseUrl;
    private final String sharedSecret;
    private final Duration timeout;

    public TierApiClient(String baseUrl, String sharedSecret, Duration timeout) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.sharedSecret = Objects.requireNonNull(sharedSecret, "sharedSecret");
        this.timeout = Objects.requireNonNull(timeout, "timeout");
        this.client = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    public CompletableFuture<TierResponse> fetchTier(UUID accountId) {
        Objects.requireNonNull(accountId, "accountId");

        if (baseUrl.isEmpty()) {
            return CompletableFuture.completedFuture(TierResponse.error(TierError.NOT_CONFIGURED));
        }

        URI uri = buildUri(accountId);
        HttpRequest.Builder b = HttpRequest.newBuilder().uri(uri).timeout(timeout).GET();

        if (!sharedSecret.isEmpty()) {
            b.header("XFragmentoSecret", sharedSecret);
        }

        HttpRequest req = b.build();

        CompletableFuture<HttpResponse<String>> sent =
                client.sendAsync(req, HttpResponse.BodyHandlers.ofString());

        CompletableFuture<TierResponse> mapped = sent.thenApply(resp -> {
            if (resp == null || resp.statusCode() != 200) {
                return TierResponse.error(TierError.BAD_RESPONSE);
            }
            Tier tier = parseTier(resp.body());
            if (tier == null) {
                return TierResponse.error(TierError.INVALID_BODY);
            }
            return TierResponse.success(tier);
        });

        return mapped.exceptionally(ex -> TierResponse.error(TierError.NETWORK_ERROR));
    }

    public Tier fetchTierSync(UUID accountId) {
        Objects.requireNonNull(accountId, "accountId");

        try {
            TierResponse resp =
                    fetchTier(accountId).get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (resp != null && resp.success()) {
                return resp.tier().orElse(Tier.unknown());
            }
        } catch (Throwable ignored) {
        }

        return Tier.unknown();
    }

    private URI buildUri(UUID accountId) {
        String trimmed = baseUrl.trim();
        if (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        String q = URLEncoder.encode(accountId.toString(), StandardCharsets.UTF_8);
        return URI.create(trimmed + "/tier?uuid=" + q);
    }

    private static Tier parseTier(String body) {
        if (body == null) return null;

        String s = body.trim();
        int len = s.length();
        boolean found = false;
        long value = 0L;

        for (int i = 0; i < len; i++) {
            int d = Character.digit(s.charAt(i), 10);
            if (d >= 0) {
                found = true;
                value = (value * 10L) + d;
                if (value > 1000L) break;
            } else if (found) {
                break;
            }
        }

        if (!found) return null;

        int lvl = value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
        TierLevel tl = TierLevel.of(lvl);
        TierStatus st = TierStatus.ACTIVE;
        return new Tier(tl, st);
    }
}