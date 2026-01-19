package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record BuffState(NavigableMap<ActorId, List<BuffInstance>> byActor) {
    public BuffState {
        Objects.requireNonNull(byActor);
        var tmp = new TreeMap<ActorId, List<BuffInstance>>();
        for (var e : byActor.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
            tmp.put(e.getKey(), List.copyOf(e.getValue()));
        }
        byActor = Collections.unmodifiableNavigableMap(tmp);
    }

    public static BuffState empty() { return new BuffState(new TreeMap<>()); }

    public List<BuffInstance> ofActor(ActorId actorId) {
        Objects.requireNonNull(actorId);
        List<BuffInstance> list = byActor.get(actorId);
        return list == null ? List.of() : list;
    }
}
