package com.pgalaxyp.fragmento.combat.domain.hit;

public record HitResult(
        boolean hit,
        boolean damageApplied
) {
    public static HitResult miss() {
        return new HitResult(false, false);
    }

    public static HitResult hitNoDamage() {
        return new HitResult(true, false);
    }

    public static HitResult damage() {
        return new HitResult(true, true);
    }
}