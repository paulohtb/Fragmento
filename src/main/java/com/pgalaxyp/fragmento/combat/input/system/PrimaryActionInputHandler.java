package com.pgalaxyp.fragmento.combat.input.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.input.api.InputContext;
import com.pgalaxyp.fragmento.combat.input.api.InputDecision;
import com.pgalaxyp.fragmento.combat.input.api.SemanticInput;
import com.pgalaxyp.fragmento.combat.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotView;
import com.pgalaxyp.fragmento.combat.intent.AbilityUseIntent;
import java.util.Map;
import java.util.Objects;

public final class PrimaryActionInputHandler {
    private final ActorInputContextProvider actorContext;
    private final InputSnapshotProvider snapshots;
    private final InputIntentSink sink;
    private final InputConsumptionPolicy policy;
    private final Map<WeaponId, AbilityId> primaryByWeapon;

    public PrimaryActionInputHandler(ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, InputIntentSink sink, InputConsumptionPolicy policy, Map<WeaponId, AbilityId> primaryByWeapon) {
        this.actorContext = Objects.requireNonNull(actorContext);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.sink = Objects.requireNonNull(sink);
        this.policy = Objects.requireNonNull(policy);
        this.primaryByWeapon = Map.copyOf(Objects.requireNonNull(primaryByWeapon));
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
            var abilityId = primaryByWeapon.get(weaponId);
            if (abilityId != null) {
                sink.emit(actorId, new AbilityUseIntent(abilityId, weaponId), snap.frameIdOrZero());
                return new InputDecision(policy.shouldBlockVanilla(ctx, input));
            }
        }

        return InputDecision.passThrough();
    }
}