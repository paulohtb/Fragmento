package com.pgalaxyp.fragmento.combat.domain.input;

public record AbilityIntent(
        SkillSlotId slot,
        AbilityIntentKind kind
) {
    public AbilityIntent {
        if (slot == null || kind == null) {
            throw new IllegalArgumentException();
        }
    }
}