package com.pgalaxyp.fragmento.tiers.common.model;

public final class TierLevel {

    public static final TierLevel TIER_0 = new TierLevel(0);
    public static final TierLevel TIER_1 = new TierLevel(1);
    public static final TierLevel TIER_2 = new TierLevel(2);
    public static final TierLevel TIER_3 = new TierLevel(3);

    private final int value;

    private TierLevel(int value) {
        this.value = clamp(value);
    }

    public static TierLevel of(int value) {
        int v = clamp(value);
        if (v == 1) return TIER_1;
        if (v == 2) return TIER_2;
        if (v == 3) return TIER_3;
        return TIER_0;
    }

    public int value() {
        return value;
    }

    public boolean isAtLeast(TierLevel other) {
        return value >= other.value;
    }

    private static int clamp(int v) {
        if (v <= 0) return 0;
        return Math.min(v, 3);
    }
}