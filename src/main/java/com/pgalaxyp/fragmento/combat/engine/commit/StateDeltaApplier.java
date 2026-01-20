package com.pgalaxyp.fragmento.combat.engine.commit;

import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
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

        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, StateDelta delta) {
        if (delta instanceof ActorSpawned(ActorId actorId, ClassId classId, WeaponId equippedWeaponId, int healthHearts, int maxHealthHearts)) {
            if (actors.containsKey(actorId)) return;
            actors.put(actorId, ActorState.withEquippedWeapon(classId, equippedWeaponId, healthHearts, maxHealthHearts));
            return;
        }

        if (delta instanceof DamageApplied(ActorId targetActorId, int hearts)) {
            ActorState prev = actors.get(targetActorId);
            if (prev == null) return;
            int nextHealth = Math.addExact(prev.healthHearts(), Math.negateExact(hearts));
            if (nextHealth < 0) nextHealth = 0;
            actors.put(targetActorId, new ActorState(prev.classId(), prev.equippedWeaponId(), nextHealth, prev.maxHealthHearts()));
        }
    }

    private StateDeltaApplier() {}
}