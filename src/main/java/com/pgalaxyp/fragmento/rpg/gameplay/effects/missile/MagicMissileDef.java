package com.pgalaxyp.fragmento.rpg.gameplay.effects.missile;

public record MagicMissileDef(double damage, double minLifetimeSeconds, double maxLifetimeSeconds) {
    public MagicMissileDef {
        if (damage <= 0.0) throw new IllegalArgumentException("damage <= 0");
        if (minLifetimeSeconds <= 0.0) throw new IllegalArgumentException("minLifetimeSeconds <= 0");
        if (maxLifetimeSeconds < minLifetimeSeconds) throw new IllegalArgumentException("maxLifetimeSeconds < minLifetimeSeconds");
    }
}