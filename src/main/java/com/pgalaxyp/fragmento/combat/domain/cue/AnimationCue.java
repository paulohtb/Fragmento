package com.pgalaxyp.fragmento.combat.domain.cue;

public record AnimationCue(
        AnimationKey key,
        long startedAtTick
) {
    public AnimationCue {
        if (key == null) {
            throw new IllegalArgumentException("AnimationCue sem key");
        }
        if (startedAtTick < 0L) {
            throw new IllegalArgumentException("startedAtTick negativo");
        }
    }
}