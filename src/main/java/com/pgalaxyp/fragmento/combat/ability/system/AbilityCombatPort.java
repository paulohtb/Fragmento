package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.Collection;

public interface AbilityCombatPort {
    AbilityCombatResult tryExecute(AbilityIntent intent, FrameContext frame, GameState state);
    AbilityCombatResult tick(FrameContext frame, GameState state);
    AbilityFrameView view(Collection<ActorId> actorIds, long frameId);
}