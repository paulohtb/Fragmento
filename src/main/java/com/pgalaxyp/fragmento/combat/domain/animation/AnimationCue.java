package com.pgalaxyp.fragmento.combat.domain.animation;

public record AnimationCue(
        AnimationKey key,
        long startedAtTick
) {}