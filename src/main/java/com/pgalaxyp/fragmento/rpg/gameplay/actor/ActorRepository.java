package com.pgalaxyp.fragmento.rpg.gameplay.actor;

import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboState;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.HealthComponent;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ActorRepository {
    private final Map<Long, ActorState> states = new HashMap<>();
    private final Map<Long, HealthComponent> health = new HashMap<>();
    private final Map<Long, ComboState> combos = new HashMap<>();

    public void putState(long actorId, ActorState state) {
        states.put(actorId, state);
    }

    public Optional<ActorState> findState(long actorId) {
        return Optional.ofNullable(states.get(actorId));
    }

    public ActorState requireState(long actorId) {
        var s = states.get(actorId);
        if (s == null) throw new IllegalStateException("Missing ActorState: " + actorId);
        return s;
    }

    public HealthComponent health(long actorId) {
        return health.computeIfAbsent(actorId, __ -> new HealthComponent(20.0));
    }

    public ComboState combo(long actorId) {
        return combos.computeIfAbsent(actorId, __ -> new ComboState());
    }

    public void setPositionAndBounds(long actorId, Vec3 pos, Aabb bounds) {
        var hc = health(actorId);
        putState(actorId, new ActorState(pos, bounds, hc.alive()));
    }
}