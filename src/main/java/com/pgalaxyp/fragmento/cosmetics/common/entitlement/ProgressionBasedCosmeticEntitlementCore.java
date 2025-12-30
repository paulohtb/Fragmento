package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import com.pgalaxyp.fragmento.common.progression.PlayerProgressionView;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import java.util.UUID;

public final class ProgressionBasedCosmeticEntitlementCore {

    private final PlayerProgressionView progression;

    public ProgressionBasedCosmeticEntitlementCore(PlayerProgressionView progression) {
        this.progression = progression;
    }

    public boolean allowed(UUID playerId, CosmeticDefinition def) {
        if (def == null || playerId == null) return false;
        int required = def.requiredLevel();
        if (required <= 0) return true;
        return progression.level(playerId) >= required;
    }

    public long version(UUID playerId) {
        return playerId == null ? 0L : progression.version(playerId);
    }

    public String label(UUID playerId) {
        return playerId == null ? "Tier: ?" : progression.label(playerId);
    }
}