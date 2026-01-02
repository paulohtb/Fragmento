package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

@FunctionalInterface
public interface CombatClock {
    CombatTime now();
}