package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.abilityModule.intent.AbilityPrimaryIntent;
import com.pgalaxyp.fragmento.combat.inputModule.api.InputContext;
import com.pgalaxyp.fragmento.combat.inputModule.api.InputDecision;
import com.pgalaxyp.fragmento.combat.inputModule.api.SemanticInput;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputIntentSink;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotView;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.Objects;

public final class PrimaryActionInputHandler {
    private final ActorInputContextProvider actorContext;
    private final InputSnapshotProvider snapshots;
    private final InputIntentSink sink;
    private final InputConsumptionPolicy policy;

    public PrimaryActionInputHandler(ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, InputIntentSink sink, InputConsumptionPolicy policy) {
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

        InputContext ctx = new InputContext(actorId, snap, weaponId);

        if (input == SemanticInput.PRIMARY_ACTION) {
            sink.emit(actorId, new AbilityPrimaryIntent(weaponId), snap.frameIdOrZero());
            return new InputDecision(policy.shouldBlockVanilla(ctx, input));
        }

        return InputDecision.passThrough();
    }
}