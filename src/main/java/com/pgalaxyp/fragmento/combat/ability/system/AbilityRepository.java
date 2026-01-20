package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.*;
import java.util.function.BiConsumer;

final class AbilityRepository {
    private final Map<ActorId, Active> activeByActor = new HashMap<>();
    private final Map<ActorId, NavigableMap<AbilityId, Long>> cooldownEndByActor = new HashMap<>();

    boolean hasActive(ActorId actorId, long frameId) {
        var a = activeByActor.get(actorId);
        return a != null && frameId >= a.startFrame && frameId < a.endFrameExclusive;
    }

    boolean isLocked(ActorId actorId, long frameId) {
        var a = activeByActor.get(actorId);
        return a != null && frameId >= a.startFrame && frameId <= a.endFrameExclusive;
    }

    Optional<AbilitySnapshot> activeOf(ActorId actorId, long frameId) {
        var a = activeByActor.get(actorId);
        return a != null && frameId >= a.startFrame && frameId < a.endFrameExclusive
                ? Optional.of(new AbilitySnapshot(a.abilityId, actorId, a.startFrame, a.endFrameExclusive))
                : Optional.empty();
    }

    List<AbilitySnapshot> activeAll(long frameId) {
        if (activeByActor.isEmpty()) return List.of();
        var out = new ArrayList<AbilitySnapshot>(activeByActor.size());
        for (var e : activeByActor.entrySet()) {
            var a = e.getValue();
            if (frameId >= a.startFrame && frameId < a.endFrameExclusive) {
                out.add(new AbilitySnapshot(a.abilityId, e.getKey(), a.startFrame, a.endFrameExclusive));
            }
        }
        return List.copyOf(out);
    }

    boolean cooldownActive(ActorId actorId, AbilityId abilityId, long frameId) {
        if (frameId < 0) throw new IllegalArgumentException();
        var inner = cooldownEndByActor.get(actorId);
        if (inner == null) return false;
        Long end = inner.get(abilityId);
        return end != null && frameId < end;
    }

    void putActive(ActorId actorId, AbilityId abilityId, long startFrame, long endFrameExclusive) {
        activeByActor.put(actorId, new Active(abilityId, startFrame, endFrameExclusive));
    }

    void startCooldown(ActorId actorId, AbilityId abilityId, long endExclusive) {
        if (endExclusive < 0) throw new IllegalArgumentException();
        var inner = cooldownEndByActor.computeIfAbsent(actorId, __ -> new TreeMap<>());
        Long prev = inner.get(abilityId);
        if (prev == null || endExclusive > prev) inner.put(abilityId, endExclusive);
    }

    List<AbilitySnapshot> endedAt(long frameId) {
        if (activeByActor.isEmpty()) return List.of();
        var out = new ArrayList<AbilitySnapshot>();
        for (var e : activeByActor.entrySet()) {
            var a = e.getValue();
            if (a.endFrameExclusive == frameId) out.add(new AbilitySnapshot(a.abilityId, e.getKey(), a.startFrame, a.endFrameExclusive));
        }
        return List.copyOf(out);
    }

    void evictExpired(long frameId, BiConsumer<ActorId, AbilitySnapshot> onEnded) {
        if (activeByActor.isEmpty()) return;
        var it = activeByActor.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            var a = e.getValue();
            if (frameId > a.endFrameExclusive) {
                it.remove();
                onEnded.accept(e.getKey(), new AbilitySnapshot(a.abilityId, e.getKey(), a.startFrame, a.endFrameExclusive));
            }
        }
    }

    void cleanupCooldowns(long frameId) {
        if (cooldownEndByActor.isEmpty()) return;
        var it = cooldownEndByActor.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            var inner = e.getValue();
            inner.entrySet().removeIf(x -> x.getValue() != null && frameId >= x.getValue());
            if (inner.isEmpty()) it.remove();
        }
    }

    private record Active(AbilityId abilityId, long startFrame, long endFrameExclusive) {
        private Active(AbilityId abilityId, long startFrame, long endFrameExclusive) {
            this.abilityId = Objects.requireNonNull(abilityId);
            if (startFrame < 0 || endFrameExclusive <= startFrame) throw new IllegalArgumentException();
            this.startFrame = startFrame;
            this.endFrameExclusive = endFrameExclusive;
        }
    }
}