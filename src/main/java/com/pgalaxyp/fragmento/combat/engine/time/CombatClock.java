package com.pgalaxyp.fragmento.combat.engine.time;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public interface CombatClock {
    CombatTime now();
}