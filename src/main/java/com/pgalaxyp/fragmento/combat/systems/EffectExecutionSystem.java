package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.effect.api.EffectService;
import com.pgalaxyp.fragmento.combat.events.effect.EffectTriggered;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import java.util.Objects;

public final class EffectExecutionSystem implements FrameSystem {

    private final EffectService effects;

    public EffectExecutionSystem(EffectService effects) {
        this.effects = Objects.requireNonNull(effects);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (EffectTriggered e : bus.events(EffectTriggered.class)) {
            var out = effects.applyResolved(frame, state, e.effectId(), e.source(), e.target());
            out.deltas().forEach(bus::emit);
        }
    }
}