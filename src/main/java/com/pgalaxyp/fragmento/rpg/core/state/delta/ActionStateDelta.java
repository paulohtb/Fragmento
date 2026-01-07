package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;

public record ActionStateDelta(
        long actorId,
        ActionState nextAction
) implements StateDelta {
    public ActionStateDelta {
        if (nextAction == null) throw new IllegalArgumentException("ActionStateDelta.nextAction");
    }
}