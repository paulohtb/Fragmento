package com.pgalaxyp.fragmento.gameplay.skill;

public record SkillResult(
        boolean success,
        int cooldownTicks,
        boolean consumedCharge
) {

    public static SkillResult failure() {
        return new SkillResult(false, 0, false);
    }

    public static SkillResult successNoCooldown() {
        return new SkillResult(true, 0, false);
    }

    public static SkillResult successWithCooldown(int ticks) {
        return new SkillResult(true, ticks, false);
    }

    public static SkillResult successWithCharge(int ticks) {
        return new SkillResult(true, ticks, true);
    }
}
