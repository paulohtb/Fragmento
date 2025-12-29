package com.pgalaxyp.fragmento.tiers.server.entitlement;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import com.pgalaxyp.fragmento.tiers.common.service.TierSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import java.util.Objects;
import java.util.UUID;

public final class TierCosmeticEntitlementService implements CosmeticEntitlementService {

    private final TierService tiers;

    public TierCosmeticEntitlementService(TierService tiers) {
        this.tiers = Objects.requireNonNull(tiers);
    }

    @Override
    public boolean allowed(UUID playerId, CosmeticDefinition def) {
        if (playerId == null || def == null) return false;

        int required = def.requiredLevel();
        if (required <= 0) return true;

        TierSnapshot snap = tiers.snapshot(playerId, System.currentTimeMillis());
        Tier tier = snap.tier();

        return tier.allows(TierLevel.of(required));
    }

    @Override
    public long version(UUID playerId) {
        if (playerId == null) return 0L;
        TierSnapshot snap = tiers.snapshot(playerId, System.currentTimeMillis());
        return snap.version();
    }

    @Override
    public String label(UUID playerId) {
        if (playerId == null) return "Tier: ?";
        TierSnapshot snap = tiers.snapshot(playerId, System.currentTimeMillis());
        return "Tier: " + snap.tier().level().value();
    }
}