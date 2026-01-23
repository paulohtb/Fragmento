package com.pgalaxyp.fragmento.combat.transport;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.actor.api.ActorState;
import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import java.util.*;

public record GameSnapshot(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors,
        AbilityFrameView abilities
) {
    public GameSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(abilities);
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }
}