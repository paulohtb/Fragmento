package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ActorSpawned;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class StateDeltaApplier {

    public static GameState applyAll(GameState base, Iterable<StateDelta> deltas) {
        if (base == null || deltas == null) {
            throw new IllegalArgumentException();
        }
        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());
        for (StateDelta delta : deltas) {
            if (delta == null) {
                throw new IllegalArgumentException();
            }
            applyOne(actors, delta);
        }
        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, StateDelta delta) {
        if (delta instanceof ActorSpawned s) {

            ActorId actorId = s.actorId();
            ClassId classId = s.classId();
            WeaponId weaponId = s.equippedWeaponId();
            int healthHearts = s.healthHearts();
            int maxHealthHearts = s.maxHealthHearts();

            if (actors.containsKey(actorId)) {
                return;
            }
            ActorState next = ActorState.withEquippedWeapon(classId, weaponId, healthHearts, maxHealthHearts);
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof DamageApplied da) {

            ActorId targetActorId = da.targetActorId();
            int hearts = da.hearts();

            ActorState prev = actors.get(targetActorId);
            if (prev == null) {
                return;
            }
            int nextHealth = Math.addExact(prev.healthHearts(), Math.negateExact(hearts));
            if (nextHealth < 0) {
                nextHealth = 0;
            }
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), nextHealth, prev.maxHealthHearts());
            actors.put(targetActorId, next);
        }
    }

    private StateDeltaApplier() {}
}