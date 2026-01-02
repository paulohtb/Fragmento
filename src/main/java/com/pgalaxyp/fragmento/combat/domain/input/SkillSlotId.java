package com.pgalaxyp.fragmento.combat.domain.input;

public record SkillSlotId(int index) {
    public SkillSlotId {
        if (index <= 0) {
            throw new IllegalArgumentException();
        }
    }
}