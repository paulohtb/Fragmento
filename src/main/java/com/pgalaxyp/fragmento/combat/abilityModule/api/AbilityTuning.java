package com.pgalaxyp.fragmento.combat.abilityModule.api;

public record AbilityTuning(int comboGapFrames) {
    public static final AbilityTuning DEFAULT = new AbilityTuning(20);

    public AbilityTuning {
        if (comboGapFrames < 0) throw new IllegalArgumentException();
    }
}