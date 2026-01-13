package com.pgalaxyp.fragmento.rpg.input.api;

import com.pgalaxyp.fragmento.rpg.input.bridge.*;
import com.pgalaxyp.fragmento.rpg.input.system.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import java.util.*;

public final class InputController {

    private final InputStateMachine stateMachine;
    private final ComboInputSystem comboSystem;
    private final InputConsumptionPolicy consumptionPolicy;
    private final FrameClock clock;
    private final InputIntentSink emitter;

    public InputController(
            InputStateMachine stateMachine,
            ComboInputSystem comboSystem,
            InputConsumptionPolicy consumptionPolicy,
            FrameClock clock,
            InputIntentSink emitter
    ) {
        if (stateMachine == null || comboSystem == null || consumptionPolicy == null || clock == null || emitter == null) {
            throw new IllegalArgumentException();
        }
        this.stateMachine = stateMachine;
        this.comboSystem = comboSystem;
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

        if (!context.snapshot().isPresent()) {
            return new InputDecision(consumeVanilla, false);
        }

        if (!context.hasWeaponInHand()) {
            return new InputDecision(consumeVanilla, false);
        }

        Optional<DomainIntent> intent = comboSystem.onPrimaryAction(context);
        if (intent.isEmpty()) {
            return new InputDecision(consumeVanilla, false);
        }

        emitter.emit(actorId, intent.get(), context.snapshot().frameIdOrZero());

        return new InputDecision(consumeVanilla, true);
    }
}