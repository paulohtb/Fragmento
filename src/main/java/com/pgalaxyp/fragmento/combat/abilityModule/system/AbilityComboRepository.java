package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.*;

public final class AbilityComboRepository {
    private static final int MAX_GAP_FRAMES = 20;
    private static final int MAX_STEP_INDEX = 1;

    private record ComboState(WeaponId weaponId, int stepIndex, long lastFrame) {}
    private final Map<ActorId, ComboState> byActor = new HashMap<>();

    public int nextStep(ActorId actorId, WeaponId weaponId, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (frameId < 0) throw new IllegalArgumentException();

        ComboState prev = byActor.get(actorId);
        if (prev == null || !prev.weaponId.equals(weaponId) || Math.subtractExact(frameId, prev.lastFrame) > MAX_GAP_FRAMES) {
            byActor.put(actorId, new ComboState(weaponId, 0, frameId));
            return 0;
        }

        int next = prev.stepIndex + 1;
        if (next > MAX_STEP_INDEX) next = 0;
        byActor.put(actorId, new ComboState(weaponId, next, frameId));
        return next;
    }
}