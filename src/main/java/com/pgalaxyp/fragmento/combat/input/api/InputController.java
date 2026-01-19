package com.pgalaxyp.fragmento.combat.input.api;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import java.util.*;

public final class InputController {
    private final InputConsumptionPolicy consumptionPolicy;
    private final InputIntentSink emitter;

    public InputController(InputConsumptionPolicy consumptionPolicy, InputIntentSink emitter) {
        this.consumptionPolicy = Objects.requireNonNull(consumptionPolicy);
        this.emitter = Objects.requireNonNull(emitter);
    }

    public InputDecision handle(InputContext context, SemanticInput input) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(input);

        boolean consumeVanilla = consumptionPolicy.shouldBlockVanilla(context, input);
        if (input != SemanticInput.PRIMARY_ACTION) return new InputDecision(consumeVanilla);

        ActorId actorId = context.actorId();
        if (actorId == null) return new InputDecision(consumeVanilla);

        InputSnapshotView snapshot = context.snapshot();
        if (!snapshot.isPresent() || !context.hasWeaponInHand()) return new InputDecision(consumeVanilla);

        emitter.emit(actorId, new PerformActionIntent(ComboInput.PRIMARY), snapshot.frameIdOrZero());
        return new InputDecision(consumeVanilla);
    }
}
