package com.pgalaxyp.fragmento.combat.domain.cue;

public record AnimationKey(String id) {
    public AnimationKey {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("AnimationKey vazio");
        }
    }
}