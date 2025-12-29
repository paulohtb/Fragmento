package com.pgalaxyp.fragmento.cosmetics.common.validation;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import java.util.UUID;

public final class CosmeticValidator {

    private final CosmeticRegistry registry;
    private final CosmeticEntitlementService entitlements;

    public CosmeticValidator(
            CosmeticRegistry registry,
            CosmeticEntitlementService entitlements
    ) {
        this.registry = registry;
        this.entitlements = entitlements;
    }

    public boolean validate(
            UUID playerId,
            CosmeticSlot slot,
            CosmeticId cosmeticId
    ) {
        CosmeticDefinition def = registry.get(cosmeticId);
        if (def == null) return false;
        if (def.slot() != slot) return false;
        return entitlements.allowed(playerId, def);
    }
}