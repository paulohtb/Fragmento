package com.pgalaxyp.fragmento.tiers.server.http;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;
import com.pgalaxyp.fragmento.tiers.common.model.TierStatus;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class TierApiClient {

    private static final String BASE_URL =
            "https://tier-a3pryean7a-uc.a.run.app";

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient client;

    public TierApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public CompletableFuture<Tier> fetchTier(UUID playerId) {
        if (playerId == null) {
            return CompletableFuture.completedFuture(Tier.inactive());
        }

        try {
            String qUuid = URLEncoder.encode(playerId.toString(), StandardCharsets.UTF_8);
            URI uri = URI.create(BASE_URL + "/tier?uuid=" + qUuid);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(TIMEOUT)
                    .GET()
                    .build();

            return client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(this::map)
                    .exceptionally(e -> Tier.inactive());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Tier.inactive());
        }
    }

    private Tier map(HttpResponse<String> response) {
        if (response == null || response.statusCode() != 200) {
            return Tier.inactive();
        }

        String body = response.body();
        if (body == null || body.isBlank()) {
            return Tier.inactive();
        }

        try {
            int level = Integer.parseInt(body.trim());
            if (level <= 0) {
                return Tier.inactive();
            }
            return new Tier(TierLevel.of(level), TierStatus.ACTIVE);
        } catch (NumberFormatException e) {
            return Tier.inactive();
        }
    }
}