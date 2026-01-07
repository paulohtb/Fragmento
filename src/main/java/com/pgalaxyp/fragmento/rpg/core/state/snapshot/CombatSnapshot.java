package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.Map;

public record CombatSnapshot(
        long version,
        Map<Long, ActorState> actors
) {
    public CombatSnapshot {
        version = Math.max(0L, version);
        actors = actors == null ? Map.of() : Map.copyOf(actors);
    }

    public static CombatSnapshot empty() {
        return new CombatSnapshot(0L, Map.of());
    }
}