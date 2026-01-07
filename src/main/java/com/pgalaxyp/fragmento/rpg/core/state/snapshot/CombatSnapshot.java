package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;

import java.util.List;
import java.util.Map;

public record CombatSnapshot(
        long version,
        Map<Long, ActorState> actors,
        List<EffectState> effects
) {
    public CombatSnapshot {
        actors = Map.copyOf(actors);
        effects = List.copyOf(effects);
    }
}