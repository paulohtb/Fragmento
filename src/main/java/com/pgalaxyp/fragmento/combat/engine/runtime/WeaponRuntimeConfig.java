package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record WeaponRuntimeConfig(
        Duration maxGapBetweenHits
) {}