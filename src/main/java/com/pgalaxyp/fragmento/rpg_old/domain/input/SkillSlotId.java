package com.pgalaxyp.fragmento.rpg_old.domain.input;

public record SkillSlotId(int index) {
    public SkillSlotId {
        if (index <= 0) {
            throw new IllegalArgumentException();
        }
    }
}