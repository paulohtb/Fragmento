package com.pgalaxyp.fragmento.rpg.domain.input;

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