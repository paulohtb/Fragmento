package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.BasicSequence;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboStepStarted;
import java.util.Objects;

public final class ComboEffectObserver {

    private final BasicSequence sequence;

    public ComboEffectObserver(BasicSequence sequence, TickBus bus) {
        this.sequence = Objects.requireNonNull(sequence);

        bus.subscribe(ComboStepStarted.class, e -> onStepStarted(e, bus));
    }

    private void onStepStarted(ComboStepStarted e, TickBus bus) {
        var step = sequence.steps().stream()
                .filter(s -> Objects.equals(s.stepId(), e.stepId()))
                .findFirst()
                .orElse(null);

        if (step == null) return;
        if (!Objects.equals(step.effectMarker(), "MISSILE")) return;

        bus.publish(new SpawnEffectRequest(e.actorId(), e.stepId()));
    }
}