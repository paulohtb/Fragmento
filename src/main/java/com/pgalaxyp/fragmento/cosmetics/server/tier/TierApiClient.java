package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.slf4j.Logger;

public final class TierApiClient {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String HEADER_CONTENT_TYPE = "Content" + (char) 45 + "Type";

    private final TierApiConfig config;
    private final HttpClient client;

    public TierApiClient(TierApiConfig config) {
        this.config = config;
        this.client = HttpClient.newBuilder().connectTimeout(timeout()).build();
        LOGGER.info("TierApiClient criado");
    }

    public CompletableFuture<Optional<CosmeticTier>> fetchTier(UUID playerId) {
        if (!config.isConfigured()) {
            LOGGER.warn("TierApiClient fetchTier ignorado config vazia");
            return CompletableFuture.completedFuture(Optional.empty());
        }
        URI uri = URI.create(config.baseUrl() + "/tier?uuid=" + playerId);
        HttpRequest.Builder b = HttpRequest.newBuilder().uri(uri).timeout(timeout()).GET();
        String secret = config.sharedSecret();
        if (!secret.isEmpty()) {
            b.header("XFragmentoSecret", secret);
        }
        HttpRequest req = b.build();
        LOGGER.info("TierApiClient fetchTier request uuid {}", playerId);

        CompletableFuture<HttpResponse<String>> sent = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        return sent.thenApply(new Function<HttpResponse<String>, Optional<CosmeticTier>>() {
            @Override
            public Optional<CosmeticTier> apply(HttpResponse<String> resp) {
                int code = resp.statusCode();
                String body = resp.body();
                LOGGER.info("TierApiClient fetchTier response code {} len {}", Integer.valueOf(code), Integer.valueOf(body != null ? body.length() : 0));
                if (code != 200) {
                    return Optional.empty();
                }
                CosmeticTier tier = parseTier(body);
                if (tier == null) {
                    return Optional.empty();
                }
                return Optional.of(tier);
            }
        }).exceptionally(new Function<Throwable, Optional<CosmeticTier>>() {
            @Override
            public Optional<CosmeticTier> apply(Throwable ex) {
                LOGGER.error("TierApiClient fetchTier erro", ex);
                return Optional.empty();
            }
        });
    }

    public CompletableFuture<Optional<CosmeticTier>> bindKey(UUID playerId, String key) {
        if (!config.isConfigured()) {
            LOGGER.warn("TierApiClient bindKey ignorado config vazia");
            return CompletableFuture.completedFuture(Optional.empty());
        }
        String k = key == null ? "" : key.trim();
        if (k.isEmpty()) {
            LOGGER.warn("TierApiClient bindKey ignorado key vazia");
            return CompletableFuture.completedFuture(Optional.empty());
        }
        URI uri = URI.create(config.baseUrl() + "/bind");
        String json = "{\"uuid\":\"" + playerId + "\",\"key\":\"" + escapeJson(k) + "\"}";
        HttpRequest.Builder b = HttpRequest.newBuilder().uri(uri).timeout(timeout()).POST(HttpRequest.BodyPublishers.ofString(json));
        b.header(HEADER_CONTENT_TYPE, "application/json");
        String secret = config.sharedSecret();
        if (!secret.isEmpty()) {
            b.header("XFragmentoSecret", secret);
        }
        HttpRequest req = b.build();
        LOGGER.info("TierApiClient bindKey request uuid {} keyLen {}", playerId, Integer.valueOf(k.length()));

        CompletableFuture<HttpResponse<String>> sent = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        return sent.thenApply(new Function<HttpResponse<String>, Optional<CosmeticTier>>() {
            @Override
            public Optional<CosmeticTier> apply(HttpResponse<String> resp) {
                int code = resp.statusCode();
                String body = resp.body();
                LOGGER.info("TierApiClient bindKey response code {} len {}", Integer.valueOf(code), Integer.valueOf(body != null ? body.length() : 0));
                if (code != 200) {
                    return Optional.empty();
                }
                CosmeticTier tier = parseTier(body);
                if (tier == null) {
                    return Optional.empty();
                }
                return Optional.of(tier);
            }
        }).exceptionally(new Function<Throwable, Optional<CosmeticTier>>() {
            @Override
            public Optional<CosmeticTier> apply(Throwable ex) {
                LOGGER.error("TierApiClient bindKey erro", ex);
                return Optional.empty();
            }
        });
    }

    private Duration timeout() {
        Duration t = config.timeout();
        if (t == null) {
            return Duration.ofSeconds(4L);
        }
        return t;
    }

    private static CosmeticTier parseTier(String body) {
        if (body == null) {
            return null;
        }
        String s = body.trim();
        int len = s.length();
        boolean found = false;
        long value = 0L;

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                found = true;
                value = (value * 10L) + (long) (c - '0');
                if (value > 1000L) {
                    break;
                }
            } else {
                if (found) break;
            }
        }

        if (!found) return null;

        int lvl;
        if (value > (long) Integer.MAX_VALUE) {
            lvl = Integer.MAX_VALUE;
        } else {
            lvl = (int) value;
        }

        CosmeticTier tier = CosmeticTier.fromLevel(lvl);
        LOGGER.info("TierApiClient parseTier {}", tier.name());
        return tier;
    }

    private static String escapeJson(String value) {
        int len = value.length();
        StringBuilder sb = new StringBuilder(len + 8);
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '"') {
                sb.append("\\\"");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}