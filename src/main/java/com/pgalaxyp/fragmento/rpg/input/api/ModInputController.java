package com.pgalaxyp.fragmento.rpg.input.api;

import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.input.bridge.IntentEmitter;
import com.pgalaxyp.fragmento.rpg.input.system.ComboInputSystem;
import com.pgalaxyp.fragmento.rpg.input.system.InputClock;
import com.pgalaxyp.fragmento.rpg.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.rpg.input.system.InputStateMachine;
import java.util.Optional;
import java.util.OptionalLong;

public final class ModInputController {

    private final InputStateMachine stateMachine;
    private final ComboInputSystem comboSystem;
    private final InputConsumptionPolicy consumptionPolicy;
    private final InputClock clock;
    private final IntentEmitter emitter;

    public ModInputController(
            InputStateMachine stateMachine,
            ComboInputSystem comboSystem,
            InputConsumptionPolicy consumptionPolicy,
            InputClock clock,
            IntentEmitter emitter
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

    public ModInputDecision handle(ModInputContext context, ModInputIntent intent) {
        if (context == null || intent == null) {
            throw new IllegalArgumentException();
        }

        long frameId = clock.frameId(context);
        if (frameId < 0) {
            throw new IllegalArgumentException();
        }

        boolean consume = consumptionPolicy.consumeVanilla(context, intent);

        Optional<DomainIntent> domain = Optional.empty();

        if (intent == ModInputIntent.PRIMARY_ACTION) {
            if (context.hasWeaponInHand()) {
                boolean allowed = stateMachine.allowPrimaryAction(context.actorId(), frameId);
                if (allowed) {
                    domain = comboSystem.onPrimaryAction(context);
                }
            }
        }

        if (domain.isPresent()) {
            emitter.emit(context.actorId(), domain.get(), OptionalLong.of(frameId));
            return consume ? ModInputDecision.emitAndConsume() : ModInputDecision.emitNoConsume();
        }

        return consume ? ModInputDecision.consumeOnly() : ModInputDecision.passThrough();
    }
}