package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityEnded;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.random.FrameEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

final class AbilityRepository {
    private final Map<ActorId, Active> activeByActor = new HashMap<>();
    private final Map<ActorId, NavigableMap<AbilityId, Long>> cooldownEndByActor = new HashMap<>();

    Optional<AbilitySnapshot> activeOf(ActorId actorId, long frameId) {
        Active a = activeByActor.get(actorId);
        if (a == null || frameId < a.startFrame || frameId >= a.endFrameExclusive) return Optional.empty();
        return Optional.of(new AbilitySnapshot(a.abilityId, actorId, a.startFrame, a.endFrameExclusive));
    }

    List<AbilitySnapshot> activeAll(Collection<ActorId> actorIds, long frameId) {
        if (actorIds == null) throw new IllegalArgumentException();
        if (frameId < 0) throw new IllegalArgumentException();
        if (actorIds.isEmpty() || activeByActor.isEmpty()) return List.of();

        ArrayList<AbilitySnapshot> out = new ArrayList<>();
        for (ActorId actorId : actorIds) {
            if (actorId == null) throw new IllegalArgumentException();
            Active a = activeByActor.get(actorId);
            if (a == null) continue;
            if (frameId >= a.startFrame && frameId < a.endFrameExclusive) {
                out.add(new AbilitySnapshot(a.abilityId, actorId, a.startFrame, a.endFrameExclusive));
            }
        }

        if (out.isEmpty()) return List.of();
        return List.copyOf(out);
    }

    boolean cooldownActive(ActorId actorId, AbilityId abilityId, long frameId) {
        if (frameId < 0) throw new IllegalArgumentException();
        NavigableMap<AbilityId, Long> inner = cooldownEndByActor.get(actorId);
        if (inner == null) return false;
        Long end = inner.get(abilityId);
        return end != null && frameId < end;
    }

    void putActive(ActorId actorId, AbilityId abilityId, long startFrame, long endFrameExclusive) {
        activeByActor.put(actorId, new Active(Objects.requireNonNull(abilityId), startFrame, endFrameExclusive));
    }

    void startCooldown(ActorId actorId, AbilityId abilityId, long endExclusive) {
        if (endExclusive < 0) throw new IllegalArgumentException();
        NavigableMap<AbilityId, Long> inner = cooldownEndByActor.computeIfAbsent(actorId, k -> new TreeMap<>());
        Long prev = inner.get(abilityId);
        if (prev == null || endExclusive > prev) inner.put(abilityId, endExclusive);
    }

    List<FrameEvent> evictEndedAtOrBefore(long frameId) {
        if (activeByActor.isEmpty()) return List.of();

        ArrayList<FrameEvent> out = new ArrayList<>();
        Iterator<Map.Entry<ActorId, Active>> it = activeByActor.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<ActorId, Active> e = it.next();
            Active a = e.getValue();
            if (frameId >= a.endFrameExclusive) {
                it.remove();
                out.add(new AbilityEnded(new AbilitySnapshot(a.abilityId, e.getKey(), a.startFrame, a.endFrameExclusive)));
            }
        }

        if (out.isEmpty()) return List.of();
        return List.copyOf(out);
    }

    void cleanupCooldowns(long frameId) {
        if (cooldownEndByActor.isEmpty()) return;

        Iterator<Map.Entry<ActorId, NavigableMap<AbilityId, Long>>> it = cooldownEndByActor.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ActorId, NavigableMap<AbilityId, Long>> e = it.next();
            NavigableMap<AbilityId, Long> inner = e.getValue();

            Iterator<Map.Entry<AbilityId, Long>> it2 = inner.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<AbilityId, Long> c = it2.next();
                Long end = c.getValue();
                if (end != null && frameId >= end) it2.remove();
            }

            if (inner.isEmpty()) it.remove();
        }
    }

    void pruneToActors(Collection<ActorId> liveActors) {
        if (liveActors == null) throw new IllegalArgumentException();
        if (!activeByActor.isEmpty()) { activeByActor.entrySet().removeIf(e -> !liveActors.contains(e.getKey())); }
        if (!cooldownEndByActor.isEmpty()) { cooldownEndByActor.entrySet().removeIf(e -> !liveActors.contains(e.getKey())); }
    }

    private record Active(AbilityId abilityId, long startFrame, long endFrameExclusive) {
        private Active {
            Objects.requireNonNull(abilityId);
            if (startFrame < 0 || endFrameExclusive <= startFrame) throw new IllegalArgumentException();
        }
    }
}