package com.pgalaxyp.fragmento.rpg.domain.input;

public record SkillSlotId(int index) {
    public SkillSlotId {
        if (index <= 0) {
            throw new IllegalArgumentException();
        }
    }
}