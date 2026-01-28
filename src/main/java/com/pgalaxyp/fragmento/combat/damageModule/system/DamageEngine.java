package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorState;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.actorModule.event.ActorUpserted;
import java.util.*;

public final class DamageEngine implements DamagePort {
    private final DamageService damage;
    private final DamageSnapshotProvider snapshots;

    public DamageEngine(DamageService damage, DamageSnapshotProvider snapshots) {
        this.damage = Objects.requireNonNull(damage);
        this.snapshots = Objects.requireNonNull(snapshots);
    }

    @Override
    public DamageOutcome resolve(DamageRequested request, FrameContext frame, GameState state) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        DamageSnapshot snap = snapshots.snapshot(state, request.sourceActorId(), request.targetActorId());
        DamageResult result = damage.resolve(new DamageRequest(request.sourceActorId(), request.targetActorId(), request.spec()), snap);
        var out = getFrameEvents(request, state, result);

        return new DamageOutcome(out);
    }

    private static ArrayList<FrameEvent> getFrameEvents(DamageRequested request, GameState state, DamageResult result) {
        int dmg = result.finalHearts();
        var out = new ArrayList<FrameEvent>(2);
        out.add(new DamageApplied(request.targetActorId(), dmg));
        ActorState prev = state.findActor(request.targetActorId()).orElse(null);
        if (prev != null) {
            int cur = prev.healthHearts();
            int next = cur <= 0 ? 0 : Math.max(0, cur - dmg);
            if (next != cur) out.add(new ActorUpserted(request.targetActorId(), new ActorState(prev.classId(), prev.equippedWeaponId(), next, prev.maxHealthHearts())));
        }

        return out;
    }
}