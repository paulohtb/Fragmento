package com.pgalaxyp.fragmento.rpg.core.state.actor;

import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;

public record ActorState(
        long actorId,
        long version,
        ActionState currentAction,
        ComboState combo
) {
    public ActorState {
        combo = combo == null ? ComboState.empty() : combo;
    }

    public static ActorState empty(long actorId) {
        return new ActorState(actorId, 0L, null, ComboState.empty());
    }

    public ActorState withAction(ActionState nextAction) {
        return new ActorState(actorId, version + 1L, nextAction, combo);
    }

    public ActorState withCombo(ComboState nextCombo) {
        return new ActorState(actorId, version + 1L, currentAction, nextCombo);
    }

    public ActorState with(ActionState nextAction, ComboState nextCombo) {
        return new ActorState(actorId, version + 1L, nextAction, nextCombo);
    }
}