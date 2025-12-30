package com.pgalaxyp.fragmento.combat.domain.action;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record ActionTiming(
        Duration totalDuration,
        Duration comboWindow
) {}