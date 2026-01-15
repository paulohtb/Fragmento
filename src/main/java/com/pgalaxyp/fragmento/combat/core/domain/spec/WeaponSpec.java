package com.pgalaxyp.fragmento.combat.core.domain.spec;

import java.util.Optional;

public record WeaponSpec(
        Optional<CooldownSpec> cooldown
) {
    public WeaponSpec {
        if (cooldown == null) {
            throw new IllegalArgumentException();
        }
        if (cooldown.isPresent() && cooldown.get() == null) {
            throw new IllegalArgumentException();
        }
        cooldown = cooldown.isPresent() ? Optional.of(cooldown.get()) : Optional.empty();
    }

    public static WeaponSpec empty() {
        return new WeaponSpec(Optional.empty());
    }

    public static WeaponSpec withCooldownFrames(int frames) {
        return new WeaponSpec(Optional.of(new CooldownSpec(frames)));
    }
}