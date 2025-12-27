package com.pgalaxyp.fragmento.cosmetics.policy;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import java.util.Objects;

public final class CosmeticAccessPolicy {

    public boolean canUse(Tier tier, TierLevel requiredLevel) {
        Objects.requireNonNull(tier, "tier");
        if (requiredLevel == null) return true;
        return tier.allows(requiredLevel);
    }
}