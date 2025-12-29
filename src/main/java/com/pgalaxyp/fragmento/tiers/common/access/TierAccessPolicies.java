package com.pgalaxyp.fragmento.tiers.common.access;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;

public final class TierAccessPolicies {

    public static final TierAccessPolicy DEFAULT = new DefaultPolicy();

    private TierAccessPolicies() {}

    private static final class DefaultPolicy implements TierAccessPolicy {

        @Override
        public boolean allowed(Tier tier, TierLevel required) {
            if (required == null) {
                return true;
            }
            if (required.value() == 0) {
                return true;
            }
            if (tier == null) {
                return false;
            }
            return tier.allows(required);
        }
    }
}