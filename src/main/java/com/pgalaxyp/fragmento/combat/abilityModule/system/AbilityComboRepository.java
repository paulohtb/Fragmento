package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.*;

final class AbilityComboRepository {
    private record ComboState(WeaponId weaponId, int stepIndex, long lastFrame) {}

    record Plan(ComboState state) {
        Plan { Objects.requireNonNull(state); }
        int step() { return state.stepIndex(); }
    }

    private final int maxStepIndex;
    private final int maxGapFrames;
    private final Map<ActorId, ComboState> byActor = new HashMap<>();

    AbilityComboRepository(int maxStepIndex, int maxGapFrames) {
        if (maxStepIndex < 0 || maxGapFrames < 0) throw new IllegalArgumentException();
        this.maxStepIndex = maxStepIndex;
        this.maxGapFrames = maxGapFrames;
    }

    Plan plan(ActorId actorId, WeaponId weaponId, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (frameId < 0) throw new IllegalArgumentException();
        ComboState prev = byActor.get(actorId);
        boolean reset = prev == null || !prev.weaponId.equals(weaponId) || Math.subtractExact(frameId, prev.lastFrame) > maxGapFrames;
        if (reset) return new Plan(new ComboState(weaponId, 0, frameId));
        int next = prev.stepIndex + 1;
        if (next > maxStepIndex) next = 0;
        return new Plan(new ComboState(weaponId, next, frameId));
    }

    void commit(ActorId actorId, Plan plan) {
        byActor.put(Objects.requireNonNull(actorId), Objects.requireNonNull(plan).state());
    }

    void pruneToActors(Collection<ActorId> liveActors) {
        Objects.requireNonNull(liveActors);
        if (!byActor.isEmpty()) byActor.entrySet().removeIf(e -> !liveActors.contains(e.getKey()));
    }
}