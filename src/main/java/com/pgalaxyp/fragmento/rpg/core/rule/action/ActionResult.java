package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public record ActionResult(
        ActorState nextState,
        List<PendingTargeting> targetings,
        boolean consumed
) {
    public static ActionResult ignored(ActorState state) {
        return new ActionResult(state, List.of(), false);
    }

    public static ActionResult applied(ActorState state, List<PendingTargeting> targetings) {
        return new ActionResult(state, List.copyOf(targetings), true);
    }
}