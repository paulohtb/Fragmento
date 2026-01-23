package com.pgalaxyp.fragmento.combat.random;

import java.util.List;
import java.util.Objects;

public final class FlowPipeline {

    private final List<FrameSystem> systems;

    public FlowPipeline(List<FrameSystem> systems) {
        this.systems = List.copyOf(Objects.requireNonNull(systems));
    }

    public FrameBus run(FrameContext frame, GameState state, List<?> intents) {
        FrameBus bus = new FrameBus(intents);
        for (FrameSystem s : systems) s.tick(frame, state, bus);
        return bus;
    }
}