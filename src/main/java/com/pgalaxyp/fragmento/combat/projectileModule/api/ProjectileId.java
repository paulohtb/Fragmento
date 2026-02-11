package com.pgalaxyp.fragmento.combat.projectileModule.api;

import java.util.Objects;

public record ProjectileId(String value) {
    public ProjectileId {
        Objects.requireNonNull(value);
    }
}
