package com.pgalaxyp.fragmento.cosmetics.server.validation;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticValidationResult;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import java.util.Objects;
import java.util.UUID;

public final class CosmeticValidator {

    private final CosmeticRegistry registry;
    private final PlayerTierResolver tierResolver;
    private final com.pgalaxyp.fragmento.cosmetics.policy.CosmeticAccessPolicy accessPolicy;

    public CosmeticValidator(
            CosmeticRegistry registry,
            PlayerTierResolver tierResolver,
            com.pgalaxyp.fragmento.cosmetics.policy.CosmeticAccessPolicy accessPolicy
    ) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.tierResolver = Objects.requireNonNull(tierResolver, "tierResolver");
        this.accessPolicy = Objects.requireNonNull(accessPolicy, "accessPolicy");
    }

    public CosmeticValidationResult validateBase(
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

        TierLevel required = def.requiredTier();
        if (required == null || required.value() == 0) {
            return CosmeticValidationResult.OK;
        }

        Tier tier = tierResolver.resolve(playerId);
        if (tier == null || !tier.active()) {
            return CosmeticValidationResult.fail("tier_unavailable");
        }

        if (!accessPolicy.canUse(tier, required)) {
            return CosmeticValidationResult.fail("tier_denied");
        }

        return CosmeticValidationResult.OK;
    }

    public CosmeticValidationResult validateForced(
            CosmeticSlot slot,
            CosmeticId cosmeticId
    ) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        CosmeticDefinition def = registry.get(cosmeticId);
        if (def == null) {
            return CosmeticValidationResult.fail("cosmetic_missing");
        }
        if (def.slot() != slot) {
            return CosmeticValidationResult.fail("slot_mismatch");
        }

        return CosmeticValidationResult.OK;
    }
}