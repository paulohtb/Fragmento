package com.pgalaxyp.fragmento.rpg_old.combat.config;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;

public interface ComboConfig {
    int maxSteps();
    Duration stepDuration();
    Duration actionLockDuration();
    Duration executionEntityLife();
}