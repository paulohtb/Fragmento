package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.intentModule.api.IntentSinkPort;
import com.pgalaxyp.fragmento.combat.abilityModule.intent.AbilityPrimaryIntent;
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
        InputSnapshotView snap = snapshots.current();
        ActorId actorId = actorContext.localActorId().orElse(null);
        if (actorId == null) return InputDecision.passThrough();

        WeaponId weaponId = actorContext.weaponInHandId(actorId, snap).orElse(null);
        if (weaponId == null) return InputDecision.passThrough();
        if (input == SemanticInput.PRIMARY_ACTION) {
            sink.enqueue(actorId, new AbilityPrimaryIntent(weaponId), snap.frameIdOrZero());
            return new InputDecision(policy.shouldBlockVanilla(new InputContext(actorId, snap, weaponId), input));
        }

        return InputDecision.passThrough();
    }
}