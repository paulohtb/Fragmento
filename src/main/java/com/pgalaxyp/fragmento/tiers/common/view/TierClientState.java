package com.pgalaxyp.fragmento.tiers.common.view;

public final class TierClientState {

    private static volatile int LEVEL;
    private static volatile long VERSION;

    private TierClientState() {}

    public static int level() {
        return LEVEL;
    }

    public static long version() {
        return VERSION;
    }

    public static void update(int level, long version) {
        if (version < VERSION) {
            return;
        }
        LEVEL = Math.max(0, level);
        VERSION = version;
    }
}