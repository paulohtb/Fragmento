package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import java.util.Map;

public record CombatSnapshot(
        Map<Long, ActorState> actors
) {}