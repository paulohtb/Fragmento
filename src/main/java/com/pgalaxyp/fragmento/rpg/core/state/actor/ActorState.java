package com.pgalaxyp.fragmento.rpg.core.state.actor;

import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;

public record ActorState(
        long actorId,
        ActionState currentAction,
        ComboState combo
) {
    public ActorState {
        if (combo == null) combo = ComboState.empty();
    }

    public static ActorState empty(long actorId) {
        return new ActorState(actorId, null, ComboState.empty());
    }
}