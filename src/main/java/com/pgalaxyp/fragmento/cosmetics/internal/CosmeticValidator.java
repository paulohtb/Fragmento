package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticValidationResult;
import com.pgalaxyp.fragmento.cosmetics.internal.access.CosmeticAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.internal.access.PlayerTierResolver;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;

public final class CosmeticValidator {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CosmeticRegistry registry;
    private final PlayerTierResolver tierResolver;
    private final CosmeticAccessPolicy accessPolicy;

    public CosmeticValidator(CosmeticRegistry registry, PlayerTierResolver tierResolver, CosmeticAccessPolicy accessPolicy) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.tierResolver = Objects.requireNonNull(tierResolver, "tierResolver");
        this.accessPolicy = Objects.requireNonNull(accessPolicy, "accessPolicy");
        LOGGER.info("CosmeticValidator iniciado");
    }

    public CosmeticValidationResult validateBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        CosmeticDefinition def = registry.getDefinition(cosmeticId);
        if (def == null) {
            LOGGER.warn("validateBase fail, cosmetic inexistente, player {}, slot {}, cosmetic {}", playerId, slot.name(), cosmeticId);
            return CosmeticValidationResult.fail("cosmetic_missing");
        }

        if (def.slot() != slot) {
            LOGGER.warn("validateBase fail, slot mismatch, player {}, asked {}, def {}, cosmetic {}", playerId, slot.name(), def.slot().name(), cosmeticId);
            return CosmeticValidationResult.fail("slot_mismatch");
        }

        CosmeticTier playerTier = tierResolver.getTier(playerId);
        if (playerTier == null) {
            LOGGER.warn("validateBase fail, tier null, player {}", playerId);
            return CosmeticValidationResult.fail("tier_unavailable");
        }

        if (!accessPolicy.canEquip(playerTier, def)) {
            LOGGER.info("validateBase denied, player {}, tier {}, required {}, cosmetic {}", playerId, playerTier.name(), def.requiredTier().name(), cosmeticId);
            return CosmeticValidationResult.fail("tier_denied");
        }

        LOGGER.info("validateBase ok, player {}, slot {}, cosmetic {}", playerId, slot.name(), cosmeticId);
        return CosmeticValidationResult.OK;
    }

    public CosmeticValidationResult validateForced(CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        CosmeticDefinition def = registry.getDefinition(cosmeticId);
        if (def == null) {
            LOGGER.warn("validateForced fail, cosmetic inexistente, slot {}, cosmetic {}", slot.name(), cosmeticId);
            return CosmeticValidationResult.fail("cosmetic_missing");
        }

        if (def.slot() != slot) {
            LOGGER.warn("validateForced fail, slot mismatch, asked {}, def {}, cosmetic {}", slot.name(), def.slot().name(), cosmeticId);
            return CosmeticValidationResult.fail("slot_mismatch");
        }

        LOGGER.info("validateForced ok, slot {}, cosmetic {}", slot.name(), cosmeticId);
        return CosmeticValidationResult.OK;
    }
}