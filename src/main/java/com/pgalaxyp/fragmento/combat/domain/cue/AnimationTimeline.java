package com.pgalaxyp.fragmento.combat.domain.cue;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record AnimationTimeline(
        AnimationKey key,
        Duration duration
) {
    public AnimationTimeline {
        if (key == null) {
            throw new IllegalArgumentException("AnimationTimeline sem key");
        }
        if (duration == null) {
            throw new IllegalArgumentException("AnimationTimeline sem duration");
        }
    }
}