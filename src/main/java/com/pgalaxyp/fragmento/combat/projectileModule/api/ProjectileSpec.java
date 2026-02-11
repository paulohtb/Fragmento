package com.pgalaxyp.fragmento.combat.projectileModule.api;

import java.util.Objects;

public record ProjectileSpec(ProjectileId id, String entityTypeId, double speed, float inaccuracy, boolean gravity) {
    public ProjectileSpec {
        Objects.requireNonNull(id);
        Objects.requireNonNull(entityTypeId);
        if (!Double.isFinite(speed) || speed <= 0.0) throw new IllegalArgumentException();
        if (!Float.isFinite(inaccuracy) || inaccuracy < 0.0f) throw new IllegalArgumentException();
    }
}
