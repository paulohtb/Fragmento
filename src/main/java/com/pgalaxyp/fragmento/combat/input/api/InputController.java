package com.pgalaxyp.fragmento.combat.input.api;

import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotView;
import com.pgalaxyp.fragmento.combat.input.system.FrameClock;
import com.pgalaxyp.fragmento.combat.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.combat.input.system.InputStateMachine;
import com.pgalaxyp.fragmento.combat.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.combat.core.events.intent.PerformActionIntent;
import java.util.Optional;

public final class InputController {

    private final InputStateMachine stateMachine;
    private final InputConsumptionPolicy consumptionPolicy;
    private final FrameClock clock;
    private final InputIntentSink emitter;

    public InputController(
            InputStateMachine stateMachine,
            InputConsumptionPolicy consumptionPolicy,
            FrameClock clock,
            InputIntentSink emitter
    ) {
        if (stateMachine == null || consumptionPolicy == null || clock == null || emitter == null) {
            throw new IllegalArgumentException();
        }
        this.stateMachine = stateMachine;
        this.consumptionPolicy = consumptionPolicy;
        this.clock = clock;
        this.emitter = emitter;
    }

    public InputDecision handle(InputContext context, SemanticInput input) {
        if (context == null || input == null) {
            throw new IllegalArgumentException();
        }

        boolean consumeVanilla = consumptionPolicy.shouldBlockVanilla(context, input);

        if (input != SemanticInput.PRIMARY_ACTION) {
            return new InputDecision(consumeVanilla, false);
        }

        Optional<ActorId> actorOpt = context.actorIdOpt();
        if (actorOpt.isEmpty()) {
            return new InputDecision(consumeVanilla, false);
        }

        ActorId actorId = actorOpt.get();

        long frameId = clock.frameId(context);
        InputStateMachine.Decision sm = stateMachine.decidePrimaryAction(actorId, frameId);
        if (sm != InputStateMachine.Decision.ALLOWED) {
            return new InputDecision(consumeVanilla, false);
        }

        InputSnapshotView snapshot = context.snapshot();
        if (!snapshot.isPresent()) {
            return new InputDecision(consumeVanilla, false);
        }

        if (!context.hasWeaponInHand()) {
            return new InputDecision(consumeVanilla, false);
        }

        DomainIntent intent = new PerformActionIntent(ComboInput.PRIMARY);
        emitter.emit(actorId, intent, snapshot.frameIdOrZero());

        return new InputDecision(consumeVanilla, true);
    }
}