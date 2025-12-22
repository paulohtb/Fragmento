package com.pgalaxyp.fragmento.cosmetics.internal.access;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticAccessPolicy {

    private static final Logger LOGGER = LogUtils.getLogger();

    public boolean canEquip(CosmeticTier playerTier, CosmeticDefinition definition) {
        Objects.requireNonNull(playerTier, "playerTier");
        Objects.requireNonNull(definition, "definition");
        boolean ok = playerTier.allows(definition.requiredTier());
        LOGGER.debug("CosmeticAccessPolicy canEquip, playerTier {}, required {}, cosmetic {}, ok {}", playerTier.name(), definition.requiredTier().name(), definition.id(), Boolean.valueOf(ok));
        return ok;
    }
}