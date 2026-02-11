package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class BasicAttackRepository {
    private final Map<ActorId, ActorState> byActor = new HashMap<>();

    ActorState stateOf(ActorId actorId) {
        return byActor.get(Objects.requireNonNull(actorId));
    }

    WeaponCombo comboOf(ActorId actorId, WeaponId weaponId) {
        var s = byActor.get(Objects.requireNonNull(actorId));
        if (s == null) return null;
        return s.byWeapon().get(Objects.requireNonNull(weaponId));
    }

    void put(ActorId actorId, WeaponId weaponId, int stepIndex, long lastStepFrame, long lastProcessedFrame) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (stepIndex < 0 || lastStepFrame < 0 || lastProcessedFrame < 0) throw new IllegalArgumentException();

        var s = byActor.get(actorId);
        if (s == null) s = new ActorState(lastProcessedFrame, new HashMap<>());
        else s = new ActorState(lastProcessedFrame, new HashMap<>(s.byWeapon()));

        s.byWeapon().put(weaponId, new WeaponCombo(stepIndex, lastStepFrame));
        byActor.put(actorId, s);
    }

    void pruneToActors(Set<ActorId> liveActors) {
        Objects.requireNonNull(liveActors);
        byActor.entrySet().removeIf(e -> !liveActors.contains(e.getKey()));
    }

    record ActorState(long lastProcessedFrame, Map<WeaponId, WeaponCombo> byWeapon) {
        ActorState {
            Objects.requireNonNull(byWeapon);
            if (lastProcessedFrame < 0) throw new IllegalArgumentException();
        }
    }

    record WeaponCombo(int stepIndex, long lastStepFrame) {
        WeaponCombo {
            if (stepIndex < 0 || lastStepFrame < 0) throw new IllegalArgumentException();
        }
    }
}
