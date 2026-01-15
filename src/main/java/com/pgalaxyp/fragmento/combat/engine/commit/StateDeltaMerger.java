package com.pgalaxyp.fragmento.combat.engine.commit;

import com.pgalaxyp.fragmento.combat.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.events.delta.ActorSpawned;
import com.pgalaxyp.fragmento.combat.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.combat.core.events.delta.StateDelta;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public final class StateDeltaMerger {

    public static List<StateDelta> mergeStable(List<StateDelta> first, List<StateDelta> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }

        List<StateDelta> all = new ArrayList<>(Math.addExact(first.size(), second.size()));
        for (StateDelta d : first) {
            if (d == null) {
                throw new IllegalArgumentException();
            }
            all.add(d);
        }
        for (StateDelta d : second) {
            if (d == null) {
                throw new IllegalArgumentException();
            }
            all.add(d);
        }

        TreeMap<ActorId, ActorSpawned> spawnByActor = new TreeMap<>();
        TreeMap<ActorId, Integer> damageByTarget = new TreeMap<>();

        for (StateDelta d : all) {
            if (d instanceof ActorSpawned s) {
                spawnByActor.putIfAbsent(s.actorId(), s);
                continue;
            }

            if (d instanceof DamageApplied da) {
                ActorId targetActorId = da.targetActorId();
                int hearts = da.hearts();

                Integer prev = damageByTarget.get(targetActorId);
                int base = prev == null ? 0 : prev;
                int next = Math.addExact(base, hearts);
                damageByTarget.put(targetActorId, next);
            }
        }

        List<StateDelta> out = new ArrayList<>();

        for (var e : spawnByActor.entrySet()) {
            out.add(e.getValue());
        }

        for (var e : damageByTarget.entrySet()) {
            ActorId targetId = e.getKey();
            int hearts = e.getValue();
            if (hearts > 0) {
                out.add(new DamageApplied(targetId, hearts));
            }
        }

        return List.copyOf(out);
    }

    private StateDeltaMerger() {}
}