package com.pgalaxyp.fragmento.combat.transport.snapshot.api;

import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import java.util.*;

public record GameSnapshot(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors,
        List<AbilityInstanceView> activeAbilities,
        NavigableMap<ActorId, ActorExecutionState> execution
) {
    public GameSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(activeAbilities);
        Objects.requireNonNull(execution);
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
        activeAbilities = List.copyOf(activeAbilities);
        execution = Collections.unmodifiableNavigableMap(new TreeMap<>(execution));
    }
}