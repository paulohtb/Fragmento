package com.pgalaxyp.fragmento.combat.abilityModule.port;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Collection;

public interface AbilityPort {
    AbilityOutcome tryExecute(ActorId actorId, AbilityId abilityId, FrameContext frame, GameState state);
    AbilityOutcome tick(FrameContext frame, GameState state);
    AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId);
}