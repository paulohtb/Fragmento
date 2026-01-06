package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.Map;

public record CombatSnapshot(
        long version,
        Map<Long, ActorState> actors
) {}