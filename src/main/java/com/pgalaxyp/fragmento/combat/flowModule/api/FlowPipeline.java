package com.pgalaxyp.fragmento.combat.flowModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.*;

public final class FlowPipeline {
    private final List<FrameSystem> systems;

    public FlowPipeline(List<FrameSystem> systems) { this.systems = List.copyOf(Objects.requireNonNull(systems)); }

    public FrameBus run(FrameContext frame, List<? extends FrameCommand> commands, Map<Class<?>, ?> initialViews) {
        var bus = new FrameBus(Objects.requireNonNull(commands), initialViews);
        for (var s : systems) s.tick(frame, bus);
        return bus;
    }
}