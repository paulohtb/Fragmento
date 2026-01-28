package com.pgalaxyp.fragmento.combat.abilityModule.port;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.Collection;

public interface AbilityPort {
    AbilityOutcome tryExecute(ActorId actorId, AbilityId abilityId, FrameContext frame, GameState state);
    AbilityOutcome tick(FrameContext frame, GameState state);
    AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId);
}