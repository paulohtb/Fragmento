package com.pgalaxyp.fragmento.rpg.core.domain.spec;

import java.util.Optional;

public record WeaponSpec(
        Optional<CooldownSpec> cooldown
) {
    public WeaponSpec {
        if (cooldown == null) {
            throw new IllegalArgumentException();
        }
    }

    public static WeaponSpec empty() {
        return new WeaponSpec(Optional.empty());
    }

    public static WeaponSpec withCooldownFrames(int frames) {
        return new WeaponSpec(Optional.of(new CooldownSpec(frames)));
    }
}