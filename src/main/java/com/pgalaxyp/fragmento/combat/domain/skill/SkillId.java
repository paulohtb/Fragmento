package com.pgalaxyp.fragmento.combat.domain.skill;

public record SkillId(int value) {
    public SkillId {
        if (value < 0) {
            throw new IllegalArgumentException("SkillId negativo");
        }
    }
}