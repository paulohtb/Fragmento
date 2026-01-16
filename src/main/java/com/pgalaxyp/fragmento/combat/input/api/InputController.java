package com.pgalaxyp.fragmento.combat.input.api;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import java.util.*;

public final class InputController {
    private final InputStateMachine stateMachine;
    private final InputConsumptionPolicy consumptionPolicy;
    private final FrameClock clock;
    private final InputIntentSink emitter;

    public InputController(InputStateMachine stateMachine, InputConsumptionPolicy consumptionPolicy, FrameClock clock, InputIntentSink emitter) {
        this.stateMachine = Objects.requireNonNull(stateMachine);
        this.consumptionPolicy = Objects.requireNonNull(consumptionPolicy);
        this.clock = Objects.requireNonNull(clock);
        this.emitter = Objects.requireNonNull(emitter);
    }

    public InputDecision handle(InputContext context, SemanticInput input) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(input);

        boolean consumeVanilla = consumptionPolicy.shouldBlockVanilla(context, input);
        if (input != SemanticInput.PRIMARY_ACTION) return new InputDecision(consumeVanilla);

        ActorId actorId = context.actorId();
        if (actorId == null) return new InputDecision(consumeVanilla);

        long frameId = clock.frameId(context);
        if (stateMachine.decidePrimaryAction(actorId, frameId) != InputStateMachine.Decision.ALLOWED) return new InputDecision(consumeVanilla);

        InputSnapshotView snapshot = context.snapshot();
        if (!snapshot.isPresent() || !context.hasWeaponInHand()) return new InputDecision(consumeVanilla);

        emitter.emit(actorId, new PerformActionIntent(ComboInput.PRIMARY), snapshot.frameIdOrZero());
        return new InputDecision(consumeVanilla);
    }
}