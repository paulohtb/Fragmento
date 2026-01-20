package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.*;
import java.util.function.BiConsumer;

final class ComboRepository {
    private final Map<ActorId, Active> activeByActor = new HashMap<>();
    private final Map<ActorId, Long> lockEndInclusiveByActor = new HashMap<>();

    Optional<ComboSnapshot> activeOf(ActorId actorId, long frameId) {
        Active a = activeByActor.get(actorId);
        if (a == null || frameId < a.startedAtFrame || frameId >= a.expiresAtFrameExclusive) return Optional.empty();
        return Optional.of(a.snapshot(actorId));
    }

    List<ComboSnapshot> activeAll(long frameId) {
        if (activeByActor.isEmpty()) return List.of();
        var out = new ArrayList<ComboSnapshot>(activeByActor.size());
        for (var e : activeByActor.entrySet()) {
            Active a = e.getValue();
            if (frameId >= a.startedAtFrame && frameId < a.expiresAtFrameExclusive) out.add(a.snapshot(e.getKey()));
        }
        return List.copyOf(out);
    }

    boolean isLocked(ActorId actorId, long frameId) {
        Long end = lockEndInclusiveByActor.get(actorId);
        return end != null && frameId <= end;
    }

    void lock(ActorId actorId, long lockEndInclusive) {
        if (lockEndInclusive < 0) throw new IllegalArgumentException();
        Long prev = lockEndInclusiveByActor.get(actorId);
        if (prev == null || lockEndInclusive > prev) lockEndInclusiveByActor.put(actorId, lockEndInclusive);
    }

    void startOrUpdate(ActorId actorId, Active next) { activeByActor.put(actorId, next); }

    void clear(ActorId actorId) {
        activeByActor.remove(actorId);
        lockEndInclusiveByActor.remove(actorId);
    }

    void cleanup(long frameId, BiConsumer<ActorId, Active> onExpired) {
        if (activeByActor.isEmpty()) return;
        var it = activeByActor.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            Active a = e.getValue();
            if (frameId >= a.expiresAtFrameExclusive) {
                it.remove();
                onExpired.accept(e.getKey(), a);
            }
        }
        lockEndInclusiveByActor.entrySet().removeIf(x -> x.getValue() != null && frameId > x.getValue());
    }

    record Active(ComboId comboId, int stepIndex, int stepsTotal, long startedAtFrame, long lastAcceptedFrame, long expiresAtFrameExclusive) {
        Active {
            Objects.requireNonNull(comboId);
            if (stepIndex < 0 || stepsTotal <= 0 || stepIndex >= stepsTotal) throw new IllegalArgumentException();
            if (startedAtFrame < 0 || lastAcceptedFrame < 0 || expiresAtFrameExclusive <= 0) throw new IllegalArgumentException();
            if (lastAcceptedFrame < startedAtFrame) throw new IllegalArgumentException();
        }

        ComboSnapshot snapshot(ActorId actorId) {
            return new ComboSnapshot(actorId, comboId, stepIndex, stepsTotal, startedAtFrame, lastAcceptedFrame, expiresAtFrameExclusive);
        }
    }
}