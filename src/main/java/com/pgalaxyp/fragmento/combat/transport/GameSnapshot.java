package com.pgalaxyp.fragmento.combat.transport;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.ActorState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
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