package com.pgalaxyp.fragmento.combat.domain.animation;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record AnimationTimeline(
        AnimationKey key,
        Duration duration
) {}