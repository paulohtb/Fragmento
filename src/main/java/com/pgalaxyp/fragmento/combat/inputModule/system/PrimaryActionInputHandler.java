package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import java.util.Objects;

public final class PrimaryActionInputHandler {
    private final ActorInputContextProvider actorContext;
    private final IntentSinkPort sink;

    public PrimaryActionInputHandler(ActorInputContextProvider actorContext, IntentSinkPort sink) {
        this.actorContext = Objects.requireNonNull(actorContext);
        this.sink = Objects.requireNonNull(sink);
    }

    public InputDecision onSemanticInput(SemanticInput input) {
        Objects.requireNonNull(input);
        if (input != SemanticInput.PRIMARY_ACTION) return InputDecision.passThrough();
        var actorId = actorContext.localActorId().orElse(null);
        if (actorId == null) return InputDecision.passThrough();
        WeaponId weaponId = actorContext.weaponInHandId(actorId).orElse(null);
        if (weaponId == null) return InputDecision.passThrough();
        return sink.enqueue(actorId, new PrimaryActionIntent(weaponId)) ? InputDecision.consume() : InputDecision.passThrough();
    }
}