package com.pgalaxyp.fragmento.combat.combo.state;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;
import java.util.concurrent.*;

public final class InMemoryComboTracker implements ComboTracker {

    private final ConcurrentHashMap<ActorId, ComboState> byActor = new ConcurrentHashMap<>();

    @Override
    public Optional<ComboState> get(ActorId actorId) {
        Objects.requireNonNull(actorId);
        return Optional.ofNullable(byActor.get(actorId));
    }

    @Override
    public void put(ActorId actorId, ComboState state) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(state);
        byActor.put(actorId, state);
    }

    @Override
    public void clear(ActorId actorId) {
        Objects.requireNonNull(actorId);
        byActor.remove(actorId);
    }
}