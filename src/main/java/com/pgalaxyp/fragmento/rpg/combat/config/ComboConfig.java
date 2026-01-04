package com.pgalaxyp.fragmento.rpg.combat.config;

import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;

public interface ComboConfig {
    int maxSteps();
    Duration stepDuration();
    Duration actionLockDuration();
}