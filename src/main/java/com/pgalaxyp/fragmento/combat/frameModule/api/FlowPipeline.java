package com.pgalaxyp.fragmento.combat.frameModule.api;

import java.util.*;

public final class FlowPipeline {
    private final List<FrameSystem> systems;

    public FlowPipeline(List<FrameSystem> systems) {
        this.systems = List.copyOf(Objects.requireNonNull(systems));
    }

    public FrameBus run(FrameContext frame, Object state, List<?> intents) {
        FrameBus bus = new FrameBus(intents);
        for (FrameSystem s : systems) s.tick(frame, state, bus);
        return bus;
    }
}