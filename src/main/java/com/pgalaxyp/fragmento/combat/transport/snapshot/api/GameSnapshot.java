package com.pgalaxyp.fragmento.combat.transport.snapshot.api;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.ActorState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.*;

public record GameSnapshot(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors,
        AbilityFrameView abilities,
        DamageFrameView damage
) {
    public GameSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(abilities);
        Objects.requireNonNull(damage);
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }
}