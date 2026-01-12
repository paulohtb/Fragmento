package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ActorSpawned;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboStarted;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
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
        TreeMap<ActorId, ComboAgg> comboByActor = new TreeMap<>();

        for (StateDelta d : all) {
            if (d instanceof ActorSpawned s) {
                spawnByActor.putIfAbsent(s.actorId(), s);
                continue;
            }

            if (d instanceof DamageApplied(ActorId targetActorId, int hearts)) {
                Integer prev = damageByTarget.get(targetActorId);
                int base = prev == null ? 0 : prev;
                int next = Math.addExact(base, hearts);
                damageByTarget.put(targetActorId, next);
                continue;
            }

            if (d instanceof ComboStarted cs) {
                ComboAgg agg = comboByActor.computeIfAbsent(cs.actorId(), k -> new ComboAgg());
                agg.acceptStart(cs);
                continue;
            }

            if (d instanceof ComboAdvanced(ActorId actorId, long stepFrameId)) {
                ComboAgg agg = comboByActor.computeIfAbsent(actorId, k -> new ComboAgg());
                agg.acceptAdvance(stepFrameId);
                continue;
            }

            if (d instanceof ComboEnded ce) {
                ComboAgg agg = comboByActor.computeIfAbsent(ce.actorId(), k -> new ComboAgg());
                agg.acceptEnd(ce);
            }
        }

        List<StateDelta> out = new ArrayList<>();

        for (var e : spawnByActor.entrySet()) {
            out.add(e.getValue());
        }

        for (var e : comboByActor.entrySet()) {
            ActorId actorId = e.getKey();
            ComboAgg agg = e.getValue();

            if (agg.start != null) {
                out.add(agg.start);
            }

            int advances = agg.advances;
            long advanceFrameId = agg.advanceFrameId;
            for (int i = 0; i < advances; i++) {
                out.add(new ComboAdvanced(actorId, advanceFrameId));
            }

            if (agg.end != null) {
                out.add(agg.end);
            }
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

    private static final class ComboAgg {

        private ComboStarted start;
        private int advances;
        private long advanceFrameId;
        private ComboEnded end;

        private void acceptStart(ComboStarted s) {
            if (end != null) {
                return;
            }
            if (start == null) {
                start = s;
            }
        }

        private void acceptAdvance(long frameId) {
            if (end != null) {
                return;
            }
            advances = Math.addExact(advances, 1);
            advanceFrameId = frameId;
        }

        private void acceptEnd(ComboEnded e) {
            end = e;
        }
    }

    private StateDeltaMerger() {}
}