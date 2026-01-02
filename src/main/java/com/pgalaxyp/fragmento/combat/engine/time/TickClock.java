package com.pgalaxyp.fragmento.combat.engine.time;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;

public final class TickClock implements CombatClock {

    private final TickSource source;

    public TickClock(TickSource source) {
        this.source = source;
    }

    @Override
    public CombatTime now() {
        long t = source != null ? source.gameTick() : 0L;
        return CombatTime.ofTicks(t);
    }
}