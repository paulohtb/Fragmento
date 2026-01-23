package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityEvent;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import java.util.*;

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

        var out = new ArrayList<AbilitySnapshot>();
        for (ActorId actorId : actorIds) {
            if (actorId == null) throw new IllegalArgumentException();
            Active a = activeByActor.get(actorId);
            if (a == null) continue;
            if (frameId >= a.startFrame && frameId < a.endFrameExclusive) {
                out.add(new AbilitySnapshot(a.abilityId, actorId, a.startFrame, a.endFrameExclusive));
            }
        }

        return out.isEmpty() ? List.of() : List.copyOf(out);
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
        NavigableMap<AbilityId, Long> inner = cooldownEndByActor.computeIfAbsent(actorId, __ -> new TreeMap<>());
        Long prev = inner.get(abilityId);
        if (prev == null || endExclusive > prev) inner.put(abilityId, endExclusive);
    }

    List<AbilityEvent> evictEndedAtOrBefore(long frameId) {
        if (activeByActor.isEmpty()) return List.of();

        var out = new ArrayList<AbilityEvent>();
        var it = activeByActor.entrySet().iterator();

        while (it.hasNext()) {
            var e = it.next();
            Active a = e.getValue();
            if (frameId >= a.endFrameExclusive) {
                it.remove();
                out.add(new AbilityEvent.Ended(
                        new AbilitySnapshot(a.abilityId, e.getKey(), a.startFrame, a.endFrameExclusive)
                ));
            }
        }

        return out.isEmpty() ? List.of() : List.copyOf(out);
    }

    void cleanupCooldowns(long frameId) {
        if (cooldownEndByActor.isEmpty()) return;

        var it = cooldownEndByActor.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            NavigableMap<AbilityId, Long> inner = e.getValue();
            inner.entrySet().removeIf(x -> x.getValue() != null && frameId >= x.getValue());
            if (inner.isEmpty()) it.remove();
        }
    }

    private record Active(AbilityId abilityId, long startFrame, long endFrameExclusive) {
        private Active {
            Objects.requireNonNull(abilityId);
            if (startFrame < 0 || endFrameExclusive <= startFrame) throw new IllegalArgumentException();
        }
    }
}
