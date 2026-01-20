package com.pgalaxyp.fragmento.combat.engine.commit;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;

public final class StateDeltaApplier {

    public static GameState applyAll(GameState base, Iterable<StateDelta> deltas) {
        if (base == null || deltas == null) throw new IllegalArgumentException();

        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());
        NavigableMap<ActorId, NavigableMap<AbilityId, Long>> cooldowns = new TreeMap<>();
        for (var e : base.cooldowns().entrySet()) cooldowns.put(e.getKey(), new TreeMap<>(e.getValue()));

        for (StateDelta delta : deltas) {
            if (delta == null) throw new IllegalArgumentException();
            applyOne(actors, cooldowns, delta);
        }

        return new GameState(base.frame(), actors, cooldowns);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, Map<ActorId, NavigableMap<AbilityId, Long>> cooldowns, StateDelta delta) {
        if (delta instanceof ActorSpawned s) {
            ActorId actorId = s.actorId();
            if (actors.containsKey(actorId)) return;
            actors.put(actorId, ActorState.withEquippedWeapon(s.classId(), s.equippedWeaponId(), s.healthHearts(), s.maxHealthHearts()));
            return;
        }

        if (delta instanceof DamageApplied da) {
            ActorId targetActorId = da.targetActorId();
            ActorState prev = actors.get(targetActorId);
            if (prev == null) return;
            int nextHealth = Math.addExact(prev.healthHearts(), Math.negateExact(da.hearts()));
            if (nextHealth < 0) nextHealth = 0;
            actors.put(targetActorId, new ActorState(prev.classId(), prev.equippedWeaponId(), nextHealth, prev.maxHealthHearts()));
            return;
        }

        if (delta instanceof CooldownStarted cs) {
            NavigableMap<AbilityId, Long> byAbility = cooldowns.computeIfAbsent(cs.actorId(), a -> new TreeMap<>());
            Long prev = byAbility.get(cs.abilityId());
            long next = cs.endFrame();
            if (prev == null || next > prev) byAbility.put(cs.abilityId(), next);
        }
    }

    private StateDeltaApplier() {}
}