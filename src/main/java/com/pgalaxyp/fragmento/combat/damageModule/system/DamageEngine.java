package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorStateView;
import java.util.*;

public final class DamageEngine implements DamagePort {
    private final DamageService damage;
    private final DamageSnapshotProvider snapshots;

    public DamageEngine(DamageService damage, DamageSnapshotProvider snapshots) {
        this.damage = Objects.requireNonNull(damage);
        this.snapshots = Objects.requireNonNull(snapshots);
    }

    @Override public DamageOutcome resolve(DamageRequested request, FrameContext frame, ActorStateView state) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        DamageSnapshot snap = snapshots.snapshot(state, request.sourceActorId(), request.targetActorId());
        DamageResult result = damage.resolve(new DamageRequest(request.sourceActorId(), request.targetActorId(), request.spec()), snap);

        return new DamageOutcome(List.of(new DamageApplied(request.targetActorId(), result.finalHearts())));
    }
}