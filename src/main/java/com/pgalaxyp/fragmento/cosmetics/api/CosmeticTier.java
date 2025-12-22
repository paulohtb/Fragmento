package com.pgalaxyp.fragmento.cosmetics.api;

public enum CosmeticTier {

    TIER_0(0),
    TIER_1(1),
    TIER_2(2),
    TIER_3(3);

    private final int level;

    CosmeticTier(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }

    public boolean allows(CosmeticTier required) {
        if (required == null) return true;
        return this.level >= required.level;
    }

    public static CosmeticTier fromLevel(int level) {
        if (level <= 0) return TIER_0;
        if (level == 1) return TIER_1;
        if (level == 2) return TIER_2;
        return TIER_3;
    }
}