package com.pgalaxyp.fragmento.features.bard_class.ability;

public record AbilityResult(
        boolean success,
        int cooldownTicks,
        boolean consumedCharge
) {

    public static AbilityResult failure() {
        return new AbilityResult(false, 0, false);
    }

    public static AbilityResult successNoCooldown() {
        return new AbilityResult(true, 0, false);
    }

    public static AbilityResult successWithCooldown(int cooldownTicks) {
        return new AbilityResult(true, cooldownTicks, false);
    }

    public static AbilityResult successWithCharge(int cooldownTicks) {
        return new AbilityResult(true, cooldownTicks, true);
    }
}
