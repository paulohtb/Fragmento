package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

import java.util.List;

public interface RuleDispatcher {

    RuleFrame applyPrimary(
            ActionIntent intent,
            ActorState current,
            long now
    );

    RuleFrame applyInterrupt(
            long actorId,
            ActorState current,
            long now
    );

    record RuleFrame(
            ActorState nextState,
            List<RpgEvent> events,
            boolean consumed
    ) {}
}