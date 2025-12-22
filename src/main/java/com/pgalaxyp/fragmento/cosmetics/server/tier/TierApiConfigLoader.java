package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

public final class TierApiConfigLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String FILE_NAME = "fragmento_cosmetics_tier.properties";

    private TierApiConfigLoader() {
    }

    public static void loadInto(TierApiConfig cfg) {
        Objects.requireNonNull(cfg, "cfg");
        Path dir = FMLPaths.CONFIGDIR.get();
        Path file = dir.resolve(FILE_NAME);

        if (!Files.exists(file)) {
            writeDefault(file);
        }

        Properties p = new Properties();
        try (BufferedReader r = Files.newBufferedReader(file)) {
            p.load(r);
        } catch (Exception ex) {
            LOGGER.error("TierApiConfigLoader erro lendo {}", file, ex);
            return;
        }

        String baseUrl = trimOrEmpty(p.getProperty("baseUrl"));
        String secret = trimOrEmpty(p.getProperty("sharedSecret"));
        String timeoutStr = trimOrEmpty(p.getProperty("timeoutSeconds"));

        if (!baseUrl.isEmpty()) {
            cfg.setBaseUrl(baseUrl);
        }
        if (!secret.isEmpty()) {
            cfg.setSharedSecret(secret);
        }
        if (!timeoutStr.isEmpty()) {
            try {
                long sec = Long.parseLong(timeoutStr);
                if (sec <= 0L) {
                    sec = 4L;
                }
                cfg.setTimeout(Duration.ofSeconds(sec));
            } catch (Exception ex) {
                LOGGER.warn("TierApiConfigLoader timeoutSeconds invalido {}", timeoutStr);
            }
        }

        LOGGER.info("TierApiConfigLoader loadInto ok baseUrlLen {} secretLen {}", Integer.valueOf(cfg.baseUrl().length()), Integer.valueOf(cfg.sharedSecret().length()));
    }

    private static void writeDefault(Path file) {
        try {
            Files.createDirectories(file.getParent());
            Properties p = new Properties();
            p.setProperty("baseUrl", "");
            p.setProperty("sharedSecret", "");
            p.setProperty("timeoutSeconds", "4");
            try (BufferedWriter w = Files.newBufferedWriter(file)) {
                p.store(w, "Fragmento Cosmetics Tier API");
            }
            LOGGER.info("TierApiConfigLoader default criado {}", file);
        } catch (Exception ex) {
            LOGGER.error("TierApiConfigLoader erro criando default {}", file, ex);
        }
    }

    private static String trimOrEmpty(String v) {
        if (v == null) {
            return "";
        }
        return v.trim();
    }
}