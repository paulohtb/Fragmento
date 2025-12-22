package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.mojang.logging.LogUtils;
import java.time.Duration;
import java.util.Objects;
import org.slf4j.Logger;

public final class TierApiConfig {

    private static final Logger LOGGER = LogUtils.getLogger();

    private volatile String baseUrl;
    private volatile String sharedSecret;
    private volatile Duration timeout;

    public TierApiConfig() {
        baseUrl = "";
        sharedSecret = "";
        timeout = Duration.ofSeconds(4L);
        LOGGER.info("TierApiConfig criado");
    }

    public String baseUrl() {
        return baseUrl;
    }

    public String sharedSecret() {
        return sharedSecret;
    }

    public Duration timeout() {
        return timeout;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl").trim();
        LOGGER.info("TierApiConfig setBaseUrl len {}", Integer.valueOf(this.baseUrl.length()));
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = Objects.requireNonNull(sharedSecret, "sharedSecret").trim();
        LOGGER.info("TierApiConfig setSharedSecret len {}", Integer.valueOf(this.sharedSecret.length()));
    }

    public void setTimeout(Duration timeout) {
        this.timeout = Objects.requireNonNull(timeout, "timeout");
        LOGGER.info("TierApiConfig setTimeout {}", this.timeout);
    }

    public boolean isConfigured() {
        return !baseUrl.isEmpty();
    }
}