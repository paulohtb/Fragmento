package com.pgalaxyp.fragmento.rpg.core.rule.interrupt;

import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public record InterruptResult(
        ActorState nextState,
        List<RpgEvent> events
) {
    public boolean consumed() {
        return nextState != null;
    }

    public static InterruptResult ignored() {
        return new InterruptResult(null, List.of());
    }
}