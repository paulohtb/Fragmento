package com.pgalaxyp.fragmento.rpg.gameplay.combat.basic;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectEnded;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputAction;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;

import java.util.Objects;

public final class ComboResolver implements Updatable {

    private final BasicSequence sequence;
    private final ActorRepository actors;
    private final TickBus bus;

    public ComboResolver(BasicSequence sequence, ActorRepository actors, TickBus bus) {
        this.sequence = Objects.requireNonNull(sequence);
        this.actors = Objects.requireNonNull(actors);
        this.bus = Objects.requireNonNull(bus);

        bus.subscribe(InputEvent.class, this::onInput);
        bus.subscribe(EffectEnded.class, this::onEffectEnd);
    }

    private void onInput(InputEvent e) {
        if (e.action() != InputAction.ATTACK_PRIMARY) return;

        var state = actors.combo(e.actorId());
        if (state.executing()) return;

        var steps = sequence.steps();
        var index = Math.floorMod(state.index(), steps.size());
        var step = steps.get(index);

        state.beginStep(index + 1, step.stepId());
        bus.publish(new ComboStepStarted(e.actorId(), step.stepId()));
    }

    private void onEffectEnd(EffectEnded e) {
        var state = actors.combo(e.actorId());
        if (!Objects.equals(state.activeStepId(), e.stepId())) return;
        state.endStep();
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
    }
}