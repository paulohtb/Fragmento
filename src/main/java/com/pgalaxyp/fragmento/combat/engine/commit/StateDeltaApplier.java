package com.pgalaxyp.fragmento.combat.engine.commit;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import java.util.*;

public final class StateDeltaApplier {

    public static GameState applyAll(GameState base, Iterable<StateDelta> deltas) {
        if (base == null || deltas == null) throw new IllegalArgumentException();
        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());
        for (StateDelta delta : deltas) {
            if (delta == null) throw new IllegalArgumentException();
            applyOne(actors, delta);
        }
        return new GameState(base.frame(), actors, base.buffs());
    }

    private static void applyOne(Map<ActorId, ActorState> actors, StateDelta delta) {
        if (delta instanceof ActorSpawned s) {
            ActorId actorId = s.actorId();
            if (actors.containsKey(actorId)) return;
            ActorState next = ActorState.withEquippedWeapon(s.classId(), s.equippedWeaponId(), s.healthHearts(), s.maxHealthHearts());
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof DamageApplied da) {
            ActorId targetActorId = da.targetActorId();
            ActorState prev = actors.get(targetActorId);
            if (prev == null) return;
            int nextHealth = Math.addExact(prev.healthHearts(), Math.negateExact(da.hearts()));
            if (nextHealth < 0) nextHealth = 0;
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), nextHealth, prev.maxHealthHearts());
            actors.put(targetActorId, next);
        }
    }

    private StateDeltaApplier() {}
}
