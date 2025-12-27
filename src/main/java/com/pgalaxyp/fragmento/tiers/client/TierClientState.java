package com.pgalaxyp.fragmento.tiers.client;

import com.pgalaxyp.fragmento.tiers.api.Tier;

public final class TierClientState {

    private static volatile Tier CURRENT = Tier.unknown();
    private static volatile long VERSION = 0L;

    private TierClientState() {}

    public static Tier get() {
        return CURRENT;
    }

    public static long version() {
        return VERSION;
    }

    public static void update(Tier tier, long version) {
        if (version < VERSION) return;
        CURRENT = tier;
        VERSION = version;
    }

    public static void clear() {
        CURRENT = Tier.unknown();
        VERSION = 0L;
    }
}