package com.pgalaxyp.fragmento.combat.domain.cue;

public record AnimationCue(
        AnimationKey key,
        long startedAtTick
) {
    public AnimationCue {
        if (startedAtTick < 0L) {
            throw new IllegalArgumentException("startedAtTick negativo");
        }
    }
}