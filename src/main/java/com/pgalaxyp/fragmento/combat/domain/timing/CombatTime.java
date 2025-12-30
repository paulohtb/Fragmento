package com.pgalaxyp.fragmento.combat.domain.timing;

public record CombatTime(long ticks) {

    public static CombatTime ofTicks(long ticks) {
        return new CombatTime(Math.max(0L, ticks));
    }

    public CombatTime plus(Duration d) {
        if (d == null) {
            return this;
        }
        return new CombatTime(ticks + Math.max(0, d.ticks()));
    }

    public boolean isAfterOrEqual(CombatTime other) {
        if (other == null) {
            return true;
        }
        return ticks >= other.ticks();
    }

    public boolean isBefore(CombatTime other) {
        if (other == null) {
            return false;
        }
        return ticks < other.ticks();
    }
}