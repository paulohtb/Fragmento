package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ActionStore {
    private final Map<ActorId, ActionRuntime> active = new HashMap<>();
    public Optional<ActionRuntime> get(ActorId actorId) { return Optional.ofNullable(active.get(actorId)); }
    public void put(ActorId actorId, ActionRuntime runtime) { active.put(actorId, runtime); }
    public void clear(ActorId actorId) { active.remove(actorId); }
    public void clearAll() { active.clear(); }
    public boolean hasActive(ActorId actorId) { return active.containsKey(actorId); }
}