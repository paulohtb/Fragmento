package com.pgalaxyp.fragmento.rpg.core.state.actor;

import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;

public record ActorState(
        long version,
        long actorId,
        ActionState currentAction,
        ComboState combo
) {
    public ActorState {
        version = Math.max(0L, version);
        combo = combo == null ? ComboState.empty() : combo;
    }

    public static ActorState initial(long actorId) {
        return new ActorState(0L, actorId, null, ComboState.empty());
    }

    public ActorState withVersion(long nextVersion) {
        return new ActorState(nextVersion, actorId, currentAction, combo);
    }

    public ActorState withAction(long nextVersion, ActionState nextAction) {
        return new ActorState(nextVersion, actorId, nextAction, combo);
    }

    public ActorState withCombo(long nextVersion, ComboState nextCombo) {
        return new ActorState(nextVersion, actorId, currentAction, nextCombo);
    }
}