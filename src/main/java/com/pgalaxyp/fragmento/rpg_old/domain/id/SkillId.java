package com.pgalaxyp.fragmento.rpg_old.domain.id;

public record SkillId(int value) {
    public SkillId {
        if (value < 0) {
            throw new IllegalArgumentException("SkillId negativo");
        }
    }
}