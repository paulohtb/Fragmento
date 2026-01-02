package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public interface ComboConfig {
    int maxSteps();
    Duration stepDuration();
    Duration actionLockDuration();
}