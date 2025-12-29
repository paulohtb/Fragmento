package com.pgalaxyp.fragmento.tiers.server.http;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

public final class TierRedeemApiClient {

    private static final String BASE_URL =
            "https://redeem-a3pryean7a-uc.a.run.app";

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient client;

    public TierRedeemApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public int redeem(UUID playerId, String code) {
        if (playerId == null || code == null || code.isBlank()) {
            return 0;
        }

        try {
            String qUuid = URLEncoder.encode(playerId.toString(), StandardCharsets.UTF_8);
            String qCode = URLEncoder.encode(code, StandardCharsets.UTF_8);

            URI uri = URI.create(
                    BASE_URL + "/redeem?uuid=" + qUuid + "&code=" + qCode
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(TIMEOUT)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return 0;
            }

            return parse(response.body());
        } catch (Exception e) {
            return 0;
        }
    }

    private static int parse(String body) {
        if (body == null) return 0;
        try {
            return Math.max(0, Integer.parseInt(body.trim()));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}