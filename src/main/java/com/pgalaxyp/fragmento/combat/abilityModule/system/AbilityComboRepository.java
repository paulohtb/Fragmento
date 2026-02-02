package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.*;

public final class AbilityComboRepository {
    private static final int MAX_GAP_FRAMES = 20;
    private record ComboState(WeaponId weaponId, int stepIndex, long lastFrame) {}
    record Plan(int step, ComboState state) {}
    private final int maxStepIndex;
    private final Map<ActorId, ComboState> byActor = new HashMap<>();

    AbilityComboRepository(int maxStepIndex) {
        if (maxStepIndex < 0) throw new IllegalArgumentException();
        this.maxStepIndex = maxStepIndex;
    }

    Plan plan(ActorId actorId, WeaponId weaponId, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (frameId < 0) throw new IllegalArgumentException();
        ComboState prev = byActor.get(actorId);
        if (prev == null || !prev.weaponId.equals(weaponId) || Math.subtractExact(frameId, prev.lastFrame) > MAX_GAP_FRAMES) {
            var s = new ComboState(weaponId, 0, frameId);
            return new Plan(0, s);
        }
        int next = prev.stepIndex + 1;
        if (next > maxStepIndex) next = 0;
        var s = new ComboState(weaponId, next, frameId);
        return new Plan(next, s);
    }

    void commit(ActorId actorId, Plan plan) {
        byActor.put(Objects.requireNonNull(actorId), Objects.requireNonNull(plan).state());
    }
}