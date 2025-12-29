package com.pgalaxyp.fragmento.tiers.common.view;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;

public final class TierClientState {

    private static volatile Tier CURRENT = Tier.inactive();
    private static volatile long VERSION;

    private TierClientState() {}

    public static Tier get() {
        return CURRENT;
    }

    public static long version() {
        return VERSION;
    }

    public static void update(Tier tier, long version) {
        if (tier == null) {
            return;
        }
        if (version < VERSION) {
            return;
        }
        CURRENT = tier;
        VERSION = version;
    }

    public static void clear() {
        CURRENT = Tier.inactive();
        VERSION = 0L;
    }
}