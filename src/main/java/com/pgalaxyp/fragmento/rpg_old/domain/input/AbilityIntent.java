package com.pgalaxyp.fragmento.rpg_old.domain.input;

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