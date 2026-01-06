package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public record ActionResult(
        ActorState nextState,
        List<RpgEvent> events
) {
    public boolean consumed() {
        return nextState != null;
    }

    public static ActionResult ignored() {
        return new ActionResult(null, List.of());
    }
}