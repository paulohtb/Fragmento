package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record WeaponRuntimeConfig(
        Duration maxGapBetweenHits
) {}