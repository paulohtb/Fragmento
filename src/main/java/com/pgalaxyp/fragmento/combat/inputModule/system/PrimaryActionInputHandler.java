package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Objects;

public final class PrimaryActionInputHandler {
    private final ActorInputContextProvider actorContext;
    private final InputSnapshotProvider snapshots;
    private final IntentSinkPort sink;
    private final InputConsumptionPolicy policy;

    public PrimaryActionInputHandler(ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, IntentSinkPort sink, InputConsumptionPolicy policy) {
        this.actorContext = Objects.requireNonNull(actorContext);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.sink = Objects.requireNonNull(sink);
        this.policy = Objects.requireNonNull(policy);
    }

    public InputDecision onSemanticInput(SemanticInput input) {
        Objects.requireNonNull(input);
        if (input != SemanticInput.PRIMARY_ACTION) return InputDecision.passThrough();

        InputSnapshotView snap = snapshots.current();
        ActorId actorId = actorContext.localActorId().orElse(null);
        if (actorId == null) return InputDecision.passThrough();

        WeaponId weaponId = actorContext.weaponInHandId(actorId).orElse(null);
        if (weaponId == null) return InputDecision.passThrough();

        long clientFrameHint = snap.frameIdOrZero();
        sink.enqueue(actorId, new PrimaryActionIntent(weaponId), clientFrameHint);

        return new InputDecision(policy.shouldBlockVanilla(new InputContext(actorId, clientFrameHint, weaponId), input));
    }
}