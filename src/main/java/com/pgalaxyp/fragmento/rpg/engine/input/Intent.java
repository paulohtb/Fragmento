package com.pgalaxyp.fragmento.rpg.engine.input;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;

public record Intent(
        long actorId,
        IntentType type,
        ActionId actionId,
        InterruptMask interrupt
) {
    public static Intent primary(long actorId, ActionId actionId) {
        return new Intent(actorId, IntentType.PRIMARY_ACTION, actionId, null);
    }

    public static Intent interrupt(long actorId, InterruptMask interrupt) {
        return new Intent(actorId, IntentType.INTERRUPT, null, interrupt);
    }
}