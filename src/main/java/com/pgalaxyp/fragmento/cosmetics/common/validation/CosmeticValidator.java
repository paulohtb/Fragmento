package com.pgalaxyp.fragmento.cosmetics.common.validation;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticValidationResult;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import java.util.Objects;
import java.util.UUID;

public final class CosmeticValidator {

    private final CosmeticRegistry registry;
    private final PlayerLevelResolver levelResolver;
    private final LevelAccessPolicy accessPolicy;

    public CosmeticValidator(
            CosmeticRegistry registry,
            PlayerLevelResolver levelResolver,
            LevelAccessPolicy accessPolicy
    ) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.levelResolver = Objects.requireNonNull(levelResolver, "levelResolver");
        this.accessPolicy = Objects.requireNonNull(accessPolicy, "accessPolicy");
    }

    public CosmeticValidationResult validateBase(
            UUID playerId,
            CosmeticSlot slot,
            CosmeticId cosmeticId
    ) {
        return validate(playerId, slot, cosmeticId);
    }

    public CosmeticValidationResult validateForced(
            UUID playerId,
            CosmeticSlot slot,
            CosmeticId cosmeticId
    ) {
        return validate(playerId, slot, cosmeticId);
    }

    private CosmeticValidationResult validate(
            UUID playerId,
            CosmeticSlot slot,
            CosmeticId cosmeticId
    ) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        CosmeticDefinition def = registry.get(cosmeticId);
        if (def == null) {
            return CosmeticValidationResult.fail("cosmetic_missing");
        }
        if (def.slot() != slot) {
            return CosmeticValidationResult.fail("slot_mismatch");
        }

        int required = def.requiredLevel();
        if (required <= 0) {
            return CosmeticValidationResult.OK;
        }

        int level = levelResolver.resolveLevel(playerId);
        if (!accessPolicy.allowed(level, required)) {
            return CosmeticValidationResult.fail("level_denied");
        }

        return CosmeticValidationResult.OK;
    }
}