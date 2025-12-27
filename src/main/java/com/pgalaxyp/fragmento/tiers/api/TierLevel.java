package com.pgalaxyp.fragmento.tiers.api;

public final class TierLevel {

    public static final TierLevel TIER_0 = new TierLevel(0);
    public static final TierLevel TIER_1 = new TierLevel(1);
    public static final TierLevel TIER_2 = new TierLevel(2);
    public static final TierLevel TIER_3 = new TierLevel(3);

    private final int value;

    private TierLevel(int value) {
        this.value = Math.max(0, Math.min(3, value));
    }

    public static TierLevel of(int value) {
        return switch (value) {
            case 1 -> TIER_1;
            case 2 -> TIER_2;
            case 3 -> TIER_3;
            default -> TIER_0;
        };
    }

    public int value() {
        return value;
    }

    public boolean isAtLeast(TierLevel other) {
        return other == null || value >= other.value;
    }
}