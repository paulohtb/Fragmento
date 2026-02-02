package com.pgalaxyp.fragmento.combat.util;

public final class HealthUnits {
    private HealthUnits() {}

    public static int heartsFromHealthPoints(float healthPoints) {
        if (!Float.isFinite(healthPoints) || healthPoints <= 0.0f) return 0;
        double hearts = Math.ceil(healthPoints / 2.0d);
        if (!Double.isFinite(hearts) || hearts <= 0.0d) return 0;
        return hearts >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) hearts;
    }

    public static float healthPointsFromHearts(int hearts) {
        if (hearts < 0) throw new IllegalArgumentException();
        return hearts * 2.0f;
    }
}