package com.pgalaxyp.fragmento.combat.engine.commit;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;

public final class StateDeltaMerger {

    public static List<StateDelta> mergeStable(List<StateDelta> first, List<StateDelta> second) {
        if (first == null || second == null) throw new IllegalArgumentException();

        List<StateDelta> all = new ArrayList<>(Math.addExact(first.size(), second.size()));
        for (StateDelta d : first) { if (d == null) throw new IllegalArgumentException(); all.add(d); }
        for (StateDelta d : second) { if (d == null) throw new IllegalArgumentException(); all.add(d); }

        TreeMap<ActorId, ActorSpawned> spawnByActor = new TreeMap<>();
        TreeMap<ActorId, Integer> damageByTarget = new TreeMap<>();
        TreeMap<ActorId, TreeMap<AbilityId, Long>> cooldownByActor = new TreeMap<>();

        for (StateDelta d : all) {
            if (d instanceof ActorSpawned s) { spawnByActor.putIfAbsent(s.actorId(), s); continue; }
            if (d instanceof DamageApplied da) {
                ActorId targetActorId = da.targetActorId();
                int base = damageByTarget.getOrDefault(targetActorId, 0);
                damageByTarget.put(targetActorId, Math.addExact(base, da.hearts()));
                continue;
            }
            if (d instanceof CooldownStarted cs) {
                var inner = cooldownByActor.computeIfAbsent(cs.actorId(), a -> new TreeMap<>());
                Long prev = inner.get(cs.abilityId());
                long next = cs.endFrame();
                if (prev == null || next > prev) inner.put(cs.abilityId(), next);
            }
        }

        List<StateDelta> out = new ArrayList<>();
        out.addAll(spawnByActor.values());
        for (var e : damageByTarget.entrySet()) if (e.getValue() > 0) out.add(new DamageApplied(e.getKey(), e.getValue()));
        for (var e : cooldownByActor.entrySet()) for (var ce : e.getValue().entrySet()) out.add(new CooldownStarted(e.getKey(), ce.getKey(), ce.getValue()));
        return List.copyOf(out);
    }

    private StateDeltaMerger() {}
}