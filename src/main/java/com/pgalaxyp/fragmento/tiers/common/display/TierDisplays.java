package com.pgalaxyp.fragmento.tiers.common.display;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;

public final class TierDisplays {

    public static final TierDisplay DEFAULT = new DefaultDisplay();

    private TierDisplays() {}

    private static final class DefaultDisplay implements TierDisplay {

        @Override
        public String label(Tier tier) {
            if (tier == null) {
                return "Tier: 0";
            }
            return "Tier: " + tier.level().value();
        }
    }
}