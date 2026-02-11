package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackRejectReason;
import com.pgalaxyp.fragmento.combat.basicAttackModule.event.BasicAttackRejected;
import com.pgalaxyp.fragmento.combat.basicAttackModule.event.BasicAttackStepTriggered;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class BasicAttackEngine {
    private final Map<WeaponId, BasicAttackDefinition> defs;
    private final BasicAttackRepository repo = new BasicAttackRepository();

    BasicAttackEngine(Map<WeaponId, BasicAttackDefinition> defs) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
    }

    FrameEvent tryTrigger(ActorId actorId, WeaponId weaponId, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (frameId < 0) throw new IllegalArgumentException();

        var def = defs.get(weaponId);
        if (def == null) return new BasicAttackRejected(actorId, weaponId, BasicAttackRejectReason.NO_DEFINITION);

        var s = repo.stateOf(actorId);
        if (s != null && s.lastProcessedFrame() == frameId) return new BasicAttackRejected(actorId, weaponId, BasicAttackRejectReason.LOCKED);

        int nextIndex = 0;
        var combo = repo.comboOf(actorId, weaponId);
        if (combo != null) {
            long dt = Math.subtractExact(frameId, combo.lastStepFrame());
            if (dt <= def.comboWindowFrames()) {
                int size = def.steps().size();
                nextIndex = size <= 1 ? 0 : (combo.stepIndex() + 1) % size;
            }
        }

        repo.put(actorId, weaponId, nextIndex, frameId, frameId);
        return new BasicAttackStepTriggered(actorId, weaponId, nextIndex);
    }

    void tick(long frameId, Set<ActorId> liveActors) {
        Objects.requireNonNull(liveActors);
        if (frameId < 0) throw new IllegalArgumentException();
        repo.pruneToActors(liveActors);
    }
}
