package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorState;
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
    private final Map<Long, Long> combatVersion = new HashMap<>();

    public Optional<ActorState> findState(long actorId) {
        return Optional.ofNullable(states.get(actorId));
    }

    public ActorState requireState(long actorId) {
        var s = states.get(actorId);
        if (s == null) throw new IllegalStateException("Missing ActorState " + actorId);
        return s;
    }

    public void setState(long actorId, Vec3 pos, Vec3 lookDir, Aabb bounds) {
        var hc = health(actorId);
        states.put(actorId, new ActorState(pos, lookDir, bounds, hc.alive()));
    }

    public HealthComponent health(long actorId) {
        return health.computeIfAbsent(actorId, __ -> new HealthComponent(20.0));
    }

    public ComboState combo(long actorId) {
        return combos.computeIfAbsent(actorId, __ -> new ComboState());
    }

    public long bumpCombatVersion(long actorId) {
        long v = combatVersion.getOrDefault(actorId, 0L) + 1L;
        combatVersion.put(actorId, v);
        return v;
    }
}