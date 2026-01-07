package com.pgalaxyp.fragmento.rpg.core.rule.interrupt;

import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public interface InterruptRule {
    ActorState apply(long actorId, ActorState current, InterruptMask cause, long now);
}